package cn.edu.pku.sei.plde.qacrashfix.faultlocalization;
public class BracketMatch {
	public String ClassName;
	public int Start;
	public int End;
	public String FileName;
	public String MName;

	public BracketMatch()
	{}
	
	public BracketMatch(int S, int E, String F)
	{
		Start = S;
		End = E;
		FileName = F;
		MName = "";
	}
	public BracketMatch(int S, int E, String F, String M)
	{
		Start = S;
		End = E;
		FileName = F;
		MName = M;
	}
	
	public BracketMatch(int S, int E, String F, String M, String C)
	{
		Start = S;
		End = E;
		ClassName = C;
		FileName = F;
		MName = M;
	}
}
