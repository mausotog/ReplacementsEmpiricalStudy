package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.Type;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTCastExpression extends JDTTreeNode {

	private final CastExpression _cast;
	public JDTCastExpression(ASTNode node) {
		super(node);
		assert (node instanceof CastExpression);
		_cast = (CastExpression) node;
	}

	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		CastExpression other = (CastExpression) referenceNode.getParent();
		if (referenceNode == other.getType()){
			_cast.setType((Type) node);
			return true;
		}
		if (referenceNode == other.getExpression()){
			_cast.setExpression((Expression) node);
			return true;
		}
		return false;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTCastExpression(ast.newCastExpression());
	}
}
