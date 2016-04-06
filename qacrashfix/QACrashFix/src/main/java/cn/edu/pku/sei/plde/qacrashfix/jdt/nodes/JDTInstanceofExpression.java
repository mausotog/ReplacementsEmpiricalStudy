package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.Type;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTInstanceofExpression extends JDTTreeNode {

	private final InstanceofExpression _instanceOf;
	public JDTInstanceofExpression(ASTNode node) {
		super(node);
		assert (node instanceof InstanceofExpression);
		_instanceOf = (InstanceofExpression) node;
	}
	
	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		InstanceofExpression other = (InstanceofExpression) referenceNode.getParent();
		if (referenceNode == other.getLeftOperand()){
			_instanceOf.setLeftOperand((Expression) node);
			return true;
		}
		if (referenceNode == other.getRightOperand()){
			_instanceOf.setRightOperand((Type) node);
			return true;
		}
		return false;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTInstanceofExpression(ast.newInstanceofExpression());
	}

}
