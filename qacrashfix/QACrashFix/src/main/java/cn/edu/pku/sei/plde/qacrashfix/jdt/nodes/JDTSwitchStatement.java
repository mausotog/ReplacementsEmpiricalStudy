package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SwitchStatement;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTSwitchStatement extends JDTTreeNode {

	private final SwitchStatement _switchStmt;
	public JDTSwitchStatement(ASTNode node) {
		super(node);
		assert(node instanceof SwitchStatement);
		_switchStmt = (SwitchStatement) node;
		_listListChildren.add(_switchStmt.statements());
	}
	
	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		if (((SwitchStatement)referenceNode.getParent()).getExpression() == referenceNode){
			_switchStmt.setExpression((Expression) node);
			return true;
		}
		return false;
	}
	
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTSwitchStatement(ast.newSwitchStatement());
	}
	
	
}
