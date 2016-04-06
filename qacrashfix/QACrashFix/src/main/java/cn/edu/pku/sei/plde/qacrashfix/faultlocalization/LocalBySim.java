package cn.edu.pku.sei.plde.qacrashfix.faultlocalization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import cn.edu.pku.sei.plde.qacrashfix.extractcode.Distance;
import cn.edu.pku.sei.plde.qacrashfix.extractcode.ListIntoString;
import cn.edu.pku.sei.plde.qacrashfix.extractcode.SimilarityMeasureMethod;
import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeGenerator;

public class LocalBySim {
	private HashMap<String, LinkedList<Integer>> fault_localtion_by_sim;
	private HashMap<Integer, LinkedList<String>> ques_words;
	
	public LocalBySim(LinkedList<String> _ques, LinkedList<FileInfo> _related_file_infos){
		
		fault_localtion_by_sim = new HashMap<String, LinkedList<Integer>>();
		
		for(FileInfo info : _related_file_infos){
			  info.source_words = new HashMap<Integer, LinkedList<String>>();
			  ArrayList<NodeInClass> units = info.parse_java_file.getUnits();
			  for(int i = 0; i < units.size(); i ++)
				  info.source_words.put(i, Distance.splitLine(units.get(i).getNode().toString()));
		}
		
		ques_words = new HashMap<Integer, LinkedList<String>>();
		for(int i = 0; i < _ques.size(); i ++)
		   ques_words.put(i,  Distance.splitLine(_ques.get(i)));
		
		mostSimilarPosInSource(_ques, _related_file_infos, SimilarityMeasureMethod.SimilarityMeasureByCommonWords);
		mostSimilarPosInSource(_ques, _related_file_infos, SimilarityMeasureMethod.SimilarityMeasureByEditDistance);
        
    }

    public HashMap<String, LinkedList<Integer>> getFaultLocaltion(){
    	return fault_localtion_by_sim;
    }
    
    private void mostSimilarPosInSource(LinkedList<String> _ques, LinkedList<FileInfo> _related_file_infos, SimilarityMeasureMethod _method){
    	 int localtion = -1;
		 int localtion2 = -1;
		 String path = null;
		 String path2 = null;
		 double maxum = Double.MAX_VALUE;
		 double maxum2 = Double.MAX_VALUE;
		 JDTTreeGenerator jtg = new JDTTreeGenerator(new ListIntoString(_ques).getStr());
		 for(FileInfo info : _related_file_infos){
		     ArrayList<NodeInClass> statements = info.parse_java_file.getUnits();
		     if(jtg.isMethodDeclaration()){
		    	 addMethodLocaltion(jtg, statements, info);
		    	 continue;
		     }
		     for(int i = 0; i < _ques.size(); i ++)
		    	 for(int j = 0; j < statements.size(); j ++){
			    	 double dis = 0.0;
			    	 if(_method == SimilarityMeasureMethod.SimilarityMeasureByCommonWords)
			    		 dis = Distance.similarityMeasureByCommonWords(ques_words.get(i), info.source_words.get(j));
			    	 else  dis = Distance.similarityMeasureByEditDistance(_ques.get(i), statements.get(j).getNode().toString());
			    	 if(maxum > dis){
			    		 maxum = dis;
			    		 localtion = statements.get(j).getBegin();
			    		 path = info.absolute_file_path;
			    	 }
			    		 
			    	 else if(maxum2 > dis && !(path.equals(info.absolute_file_path) && localtion == j)){
			    		 maxum2 = dis;
			    		 localtion2 = statements.get(j).getBegin();
			    		 path2 = info.absolute_file_path;
			    	 }
		        }
		 }
		 
		 addLocaltion(path, localtion);
		 addLocaltion(path2, localtion2);
    }
    
    private void addMethodLocaltion(JDTTreeGenerator jtg, ArrayList<NodeInClass> statements, FileInfo info){
		for(int i = 0; i < statements.size(); i ++)
			if(jtg.getMethodName().equals(statements.get(i).getMethodName()))
				addLocaltion(info.absolute_file_path, statements.get(i).getBegin());
    }
    
    private void addLocaltion(String _path, int _localtion){
		 if(_localtion == -1 || _path == null)
			 return;
		 if(!fault_localtion_by_sim.containsKey(_path)){
			LinkedList<Integer> localtions = new LinkedList<Integer>();
			localtions.add(_localtion);
			fault_localtion_by_sim.put(_path, localtions);
		 }
		 else{
			LinkedList<Integer> localtions = fault_localtion_by_sim.get(_path);
			if(!localtions.contains(_localtion))
			   localtions.add(_localtion);
		 }
    }
    
}
