package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.PrefixExpression;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTPrefixExpression extends JDTTreeNode {

	private final PrefixExpression _prefixExpr;
	public JDTPrefixExpression(ASTNode node) {
		super(node);
		assert(node instanceof PrefixExpression);
		_prefixExpr = (PrefixExpression) node;
	}
	
	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		if (((PrefixExpression)referenceNode.getParent()).getOperand() == referenceNode){
			_prefixExpr.setOperand((Expression) node);
			return true;
		}
		return false;
	}

	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		PrefixExpression prefixExpr = ast.newPrefixExpression();
		prefixExpr.setOperator(_prefixExpr.getOperator());
		return new JDTPrefixExpression(prefixExpr);
	}
}
