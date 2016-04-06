package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTSuperConstructorInvocation extends JDTTreeNode {

	private SuperConstructorInvocation _superConstructor;
	public JDTSuperConstructorInvocation(ASTNode node) {
		super(node);
		assert(node instanceof SuperConstructorInvocation);
		_superConstructor = (SuperConstructorInvocation) node;
		_listListChildren.add(_superConstructor.arguments());
		_listListChildren.add(_superConstructor.typeArguments());
	}

	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		if (((SuperConstructorInvocation)referenceNode.getParent()).getExpression() == referenceNode){
			_superConstructor.setExpression((Expression) node);
			return true;
		}
		return false;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTSuperConstructorInvocation(ast.newSuperConstructorInvocation());
	}
	
}
