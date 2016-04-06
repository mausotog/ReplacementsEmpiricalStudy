package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Type;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTQualifiedType extends JDTTreeNode {

	private final QualifiedType _qualifiedType;
	public JDTQualifiedType(ASTNode node) {
		super(node);
		assert(node instanceof QualifiedType);
		_qualifiedType = (QualifiedType) node;
	}
	
	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		QualifiedType other = (QualifiedType) referenceNode.getParent();
		if (other.getName() == referenceNode){
			_qualifiedType.setName((SimpleName) node);
			return true;
		}
		if (other.getQualifier() == referenceNode){
			_qualifiedType.setQualifier((Type) node);
			return true;
		}
		return false;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTQualifiedType(ast.newQualifiedType(null, null));
	}

}
