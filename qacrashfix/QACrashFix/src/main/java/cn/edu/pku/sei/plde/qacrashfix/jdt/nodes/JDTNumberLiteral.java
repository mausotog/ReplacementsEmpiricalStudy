package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.NumberLiteral;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTNumberLiteral extends JDTTreeNode {

	private final NumberLiteral _numberLiteral;
	
	public JDTNumberLiteral(ASTNode node) {
		super(node);
		assert(node instanceof NumberLiteral);
		_numberLiteral = (NumberLiteral) node;
	}

	@Override
	public Object getValue() {
		return _numberLiteral.getToken();
	}
	
	@Override
	public boolean setValue(Object value) {
		_numberLiteral.setToken((String) value);
		return true;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		NumberLiteral num = ast.newNumberLiteral();
		num.setToken(_numberLiteral.getToken());
		return new JDTNumberLiteral(num);
	}
}
