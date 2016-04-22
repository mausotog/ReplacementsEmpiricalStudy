package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.WhileStatement;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTWhileStatement extends JDTTreeNode {

	private final WhileStatement _while;
	public JDTWhileStatement(ASTNode node) {
		super(node);
		assert (node instanceof WhileStatement);
		_while = (WhileStatement)node;
	}

	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		WhileStatement other = (WhileStatement) referenceNode.getParent();
		if (other == null) return false;
		if (referenceNode == other.getExpression()){
			_while.setExpression((Expression) node);
			return true;
		}
		if (other.getBody() == referenceNode){
			_while.setBody((Statement) node);
			return true;
		}
		return false;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTWhileStatement(ast.newWhileStatement());
	}
	
}
