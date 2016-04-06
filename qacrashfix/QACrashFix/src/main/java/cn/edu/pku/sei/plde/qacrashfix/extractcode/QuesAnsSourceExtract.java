package cn.edu.pku.sei.plde.qacrashfix.extractcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import cn.edu.pku.sei.plde.qacrashfix.experiments.ExperimentStatistics;
import cn.edu.pku.sei.plde.qacrashfix.faultlocalization.FileInfo;
import cn.edu.pku.sei.plde.qacrashfix.faultlocalization.LocalBySim;
import cn.edu.pku.sei.plde.qacrashfix.faultlocalization.NodeInClass;
import cn.edu.pku.sei.plde.qacrashfix.faultlocalization.ProcessTrace;
import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeGenerator;

public class QuesAnsSourceExtract {
	
   private LinkedList<QuesAnsSource> ques_ans_source_list = new LinkedList<QuesAnsSource>();
   private LinkedList<QuesAnsSource> ques_ans_source_superior = new LinkedList<QuesAnsSource>();
   private LinkedList<QuesAnsSource> ques_ans_source_inferior = new LinkedList<QuesAnsSource>();
   
   public QuesAnsSourceExtract(String _source_path, String _stackoverflow_path, String _issue_time, ExperimentStatistics es){
	   
	    //true means stackoverflow'time earlier than issue
	    try{
	        QuesAnsBlockExtractFromWeb qaefw = new QuesAnsBlockExtractFromWeb(_stackoverflow_path, _issue_time, false);
	        QuesAnsSimplify qasf = new QuesAnsSimplify(qaefw.getQuesAnsBlockFromWebList());
		    LinkedList<QuesAns> ques_ans_list = qasf.getQuesAnsList();
		    ProcessTrace pt = new ProcessTrace(_source_path);
		    extractSourceFromTraceInfo(ques_ans_list, pt);
		    extractSourceFromSimInfo(ques_ans_list, pt);
		    ques_ans_source_list.addAll(ques_ans_source_superior);
		    ques_ans_source_list.addAll(ques_ans_source_inferior);
		    es.ques_ans_num += ques_ans_list.size();
//		    System.out.println("Ques_ans number: " + es.ques_ans_num);
//		    for (QuesAns ques_ans: ques_ans_list){
//			    System.out.println("  " + ques_ans.getQues() + "\n    " + ques_ans.getAns() + "\n");
//		    }
		    es.ques_ans_source_num += ques_ans_source_list.size();
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
   }
   
   public LinkedList<QuesAnsSource> getQuesAnsSourceList(){
	   return ques_ans_source_list;
   }
   
   private void extractSourceFromTraceInfo(LinkedList<QuesAns> _ques_ans_list, ProcessTrace _pt){
	   LinkedList<FileInfo> relatedFileInfos = _pt.getRelatedFileInfos();
	   Iterator<QuesAns> iter = _ques_ans_list.iterator();
	   while(iter.hasNext()){
		   QuesAns ques_ans = iter.next();
		   for(FileInfo info : relatedFileInfos){
		       LinkedList<Integer> localtions = info.getSuspiciousLine();
		       addQuesAnsSource(relatedFileInfos, ques_ans.getQues(), ques_ans.getAns(), info.absolute_file_path, localtions);
		   }
	   }
   }
   
   private void extractSourceFromSimInfo(LinkedList<QuesAns> _ques_ans_list, ProcessTrace _pt){
		   LinkedList<FileInfo> relatedFileInfos = _pt.getRelatedFileInfos();
		   Iterator<QuesAns> iter = _ques_ans_list.iterator();
		   while(iter.hasNext()){
			   QuesAns ques_ans = iter.next();
			   LocalBySim lbs = new LocalBySim(ques_ans.getQues(), _pt.getRelatedFileInfos());
			   HashMap<String, LinkedList<Integer>> faultLocaltionBySim = lbs.getFaultLocaltion();
			   Iterator<String> iter2 = faultLocaltionBySim.keySet().iterator();
			   while(iter2.hasNext()){	
					 String path = iter2.next();
					 LinkedList<Integer> localtions = faultLocaltionBySim.get(path);
                     addQuesAnsSource(relatedFileInfos, ques_ans.getQues(), ques_ans.getAns(), path, localtions);
			   }   
		   }
   }
   
   private void addQuesAnsSource(LinkedList<FileInfo> relatedFileInfos, LinkedList<String> ques, LinkedList<String> ans, String path, LinkedList<Integer> localtions){
	   FileInfo target = null;
	   for(FileInfo info : relatedFileInfos){
		   if(path.equals(info.absolute_file_path)){
			   target = info;
			   break;
		   }
	   }
	   
	   for(int localtion : localtions){
		   if(!addMethod(ques, ans, target, path, localtion)){
			   slidingWindow(ques, ans, target, path, localtion);
		   }
	    }  
   }
   
	private void slidingWindow(LinkedList<String> _ques, LinkedList<String> _ans, FileInfo target, String _path, int _localtion){
		try{
			if(_localtion < 0 || target == null)
				return;
			LinkedList<QuesAnsSource> ques_ans_source_part_superior = new LinkedList<QuesAnsSource>();
			LinkedList<QuesAnsSource> ques_ans_source_part_inferior = new LinkedList<QuesAnsSource>();
			ArrayList<NodeInClass> units = target.parse_java_file.getUnits();

			int index = -1;
			for(int i = 0; i < units.size(); i ++)
				if(units.get(i).getBegin() == _localtion){
					index = i;
					break;
				}
				
			//int index = binarySearch(units, _localtion);
			if(index == -1)
				return;
			int size = _ques.size();
			for(int i = index - size; i <= index + 1; i ++){
				if(i < 0) continue;
				if(i + size - 1 >= units.size()) break;
				LinkedList<String> source_snippet = new LinkedList<String>();
				for(int j = 0; j < size; j ++){
					source_snippet.addAll(new StringIntoList(units.get(i + j).getNode().toString().trim()).getLineList());
				}
				if(source_snippet.size() == size && CodeTypeMatch.isTopLevelTypeMatch(source_snippet, _ques)){
					QuesAnsSource qas = new QuesAnsSource(_ques, _ans, source_snippet,  target.origin_source, 
							_path, units.get(i).getBegin(), units.get(i + size - 1).getEnd());
						if(units.get(i).getBegin() <= _localtion && 
								units.get(i + size - 1).getBegin() >= _localtion &&
								!ques_ans_source_part_superior.contains(qas)){
							ques_ans_source_part_superior.add(qas);
						}
						else if(!ques_ans_source_part_inferior.contains(qas))
							ques_ans_source_part_inferior.add(qas);

				}
			}
			ques_ans_source_superior.addAll(ques_ans_source_part_superior);
			ques_ans_source_inferior.addAll(ques_ans_source_part_inferior);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	 private int binarySearch(ArrayList<NodeInClass> units, int des){   
	        int low = 0;   
	        int high = units.size() - 1;   
	        while(low <= high) {   
	            int middle = (low + high)/2;   
	            if(des == units.get(middle).getBegin()) {   
	                return middle;   
	            }else if(des < units.get(middle).getBegin()) {   
	                high = middle - 1;   
	            }else {   
	                low = middle + 1;   
	            }  
	        }  
	        return -1;  
	 }  
	
	private boolean addMethod(LinkedList<String> _ques, LinkedList<String> _ans, FileInfo target, String _path, int _localtion){
		JDTTreeGenerator jtg = new JDTTreeGenerator(new ListIntoString(_ques).getStr());
		if(jtg.isMethodDeclaration()){
			ArrayList<NodeInClass> units = target.parse_java_file.getUnits();
			for(int i = 0; i < units.size(); i ++){
				NodeInClass unit = units.get(i);
				//if(unit.getBegin() > _localtion)
					//break;
				if(unit.getBegin() == _localtion && jtg.getMethodName().equals(unit.getMethodName())){
					JDTTreeGenerator jtg_unit = new JDTTreeGenerator(unit.getNode().toString());
					if(!jtg_unit.isMethodDeclaration()) continue;
					QuesAnsSource qas = new QuesAnsSource(_ques, _ans, new StringIntoList(unit.getNode().toString()).getLineList(),  
							target.origin_source, _path, unit.getBegin(), unit.getEnd());
					if(!ques_ans_source_superior.contains(qas))
						ques_ans_source_superior.add(qas);
				}
			}
			return true;
		}
		return false;
	}
	/*
		private boolean addMethod(LinkedList<String> _ques, LinkedList<String> _ans, FileInfo target, String _path, int _localtion){
			JDTTreeGenerator jtg = new JDTTreeGenerator(new ListIntoString(_ques).getStr());
			if(jtg.isMethodDeclaration()){
				ArrayList<NodeInClass> units = target.parse_java_file.getUnits();
				int index = binarySearch(units, _localtion);
				if(index != -1){
					NodeInClass unit = units.get(index);
					if(jtg.getMethodName().equals(unit.getMethodName())){
						JDTTreeGenerator jtg_unit = new JDTTreeGenerator(unit.getNode().toString());
						if(jtg_unit.isMethodDeclaration()){
							QuesAnsSource qas = new QuesAnsSource(_ques, _ans, new StringIntoList(unit.getNode().toString()).getLineList(),  
									target.origin_source, _path, unit.getBegin(), unit.getEnd());
							if(!ques_ans_source_superior.contains(qas))
								ques_ans_source_superior.add(qas);
						}
					}
				}
				return true;
			}
			return false;
		}
		*/
	 
}
