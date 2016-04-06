package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.BooleanLiteral;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTBooleanLiteral extends JDTTreeNode {

	private final BooleanLiteral _bool;
	public JDTBooleanLiteral(ASTNode node) {
		super(node);
		assert (node instanceof BooleanLiteral);
		_bool = (BooleanLiteral) node;
	}
	@Override
	public Object getValue() {
		return _bool.booleanValue();
	}
	
	@Override
	public boolean setValue(Object value) {
		_bool.setBooleanValue((boolean)value);
		return true;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTBooleanLiteral(ast.newBooleanLiteral(_bool.booleanValue()));
	}
}
