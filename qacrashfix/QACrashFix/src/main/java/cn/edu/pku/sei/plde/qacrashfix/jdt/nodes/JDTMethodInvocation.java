package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;
import cn.edu.pku.sei.plde.qacrashfix.tree.TreeNode;

public class JDTMethodInvocation extends JDTTreeNode {
	private final MethodInvocation _method;
	public JDTMethodInvocation(ASTNode node) {
		super(node);
		assert (node instanceof MethodInvocation);
		_method = (MethodInvocation) node;
		_listListChildren.add(_method.arguments());
		_listListChildren.add(_method.typeArguments());
	}
	
	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		MethodInvocation referenceMethod = (MethodInvocation) referenceNode.getParent();
		node.delete();
		if (referenceNode == referenceMethod.getExpression()){
			_method.setExpression((Expression) node);
			return true;
		}
		if (referenceNode == referenceMethod.getName()){
			_method.setName((SimpleName) node);
			return true;
		}
		return false;
	}
	
	
	@Override
	public TreeNode createNewNodeInTree(TreeNode node){
		return new JDTMethodInvocation(((JDTTreeNode) node).getASTNode().getAST().newMethodInvocation());
	}
	
}
