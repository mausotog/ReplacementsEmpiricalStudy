package cn.edu.pku.sei.plde.qacrashfix.extractcode;

import java.util.LinkedList;

public class StringIntoList {
	private LinkedList<String> line_list;
	
	public StringIntoList(String _content){
		String line_array[] = _content.split("\n");
		line_list = new LinkedList<String>();
		int len = line_array.length;
		for(int i = 0; i < len; i ++){
			//line_list.add(line_array[i].trim());
			line_list.add(line_array[i].replace("\r", "").replace("\n", ""));
		}
	}
    
	public LinkedList<String> getLineList(){
		return line_list;
	}

}
