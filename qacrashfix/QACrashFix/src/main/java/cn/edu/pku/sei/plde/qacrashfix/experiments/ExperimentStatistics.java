package cn.edu.pku.sei.plde.qacrashfix.experiments;

public class ExperimentStatistics {
	public String url = "";
	public int stackoverflow_num = 0;
	public int origin_fix = 0;
	public int same_filter_fix = 0;
	public int jdt_parse_filter_fix = 0;
	public int compile_filter_fix = 0;
	public int remain_fix = 0;
	
	public int ques_ans_num = 0;
	public int ques_ans_source_num = 0; 
	
	public long first_begin_time = 0;
	public long first_time = 0;
	public long first_begin_compile_time = 0;
	public long first_compile_time = 0;
	public long all_begin_time = 0;
	public long all_time = 0;
	public long all_begin_compile_time = 0;
	public long all_compile_time = 0;
	public boolean compile_success = false;
}
