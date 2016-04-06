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

package com.github.gumtreediff.matchers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.github.gumtreediff.tree.ITree;

public class MappingStore implements Iterable<Mapping> {

    private Map<ITree, ITree> srcs;
    private Map<ITree, ITree> dsts;

    public MappingStore(Set<Mapping> mappings) {
        this();
        for (Mapping m: mappings) link(m.getFirst(), m.getSecond());
    }

    public MappingStore() {
        srcs = new  HashMap<>();
        dsts = new HashMap<>();
    }

    public Set<Mapping> asSet() {
        Set<Mapping> mappings = new HashSet<>();
        for (ITree src : srcs.keySet())
            mappings.add(new Mapping(src, srcs.get(src)));
        return mappings;
    }

    public MappingStore copy() {
        return new MappingStore(asSet());
    }

    public void link(ITree src, ITree dst) {
        srcs.put(src, dst);
        dsts.put(dst, src);
    }

    public void unlink(ITree src, ITree dst) {
        srcs.remove(src);
        dsts.remove(dst);
    }

    public ITree firstMappedSrcParent(ITree src) {
        ITree p = src.getParent();
        if (p == null) return null;
        else {
            while (!hasSrc(p)) {
                p = p.getParent();
                if (p == null) return p;
            }
            return p;
        }
    }

    public ITree firstMappedDstParent(ITree dst) {
        ITree p = dst.getParent();
        if (p == null) return null;
        else {
            while (!hasDst(p)) {
                p = p.getParent();
                if (p == null) return p;
            }
            return p;
        }
    }

    public ITree getDst(ITree src) {
        return srcs.get(src);
    }

    public ITree getSrc(ITree dst) {
        return dsts.get(dst);
    }

    public boolean hasSrc(ITree src) {
        return srcs.containsKey(src);
    }

    public boolean hasDst(ITree dst) {
        return dsts.containsKey(dst);
    }

    public boolean has(ITree src, ITree dst) {
        return srcs.get(src) == dst;
    }

    @Override
    public Iterator<Mapping> iterator() {
        return asSet().iterator();
    }

    @Override
    public String toString() {
        return asSet().toString();
    }

}
