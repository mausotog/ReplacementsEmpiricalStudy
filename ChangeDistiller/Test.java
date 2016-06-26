import java.io.File;
import java.util.List;

import ch.uzh.ifi.seal.changedistiller.JavaChangeDistillerModule;
import ch.uzh.ifi.seal.changedistiller.distilling.FileDistiller;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;

import com.google.inject.Guice;
import com.google.inject.Injector;
//import ch.uzh.ifi.seal.changedistiller.ChangeDistiller.Language;
public class Test{
	private static FileDistiller createFileDistiller () {
	
            Injector injector = Guice.createInjector(new JavaChangeDistillerModule());
            return injector.getInstance(FileDistiller.class);
   
	}
	public static void main(String args[]){
	File left = new File("/home/mau/Research/tools-changedistiller/resources/testdata/src_change/TestLeft.java");
	File right = new File("/home/mau/Research/tools-changedistiller/resources/testdata/src_change/TestRight.java");
	FileDistiller distiller = Test.createFileDistiller();
//	FileDistiller distiller = Test.createFileDistiller(Language.JAVA);
	try{
	    distiller.extractClassifiedSourceCodeChanges(left, right);
	} catch(Exception e) {
	    /* An exception most likely indicates a bug in ChangeDistiller. Please file a
	       bug report at https://bitbucket.org/sealuzh/tools-changedistiller/issues and
	       attach the full stack trace along with the two files that you tried to distill. */
	    System.err.println("Warning: error while change distilling. " + e.getMessage());
	}

	List<SourceCodeChange> changes = distiller.getSourceCodeChanges();
	if(changes != null) {
	    for(SourceCodeChange change : changes) {
	    	switch(change.getChangeType()){
	    	case ADDING_ATTRIBUTE_MODIFIABILITY:
	    	case ADDING_CLASS_DERIVABILITY:
	    	case ADDING_METHOD_OVERRIDABILITY:
	    	case ADDITIONAL_CLASS:
	    	case ADDITIONAL_FUNCTIONALITY:
	    	case ADDITIONAL_OBJECT_STATE:
	    	case ALTERNATIVE_PART_INSERT:
	    	case COMMENT_INSERT:
	    	case DOC_INSERT:
	    	case PARAMETER_INSERT:
	    	case PARENT_CLASS_INSERT:
	    	case PARENT_INTERFACE_INSERT:
	    	case RETURN_TYPE_INSERT:
	    	case STATEMENT_INSERT:
	    		System.out.println("Insert");
	    		break;
	    		
	    	case ALTERNATIVE_PART_DELETE:
	    	case COMMENT_DELETE:
	    	case DOC_DELETE:
	    	case PARAMETER_DELETE:
	    	case PARENT_CLASS_DELETE:
	    	case PARENT_INTERFACE_DELETE:
	    	case REMOVED_CLASS:
	    	case REMOVED_FUNCTIONALITY:
	    	case REMOVED_OBJECT_STATE:
	    	case REMOVING_ATTRIBUTE_MODIFIABILITY:
	    	case REMOVING_CLASS_DERIVABILITY:
	    	case REMOVING_METHOD_OVERRIDABILITY:
	    	case RETURN_TYPE_DELETE:
	    	case STATEMENT_DELETE:
	    		System.out.println("Delete");
	    		break;
	    		
	    	case ATTRIBUTE_RENAMING:
	    	case ATTRIBUTE_TYPE_CHANGE:
	    	case CLASS_RENAMING:
	    	case COMMENT_MOVE: //I assumme this to be a replace for an empty statement
	    	case COMMENT_UPDATE:
	    	case CONDITION_EXPRESSION_CHANGE:
	    	case DECREASING_ACCESSIBILITY_CHANGE:
	    	case DOC_UPDATE:
	    	case INCREASING_ACCESSIBILITY_CHANGE:
	    	case METHOD_RENAMING:
	    	case PARAMETER_ORDERING_CHANGE:
	    	case PARAMETER_RENAMING:
	    	case PARAMETER_TYPE_CHANGE:
	    	case PARENT_CLASS_CHANGE:
	    	case PARENT_INTERFACE_CHANGE:
	    	case RETURN_TYPE_CHANGE:
	    	case STATEMENT_ORDERING_CHANGE:
	    	case STATEMENT_PARENT_CHANGE:
	    	case STATEMENT_UPDATE:
	    		System.out.println("Replace/Update");
	    		break;
	    		
	    	case UNCLASSIFIED_CHANGE:
	    		System.out.println("Unclassified change");
	    		break;

	    	}
	        // see Javadocs for more information
	    	System.out.println("Change type: " + change.getChangeType());
	    	System.out.println("Label: " + change.getLabel());	
	    	System.out.println("Change: " + change.toString());
	    	System.out.println("Changed Entity: " + change.getChangedEntity());
	    	System.out.println("Significance level: " + change.getSignificanceLevel());
	    	System.out.println("Hash Code: " + change.hashCode() + "\n");
	    }
	}
	}
}
