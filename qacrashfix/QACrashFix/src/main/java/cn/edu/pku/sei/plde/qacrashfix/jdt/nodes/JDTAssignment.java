package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Expression;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTAssignment extends JDTTreeNode {

	private final Assignment _assign;
	public JDTAssignment(ASTNode node) {
		super(node);
		assert (node instanceof Assignment);
		_assign = (Assignment) node;
	}
	
	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		Assignment other = (Assignment) referenceNode.getParent();
		
		if (referenceNode == other.getLeftHandSide()){
			if (!(node instanceof Expression)) return false;
			_assign.setLeftHandSide((Expression) node);
			return true;
		}
		
		if (referenceNode == other.getRightHandSide() ){
			_assign.setRightHandSide((Expression) node);
			return true;
		}
		
		return false;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		Assignment assign = ast.newAssignment();
		assign.setOperator(_assign.getOperator());
		return new JDTAssignment(assign);
	}
}
