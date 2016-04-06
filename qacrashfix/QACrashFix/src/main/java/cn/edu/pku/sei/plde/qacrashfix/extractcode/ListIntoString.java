package cn.edu.pku.sei.plde.qacrashfix.extractcode;

import java.util.Iterator;
import java.util.LinkedList;

public class ListIntoString {
	private String str;
	public ListIntoString(LinkedList<String> _list){
		StringBuilder sb = new StringBuilder();
		Iterator<String> it = _list.iterator();
		while(it.hasNext()){
			sb.append(it.next() + "\n");
		}
		str = sb.toString();
	}
	
	public String getStr(){
		return str;
	}
}
