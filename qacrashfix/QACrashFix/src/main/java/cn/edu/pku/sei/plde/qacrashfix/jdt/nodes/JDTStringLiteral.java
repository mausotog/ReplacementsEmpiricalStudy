package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.StringLiteral;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTStringLiteral extends JDTTreeNode {

	private final StringLiteral _strLiteral;
	
	public JDTStringLiteral(ASTNode node) {
		super(node);
		_strLiteral = (StringLiteral) node;
	}

	@Override
	public Object getValue(){
		return _strLiteral.getLiteralValue();
	}
	
	@Override
	public boolean setValue(Object value) {
		assert (value instanceof String);
		_strLiteral.setLiteralValue((String) value);
		return true;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		StringLiteral newStr = ast.newStringLiteral();
		newStr.setLiteralValue(_strLiteral.getLiteralValue());
		return new JDTStringLiteral(newStr);
	}
	
}
