package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.SimpleName;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTBreakStatement extends JDTTreeNode {

	private final BreakStatement _break;
	public JDTBreakStatement(ASTNode node) {
		super(node);
		assert (node instanceof BreakStatement);
		_break = (BreakStatement) node;
	}
	
	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		BreakStatement other = (BreakStatement) referenceNode.getParent();
		if (referenceNode == other.getLabel()){
			_break.setLabel((SimpleName) node);
			return true;
		}
		return false;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTBreakStatement(ast.newBreakStatement());
	}
	
}
