package cn.edu.pku.sei.plde.qacrashfix.tree;

import java.util.Iterator;
import java.util.Map;

public abstract class TreeNodeMapper {
	private TreeNode _srcTree;
	private TreeNode _destTree;
	protected Map<TreeNode, TreeNode> _map;
	
	
	public TreeNodeMapper(TreeNode sourceTree, TreeNode destinationTree){
		_srcTree = sourceTree;
		_destTree = destinationTree;
	}
	
	protected abstract void generateMappings();
	
	public Map<TreeNode, TreeNode> getMappings(){
		if (_map==null)
			generateMappings();
		return _map;
	}
	
	public TreeNode get(TreeNode node){
		return _map.get(node);
	}
	
	public TreeNode getSourceTree(){
		return _srcTree;
	}
	
	public void setSourceTree(TreeNode srcTree){
		_srcTree = srcTree;
	}
	
	public TreeNode getDestinationTree(){
		return _destTree;
	}
	
	public void setDestinationTree(TreeNode destTree){
		_destTree = destTree;
	}
	
	public final static class TreeNodePair{
		public final TreeNode first;
		public final TreeNode second;
		public TreeNodePair(TreeNode first, TreeNode second){
			this.first = first;
			this.second  = second;
		}
		
	}
	
	public static void mapDescendants(Map<TreeNode, TreeNode> mappings, TreeNode t1, TreeNode t2){
		mappings.put(t1, t2);
		Iterator<TreeNode> itr1 = t1.getChildren().iterator();
		Iterator<TreeNode> itr2 = t2.getChildren().iterator();
		while (itr1.hasNext()){
			mapDescendants(mappings, itr1.next(), itr2.next());
		}
	}
	
	public void put(TreeNode key, TreeNode value){
		_map.put(key, value);
	}
	
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		for (Map.Entry<TreeNode, TreeNode> entry : getMappings().entrySet()){
			stringBuilder.append("mapping: ");
			stringBuilder.append(entry.getKey().getValue());
			stringBuilder.append(" ----> ");
			stringBuilder.append(entry.getValue().getValue());
			stringBuilder.append("\n");
		}
		return stringBuilder.toString();
	}
	
}
