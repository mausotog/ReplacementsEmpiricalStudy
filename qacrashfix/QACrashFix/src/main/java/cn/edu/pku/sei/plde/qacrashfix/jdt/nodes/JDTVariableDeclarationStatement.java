package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTVariableDeclarationStatement extends JDTTreeNode {

	private final VariableDeclarationStatement _variableDeclarationStatement;
	
	public JDTVariableDeclarationStatement(ASTNode node) {
		super(node);
		assert(node instanceof VariableDeclarationStatement);
		_variableDeclarationStatement = (VariableDeclarationStatement) node;
		_listListChildren.add(_variableDeclarationStatement.modifiers());
		_listListChildren.add(_variableDeclarationStatement.fragments());
	}
	
	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		if (((VariableDeclarationStatement)referenceNode.getParent()).getType() == referenceNode){
			_variableDeclarationStatement.setType((Type) node);
			return true;
		}
		return false;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTVariableDeclarationStatement(ast.newVariableDeclarationStatement(null));
	}

}
