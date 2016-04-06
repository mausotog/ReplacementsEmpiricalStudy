package cn.edu.pku.sei.plde.qacrashfix.tree.edits;

import cn.edu.pku.sei.plde.qacrashfix.tree.TreeNode;

public class InsertAction extends TreeEditAction {
	
	private final TreeNode _insertedNode;
	private final TreeNode _parentNode;
	private final TreeNode _referenceNode;
	private final int _index;
	
	public InsertAction(TreeNode insertedNode, TreeNode parentNode, int position){
		_referenceNode = insertedNode;
		_insertedNode = insertedNode.createNewNodeInTree(parentNode);
		_parentNode = parentNode;
		_index = position;
	}
	
	public int getIndex(){
		return _index;
	}
	public TreeNode getInsertedNode(){
		return _insertedNode;
	}
	
	public TreeNode getReferenceNode(){
		return _referenceNode;
	}
	
	public TreeNode getParentNode(){
		return _parentNode;
	}
	
	@Override
	public void Do() {
		if (_parentNode != null)
			_parentNode.doInsert(this);
	}
	
	@Override
	public String toString(){
		return "insertion: " + _insertedNode.getValue()+ " under " + _parentNode.getValue() + " at " + _index;
	}

	@Override
	public Object accept(IEditActionVisitor visitor) {
		return visitor.visit(this);
	}
}
