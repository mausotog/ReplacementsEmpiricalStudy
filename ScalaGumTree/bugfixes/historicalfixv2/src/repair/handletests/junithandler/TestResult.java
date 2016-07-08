package repair.handletests.junithandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Matias Martinez,  matias.martinez@inria.fr
 *
 */
public class TestResult {
	
	public int casesExecuted = 0;
	public int failures = -1;
	public List<String> successTest =new ArrayList<String>();
	 
	public  List<String> failTest =new ArrayList<String>();

	 public List<String> getSuccessTest() {
		return successTest;
	}

	public void setSuccessTest(List<String> successTest) {
		this.successTest = successTest;
	}

	public List<String> getFailures() {
		return failTest;
	}

	public void setFailTest(List<String> failTest) {
		this.failTest = failTest;
	}

	public boolean wasSuccessful(){
		return failTest.isEmpty();
	}

	@Override
	public String toString() {
		return "TR: Success: "+ failTest.isEmpty() + ", failTest= "
				+ failures + ", was successful: "+this.wasSuccessful()+"] ,"+ this.failTest;
	}

	public int getFailureCount(){
		return failures;
	}
	
	 
	 
}
