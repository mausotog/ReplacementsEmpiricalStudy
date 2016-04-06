package cn.edu.pku.sei.plde.qacrashfix.faultlocalization;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;

import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTUtils;

public class NodeGenerator extends ASTVisitor{
	
	private LinkedList<MyNode> nodes;
    
	public NodeGenerator(String _code) {
		nodes = new LinkedList<MyNode>();
		generateASTNodeForSource(_code);
	}
    
	public void generateASTNodeForSource(String _code){
		ASTNode root = JDTUtils.getASTNodeForSource(_code, ASTParser.K_EXPRESSION);
		root.accept(this);
		if(nodes.size() == 0){
			root = JDTUtils.getASTNodeForSource(_code, ASTParser.K_STATEMENTS);
			root.accept(this);
		}
		Collections.sort(nodes);
		Iterator<MyNode> it = nodes.iterator();
		while(it.hasNext()){
			if(hasChild(it.next().node))
				it.remove();
		}
	}
	
	public LinkedList<MyNode> getNodes(){
		return nodes;
	}

	@Override
	public void postVisit(ASTNode _node) {
		super.postVisit(_node);
		ASTNode node = _node;
		int count = 0;
		while(node.getParent() != null){
			node = node.getParent();
			count++;
	    }
		
		if(count <= 3) return;//添加的成分
		nodes.add(new MyNode(_node,count));
	}
	
	private boolean hasChild(ASTNode _node){
		Iterator<MyNode> it = nodes.iterator();
		while(it.hasNext()){
			ASTNode node = it.next().node;
			while(node.getParent() != null){
				node = node.getParent();
				if(node.equals(_node))
					return true;
			}
		}
		return false;
	}
}
