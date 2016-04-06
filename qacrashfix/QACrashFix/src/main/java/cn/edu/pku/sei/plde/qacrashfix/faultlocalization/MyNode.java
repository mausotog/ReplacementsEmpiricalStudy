package cn.edu.pku.sei.plde.qacrashfix.faultlocalization;

import org.eclipse.jdt.core.dom.ASTNode;

public class MyNode implements Comparable<MyNode>{
	
	public ASTNode node;
	public int depth;
	
	public MyNode(ASTNode _node, int _depth){
		node = _node;
		depth = _depth;
	}
	
	@Override
	public int compareTo(MyNode _arg0){
		if(this.depth < _arg0.depth) return 1;
		if(this.depth > _arg0.depth) return -1;
		return 0;
	}
}
