package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.PostfixExpression;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTPostfixExpression extends JDTTreeNode {

	private final PostfixExpression _postExpr;
	public JDTPostfixExpression(ASTNode node) {
		super(node);
		assert(node instanceof PostfixExpression);
		_postExpr = (PostfixExpression) node;
	}
	
	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		if (((PostfixExpression) referenceNode.getParent()).getOperand() == referenceNode){
			_postExpr.setOperand((Expression) node);
			return true;
		}
		return false;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		PostfixExpression postExpr = ast.newPostfixExpression();
		postExpr.setOperator(_postExpr.getOperator());
		return new JDTPostfixExpression(postExpr);
	}

}
