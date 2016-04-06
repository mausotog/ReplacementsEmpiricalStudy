package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.Statement;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTForStatement extends JDTTreeNode {

	private final ForStatement _for;
	
	public JDTForStatement(ASTNode node) {
		super(node);
		assert (node instanceof ForStatement);
		_for = (ForStatement) node;
		_listListChildren.add(_for.initializers());
		_listListChildren.add(_for.updaters());
	}
	
	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		ForStatement other = (ForStatement) referenceNode.getParent();
		if (referenceNode == other.getExpression()){
			_for.setExpression((Expression) node);
			return true;
		}
		if (referenceNode == other.getBody()){
			_for.setBody((Statement) node);
			return true;
		}
		return false;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTForStatement(ast.newForStatement());
	}
	
}
