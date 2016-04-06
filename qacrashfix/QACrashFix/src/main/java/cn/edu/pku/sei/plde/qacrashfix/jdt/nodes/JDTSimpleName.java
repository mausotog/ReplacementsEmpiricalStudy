package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.SimpleName;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTSimpleName extends JDTTreeNode {
	private SimpleName _simpleName = null;
	public JDTSimpleName(ASTNode node) {
		super(node);
		assert (node instanceof SimpleName);
		_simpleName = (SimpleName) node;
	}
	
	public boolean setValue(Object value){
		assert(value instanceof String);
		_simpleName.setIdentifier((String) value);
		return true;
	}
	
	public SimpleName getSimpleName(){
		return _simpleName;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTSimpleName(ast.newSimpleName(_simpleName.getIdentifier()));
	}
	
}
