package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTParenthesizedExpression extends JDTTreeNode {

	private final ParenthesizedExpression _parenExpr;
	public JDTParenthesizedExpression(ASTNode node) {
		super(node);
		assert(node instanceof ParenthesizedExpression);
		_parenExpr = (ParenthesizedExpression) node;
	}
	
	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		if (((ParenthesizedExpression)referenceNode.getParent()).getExpression() == referenceNode){
			_parenExpr.setExpression((Expression) node);
			return true;
		}
		return false;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTParenthesizedExpression(ast.newParenthesizedExpression());
	}

}
