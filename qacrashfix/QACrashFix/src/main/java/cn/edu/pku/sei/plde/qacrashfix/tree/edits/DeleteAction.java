package cn.edu.pku.sei.plde.qacrashfix.tree.edits;

import cn.edu.pku.sei.plde.qacrashfix.tree.TreeNode;

/**
 * @author Hansheng Zhang
 * Delete action, for deleting a node from the tree
 */

public class DeleteAction extends TreeEditAction{
	private final TreeNode _deletedNode;
	public DeleteAction(TreeNode deletedNode) {
		_deletedNode = deletedNode;
	}
	
	
	@Override
	public void Do() {
		_deletedNode.getParent().doDelete(this);
	}

	public TreeNode getDeletedNode() {
		return _deletedNode;
	}
	
	@Override
	public String toString() {
		return "delete " +  _deletedNode.getValue() + " under " + _deletedNode.getParent().getValue();
	}

	@Override
	public Object accept(IEditActionVisitor visitor) {
		return visitor.visit(this);
	}

}
