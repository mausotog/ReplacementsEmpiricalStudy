package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.Expression;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTArrayAccess extends JDTTreeNode {

	private final ArrayAccess _arrayAccess;
	public JDTArrayAccess(ASTNode node) {
		super(node);
		assert(node instanceof ArrayAccess);
		_arrayAccess = (ArrayAccess) node;
	}
	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		ArrayAccess other = (ArrayAccess) referenceNode.getParent();
		if (referenceNode == other.getArray()){
			_arrayAccess.setArray((Expression) node);
			return true;
		}
		if (referenceNode == other.getIndex()){
			_arrayAccess.setIndex((Expression) node);
			return true;
		}
		return false;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTArrayAccess(ast.newArrayAccess());
	}
}
