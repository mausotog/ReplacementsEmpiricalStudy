package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ThrowStatement;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTThrowStatement extends JDTTreeNode {

	private final ThrowStatement _throw;
	public JDTThrowStatement(ASTNode node) {
		super(node);
		assert(node instanceof ThrowStatement);
		_throw = (ThrowStatement) node;
	}
	
	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		if (((ThrowStatement)referenceNode.getParent()).getExpression() == referenceNode){
			_throw.setExpression((Expression) node);
			return true;
		}
		return false;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTThrowStatement(ast.newThrowStatement());
	}
}
