package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.SimpleName;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTMemberValuePair extends JDTTreeNode {

	private final MemberValuePair _memberValue;
	public JDTMemberValuePair(ASTNode node) {
		super(node);
		assert(node instanceof MemberValuePair);
		_memberValue = (MemberValuePair) node;
	}
	
	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		MemberValuePair other = (MemberValuePair) referenceNode.getParent();
		if (other.getName() == referenceNode){
			_memberValue.setName((SimpleName) node);
			return true;
		}
		if (other.getValue() == referenceNode){
			other.setValue((Expression) node);
			return true;
		}
		return false;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTMemberValuePair(ast.newMemberValuePair());
	}

}
