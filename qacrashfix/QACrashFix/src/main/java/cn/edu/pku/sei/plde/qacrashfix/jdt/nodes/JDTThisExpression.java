package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.ThisExpression;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTThisExpression extends JDTTreeNode {

	private final ThisExpression _thisExpression;
	public JDTThisExpression(ASTNode node) {
		super(node);
		assert (node instanceof ThisExpression);
		_thisExpression = (ThisExpression) node;
	}
	
	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		if (((ThisExpression)referenceNode.getParent()).getQualifier() == referenceNode){
			_thisExpression.setQualifier((Name) node);
			return true;
		}
		return false;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTThisExpression(ast.newThisExpression());
	}
}

