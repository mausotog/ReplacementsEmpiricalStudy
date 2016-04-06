package cn.edu.pku.sei.plde.qacrashfix.extractcode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.PrototypicalNodeFactory;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.Span;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import cn.edu.pku.sei.plde.qacrashfix.faultlocalization.ReadFile;
import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeGenerator;

public class QuesAnsBlockExtractFromWeb
{
	
	private ArrayList<LinkedList<String>> ques_of_block;
	private LinkedList<QuesAnsBlockFromWeb> ques_ans_block_from_web_list;
	
	public QuesAnsBlockExtractFromWeb(String _stackoverflow_path, String _issue_time, boolean _early) throws ParseException{
		String page = new ReadFile(_stackoverflow_path).getContent();
		ques_of_block = new ArrayList<LinkedList<String>>();
		ques_ans_block_from_web_list = new LinkedList<QuesAnsBlockFromWeb>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//小写的mm表示的是分钟  
        try{
		   Date date = sdf.parse(_issue_time); 		
		   getCode(page, true, date, _early);
		   getCode(page, false, date, _early);
        }catch(Exception e){
        	e.printStackTrace();
        }
	}
	
	public LinkedList<QuesAnsBlockFromWeb> getQuesAnsBlockFromWebList(){
		return ques_ans_block_from_web_list;
	}
	
	private NodeList getNodeList(String _html, NodeFilter _filter){
        Parser parser = Parser.createParser(_html, "utf8");
        NodeList node_list = null;
        try{
           node_list = parser.extractAllNodesThatMatch(_filter);
        }
        catch(Exception e){
        	e.printStackTrace();
        }
        return node_list;
	}
	
	private void getCode(String _html, boolean _isques,Date _issue_time, boolean _early){
		NodeFilter filter;
		if(_isques)
			filter = new AndFilter( new TagNameFilter("div"),new HasAttributeFilter("class","question"));
		else 
			filter = new AndFilter(new TagNameFilter("div") , new OrFilter(new HasAttributeFilter("class", "answer accepted-answer"), new HasAttributeFilter("class","answer")));
		
		NodeList node_list = getNodeList(_html, filter);
		NodeIterator block_iterator  = node_list.elements();
		try {
			while(block_iterator.hasMoreNodes()){
				Node block_node = block_iterator.nextNode();
				String ans_text_of_block = null;
				if (!_isques){
					ans_text_of_block = StringEscapeUtils.unescapeHtml3(block_node.toPlainTextString());
				}
                
				String block_html = block_node.toHtml();
                if(_early && codeIsLate(block_html, _issue_time)) 
                	continue;
                
                NodeList code_node_list = getCodeNode(block_html, _isques);
                NodeIterator code_iterator = code_node_list.elements();
                ArrayList<LinkedList<String>> code_of_block = new ArrayList<LinkedList<String>>();
            	Map<LinkedList<String>, Integer> index_map = new HashMap<LinkedList<String>, Integer>();
                
                while(code_iterator.hasMoreNodes()){
                	Node code_node = code_iterator.nextNode();
                	String code_node_unescape = StringEscapeUtils.unescapeHtml3(code_node.toPlainTextString());
                	code_node_unescape = replaceEllipses(code_node_unescape);
                	code_node_unescape = modifyPrintStackTrace(code_node_unescape);
                	try{
                	   JDTTreeGenerator jtg = new JDTTreeGenerator(code_node_unescape);
                	   LinkedList<String> code = (new StringIntoList(jtg.getFormalizedCode())).getLineList();
                	   if(code != null && code.size() != 0){
                	      code_of_block.add(code);
                	      if (!_isques)
                	    	  index_map.put(code, ans_text_of_block.indexOf(code_node_unescape));
                	   }
                	}
                	catch(Exception e){
                		continue;
                	}
                }
                
                if(_isques && code_of_block.size() > 0)
                	ques_of_block = new ArrayList<LinkedList<String>>(code_of_block);
                if(!_isques && code_of_block.size() > 0){
                	QuesAnsBlockFromWeb qabfw = new QuesAnsBlockFromWeb(code_of_block, ans_text_of_block, ques_of_block, index_map);
                	ques_ans_block_from_web_list.add(qabfw);
                }
			}
		} 
		
		catch (ParserException e) {
			e.printStackTrace();
		}
	}
	
    private String replaceEllipses(String original){
        if(original.contains("..."))
        	return original.replace("...", " ");
        return original;
    }

    private String modifyPrintStackTrace(String code){
    	if(code.contains("e.print") && code.contains("Trace();") && !code.contains("e.printStackTrace();")){
    		int index1 = code.indexOf("e.print");
    		int index2 = code.indexOf("Trace();") + "Trace();".length();
    		String sub = code.substring(index1, index2);
    		code = code.replace(sub, "e.printStackTrace();");
    	}	
    	return code;
    }
	
	private NodeList getCodeNode(String _html, boolean _isques){
		NodeFilter filter = null;
		if(_isques)
			filter = new AndFilter(new TagNameFilter("td") , new HasAttributeFilter("class", "postcell"));
		else
			filter = new AndFilter(new TagNameFilter("td") , new HasAttributeFilter("class","answercell"));
		
		NodeList node_list = null;
		try{
		    Parser parser = new Parser(_html);
  	        node_list = parser.extractAllNodesThatMatch(filter);
            PrototypicalNodeFactory factory = new PrototypicalNodeFactory();
            factory.registerTag( new CodeTag() );
        
  	        parser = new Parser(node_list.elementAt(0).toHtml());
            parser.setNodeFactory ( factory );
            filter =  new TagNameFilter("code");
            node_list = parser.extractAllNodesThatMatch(filter);
		}
		
		catch(Exception e){
			e.printStackTrace();
		}
        
        return node_list;
	}
	
	private boolean codeIsLate(String _html, Date _issue_time){
		  Date ans_time = getTime(_html);
          if(ans_time.compareTo(_issue_time) != -1)//ansTime < issueTime
        	  return false;
          return true;
	}
	
	private Date getTime(String _page)
	{
		try{
		   NodeFilter filter = new AndFilter(new TagNameFilter("div") , new HasAttributeFilter("class", "user-action-time"));
		   NodeList node_list = getNodeList(_page, filter);
		   NodeIterator iterator = node_list.elements();
		   while(iterator.hasMoreNodes()){
		       String str = iterator.nextNode().toHtml();
		       if(str.contains("asked") || str.contains("answered")){
		          Parser parser = new Parser(str);
		          Node node = parser.extractAllNodesThatMatch(new TagNameFilter("span")).elementAt(0);
		    	  Span span = (Span) node;
		          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//小写的mm表示的是分钟  
		          Date date = sdf.parse(span.getAttribute("title").replace("Z", "").trim()); 
		          return date;
		       }
		   }
		 }
		
		catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
}
