package cn.edu.pku.sei.plde.qacrashfix.faultlocalization;

import org.eclipse.jdt.core.dom.ASTNode;

public class NodeInClass{
	
	private ASTNode node;
	private int begin;
	private int end;
	private String type;
	private String method_name;
	
	public NodeInClass(ASTNode _node, int _begin, int _end, String _type, String _method_name){
		node = _node;
		begin = _begin;
		end = _end;
		type = _type;
		method_name = _method_name;
	}
	
	public ASTNode getNode(){
		return node;
	}
	
	public int getBegin(){
		return begin;
	}
	
	public int getEnd(){
		return end;
	}
	
	public String getType(){
		return type;
	}
	
	public String getMethodName(){
		return method_name;
	}
}
