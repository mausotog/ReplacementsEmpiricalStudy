package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTModifier extends JDTTreeNode {

	private final Modifier _modifier;
	public JDTModifier(ASTNode node) {
		super(node);
		assert (node instanceof Modifier);
		_modifier = (Modifier) node;
	}
	
	@Override
	public Object getValue() {
		return _modifier.getKeyword();
	}
	
	@Override
	public boolean setValue(Object value) {
		_modifier.setKeyword((ModifierKeyword) value);
		return true;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTModifier(ast.newModifier(_modifier.getKeyword()));
	}
}
