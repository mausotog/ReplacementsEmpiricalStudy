package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Expression;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTAssertStatement extends JDTTreeNode {

	private final AssertStatement _assert;
	public JDTAssertStatement(ASTNode node) {
		super(node);
		assert(node instanceof AssertStatement);
		_assert = (AssertStatement) node;
	}
	
	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		AssertStatement other = (AssertStatement) referenceNode.getParent();
		if (other.getMessage() == referenceNode){
			_assert.setMessage((Expression) node);
			return true;
		}
		if (other.getExpression() == referenceNode){
			_assert.setExpression((Expression) node);
			return true;
		}
		return false;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTAssertStatement(ast.newAssertStatement());
	}

}
