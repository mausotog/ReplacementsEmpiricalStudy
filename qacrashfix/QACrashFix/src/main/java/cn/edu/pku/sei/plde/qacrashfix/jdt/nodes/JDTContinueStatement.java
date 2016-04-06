package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.SimpleName;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTContinueStatement extends JDTTreeNode {

	private final ContinueStatement _cont;
	
	public JDTContinueStatement(ASTNode node) {
		super(node);
		assert(node instanceof ContinueStatement);
		_cont = (ContinueStatement) node;
	}

	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		if (referenceNode == ((ContinueStatement) referenceNode.getParent()).getLabel()){
			_cont.setLabel((SimpleName) node);
			return true;
		}
		return false;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTContinueStatement(ast.newContinueStatement());
	}
}
