package cn.edu.pku.sei.plde.qacrashfix.extractcode;

import java.util.ArrayList;
import java.util.LinkedList;

import cn.edu.pku.sei.plde.qacrashfix.faultlocalization.Config;

public class QuesAnsMatch {
	private SimilarityMeasureMethod method;
	private int ques_index;
	private int ans_index;
	private int match_pos_in_ques;
	private int match_pos_in_ans;
	
	class CompareResultOfQuesAndAns{
		public double score;
		public int match_num;
		public int pos_in_ques;
		public int pos_in_ans;
		
		public CompareResultOfQuesAndAns(int _match_num, int _pos_in_ques, int _pos_in_ans){
			match_num = _match_num;
			pos_in_ques = _pos_in_ques;
			pos_in_ans = _pos_in_ans;
		}
	}
	
	public QuesAnsMatch(ArrayList<LinkedList<String>> _ques_of_block, ArrayList<LinkedList<String>> _ans_of_block, SimilarityMeasureMethod _method){
		ques_index = -1;
		ans_index = -1;
		match_pos_in_ques = -1;
		match_pos_in_ans = -1;
		method = _method;
		this.matchQuesAndAns(_ques_of_block, _ans_of_block);
	}
	
	public int getQuesIndex(){
		return ques_index;
	}
	
	public int getAnsIndex(){
		return ans_index;
	}
	
	public int getMatchPosInQues(){
		return match_pos_in_ques;
	}
	
	public int getMatchPosInAns(){
		return match_pos_in_ans;
	}
	
    private void matchQuesAndAns(ArrayList<LinkedList<String>> _ques_of_block, ArrayList<LinkedList<String>> _ans_of_block){//寻找需要比较的ques和ans
       int maxum = 0, ques_of_block_size = _ques_of_block.size(), ans_of_block_size = _ans_of_block.size();
 	   for(int i = 0; i < ques_of_block_size; i ++)
 		   for(int j = 0; j < ans_of_block_size; j ++){
 			  CompareResultOfQuesAndAns crqa = maxMatchNum(_ques_of_block.get(i), _ans_of_block.get(j));
 			   if(maxum < crqa.match_num){
 				   maxum = crqa.match_num;
 				   ques_index = i;
 				   ans_index = j;
 				   match_pos_in_ques = crqa.pos_in_ques;
 				   match_pos_in_ans = crqa.pos_in_ans;
 			   }
 		   }
    }
    
    private CompareResultOfQuesAndAns maxMatchNum(LinkedList<String> _ques, LinkedList<String> _ans){//寻找两者最匹配的位置
        int ques_size = _ques.size(), ans_size = _ans.size(), pos_in_ques = -1, pos_in_ans = -1, maxum = 0, num = 0;
 	    for(int i = 0; i < ques_size; i ++){
                int j;
                for(j = 0; j < ans_size; j ++){

                	Distance dis = new Distance(_ques.get(i), _ans.get(j));
                	if(method == SimilarityMeasureMethod.SimilarityMeasureByEditDistance && dis.editDistanceMatch()) break;
                	else if(method == SimilarityMeasureMethod.SimilarityMeasureByCommonWords && dis.structureDistanceMatch()) 
                		break;
                }
                if(j >= ans_size)
                	continue;
                String ques_line = _ques.get(i);
                String ans_line = _ans.get(j);
                if(ques_line.length() < Config.linesize || ans_line.length() < Config.linesize)
                	continue;
 	    	    num = matchNum(i, _ques, j, _ans);
     	        if(maxum < num){
     		       maxum = num;
     		       pos_in_ques = i;
     		       pos_in_ans = j;
     	        }
        }
 	    return new CompareResultOfQuesAndAns(maxum, pos_in_ques, pos_in_ans);
    }
    
    public int matchNum(int _ques_index, LinkedList<String> _ques, int _ans_index, LinkedList<String> _ans){
 	   int result = 0;
 	   while(_ques_index < _ques.size() && _ans_index < _ans.size()){
 		   String ques_line = _ques.get(_ques_index);
 		   String ans_line = _ans.get(_ans_index);
 		   Distance dis = new Distance(ques_line, ans_line);
 		   if((method == SimilarityMeasureMethod.SimilarityMeasureByEditDistance && dis.editDistanceMatch())
 				|| (method == SimilarityMeasureMethod.SimilarityMeasureByCommonWords && dis.structureDistanceMatch())){
 			   result ++;
 			   _ques_index ++;
 			   _ans_index ++;
 		   }
 		   else _ans_index ++;
 	   }
 	   return result;
    }
}
