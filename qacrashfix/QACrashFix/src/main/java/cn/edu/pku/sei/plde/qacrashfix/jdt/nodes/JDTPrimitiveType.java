package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.PrimitiveType.Code;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTPrimitiveType extends JDTTreeNode {

	private final PrimitiveType  _primitiveType;
	
	public JDTPrimitiveType(ASTNode node) {
		super(node);
		assert(node instanceof PrimitiveType);
		_primitiveType = (PrimitiveType) node;
	}
	
	@Override
	public Object getValue() {
		return _primitiveType.getPrimitiveTypeCode();
	}
	
	@Override
	public boolean setValue(Object value) {
		_primitiveType.setPrimitiveTypeCode((Code) value);
		return true;
	}

	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTPrimitiveType(ast.newPrimitiveType(_primitiveType.getPrimitiveTypeCode()));
	}
}
