package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTAnonymousClassDeclaration extends JDTTreeNode {

	private AnonymousClassDeclaration _aClassDec;
	public JDTAnonymousClassDeclaration(ASTNode node) {
		super(node);
		assert (node instanceof AnonymousClassDeclaration);
		_aClassDec = (AnonymousClassDeclaration) node;
		_listListChildren.add(_aClassDec.bodyDeclarations());
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTAnonymousClassDeclaration(ast.newAnonymousClassDeclaration());
	}
}
