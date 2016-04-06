package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Type;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTAnnotationTypeMemberDeclaration extends JDTTreeNode {

	private final AnnotationTypeMemberDeclaration _annoType;
	
	public JDTAnnotationTypeMemberDeclaration(ASTNode node) {
		super(node);
		assert (node instanceof AnnotationTypeMemberDeclaration);
		_annoType = (AnnotationTypeMemberDeclaration) node;
		_listListChildren.add(_annoType.modifiers());
	}

	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		AnnotationTypeMemberDeclaration other = (AnnotationTypeMemberDeclaration) referenceNode.getParent();
		if (other.getName() == referenceNode){
			_annoType.setName((SimpleName) node);
			return true;
		}
		if (other.getType() == referenceNode){
			_annoType.setType((Type) node);
			return true;
		}
		if (other.getDefault() == referenceNode){
			_annoType.setDefault((Expression) node);
			return true;
		}
		return false;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTAnnotationTypeMemberDeclaration(ast.newAnnotationTypeMemberDeclaration());
	}
	
}
