package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.SimpleType;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTSimpleType extends JDTTreeNode {

	private final SimpleType _simpleType;
	
	public JDTSimpleType(ASTNode node) {
		super(node);
		assert (node instanceof SimpleType);
		_simpleType = (SimpleType) node;
	}

	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		SimpleType parentThat = (SimpleType) referenceNode.getParent();
		if (referenceNode == parentThat.getName()){
			_simpleType.setName((Name) node);
			return true;
		}
		return false;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTSimpleType(ast.newSimpleType(ast.newSimpleName("_TEMP_")));
	}
}
