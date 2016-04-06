package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.SimpleName;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTFieldAccess extends JDTTreeNode {

	private final FieldAccess _fieldAccess;
	
	public JDTFieldAccess(ASTNode node) {
		super(node);
		assert (node instanceof FieldAccess);
		_fieldAccess = (FieldAccess) node;
	}

	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		FieldAccess other = (FieldAccess) referenceNode.getParent();
		if (referenceNode == other.getName()){
			_fieldAccess.setName((SimpleName) node);
			return true;
		}
		if (referenceNode == other.getExpression()){
			_fieldAccess.setExpression((Expression) node);
			return true;
		}
		return false;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTFieldAccess(ast.newFieldAccess());
	}
	
}
