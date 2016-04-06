package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CharacterLiteral;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTCharacterLiteral extends JDTTreeNode {

	private final CharacterLiteral _char;
	
	public JDTCharacterLiteral(ASTNode node) {
		super(node);
		assert(node instanceof CharacterLiteral);
		_char = (CharacterLiteral) node;
	}
	
	@Override
	public Object getValue() {
		return _char.charValue();
	}
	
	@Override
	public boolean setValue(Object value) {
		_char.setCharValue((char)value);
		return true;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		CharacterLiteral charLiteral = ast.newCharacterLiteral();
		charLiteral.setCharValue(_char.charValue());
		return new JDTCharacterLiteral(charLiteral);
	}
}
