package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTSingleMemberAnnotation extends JDTTreeNode {

	private final SingleMemberAnnotation _singleMemberAnno;
	
	public JDTSingleMemberAnnotation(ASTNode node) {
		super(node);
		assert (node instanceof SingleMemberAnnotation);
		_singleMemberAnno = (SingleMemberAnnotation) node;
	}
	
	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		SingleMemberAnnotation other = (SingleMemberAnnotation) referenceNode.getParent();
		if (referenceNode == other.getTypeName()){
			_singleMemberAnno.setTypeName((Name) node);
			return true;
		}
		if (referenceNode == other.getValue()){
			_singleMemberAnno.setValue((Expression) node);
			return true;
		}
		return false;
	}

	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTSingleMemberAnnotation(ast.newSingleMemberAnnotation());
	}
	
}
