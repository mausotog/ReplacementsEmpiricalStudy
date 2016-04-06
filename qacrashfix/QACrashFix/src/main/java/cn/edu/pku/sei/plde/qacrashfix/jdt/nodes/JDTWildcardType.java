package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.WildcardType;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTWildcardType extends JDTTreeNode {

	private final WildcardType _wildType;
	public JDTWildcardType(ASTNode node) {
		super(node);
		assert (node instanceof WildcardType);
		_wildType = (WildcardType) node;
	}

	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		WildcardType other =(WildcardType) referenceNode.getParent();
		if (other.getBound() == referenceNode){
			_wildType.setBound((Type) node, other.isUpperBound());
			return true;
		}
		return false;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		WildcardType wild = ast.newWildcardType();
		wild.setUpperBound(_wildType.isUpperBound());
		return new JDTWildcardType(wild);
	}
}
