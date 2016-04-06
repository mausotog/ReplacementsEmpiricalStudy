package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ReturnStatement;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTReturnStatement extends JDTTreeNode {

	private final ReturnStatement _returnStmt;
	public JDTReturnStatement(ASTNode node) {
		super(node);
		assert (node instanceof ReturnStatement);
		_returnStmt = (ReturnStatement) node;
	}

	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		if (((ReturnStatement)referenceNode.getParent()).getExpression() == referenceNode){
			_returnStmt.setExpression((Expression) node);
			return true;
		}
		return false;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTReturnStatement(ast.newReturnStatement());
	}
	
}
