package cn.edu.pku.sei.plde.qacrashfix.faultlocalization;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadFile {
	private String content;
	
	public ReadFile(String _file_path){
	 	   StringBuilder sb = new StringBuilder();
	 	   try{
	 		   BufferedReader br = new BufferedReader(new FileReader(_file_path));
	 		   String line = "";
	 		   while ((line = br.readLine()) != null){
	 			   sb.append(line+"\n");
	 		   }
	 		   br.close();
	 	   }
	 	   catch (IOException e){
	 		   e.printStackTrace();
	 	   }
	 	   content = sb.toString();
	}
    
	public String getContent(){
		return content;
	}
}
