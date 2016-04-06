package cn.edu.pku.sei.plde.qacrashfix.extractcode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.edu.pku.sei.plde.qacrashfix.faultlocalization.Config;
/**
 * match ques ans
 * @author wj
 *
 */
public class QuesAnsSimplify {
	   private LinkedList<QuesAns> ques_ans_list;
	   
	   public QuesAnsSimplify(LinkedList<QuesAnsBlockFromWeb> _ques_ans_block_from_web_list){
		   ques_ans_list = new LinkedList<QuesAns>();
		   Iterator<QuesAnsBlockFromWeb> iter = _ques_ans_block_from_web_list.iterator();
		   while(iter.hasNext()){
			   QuesAnsBlockFromWeb qabfw = iter.next();
			   restoreQuesAndAnsByLanguageProcess(qabfw);
			   restoreQuesAndAnsByCompare(qabfw,  SimilarityMeasureMethod.SimilarityMeasureByEditDistance);
			   restoreQuesAndAnsByCompare(qabfw,  SimilarityMeasureMethod.SimilarityMeasureByCommonWords);
		   }
		   filterQuesAns();
	   }
	   
	   public LinkedList<QuesAns> getQuesAnsList(){
		   return ques_ans_list;
	   }
	   
	   private void restoreQuesAndAnsByCompare(QuesAnsBlockFromWeb _qabfw, SimilarityMeasureMethod _method){

		   ArrayList<LinkedList<String>> _ques_block = _qabfw.getQuesOfBlock();
		   ArrayList<LinkedList<String>> _ans_block = _qabfw.getAnsOfBlock();
    	   LinkedList<String> ques = null;
    	   QuesAnsMatch qam = new QuesAnsMatch(_ques_block, _ans_block, _method);
    	   int ques_index = qam.getQuesIndex();
    	   int ans_index = qam.getAnsIndex();
    	   int match_pos_in_ques = qam.getMatchPosInQues();
    	   int match_pos_in_ans = qam.getMatchPosInAns();
    	   if(ques_index != -1 && ans_index != -1){
    	      ques = restoreQues(_ques_block.get(ques_index), _ans_block.get(ans_index), match_pos_in_ques, match_pos_in_ans, _method);
    	      restoreAns(_ans_block, ques, _method);
    	   }

       }
	   
       private void restoreQuesAndAnsByLanguageProcess(QuesAnsBlockFromWeb _qabfw){
    	   ArrayList<LinkedList<String>> _ans_block = _qabfw.getAnsOfBlock();
    	   String _ans_text_of_block = _qabfw.getAnsTextOfBlock();
    	   Map<LinkedList<String>, Integer> _ans_index_map = _qabfw.getAnsIndexMap();
//    	   _ans_text_of_block = _ans_text_of_block.replaceAll(" ", "");
    	   String process_str = _ans_text_of_block.toLowerCase();
    	   if(!process_str.toLowerCase().contains("insteadof") && !process_str.toLowerCase().contains("to"))
    		   return;
 
    	   LinkedList<String> ques = null, ans = null;
    	   int positive_min_dis = Integer.MAX_VALUE;
    	   int index = -1;
    	   boolean is_insteadof = false, is_to = false;
    	   Pattern pattern_insteadof = Pattern.compile("\\binstead of\\b");
    	   Matcher matcher_insteadof = pattern_insteadof.matcher(process_str);
    	   Pattern pattern_to = Pattern.compile("\\bto\\b");
    	   Matcher matcher_to = pattern_to.matcher(process_str);
    	   MatchResult mr = null;
    	   int start = -1;
    	   if(matcher_insteadof.find()){
    		  is_insteadof = true;
    		  mr = matcher_insteadof.toMatchResult();
    		  index = mr.start() + "instead of".length();
    	   }
    	   else if (matcher_to.find()){
    		  is_to = true;
    		  mr = matcher_to.toMatchResult();
    		  index = mr.start() + "to".length();
    	   }
    	   Iterator<LinkedList<String>> iter = _ans_block.iterator();
    	   while(iter.hasNext()){
    		   LinkedList<String> list = iter.next();
    		   int code_index = _ans_index_map.get(list);
    		   int dis = code_index - index;
    		   if(dis > 0 && positive_min_dis > dis){
    			   positive_min_dis = dis;
    			   if (is_insteadof)
    				   ques = new LinkedList<String>(list);
    			   else if (is_to)
    				   ans = new LinkedList<String>(list);
    		   }
    	   }
    	   
    	   if (positive_min_dis <= 25){
	    	   if(ques != null){
	    		   int ques_index = _ans_block.indexOf(ques);
	    		   //instead of... (use...)
	    		   if(ques_index + 1 < _ans_block.size()){
	    		       ans = new LinkedList<String>(_ans_block.get(ques_index + 1));
	    		       if(is_insteadof)
	    	              ques_ans_list.add(new QuesAns(ques, ans, null, 0.0));
	    		   }
	    		   
	    		   //(use...)instead of...
	    		   if(ques_index - 1 >= 0){
	    		       ans = new LinkedList<String>(_ans_block.get(ques_index - 1));
	    		       if(is_insteadof)
	    	              ques_ans_list.add(new QuesAns(ques, ans, null, 0.0));	    		   }
	    	   }
	    	   else if (ans != null){
	    		   int ans_index = _ans_block.indexOf(ans);
	    		   if (ans_index > 0){
		    		   ques = new LinkedList<String>(_ans_block.get(ans_index - 1));
			           ques_ans_list.add(new QuesAns(ques, ans, null, 0.0));
	    		   }
	    	   }
    	   }
       }
       
       private double getMinDistance(SimilarityMeasureMethod method){
    	   double min_distance = Double.MAX_VALUE;
    	   Iterator<QuesAns> iter = ques_ans_list.iterator();
    	   while(iter.hasNext()){
    		   QuesAns ques_ans = iter.next();
    		   if(method == SimilarityMeasureMethod.SimilarityMeasureByCommonWords && 
    				   min_distance > ques_ans.getStructureDistance()){
    			   min_distance = ques_ans.getStructureDistance();
    		   }
    			
    		   if(method == SimilarityMeasureMethod.SimilarityMeasureByEditDistance && 
    				   min_distance > ques_ans.getEditDistance()){
    			   min_distance = ques_ans.getEditDistance();
    		   }
    		   
    	   }
    	   return min_distance;
       }
       private void filterQuesAns(){
		   LinkedList<QuesAns> tmp_list = ques_ans_list;
		   ques_ans_list = new LinkedList<QuesAns>();
		   for (QuesAns candidate:tmp_list){
			   try{
				 if(CodeTypeMatch.isTypeMatch(candidate.getQues(), candidate.getAns()))
				   ques_ans_list.add(candidate);
			   }
			   catch(Exception e){
			   }
		   }
		   
    	   double min_edit_distance = getMinDistance(SimilarityMeasureMethod.SimilarityMeasureByEditDistance);
    	   double min_structure_distance = getMinDistance(SimilarityMeasureMethod.SimilarityMeasureByCommonWords);
    	   Iterator<QuesAns> iter = ques_ans_list.iterator();
    	   while(iter.hasNext()){
    		   QuesAns ques_ans = iter.next();
    		   if(ques_ans.getLanguageDistance() == Double.MAX_VALUE &&
    			  ques_ans.getEditDistance() == Double.MAX_VALUE &&
    			  ques_ans.getStructureDistance() == Double.MAX_VALUE){
    			   iter.remove();
    			   continue;
    		   }
    			   
    		   if(ques_ans.getLanguageDistance() != Double.MAX_VALUE)
    			   continue;
    		   
    		   if(ques_ans.getEditDistance() != Double.MAX_VALUE && ques_ans.getEditDistance() > min_edit_distance)
    			   iter.remove();
    		   
    		   if(ques_ans.getStructureDistance() != Double.MAX_VALUE && ques_ans.getStructureDistance() > min_structure_distance)
    			   iter.remove();  
    	   }
    	   
    	   //过滤掉重复的 但distance计算方法不同的
		   LinkedList<QuesAns> tmp_list2 = ques_ans_list;
		   ques_ans_list = new LinkedList<QuesAns>();
		   for (QuesAns candidate : tmp_list2){
			   if(!contain(candidate))
				   ques_ans_list.add(candidate);
		   }
       }
       
       private double getScore(LinkedList<String> _ques, LinkedList<String> _ans, SimilarityMeasureMethod method){
    	   int ques_index = 0, ans_index = 0, match_num = 0;
    	   double total_score = 0.0;
    	   while(true){
    		   if(ques_index >= _ques.size() || ans_index >= _ans.size())
    			   break;
    		   String ques_line = _ques.get(ques_index);
    		   String ans_line = _ans.get(ans_index);
    		   if(ques_line.length() < Config.linesize){
    			   ques_index ++;
    			   continue;
    		   }
    		   if(ans_line.length() < Config.linesize){
    			   ans_index ++;
    			   continue;
    		   }
    		   Distance dis = new Distance(ans_line, ques_line);
    		   if((method == SimilarityMeasureMethod.SimilarityMeasureByEditDistance && dis.editDistanceMatch()) ||
    				(method == SimilarityMeasureMethod.SimilarityMeasureByCommonWords && dis.structureDistanceMatch())){
    			   if(method == SimilarityMeasureMethod.SimilarityMeasureByEditDistance)
    			       total_score += dis.getEditDistance();
    			   if(method == SimilarityMeasureMethod.SimilarityMeasureByCommonWords)
    				   total_score += dis.getStructureDistance();
    			   match_num ++;
    			   ques_index ++;
    			   ans_index ++;
    		   }
    		   
    		   else{
    			   int ans_insert_count = ansInsert(ques_line, _ans, ans_index + 1, method);
    			   if(ans_insert_count != -1){
    				   ans_index += ans_insert_count;
    			   }
    			   
    			   else{
    				   ques_index ++;
    				   ans_index ++;
    			   }
    		   }
    	   }
    	   
    	   if(match_num == 0)
    		   return Double.MAX_VALUE;
    	   else return (total_score / match_num) * (_ques.size() * _ques.size() + _ans.size() * _ans.size());
    	   
       }
       
       private LinkedList<String> restoreQues(LinkedList<String> _ques, LinkedList<String> _ans, int _match_pos_in_ques, 
    		   int _match_pos_in_ans, SimilarityMeasureMethod method){
    	   LinkedList<String> temp_ques = new LinkedList<String>(_ques);
    	   LinkedList<String> temp_ans = new LinkedList<String>(_ans);
    	   LinkedList<String> ques = new LinkedList<String>();
    	   while(true){
    		   if(_match_pos_in_ques >= temp_ques.size() || _match_pos_in_ans >= temp_ans.size())
    			   break;
    		   
    		   String ans_line = temp_ans.get(_match_pos_in_ans);
    		   String ques_line = temp_ques.get(_match_pos_in_ques);
    		   
    		   if(ques_line.length() < Config.linesize){
    			   if(ques.size() == 0){//如果大括号在中间出现则保留，否则删除
    			     _match_pos_in_ques ++;
    			     if(ans_line.length() < Config.linesize)
    			    	 _match_pos_in_ans ++;
    			     continue;
    			   }
    		   }

    		   Distance dis = new Distance(ans_line, ques_line);
    		   if((method == SimilarityMeasureMethod.SimilarityMeasureByEditDistance && dis.editDistanceMatch()) ||
    				(method == SimilarityMeasureMethod.SimilarityMeasureByCommonWords && dis.structureDistanceMatch())){
    			   ques.add(ques_line);
    			   if(!bigBracketMatch(ques))
    				   ques.removeLast();
    			   _match_pos_in_ques ++;
    			   _match_pos_in_ans ++;

    		   }
    		   
    		   else{
    			   int ans_insert_count = ansInsert(ques_line, temp_ans, _match_pos_in_ans + 1, method);
    			   int ques_insert_count = quesInsert(ans_line, temp_ques, _match_pos_in_ques + 1, method);
    			   
    			   if(ans_insert_count != -1){
    				   _match_pos_in_ans += ans_insert_count;
    			   }
    			   
    			   else if(ques_insert_count != -1){
    				   ques.clear();
    				   for(int k = 0; k <= _match_pos_in_ans - 1; k ++)
    					   temp_ans.remove(0);
    				   _match_pos_in_ans = 0;
    				   _match_pos_in_ques += ques_insert_count;
    			   }
    			   
    			   else{
    				   _match_pos_in_ques ++;
    				   _match_pos_in_ans ++;
    			   }
    		   }
    	   }
    	   return ques;
       }
       
       private boolean bigBracketMatch(LinkedList<String> _ques){
    	   String last_line = _ques.getLast();
    	   if(!last_line.equals("}"))
    		   return true;
    	   int count = 0;
    	   Iterator<String> iter = _ques.iterator();
    	   while(iter.hasNext()){
    		   String line = iter.next();
    		   for(int i = 0; i < line.length(); i ++){
    			   if(line.charAt(i) == '{')
    				   count ++;
    			   else if(line.charAt(i) == '}')
    				   count --;
    			   if(count < 0)
    				   return false;
    		   } 
    	   }
    	   return true;
       }
       
       private void restoreAns(ArrayList<LinkedList<String>> _ans_of_block, LinkedList<String> ques, SimilarityMeasureMethod method){//还需要完善
    	   if(ques == null)
    		   return;
           Iterator<LinkedList<String>> iter = _ans_of_block.iterator();
           while(iter.hasNext()){
        	   LinkedList<String> ans = iter.next();
        	   String ques_str = new ListIntoString(ques).getStr();
        	   String ans_str = new ListIntoString(ans).getStr();
        	   //if(!ans.equals(ques))
        	   if(!ques_str.contains(ans_str))
        		   ques_ans_list.add(new QuesAns(ques, ans, method, getScore(ques, ans, method)));
           }
       }
       
       private boolean contain(QuesAns other){
    	   Iterator<QuesAns> iter = ques_ans_list.iterator();
    	   while(iter.hasNext()){
    		   QuesAns ques_ans = iter.next();
    		   if(ques_ans.equal(other))
    			   return true;
    	   }
    	   return false;
       }
       
       private int ansInsert(String _line, LinkedList<String> _ans, int _index, SimilarityMeasureMethod method){//判断_ans[index-1]是否为插入语句
    	   if(_line.equals("{") || _line.equals("}")) return -1;
    	   
    	   int size = _ans.size();
    	   for(int i = _index; i < size; i ++){
    		   Distance dis = new Distance(_line, _ans.get(i));
    		   if((method == SimilarityMeasureMethod.SimilarityMeasureByEditDistance && dis.editDistanceMatch())
    			  || (method == SimilarityMeasureMethod.SimilarityMeasureByCommonWords && dis.structureDistanceMatch())){
    			   return i - _index + 1;
    		   }
    	   }
    	   
    	   return -1;
       }
       
       private int quesInsert(String _line, LinkedList<String> _ques, int _index, SimilarityMeasureMethod method){//判断_ques[index-1]和其之前语句是否为插入语句
    	   if(_line.equals("{") || _line.equals("}")) return -1;
    	   
    	   int size = _ques.size();
    	   for(int i = _index; i < size; i ++){
    		   Distance dis = new Distance(_line, _ques.get(i));
    		   if((method == SimilarityMeasureMethod.SimilarityMeasureByEditDistance && dis.editDistanceMatch())
    			  || (method == SimilarityMeasureMethod.SimilarityMeasureByCommonWords && dis.structureDistanceMatch())){
    			   return i - _index + 1;
    		   }
    	   }
    	   
    	   return -1;
       }
  
}

