package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.Statement;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTDoStatement extends JDTTreeNode {

	private final DoStatement _do;
	public JDTDoStatement(ASTNode node) {
		super(node);
		assert (node instanceof DoStatement);
		_do = (DoStatement) node;
	}

	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		DoStatement other = (DoStatement) referenceNode.getParent();
		if (referenceNode == other.getExpression()){
			_do.setExpression((Expression) node);
			return true;
		}
		if (referenceNode == other.getBody()){
			_do.setBody((Statement) node);
			return true;
		}
		return false;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTDoStatement(ast.newDoStatement());
	}
}
