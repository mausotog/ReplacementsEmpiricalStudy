package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTInfixExpression extends JDTTreeNode {

	private final InfixExpression _infixExpr;
	public JDTInfixExpression(ASTNode node) {
		super(node);
		assert(node instanceof InfixExpression);
		_infixExpr = (InfixExpression) node;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		InfixExpression infixExpr = ast.newInfixExpression();
		infixExpr.setOperator(_infixExpr.getOperator());
		return new JDTInfixExpression(infixExpr);
	}
	
	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		InfixExpression other = (InfixExpression) referenceNode.getParent();
		if (referenceNode == other.getLeftOperand()){
			_infixExpr.setLeftOperand((Expression) node);
			return true;
		}
		if (referenceNode == other.getRightOperand()){
			_infixExpr.setRightOperand((Expression) node);
			return true;
		}
		return false;
	}
}
