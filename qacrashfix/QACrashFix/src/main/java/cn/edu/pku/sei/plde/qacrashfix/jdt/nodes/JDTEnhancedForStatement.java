package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTEnhancedForStatement extends JDTTreeNode {

	private final EnhancedForStatement _enhancedFor;
	public JDTEnhancedForStatement(ASTNode node) {
		super(node);
		assert (node instanceof EnhancedForStatement);
		_enhancedFor = (EnhancedForStatement) node;
	}

	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		EnhancedForStatement other = (EnhancedForStatement) referenceNode.getParent();
		if (other == null) return false;
		if (referenceNode == other.getParameter()){
			_enhancedFor.setParameter((SingleVariableDeclaration) node);
			return true;
		}
		if (referenceNode == other.getExpression()){
			if (!(node instanceof Expression)) return false;
			_enhancedFor.setExpression((Expression) node);
			return true;
		}
		if (referenceNode == other.getBody()){
			if (node == null) return false;
			_enhancedFor.setBody((Statement) node);
			return true;
		}
		return false;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTEnhancedForStatement(ast.newEnhancedForStatement());
	}
	
}
