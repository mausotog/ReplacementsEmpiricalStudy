package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Initializer;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTInitializer extends JDTTreeNode {

	private final Initializer _init;
	public JDTInitializer(ASTNode node) {
		super(node);
		assert (node instanceof Initializer);
		_init = (Initializer) node;
	}
	
	@Override
	protected boolean setChildWithReference(ASTNode node, ASTNode referenceNode) {
		if (((Initializer)referenceNode.getParent()).getBody() == referenceNode){
			_init.setBody((Block) node);
			return true;
		}
		return false;
	}

	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTInitializer(ast.newInitializer());
	}
}
