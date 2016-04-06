package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SwitchCase;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTSwitchCase extends JDTTreeNode {

	private final SwitchCase _case;
	public JDTSwitchCase(ASTNode node) {
		super(node);
		assert(node instanceof SwitchCase);
		_case = (SwitchCase) node;
	}
	
	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		if (((SwitchCase)referenceNode.getParent()).getExpression() == referenceNode){
			_case.setExpression((Expression) node);
			return true;
		}
		return false;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTSwitchCase(ast.newSwitchCase());
	}
}
