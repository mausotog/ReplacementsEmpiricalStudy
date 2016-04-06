package cn.edu.pku.sei.plde.qacrashfix.jdt.nodes;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeNode;

public class JDTBlock extends JDTTreeNode{

	private final Block _block;
	public JDTBlock(ASTNode node) {
		super(node);
		assert (node instanceof Block);
		_block = (Block) node;
		_listListChildren.add(_block.statements());
	}
	
	public Block getBlock(){
		return _block;
	}
	
	@Override
	protected JDTTreeNode createNewInAST(AST ast) {
		return new JDTBlock(ast.newBlock());
	}
}
