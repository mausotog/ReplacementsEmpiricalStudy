package cn.edu.pku.sei.plde.qacrashfix.tree;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.edu.pku.sei.plde.qacrashfix.tree.TreeNodeMapper.TreeNodePair;

/**
 * This class implements the algorithm descried in the ASE14 paper 
 * <a href="http://dl.acm.org/citation.cfm?doid=2642937.2642982">Fine-grained and accurate source code differencing </a>
 * @author Hansheng Zhang
 *
 */
public class GumTreeMapper implements IMappingGenerator {

	private TreeNode _srcNode;
	private TreeNode _destNode;
	private Map<TreeNode, TreeNode> _map;
	private double _minDice = 0.5;
	private final Logger logger = LogManager.getLogger();
	public GumTreeMapper(double minDice){
		this();
		_minDice = minDice;
	}
	private void mapDescendants(TreeNode key, TreeNode value){
		logger.debug("add tree mappings: " + key.getValue() + " --> " + value.getValue());
		TreeNodeMapper.mapDescendants(_map, key, value);
	}
	
	public GumTreeMapper(){
		_map = new HashMap<TreeNode, TreeNode>();
	}
	
	public static Comparator<TreeNode> heightComparator = new Comparator<TreeNode>() {
		public int compare(TreeNode o1, TreeNode o2) {
			return o2.getHeight() - o1.getHeight();
		}
	};

	@Override
	public Map<TreeNode, TreeNode> generateMapping(TreeNode nodeSource,
			TreeNode nodeTarget) {
		logger.debug("start generating mappings...");
		_srcNode = nodeSource;
		_destNode = nodeTarget;
		collectTopDownBijectiveMappings();
		bottomUpAdjust(_srcNode);
		if (_srcNode.getLabel().equals(_destNode.getLabel()))
			topDownAdjust(_srcNode, _destNode);
		return _map;
	}

	private void collectTopDownBijectiveMappings() {
		LinkedList<TreeNodePair> candidates = new LinkedList<TreeNodePair>();
		LinkedList<TreeNode> l1 = new LinkedList<TreeNode>();
		l1.add(_srcNode);
		LinkedList<TreeNode> l2 = new LinkedList<TreeNode>();
		l2.add(_destNode);
		while (!l1.isEmpty() && !l2.isEmpty()) {
			
			if (l1.getFirst().getHeight() != l2.getFirst().getHeight()) {
				if (l1.getFirst().getHeight() > l2.getFirst().getHeight())
					open(l1.removeFirst(), l1);
				else
					open(l2.removeFirst(), l2);
				continue;
			}
			
			LinkedList<TreeNode> h1 = pop(l1);
			LinkedList<TreeNode> h2 = pop(l2);
			LinkedList<TreeNodePair> pairs = new LinkedList<TreeNodePair>();
			for (TreeNode t1 : h1) {
				for (TreeNode t2 : h2) {
					if (t1.isomorphic(t2))
						pairs.add(new TreeNodePair(t1, t2));
				}
			}
			
			for (TreeNodePair pair : pairs) {
				boolean flag = false;
				for (TreeNodePair pairOther : pairs) {
					if (pair.first == pairOther.first
							&& pair.second != pairOther.second)
						flag = true;
					if (pair.first != pairOther.first
							&& pair.second == pairOther.second)
						flag = true;
				}
				if (flag == false) {
					mapDescendants(pair.first, pair.second);
				} else
					candidates.add(pair);
			}
			
			for (TreeNode t1 : h1) {
				if (!_map.containsKey(t1) && !isNodeInPairsFirst(t1, candidates))
					open(t1, l1);
			}
			for (TreeNode t2 : h2) {
				if (!_map.containsValue(t2) && !isNodeInPairsSecond(t2, candidates))
					open(t2, l2);
			}
		}
		while (!candidates.isEmpty()) {
			TreeNodePair pair = candidates.removeFirst();
			TreeNodeMapper.mapDescendants(_map, pair.first, pair.second);
			List<TreeNodePair> toDelete = new LinkedList<TreeNodeMapper.TreeNodePair>();
			for (TreeNodePair pairOther : candidates) {
				if (pairOther.first == pair.first
						|| pairOther.second == pairOther.second)
					toDelete.add(pairOther);
			}
			candidates.removeAll(toDelete);
		}
	}
	
	private void bottomUpAdjust(TreeNode node){
		if (_map.containsKey(node))
			return;
		Map<TreeNode, Integer> weights = new HashMap<TreeNode, Integer>();
		for (TreeNode child : node.getChildren()){ // get the most suspicious one
			bottomUpAdjust(child);
			TreeNode parent = child.getParent();
			if (parent != null && _map.containsKey(child)){
				TreeNode parentThat = _map.get(child).getParent();
				if (weights.containsKey(parentThat))
					weights.put(parentThat, weights.get(parentThat)+1);
				else weights.put(parentThat, 1);
			}
		}
		Integer max = 0;
		TreeNode partner = null;
		for (Map.Entry<TreeNode, Integer> entry: weights.entrySet()){
			if (entry.getValue() > max){
				max = entry.getValue();
				partner = entry.getKey();
			}
		}
		if (partner != null && dice(node, partner, _map) > _minDice){
			addMapping(node, partner); // bottom-up matching
			if (node.getChildren().size() == partner.getChildren().size()){ //recovery matching
				for (int i=0; i<node.getChildren().size(); i++){
					TreeNode childThis = node.getChildren().get(i);
					TreeNode childThat = partner.getChildren().get(i);
					if (!_map.containsKey(childThis) ||
							(childThat!=null && ! childThat.equals(_map.get(childThis))))
						addMapping(childThis, childThat);
				}
			}
		}
	}
	
	private void topDownAdjust(TreeNode srcNode, TreeNode destNode) {
		if (_map.containsKey(srcNode) || _map.containsValue(destNode) )
			return;
		if (!srcNode.getLabel().equals(destNode.getLabel()))
			return;
		_map.put(srcNode, destNode);
		if (srcNode.getChildren().size() == destNode.getChildren().size()){
			Iterator<TreeNode> itr1 = srcNode.getChildren().iterator();
			Iterator<TreeNode> itr2 = destNode.getChildren().iterator();
			while (itr1.hasNext())
				topDownAdjust(itr1.next(), itr2.next());
		}
	}
	
	private boolean addMapping(TreeNode key, TreeNode value){
		logger.debug("add Mapping:" + key.getValue() + " --> " + value.getValue());
		if (_map.containsKey(key) || _map.containsValue(value))
			return false;
		_map.put(key, value);
		return true;
	}
	
	private void open(TreeNode node, LinkedList<TreeNode> list) {
		list.addAll(node.getChildren());
		Collections.sort(list, heightComparator);
	}

	private LinkedList<TreeNode> pop(LinkedList<TreeNode> list) {
		assert (!list.isEmpty());
		LinkedList<TreeNode> rlt = new LinkedList<TreeNode>();
		int height = list.getFirst().getHeight();
		while (!list.isEmpty() && list.getFirst().getHeight() == height)
			rlt.add(list.removeFirst());
		return rlt;
	}
	
	
	private double dice(TreeNode node1, TreeNode node2, final Map<TreeNode, TreeNode> mappings){
		int matchedCount = 0;
		List<TreeNode> nodes1 = getAllNodes(node1);
		List<TreeNode> nodes2 = getAllNodes(node2);
		double totalCount = nodes1.size() + nodes2.size() - 2;
		for (TreeNode n1: nodes1)
			for (TreeNode n2: nodes2){
				if (mappings.get(n1) == n2)
					matchedCount++;
			}
		return (double) matchedCount* 2.0 / totalCount;
	}
	
	private List<TreeNode> getAllNodes(TreeNode node){
		List<TreeNode> rlt = new LinkedList<TreeNode>();
		rlt.add(node);
		for (TreeNode child : node.getChildren())
			rlt.addAll(getAllNodes(child));
		return rlt;
	}
	
	private boolean isNodeInPairsFirst(TreeNode node, LinkedList<TreeNodePair> pairs){
		for (TreeNodePair pair : pairs){
			if (node == pair.first)
				return true;
		}
		return false;
	}
	
	private boolean isNodeInPairsSecond(TreeNode node, LinkedList<TreeNodePair> pairs){
		for (TreeNodePair pair : pairs){
			if (node == pair.second)
				return true;
		}
		return false;
	}
}
