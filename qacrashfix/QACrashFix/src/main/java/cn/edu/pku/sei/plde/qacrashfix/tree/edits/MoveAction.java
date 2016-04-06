package cn.edu.pku.sei.plde.qacrashfix.tree.edits;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;
import cn.edu.pku.sei.plde.qacrashfix.tree.TreeNode;

public class MoveAction extends TreeEditAction{
	private final TreeNode _node;
	private final TreeNode _newParent;
	private final int _index;
	private final TreeNode _referenceNode;
	
	public MoveAction(TreeNode node, TreeNode newParent, int index, TreeNode referenceNode){
		assert(((JDTTreeNode)node).getASTNode().getAST() == ((JDTTreeNode)newParent).getASTNode().getAST());
		_node = node;
		_newParent = newParent;
		_index = index;
		_referenceNode = referenceNode;
	}
	
	public TreeNode getReferenceNode(){
		return _referenceNode;
	}
	
	@Override
	public void Do() {
		_newParent.doMove(this);
	}
	
	public TreeNode getMovedNode(){
		return _node;
	}
	
	public int getIndex(){
		return _index;
	}
	
	public TreeNode getNewParent(){
		return _newParent;
	}
	
	@Override
	public String toString(){
		
			return "move: " + _node.getValue() + " to " + _newParent.getValue() + " at " + _index;
		
	}

	@Override
	public Object accept(IEditActionVisitor visitor) {
		return visitor.visit(this);
	}
}
