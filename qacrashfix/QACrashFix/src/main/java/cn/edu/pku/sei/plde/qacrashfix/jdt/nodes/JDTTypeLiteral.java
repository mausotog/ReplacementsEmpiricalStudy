package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeLiteral;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTTypeLiteral extends JDTTreeNode {

	private final TypeLiteral _typeLiteral;
	public JDTTypeLiteral(ASTNode node) {
		super(node);
		assert (node instanceof TypeLiteral);
		_typeLiteral = (TypeLiteral) node;
	}
	
	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		if (((TypeLiteral) referenceNode.getParent()).getType() == referenceNode){
			_typeLiteral.setType((Type) node);
			return true;
		}
		return false;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTTypeLiteral(ast.newTypeLiteral());
	}
}
