package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTAnnotationTypeDeclaration extends JDTTreeNode {

	private final AnnotationTypeDeclaration _annoType;
	
	public JDTAnnotationTypeDeclaration(ASTNode node) {
		super(node);
		assert (node instanceof AnnotationTypeDeclaration);
		_annoType = (AnnotationTypeDeclaration) node;
		_listListChildren.add(_annoType.modifiers());
		_listListChildren.add(_annoType.bodyDeclarations());
	}
	
	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		if (((AnnotationTypeDeclaration)referenceNode.getParent()).getName() == referenceNode){
			_annoType.setName((SimpleName) node);
			return true;
		}
		return false;
	}

	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTAnnotationTypeDeclaration(ast.newAnnotationTypeDeclaration());
	}
}
