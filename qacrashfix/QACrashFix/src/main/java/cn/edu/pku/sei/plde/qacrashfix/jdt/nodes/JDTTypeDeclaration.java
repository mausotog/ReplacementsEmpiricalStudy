package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTTypeDeclaration extends JDTTreeNode {

	private final TypeDeclaration _typeDeclaration;
	public JDTTypeDeclaration(ASTNode node) {
		super(node);
		assert(node instanceof TypeDeclaration);
		_typeDeclaration = (TypeDeclaration) node;
		_listListChildren.add(_typeDeclaration.typeParameters());
		_listListChildren.add(_typeDeclaration.bodyDeclarations());
		_listListChildren.add(_typeDeclaration.modifiers());
	}

	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		if (referenceNode == null) return false;
		TypeDeclaration other = (TypeDeclaration) referenceNode.getParent();
		if (other == null) return false;
		if (_typeDeclaration == null) return false;
		if (referenceNode == other.getSuperclassType()){
			if (!(node instanceof Type)) return false;
			_typeDeclaration.setSuperclassType((Type) node);
			return true;
		}
		if (referenceNode == other.getName()){
			if (!(node instanceof SimpleName)) return false;
			_typeDeclaration.setName((SimpleName) node);
			return true;
		}
		return false;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		TypeDeclaration type = ast.newTypeDeclaration();
		type.setInterface(_typeDeclaration.isInterface());
		return new JDTTypeDeclaration(type);
	}
	
}
