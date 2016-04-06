package cn.edu.pku.sei.plde.qacrashfix.faultlocalization;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import cn.edu.pku.sei.plde.qacrashfix.extractcode.StringIntoList;
import cn.edu.pku.sei.plde.qacrashfix.jdt.JDTTreeGenerator;

/**
 * used to get wanted patches for certain exception
 */
public class ProcessTrace {
	private String project_path;
	private LinkedList<FileInfo> related_file_infos;
	public ProcessTrace(String _project_path) {
		this.project_path = _project_path;
		this.related_file_infos = new LinkedList<FileInfo>();
		this.getRelatedFilesFromTrace();
	}
    
	public LinkedList<FileInfo> getRelatedFileInfos(){
		return related_file_infos;
	}

	/**
	 * use the traces
	 * @return a list of FileInfo with each describing a related file
	 */
	private void copyFile(String srcFilePath, String targetFilePath){
		try {
			Process cp = Runtime.getRuntime().exec("cmd /c copy " + srcFilePath + " " + targetFilePath);
			cp.waitFor();
			cp.destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    private LinkedHashMap<String, String> getAllFilesInProj(){//搜集项目中所有java文件，将其文件名和绝对路径存放在table中
		   LinkedHashMap<String, String> table_files = new LinkedHashMap<String, String>(); 
		   File file = new File(this.project_path);
		   LinkedList<File> file_list = new LinkedList<File>();
		   file_list.add(file);
		   while (!file_list.isEmpty()){
			     File first_file = file_list.removeFirst();
			     File [] sub_files = first_file.listFiles();
			     for (File sub_file : sub_files){
				     if (sub_file.isDirectory())
				    	 file_list.add(sub_file);
				     else if (sub_file.getName().endsWith(".java")){
				    	 table_files.put(sub_file.getName(), sub_file.getAbsolutePath()); 
				    	 if(new File(sub_file.getName() + ".bak").exists())
				    		 copyFile(sub_file.getName() + ".bak", sub_file.getName());
				     }
			     }
		   }
		   return table_files;
    }
    
    private void extractFaultLocaltionFromNormalTrace(String _line, LinkedHashMap<String, String> _table_files, LinkedHashMap<String, FileInfo> _table_rlts){
    	    int left_bracket_pos = _line.lastIndexOf('(');
			int right_bracket_pos = _line.lastIndexOf(')');
			int semicolon_pos = _line.lastIndexOf(':');
			if (left_bracket_pos == -1 || right_bracket_pos == -1 || semicolon_pos == -1 || !(left_bracket_pos < semicolon_pos && semicolon_pos < right_bracket_pos))
				  return;
			String file_name = _line.substring(left_bracket_pos + 1, semicolon_pos);
			int line_index = Integer.parseInt(_line.substring(semicolon_pos + 1, right_bracket_pos));
			addFaultLocaltion(file_name, line_index, _table_files, _table_rlts);
    }
    
    private void extractFaultLocaltionFromUnnormalTrace(String _line, LinkedHashMap<String, String> _table_files, LinkedHashMap<String, FileInfo> _table_rlts){
            String[] words = _line.split(" |\\{|\\}|:|/|$|\\(|\\)");
            for (String word : words){
                if (word.contains(".")){
        	        int index = word.lastIndexOf(".");
        	        String file_name = word.substring(index + 1) + ".java";
        	        addFaultLocaltion(file_name, -1, _table_files, _table_rlts);
		        }
            }
    }
    
    private void addFaultLocaltion(String _file_name, int _line_index, LinkedHashMap<String, String> _table_files, LinkedHashMap<String, FileInfo> _table_rlts){
		String path = _table_files.get(_file_name); // match with filename. arbitrary
		if (path == null)
			return;
		FileInfo info = _table_rlts.get(path);
		if (info == null){
		   info = new FileInfo();
		   info.absolute_file_path = path;
		   String content = (new ReadFile(info.absolute_file_path)).getContent();
		   info.parse_java_file = new ParseJavaFile(content);
		   info.origin_source = (new StringIntoList(content)).getLineList();
		   try{
			   info.class_begin = (new FindMethodLocaltion()).FindClassBegin(info.absolute_file_path);
		   }
		   catch (Exception e){
			   e.printStackTrace();
		   }
		   info.addSuspiciousLine(_line_index);
		   _table_rlts.put(info.absolute_file_path, info);
		}
		else info.addSuspiciousLine(_line_index);
    }
    
    private void addRelatedFileInfos(LinkedHashMap<String, FileInfo> _table_rlts){
		    for (FileInfo info : _table_rlts.values()){
			    LinkedList<Integer> suspicious_lines = info.getSuspiciousLine();
			    int size = suspicious_lines.size();
			    if(size >= 2 && suspicious_lines.contains(-1)){
					for(int i = 0; i < size; i ++)
						if(suspicious_lines.get(i) == -1){
							suspicious_lines.remove(i);
							size --;
						}
			    }
			    related_file_infos.add(info);
		     }
    }
    
    private void getRelatedFilesFromTrace(){
    	ReadFile rf = new ReadFile(project_path+"\\exception.trace");
		LinkedList<String> traces = (new StringIntoList(rf.getContent())).getLineList();
		LinkedHashMap<String, String> table_files = getAllFilesInProj();//<filename, absolutepath>
        LinkedHashMap<String, FileInfo> table_rlts = new LinkedHashMap<String,FileInfo>(); //absolute path -> fileInfo
        
		for (String line : traces)
			if (line.contains(".java"))
			    extractFaultLocaltionFromNormalTrace(line, table_files, table_rlts);
		for(String line : traces)
			if (line.contains("{"))//process Window{426f9f28 u0 org.onepf.angrybots/com.openiab.BillingActivity}
				extractFaultLocaltionFromUnnormalTrace(line, table_files, table_rlts);
		addRelatedFileInfos(table_rlts);
    }
}
