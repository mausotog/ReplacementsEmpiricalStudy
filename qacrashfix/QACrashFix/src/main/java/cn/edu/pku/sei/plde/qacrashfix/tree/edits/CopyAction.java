package cn.edu.pku.sei.plde.qacrashfix.tree.edits;

import cn.edu.pku.sei.plde.qacrashfix.tree.TreeNode;

public class CopyAction extends TreeEditAction{

	private final TreeNode _node;
	private final TreeNode _parent;
	private final int _index;
	private final TreeNode _referenceNode;
	private final TreeNode _oldNode;
	public CopyAction(TreeNode node, TreeNode parent, int index, TreeNode referenceNode){
		_node = node.makeDeepCopyInTree(node);
		_parent = parent;
		_index = index;
		_referenceNode  = referenceNode;
		_oldNode = node;
	}
	
	public int getIndex(){
		return _index;
	}
	
	public TreeNode getCopiedNode(){
		return _node;
	}
	
	public TreeNode getOldNode(){
		return _oldNode;
	}
	
	public TreeNode getReferenceNode(){
		return _referenceNode;
	}
	
	public TreeNode getParentNode(){
		return _parent;
	}
	
	@Override
	public void Do() {
		_parent.doCopy(this);
	}
	@Override
	public String toString() {
		return "copy: "+ _node.getValue() + " under " + _parent.getValue() + " at " + _index;
	}

	@Override
	public Object accept(IEditActionVisitor visitor) {
		return visitor.visit(this);
	}
}
