package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTEnumDeclaration extends JDTTreeNode {

	private final EnumDeclaration _enumDec;
	public JDTEnumDeclaration(ASTNode node) {
		super(node);
		assert (node instanceof EnumDeclaration);
		_enumDec = (EnumDeclaration) node;
		_listListChildren.add(_enumDec.modifiers());
		_listListChildren.add(_enumDec.superInterfaceTypes());
		_listListChildren.add(_enumDec.enumConstants());
		_listListChildren.add(_enumDec.bodyDeclarations());
	}
	
	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		if (((EnumDeclaration)referenceNode.getParent()).getName() == referenceNode){
			_enumDec.setName((SimpleName) node);
			return true;
		}
		return false;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTEnumDeclaration(ast.newEnumDeclaration());
	}

}
