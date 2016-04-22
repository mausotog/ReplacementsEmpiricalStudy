package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;


import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.Type;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTArrayType extends JDTTreeNode {

	private final ArrayType _arrayType;
	public JDTArrayType(ASTNode node) {
		super(node);
		assert (node instanceof ArrayType);
		_arrayType = (ArrayType) node;
	}
	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		if (node == null) return false;
		_arrayType.setComponentType((Type) node);
		return true;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTArrayType(ast.newArrayType(_arrayType.getComponentType()));
	}
}
