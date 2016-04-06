package cn.edu.pku.sei.plde.qacrashfix.tree.edits;

import cn.edu.pku.sei.plde.qacrashfix.tree.TreeNode;

public class ReplaceAction extends TreeEditAction{

	private final TreeNode _replacedNode;
	private final TreeNode _newNode;
	
	public ReplaceAction(TreeNode replacedNode, TreeNode newNode){
		if (replacedNode.getLabel().equals(newNode.getLabel()))
			try {
				throw new Exception(replacedNode.getLabel());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		_replacedNode = replacedNode;
		_newNode = newNode.createNewNodeInTree(replacedNode);
	}
	
	public TreeNode getReplacedNode(){
		return _replacedNode;
	}
	
	public TreeNode getNewNode(){
		return _newNode;
	}

	@Override
	public void Do() {
		_replacedNode.getParent().doReplace(this);
	}
	
	@Override
	public String toString(){
		return "replace: " + _replacedNode.getValue() + " with " + _newNode.getValue(); 
	}

	@Override
	public Object accept(IEditActionVisitor visitor) {
		return visitor.visit(this);
	}
}
