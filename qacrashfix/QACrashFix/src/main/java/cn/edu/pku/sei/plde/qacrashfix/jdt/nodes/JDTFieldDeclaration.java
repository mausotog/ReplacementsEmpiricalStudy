package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Type;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTFieldDeclaration extends JDTTreeNode {

	private final FieldDeclaration _fieldDec;
	
	public JDTFieldDeclaration(ASTNode node) {
		super(node);
		assert (node instanceof FieldDeclaration);
		_fieldDec = (FieldDeclaration) node;
		_listListChildren.add(_fieldDec.fragments());
	}
	
	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		if (node == null) return false;
		if (referenceNode == ((FieldDeclaration)referenceNode.getParent()).getType()){
			_fieldDec.setType((Type) node);
			return true;
		}
		return false;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTFieldDeclaration(ast.newFieldDeclaration(ast.newVariableDeclarationFragment()));
	}

}
