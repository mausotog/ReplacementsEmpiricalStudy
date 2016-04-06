package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ConstructorInvocation;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTConstructorInvocation extends JDTTreeNode {

	private final ConstructorInvocation _constructorInvocation;
	public JDTConstructorInvocation(ASTNode node) {
		super(node);
		assert(node instanceof ConstructorInvocation);
		_constructorInvocation = (ConstructorInvocation) node;
		_listListChildren.add(_constructorInvocation.arguments());
		_listListChildren.add(_constructorInvocation.typeArguments());
	}

	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTConstructorInvocation(ast.newConstructorInvocation());
	}
	
}
