package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTTypeDeclarationStatement extends JDTTreeNode {

	private final TypeDeclarationStatement _typeDecStmt;
	public JDTTypeDeclarationStatement(ASTNode node) {
		super(node);
		assert(node instanceof TypeDeclarationStatement);
		_typeDecStmt = (TypeDeclarationStatement) node;
	}
	
	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		if (((TypeDeclarationStatement)referenceNode.getParent()).getDeclaration() == referenceNode){
			_typeDecStmt.setDeclaration((AbstractTypeDeclaration) node);
			return true;
		}
		return false;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTTypeDeclarationStatement(ASTNode.copySubtree(ast, _typeDecStmt));
	}

}
