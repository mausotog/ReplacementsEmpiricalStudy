package cn.edu.pku.sei.plde.qacrashfix.extractcode;

import java.util.LinkedList;

public class QuesAns {
    private LinkedList<String> ques;
    private LinkedList<String> ans;
    private double edit_distance = Double.MAX_VALUE;
    private double structure_distance = Double.MAX_VALUE;
    private double language_distance = Double.MAX_VALUE;
    
    public QuesAns(LinkedList<String> _ques, LinkedList<String> _ans, SimilarityMeasureMethod method, double _distance){
    	ques = new LinkedList<String>(_ques);
    	ans = new LinkedList<String>(_ans);
    	if(method == SimilarityMeasureMethod.SimilarityMeasureByEditDistance)
    		edit_distance = _distance;
    	else if(method == SimilarityMeasureMethod.SimilarityMeasureByCommonWords)
    		structure_distance = _distance;
    	else language_distance = _distance;
    }
    
    public LinkedList<String> getQues(){
    	return ques;
    }
    
    public LinkedList<String> getAns(){
    	return ans;
    }
    
    public boolean equal(QuesAns other){
    	return ques.equals(other.getQues()) && ans.equals(other.getAns());
    	
    }
    
    public double getEditDistance(){
    	return edit_distance;
    }
    
    public double getStructureDistance(){
    	return structure_distance;
    }
    
    public double getLanguageDistance(){
    	return language_distance;
    }
}
