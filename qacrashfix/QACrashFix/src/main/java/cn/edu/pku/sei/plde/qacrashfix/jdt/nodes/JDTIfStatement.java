package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Statement;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTIfStatement extends JDTTreeNode {

	private final IfStatement _ifStmt; 
	public JDTIfStatement(ASTNode node) {
		super(node);
		assert(node instanceof IfStatement);
		_ifStmt = (IfStatement) node;
	}

	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTIfStatement(ast.newIfStatement());
	}
	
	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		return alignChild(node, referenceNode);
	}
	
	private boolean alignChild(ASTNode node, ASTNode referenceNode){
		assert(node.getAST() == _ifStmt.getAST());
		IfStatement ifStmtThat = (IfStatement) referenceNode.getParent();
		if (referenceNode == ifStmtThat.getExpression())
			_ifStmt.setExpression((Expression) node);
		else if (referenceNode == ifStmtThat.getThenStatement())
			_ifStmt.setThenStatement((Statement) node);
		else if (referenceNode == ifStmtThat.getElseStatement())
			_ifStmt.setElseStatement((Statement) node);
		else {
			assert(false);
			return false;
		}
		return true;
	}
}
