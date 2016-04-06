package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTEnumConstantDeclaration extends JDTTreeNode {

	private final EnumConstantDeclaration _enumConsDec;
	public JDTEnumConstantDeclaration(ASTNode node) {
		super(node);
		assert (node instanceof EnumConstantDeclaration);
		_enumConsDec = (EnumConstantDeclaration) node;
		_listListChildren.add(_enumConsDec.arguments());
		_listListChildren.add(_enumConsDec.modifiers());
	}

	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		EnumConstantDeclaration other = (EnumConstantDeclaration) referenceNode.getParent();
		if (other.getName() == referenceNode){
			_enumConsDec.setName((SimpleName) node);
			return true;
		}
		if (other.getAnonymousClassDeclaration() == referenceNode){
			_enumConsDec.setAnonymousClassDeclaration((AnonymousClassDeclaration) node);
			return true;
		}
		return false;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTEnumConstantDeclaration(ast.newEnumConstantDeclaration());
	}
}
