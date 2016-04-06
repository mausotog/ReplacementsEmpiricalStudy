package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SuperFieldAccess;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTSuperFieldAccess extends JDTTreeNode {

	private final SuperFieldAccess _superField;
	public JDTSuperFieldAccess(ASTNode node) {
		super(node);
		assert(node instanceof SuperFieldAccess);
		_superField = (SuperFieldAccess) node;
	}
	
	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		SuperFieldAccess other = (SuperFieldAccess) referenceNode.getParent();
		if (other.getName() == referenceNode){
			_superField.setName((SimpleName) node);
			return true;
		}
		if (other.getQualifier() == referenceNode){
			_superField.setQualifier((Name) node);
			return true;
		}
		return false;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTSuperFieldAccess(ast.newSuperFieldAccess());
	}

}
