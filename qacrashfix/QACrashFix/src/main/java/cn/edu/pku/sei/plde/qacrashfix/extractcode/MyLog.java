package cn.edu.pku.sei.plde.qacrashfix.extractcode;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class MyLog {
	private Logger log;
    public MyLog(String _path) {
    	try{
           log = Logger.getLogger(_path); 
           log.setUseParentHandlers(false);  
           log.setLevel(Level.INFO); 
           File file = new File(_path);
           if(file.exists())
        	   file.delete();
           FileHandler fileHandler = new FileHandler(_path); 
           fileHandler.setLevel(Level.INFO); 
           fileHandler.setFormatter(new MyLogHander()); 
           log.addHandler(fileHandler); 
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
   } 
   
   public void logQuesAnsSource(QuesAnsSource _qas){
	   logSignLine("begin");
	   logList(_qas.getQues());
	   logList(_qas.getAns());
	   logList(_qas.getSource());
	   logStr(_qas.getPath());
	   logStr(String.valueOf(_qas.getBegin()));
	   logSignLine("end");
   }
   
   public void logList(LinkedList<String> _content){
	   StringBuilder sb = new StringBuilder();
	   Iterator<String> iter = _content.iterator();
	   while(iter.hasNext()){
		   sb.append(iter.next() + "\r\n");
	   }
	   logStr(sb.toString());
   }
   
   private void logStr(String _str){
	   log.info(_str);
   }
   
   private void logSignLine(String _str){
	   logStr("****************************************   " + _str + "   ****************************************");
   }
}

class MyLogHander extends Formatter { 
    @Override 
    public String format(LogRecord _record) { 
            return _record.getMessage()+"\r\n"; 
    } 
}