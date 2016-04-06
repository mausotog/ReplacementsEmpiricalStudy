package cn.edu.pku.sei.plde.qacrashfix.extractcode;

import java.util.Iterator;
import java.util.LinkedList;

import cn.edu.pku.sei.plde.qacrashfix.faultlocalization.Config;
import cn.edu.pku.sei.plde.qacrashfix.faultlocalization.JavaKeyWords;
import cn.edu.pku.sei.plde.qacrashfix.faultlocalization.MyNode;
import cn.edu.pku.sei.plde.qacrashfix.faultlocalization.NodeGenerator;

public class Distance {
	
	private double edit_distance;
	private double structure_distance;
	
	public double getEditDistance(){
		return edit_distance;
	}
	
	public double getStructureDistance(){
		return structure_distance;
	}
	
	public boolean editDistanceMatch(){
		if(edit_distance < Config.threshold_of_edit_distance)
			return true;
		return false;
	}
	
	public boolean structureDistanceMatch(){
		if(structure_distance < Config.threshold_of_structure_distance)
			return true;
		return false;
	}
	
	public Distance(String _fir_line, String _sec_line){
		edit_distance = similarityMeasureByEditDistance(_fir_line, _sec_line);
		structure_distance = similarityMeasureByCommonWords(splitLine(_fir_line), splitLine(_sec_line));
	}
   
    private static int getMin(int _a, int _b, int _c){
		int min = (_a <= _b) ? _a : _b;
		return (min <= _c ) ? min : _c;
	}
	
	public static double similarityMeasureByEditDistance(String _fir_line, String _sec_line) {
		for(String keyword : JavaKeyWords.javaKeyWords){
		    if(_fir_line.contains(keyword))
			    _fir_line = _fir_line.replaceAll(keyword, "");
			if(_sec_line.contains(keyword))
				_sec_line = _sec_line.replaceAll(keyword, "");
	    }
		
		_fir_line = _fir_line.replaceAll(" ", "").trim();
		_sec_line = _sec_line.replaceAll(" ", "").trim();
		int fir_line_len = _fir_line.length();
		int sec_line_len = _sec_line.length();
		
		int dis[][] = new int[fir_line_len + 1][];
		for(int i = 0; i < fir_line_len + 1; i ++) {
			dis[i] = new int[sec_line_len + 1];
		}
		for(int i = 0; i <= fir_line_len; i ++) {
			dis[i][0]=i;
		}
		for(int j = 0; j <= sec_line_len; j++) {
			dis[0][j]=j;
		}
		
		for(int i = 1; i <= fir_line_len; i++) {
			for(int j = 1; j <= sec_line_len; j++) {	
				int equal = (_fir_line.charAt(i - 1) == _sec_line.charAt(j - 1)) ? 0 : 1;		
				dis[i][j] = getMin(dis[i - 1][j] + 1, dis[i][j - 1] + 1, dis[i - 1][j - 1] + equal);
			}
		}
		
		return dis[fir_line_len][sec_line_len] * 1.0 / Math.pow((fir_line_len * sec_line_len), 0.5);
		//return dis[fir_line_len][sec_line_len] * 1.0 / (fir_line_len + sec_line_len);
	}
	
	public static LinkedList<String> splitLine(String _line){
		 LinkedList<String> words = new LinkedList<String>();
	 	 LinkedList<MyNode> nodes = new LinkedList<MyNode>();
	 	 try{
	 		 NodeGenerator ng = new NodeGenerator(_line);
	 		 nodes = ng.getNodes();
	 	 }
	 	 catch(Exception e){
	 		 e.printStackTrace();
	 	 }
	 	 if(nodes.size() == 0) return words;
	 	 for(int j = 0; j < nodes.size(); j ++){
	 	    MyNode myNode = nodes.get(j);
	 		if(JavaKeyWords.isJavaKeyWord(myNode.node.toString()))
	 			continue;
	 		
	 		words.add(myNode.node.toString());
      }
	   return words;
	}
	
	public static double similarityMeasureByCommonWords(LinkedList<String> _fir_line_words, LinkedList<String> _sec_line_words){
		if(_fir_line_words.isEmpty() || _sec_line_words.isEmpty() || _fir_line_words.size() == 0 || _sec_line_words.size() == 0)
			return 1.0;
        double count = 0.0;
        Iterator<String> it = _fir_line_words.iterator();
        while(it.hasNext()){
        	String word = it.next();
        	if(_sec_line_words.contains(word))
        		count += 1.0;
        }
        int minum = _fir_line_words.size() > _sec_line_words.size() ? _sec_line_words.size() : _fir_line_words.size();
		return 1 - (count / minum);
	}
}

