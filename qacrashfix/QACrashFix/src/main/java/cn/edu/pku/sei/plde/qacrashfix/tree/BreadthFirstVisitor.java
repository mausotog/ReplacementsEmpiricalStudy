package cn.edu.pku.sei.plde.qacrashfix.tree;

import java.util.LinkedList;

/**
 * Implement a breadth first visitor
 * @author Hansheng Zhang
 */

public abstract class BreadthFirstVisitor extends TreeNodeVisitor {
	@Override
	protected void implementVisit(TreeNode node) {
		if (node == null)
			return;
		LinkedList<TreeNode> list = new LinkedList<TreeNode>();
		list.add(node);
		while (!list.isEmpty()){
			TreeNode toVisit = list.removeFirst();
			visit(toVisit);
			list.addAll(toVisit.getChildren());
		}
	}

	@Override
	public abstract void visit(TreeNode node);

}
