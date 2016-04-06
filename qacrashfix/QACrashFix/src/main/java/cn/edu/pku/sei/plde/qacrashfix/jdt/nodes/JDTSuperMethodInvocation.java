package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTSuperMethodInvocation extends JDTTreeNode {
	
	private final SuperMethodInvocation _superMethod;
	public JDTSuperMethodInvocation(ASTNode node) {
		super(node);
		assert (node instanceof SuperMethodInvocation);
		_superMethod = (SuperMethodInvocation) node;
		_listListChildren.add(_superMethod.arguments());
		_listListChildren.add(_superMethod.typeArguments());
	}
	
	public SuperMethodInvocation getSuperMethodInvocation(){
		return _superMethod;
	}
	
	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		SuperMethodInvocation other = (SuperMethodInvocation) referenceNode.getParent();
		if (referenceNode == other.getName()){
			_superMethod.setName((SimpleName) node);
			return true;
		}
		if (referenceNode == other.getQualifier()){
			_superMethod.setQualifier((Name) node);
			return true;
		}
		return false;
	}

	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTSuperMethodInvocation(ast.newSuperMethodInvocation());
	}

}
