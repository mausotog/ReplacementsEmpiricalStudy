package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.Type;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTClassInstanceCreation extends JDTTreeNode {

	private final ClassInstanceCreation _classInstanceCreation;
	public JDTClassInstanceCreation(ASTNode node) {
		super(node);
		assert (node instanceof ClassInstanceCreation);
		_classInstanceCreation = (ClassInstanceCreation) node;
		_listListChildren.add(_classInstanceCreation.arguments());
		_listListChildren.add(_classInstanceCreation.typeArguments());
	}
	
	
	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		ClassInstanceCreation other = (ClassInstanceCreation) referenceNode.getParent();
		if (referenceNode == other.getAnonymousClassDeclaration()){
			_classInstanceCreation.setAnonymousClassDeclaration((AnonymousClassDeclaration) node);
			return true;
		}
		if (referenceNode == other.getExpression()){
			_classInstanceCreation.setExpression((Expression) node);
			return true;
		}
		if (referenceNode == other.getType()){
			_classInstanceCreation.setType((Type) node);
			return true;
		}
		return false;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTClassInstanceCreation(ast.newClassInstanceCreation());
	}
	
}
