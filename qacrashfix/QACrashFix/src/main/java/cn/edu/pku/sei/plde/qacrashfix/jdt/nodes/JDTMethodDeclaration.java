package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Type;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTMethodDeclaration extends JDTTreeNode {
	private final MethodDeclaration _methodDeclaration;
	
	public JDTMethodDeclaration(ASTNode node) {
		super(node);
		_methodDeclaration  = (MethodDeclaration) node;
		_listListChildren.add(_methodDeclaration.modifiers());
		_listListChildren.add(_methodDeclaration.parameters());
		_listListChildren.add(_methodDeclaration.thrownExceptions());
		_listListChildren.add(_methodDeclaration.typeParameters());
	}
	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		MethodDeclaration other= (MethodDeclaration) referenceNode.getParent();
		if (referenceNode==other.getName()){
			_methodDeclaration.setName((SimpleName) node);
			return true;
		}
		if (referenceNode == other.getReturnType2()){
			_methodDeclaration.setReturnType2((Type) node);
			return true;
		}
		if (referenceNode == other.getBody()){
			_methodDeclaration.setBody((Block) node);
			return true;
		}
		return false;
	}
	
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTMethodDeclaration(ast.newMethodDeclaration());
	}
	
}
