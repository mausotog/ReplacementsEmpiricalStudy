package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTLabeledStatement extends JDTTreeNode {

	private final LabeledStatement _labelStmt;
	public JDTLabeledStatement(ASTNode node) {
		super(node);
		assert(node instanceof LabeledStatement);
		_labelStmt = (LabeledStatement) node;
	}
	
	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		LabeledStatement other = (LabeledStatement) referenceNode.getParent();
		if (other.getLabel() == referenceNode){
			_labelStmt.setLabel((SimpleName) node);
			return true;
		}
		if (other.getBody() == referenceNode){
			_labelStmt.setBody((Statement) node);
			return true;
		}
		return false;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTLabeledStatement(ast.newLabeledStatement());
	}

}
