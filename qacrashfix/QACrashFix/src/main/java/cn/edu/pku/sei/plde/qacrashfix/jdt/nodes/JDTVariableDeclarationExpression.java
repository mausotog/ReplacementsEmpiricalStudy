package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTVariableDeclarationExpression extends JDTTreeNode {

	private final VariableDeclarationExpression _varDecExpr;
	
	public JDTVariableDeclarationExpression(ASTNode node) {
		super(node);
		assert (node instanceof VariableDeclarationExpression);
		_varDecExpr = (VariableDeclarationExpression) node;
		_listListChildren.add(_varDecExpr.fragments());
		_listListChildren.add(_varDecExpr.modifiers());
	}
	
	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		if (((VariableDeclarationExpression)referenceNode.getParent()).getType() == referenceNode){
			_varDecExpr.setType((Type) node);
			return true;
		}
		return false;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTVariableDeclarationExpression(ast.newVariableDeclarationExpression(null));
	}
}
