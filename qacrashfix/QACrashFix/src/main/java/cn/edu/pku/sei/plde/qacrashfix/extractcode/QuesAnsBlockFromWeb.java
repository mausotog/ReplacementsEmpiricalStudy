package cn.edu.pku.sei.plde.qacrashfix.extractcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
/**
 * extract question,ans,ans_text from web page
 * @author wj
 *
 */
public class QuesAnsBlockFromWeb {
	private ArrayList<LinkedList<String>> ans_of_block;
	private String ans_text_of_block;
	private ArrayList<LinkedList<String>> ques_of_block;
	private Map<LinkedList<String>, Integer> ans_index_map;
	
	public QuesAnsBlockFromWeb(ArrayList<LinkedList<String>> _ans_of_block, String _ans_text_of_block, ArrayList<LinkedList<String>> _ques_of_block, Map<LinkedList<String>, Integer> _ans_index_map){
		ans_of_block = new ArrayList<LinkedList<String>>(_ans_of_block);
		ans_text_of_block = new String(_ans_text_of_block);
		ques_of_block = new ArrayList<LinkedList<String>>(_ques_of_block);
		ans_index_map = new HashMap<LinkedList<String>, Integer>(_ans_index_map);
	}
	
	public ArrayList<LinkedList<String>> getAnsOfBlock(){
		return ans_of_block;
	}
	
	public String getAnsTextOfBlock(){
		return ans_text_of_block;
	}
	
	public ArrayList<LinkedList<String>> getQuesOfBlock(){
		return ques_of_block;
	}

	public Map<LinkedList<String>, Integer> getAnsIndexMap(){
		return ans_index_map;
	}
	
	public boolean equal(QuesAnsBlockFromWeb other){
		return ans_of_block.equals(other.getAnsOfBlock()) && ques_of_block.equals(other.getQuesOfBlock());
	}
	
}
