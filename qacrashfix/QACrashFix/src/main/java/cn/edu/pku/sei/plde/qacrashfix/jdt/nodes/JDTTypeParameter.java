package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeParameter;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTTypeParameter extends JDTTreeNode {

	private final TypeParameter _typePara;
	public JDTTypeParameter(ASTNode node) {
		super(node);
		assert (node instanceof TypeParameter);
		_typePara = (TypeParameter) node;
		_listListChildren.add(_typePara.typeBounds());
	}

	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		if (((TypeParameter) referenceNode.getParent()).getName() == referenceNode){
			_typePara.setName((SimpleName) node);
			return true;
		}
		return false;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTTypeParameter(ast.newTypeParameter());
	}
}
