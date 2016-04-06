package cn.edu.pku.sei.plde.qacrashfix.tree;

/**
 * Declares a visitor to the tree node
 * @author Hansheng Zhang
 */

public abstract class TreeNodeVisitor {
	/**
	 * Inner visiting of a visitor: breadth first, depth first, bottom up, etc
	 * @param node
	 */
	protected abstract void implementVisit(TreeNode node);
	
	/***
	 * Visit a node
	 * @param node
	 */
	
	public abstract void visit(TreeNode node);
}
