/*
 * This file is part of GumTree.
 *
 * GumTree is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GumTree is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with GumTree.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011-2015 Jean-Rémy Falleri <jr.falleri@gmail.com>
 * Copyright 2011-2015 Floréal Morandat <florealm@gmail.com>
 */

package com.github.gumtreediff.matchers.heuristic.gt;

import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.optimal.rted.RtedMatcher;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.matchers.Mapping;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.optimal.rted.RtedMatcher;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.gumtreediff.tree.TreeUtils.postOrder;
import static com.github.gumtreediff.tree.TreeUtils.removeMatched;

/**
 * Match the nodes using a bottom-up approach. It browse the nodes of the source and destination trees
 * using a post-order traversal, testing if the two selected trees might be mapped. The two trees are mapped 
 * if they are mappable and have a dice coefficient greater than SIM_THRESHOLD. Whenever two trees are mapped
 * a exact ZS algorithm is applied to look to possibly forgotten nodes.
 */
public class FirstMatchBottomUpMatcher extends Matcher {
    private static final double SIM_THRESHOLD = Double.parseDouble(System.getProperty("gumtree.match.bu.sim", "0.5"));

    private static final int SIZE_THRESHOLD = Integer.parseInt(System.getProperty("gumtree.match.bu.size", "200"));

    private Map<Integer, ITree> srcIds = new HashMap<>();

    private Map<Integer, ITree> dstIds = new HashMap<>();

    public FirstMatchBottomUpMatcher(ITree src, ITree dst, MappingStore store) {
        super(src, dst, store);
    }

    public void match() {
        List<ITree> srcs = postOrder(src);
        List<ITree> dsts = postOrder(dst);
        for (ITree t : srcs)
            srcIds.put(t.getId(), t);
        for (ITree t : dsts)
            dstIds.put(t.getId(), t);
        match(TreeUtils.removeMapped(srcs), TreeUtils.removeMapped(dsts));
        clean();
    }

    private void match(List<ITree> poSrc, List<ITree> poDst) {
        for (ITree src: poSrc)  {
            for (ITree dst: poDst) {
                if (src.isMatchable(dst) && !(src.isLeaf() || dst.isLeaf())) {
                    double sim = jaccardSimilarity(src, dst);
                    if (sim >= SIM_THRESHOLD || (src.isRoot() && dst.isRoot()) ) {
                        if (!(src.areDescendantsMatched() || dst.areDescendantsMatched()))
                            lastChanceMatch(src, dst);
                        addMapping(src, dst);
                        break;
                    }
                }
            }
        }
    }

    //FIXME checks if it is better or not to remove the already found mappings.
    private void lastChanceMatch(ITree src, ITree dst) {
        ITree cSrc = removeMatched(src.deepCopy());
        ITree cDst = removeMatched(dst.deepCopy());
        if (cSrc.getSize() < SIZE_THRESHOLD && cDst.getSize() < SIZE_THRESHOLD) {
            Matcher m = new RtedMatcher(cSrc, cDst, new MappingStore());
            for (Mapping candidate: m.getMappings()) {
                ITree left = srcIds.get(candidate.getFirst().getId());
                ITree right = dstIds.get(candidate.getSecond().getId());
                if (left.getId() == src.getId() || right.getId() == dst.getId()) {
                    continue;
                } else if (left.isMatched() && right.isMatched()) {
                    continue;
                } else if (!left.isMatchable(right)) {
                    continue;
                } else if (left.getParent().getType() != right.getParent().getType()) {
                    continue;
                } else addMapping(left, right);
            }

            for (ITree t : cSrc.getTrees())
                srcIds.get(t.getId()).setMatched(true);
            for (ITree t : cDst.getTrees())
                dstIds.get(t.getId()).setMatched(true);
        }

    }
}
