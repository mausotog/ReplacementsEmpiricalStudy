package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.Expression;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTConditionalExpression extends JDTTreeNode {

	private final ConditionalExpression _condExpr;
	
	public JDTConditionalExpression(ASTNode node) {
		super(node);
		assert (node instanceof ConditionalExpression);
		_condExpr = (ConditionalExpression) node;
	}

	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		ConditionalExpression other = (ConditionalExpression) referenceNode.getParent();
		if (referenceNode == other.getExpression()){
			_condExpr.setExpression((Expression) node);
			return true;
		}
		if (referenceNode == other.getThenExpression()){
			_condExpr.setThenExpression((Expression) node);
			return true;
		}
		if (referenceNode == other.getElseExpression()){
			_condExpr.setElseExpression((Expression) node);
			return true;
		}
		return false;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTConditionalExpression(ast.newConditionalExpression());
	}
	
}
