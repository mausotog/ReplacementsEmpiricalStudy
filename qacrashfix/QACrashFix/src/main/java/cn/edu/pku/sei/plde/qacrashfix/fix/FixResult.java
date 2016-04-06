package cn.edu.pku.sei.plde.qacrashfix.fix;

import java.io.File;
import java.util.LinkedList;
/**
 * 
 * @author Jie Wang
 *
 */
public final class FixResult {
	private final String _patchedFilePath;
	private final int _begin;
	private final int _end;
	private final LinkedList<String> _complete_source;
	private final String _patch;
	private final static String _DELIMITER = "**********************************\n";
	
	public FixResult(String patchedFilePath, int begin, int end, LinkedList<String> complete_source, String patch){
		_patchedFilePath = patchedFilePath;
		_begin =  begin;
		_end = end;
		_complete_source = complete_source;
		_patch = patch;
	}
	
	private String getModifiedSourceCode(){
		StringBuilder  sb  = new  StringBuilder();
		for (int i= _begin; i<_end; i++)
			sb.append(_complete_source.get(i) + "\n");
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (! (obj instanceof FixResult))
			return false;
		FixResult other = (FixResult) obj;
		return _patchedFilePath.equals(other._patchedFilePath) &&
				_begin == other._begin &&
				_end == other._end &&
				_complete_source.equals(other._complete_source) && 
				_patch.equals(other._patch);
	}
	
	public String getPatchedFilePath(){
		return _patchedFilePath;
	}
	
	public String getPatchedFileName(){
		File file = new File(_patchedFilePath);
		return file.getName();
	}
	
	public int getBegin(){
		return _begin;
	}
	
	public int getEnd(){
		return _end;
	}
	
	public LinkedList<String> getCompleteSource(){
		return _complete_source;
	}
	
	public String getPatch(){
		return _patch;
	}

	public String getFixedSource(){
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < _begin - 1; i ++)
			sb.append(_complete_source.get(i) + "\n");
		sb.append(_patch + "\n");
		for (int i = _end; i < _complete_source.size(); i ++)
			sb.append(_complete_source.get(i) + "\n");
		return sb.toString();
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
        sb.append(_patchedFilePath);
        sb.append(_DELIMITER);
        sb.append(getModifiedSourceCode());
        sb.append(_DELIMITER);
        sb.append(_patch);
        return sb.toString();
	}
}
