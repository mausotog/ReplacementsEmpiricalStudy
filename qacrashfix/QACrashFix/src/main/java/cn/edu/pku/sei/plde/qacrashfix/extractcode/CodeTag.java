package cn.edu.pku.sei.plde.qacrashfix.extractcode;

import org.htmlparser.tags.CompositeTag;

public class CodeTag extends CompositeTag {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String[] mIds = new String[] {"code"};  
   
    public String[] getIds() {  
        return mIds;  
    }  
   
    public String[] getEnders() 
    {  
        return mIds;  
    }
}
 