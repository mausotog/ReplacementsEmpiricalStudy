import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;

import com.github.gumtreediff.actions.ActionGenerator;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.client.Run;
import com.github.gumtreediff.gen.Generators;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.ITree;


public class CountInstancesOfTemplate1 {

	public static void main(String[] args) {



		try{
			Run.initGenerators();
			String file1 = new String(args[0]);
			String file2 = new String(args[1]);
			ITree src = Generators.getInstance().getTree(file1).getRoot();
			ITree dst = Generators.getInstance().getTree(file2).getRoot();
			Matcher m = Matchers.getInstance().getMatcher(src, dst); // retrieve the default matcher
			m.match();
			m.getMappings(); // return the mapping store
			ActionGenerator g = new ActionGenerator(src, dst, m.getMappings());
			g.generate();
			List<Action> actions = g.getActions(); // return the actions
			for (Action action : actions) {
				System.out.println(action.toString());
			}
			System.out.println("Looks like TestGrumtree works");
		}catch(IOException e){

		}
	}
/*
	ArrayList allInfor = new ArrayList[(String, String, ArrayBuffer[String])]; // var name, method name, args name
	var classInstanceInfor = new ArrayBuffer[(String, ArrayBuffer[String])]; // class name, args name
	var arrayAccess = new ArrayBuffer[(String, ArrayBuffer[String])]; // var name, index name


	private ArrayList matchAllTempaltes(String file1 , String file2) {
		ArrayList results = new ArrayList<Boolean>();
		List<Action> actions, MyJdtTreeGenerator srcTC, MyJdtTreeGenerator dstTC;
		getDiffActions(file1, file2, actions, srcTC, dstTC);
		gatherAllInforX(actions,srcTC, dstTC);
		if(actions.isEmpty()){
			System.out.println("Bachle: Warning in match Template: actions size is zero!!!");
		}
		if(!allInfor.isEmpty()) {
			results.add(match1ParameterReplacer(actions));
			//results.add(match2MethodReplacer(actions));
			//results.add(match3ParameterAdderRemover(actions));
		}
		*//*results.add(match4ExpressionReplacer(actions))
		results.add(match5ExpressionAdderRemover(actions))
		results.add(match6ObjectInitializer(actions))
		results.add(match7AddNullChecker(actions))
		results.add(match8AddRangeChecker(actions))
		results.add(match9AddColectionSizeChecker(actions))
		results.add(match10AddCastChecker(actions))*/
/*		return results;
	}
*/
	/*private void getDiffActions(String file1, String file2, List<Action> actions, MyJdtTreeGenerator srcTC, MyJdtTreeGenerator dstTC){

		srcTC = srcTG.generateFromFile(file1);
		dstTC = dstTG.generateFromFile(file2);
		src = srcTC.getRoot();
		dst = dstTC.getRoot();
		m = Matchers.getInstance().getMatcher(src, dst);
		m.`match`();
		val g = new ActionGenerator(src, dst, m.getMappings)
		g.generate();
		val actions = g.getActions;
	}*/
	/*
	private def gatherAllInforX(actions: List[Action],stg: MyJdtTreeGenerator, dtg: MyJdtTreeGenerator ) : ArrayBuffer[(String, String, ArrayBuffer[String])] ={
			val currentInfor = new ArrayBuffer[(String, String, ArrayBuffer[String])]()

					def helper(node: ITree, func: (ASTNode, CompilationUnit) =>  ArrayBuffer[(String, String, ArrayBuffer[String])]):  ArrayBuffer[(String, String, ArrayBuffer[String])] = {
							val jdtNode = JdtHandling.getJDTNodeOfITree(stg.getCompUnit, node)
									if (jdtNode != null) {
										return func(jdtNode, stg.getCompUnit)
									} else {
										//println("JDT2")
										val jdtNode2 = JdtHandling.getJDTNodeOfITree(dtg.getCompUnit, node)
												//println(jdtNode2)
												return func(jdtNode2, dtg.getCompUnit)
									}
					}

					for (ac <- actions) {
						if (nodeClassName(ac.getNode.getParent) == "MethodInvocation") {
							currentInfor.appendAll(helper(ac.getNode.getParent, gatherMethodIvocationInfor))
						}else if(nodeClassName(ac.getNode) == "MethodInvocation"){
							currentInfor.appendAll(helper(ac.getNode, gatherMethodIvocationInfor))
						} else if(nodeClassName(ac.getNode) == "InstanceofExpression"){
							val res = helper(ac.getNode, gatherInstanceofExpInfor)
									if(res != null)
										currentInfor.appendAll(res)
						} else if(nodeClassName(ac.getNode.getParent) == "InstanceofExpression"){
							val res = helper(ac.getNode.getParent, gatherInstanceofExpInfor)
									if(res != null)
										currentInfor.appendAll(res)
						} else if(nodeClassName(ac.getNode) == "Assignment"){
							currentInfor.appendAll(helper(ac.getNode, gatherAssignExpInfor))
						} else if(nodeClassName(ac.getNode.getParent) == "Assignment"){
							currentInfor.appendAll(helper(ac.getNode.getParent, gatherAssignExpInfor))
						} else if(nodeClassName(ac.getNode) == "InfixExpression"){
							currentInfor.appendAll(helper(ac.getNode, gatherExpInfor))
						} else if(nodeClassName(ac.getNode.getParent) == "InfixExpression"){
							currentInfor.appendAll(helper(ac.getNode.getParent, gatherExpInfor))
						} else if(nodeClassName(ac.getNode.getParent) == "PostfixExpression" || nodeClassName(ac.getNode.getParent) == "PrefixExpression"
						|| nodeClassName(ac.getNode.getParent) == "ConditionalExpression" ){
							currentInfor.appendAll(helper(ac.getNode.getParent, gatherExpInfor))
						} else if(nodeClassName(ac.getNode) == "ArrayAccess"){
							currentInfor.appendAll(helper(ac.getNode, gatherArrayAccess))
						} else if(nodeClassName(ac.getNode.getParent) == "ArrayAccess"){
							//println("xxx")
							currentInfor.appendAll(helper(ac.getNode.getParent, gatherArrayAccess))
						}  else if(nodeClassName(ac.getNode.getParent) == "FieldAccess"){
							//println("xxx")
							currentInfor.appendAll(helper(ac.getNode.getParent, gatherFieldAccess))
						} else if(nodeClassName(ac.getNode.getParent) == "SuperConstructorInvocation"){
							//println("xxx")
							currentInfor.appendAll(helper(ac.getNode.getParent, gatherSuperConstructor))
						} else if(nodeClassName(ac.getNode.getParent) == "SuperMethodInvocation"){
							//println("xxx")
							currentInfor.appendAll(helper(ac.getNode.getParent, gatherSuperMethod))
						} else if((nodeClassName(ac.getNode) == "SimpleName" && nodeClassName(ac.getNode.getParent) == "SimpleType" &&
						nodeClassName(ac.getNode.getParent.getParent) == "ClassInstanceCreation")){
							currentInfor.appendAll(helper(ac.getNode.getParent.getParent, gatherClassInstance))
						} else if((nodeClassName(ac.getNode) == "SimpleName" &&
						nodeClassName(ac.getNode.getParent) == "ClassInstanceCreation")){
							currentInfor.appendAll(helper(ac.getNode.getParent, gatherClassInstance))
						} else if((nodeClassName(ac.getNode) == "SimpleName" && nodeClassName(ac.getNode.getParent) == "SimpleType" &&
						nodeClassName(ac.getNode.getParent.getParent) == "CastExpression")){
							currentInfor.appendAll(helper(ac.getNode.getParent.getParent, gatherExpInfor))
						}
						else if(nodeClassName(ac.getNode) == "SimpleName" && nodeClassName(ac.getNode.getParent) == "SimpleType" && nodeClassName(ac.getNode.getParent.getParent) == "SingleVariableDeclaration"){
							currentInfor.appendAll(helper(ac.getNode.getParent.getParent, gatherVarDec))
						}
					}
					return currentInfor
	}*/

/*

	private boolean match1ParameterReplacer(List<Action> actions){
		boolean res = false;
		ArrayList<Action> visitedActions = new ArrayList<Action>();
		for (Action ac : actions) {
			visitedActions.add(ac);
			boolean sameParent = ac.getNode().getParent().isSimilar(visitedActions.get(0).getNode().getParent());
			//println("Similar Parent with actions[0]: " + sameParent)
			if (sameParent) {
				if (nodeClassName(ac.getNode().getParent()) == "MethodInvocation") {
					if (nodeClassName(ac.getNode()) != "SimpleName") {
						res = (actionName(ac) == "Update")? true:false;
					} else {
						return false;
					}
				}
			} else {
				return false;
			}
		}
		return res;
	}

	private String nodeClassName(ITree node){
		ASTNode.nodeClassForType(node.getType()).getSimpleName();
	}

	private String actionName(Action ac){ 
		ac.getClass().getSimpleName();
	}


*/

	/*
	def match1ParameterReplacer(actions: List[Action],stg: JdtTreeGenerator, dtg: JdtTreeGenerator ) : Boolean ={
		for(ac <- actions){
			if(nodeClassName(ac.getNode) == "SimpleName"){
				val realName = extractRealSimpleName(ac.getNode.getShortLabel)
			}
		}
		true
	}

	def match2MethodReplacer(actions: List[Action]): Boolean = {
		if (actions.size > 1) return false
				for (ac <- actions if nodeClassName(ac.getNode.getParent) == "MethodInvocation" 
				if nodeClassName(ac.getNode) == "SimpleName"
				if ac.getClass.getSimpleName == "Update") {
					return true
				}
		false
	}

	def match3ParameterAdderRemover(actions: List[Action]): Boolean = {
		var res = false
				val visitedActions = new ArrayList[Action]()
				for (ac <- actions) {
					visitedActions.add(ac)
					val sameParent = ac.getNode.getParent.isSimilar(visitedActions.get(0).getNode.getParent)
					//println("Similar Parent with actions[0]: " + sameParent)
					if (sameParent) {
						if (nodeClassName(ac.getNode.getParent) == "MethodInvocation") {
							if (nodeClassName(ac.getNode) != "SimpleName") {
								if (actionName(ac) == "Insert" || actionName(ac) == "Delete") {
									res = true
								}
							} else {
								return false
							}
						}
					} else {
						return false
					}
				}
		res
	}

	def match4ExpressionReplacer(actions: List[Action]): Boolean = {
		var res = false
				val visitedActions = new ArrayList[Action]()
				for (ac <- actions) {
					visitedActions.add(ac)
					val sameParent = ac.getNode.getParent.isSimilar(visitedActions.get(0).getNode.getParent)
					//println("Similar Parent with actions[0]: " + sameParent)
					if (sameParent) {
						if (nodeClassName(ac.getNode.getParent) == "IfStatement" || 
								nodeClassName(ac.getNode.getParent) == "WhileStatement") {
							if (actionName(ac) == "Update") {
								res = true
							}
						} else {
						}
					} else {
						return false
					}
				}
		res
	}

	def match5ExpressionAdderRemover(actions: List[Action]): Boolean = {
		var res = false
				val visitedActions = new ArrayList[Action]()
				for (ac <- actions) {
					visitedActions.add(ac)
					val sameParent = ac.getNode.getParent.isSimilar(visitedActions.get(0).getNode.getParent)
					//println("Similar Parent with actions[0]: " + sameParent)
					if (sameParent) {
						if (nodeClassName(ac.getNode.getParent) == "IfStatement" || 
								nodeClassName(ac.getNode.getParent) == "WhileStatement") {
							if (actionName(ac) == "Update") {
								return false
							}
						} else {
						}
					} else {
						if (nodeClassName(ac.getNode.getParent) == "InfixExpression") {
							if (actionName(ac) == "Insert" || actionName(ac) == "Delete") {
								res = true
							}
						} else {
							return false
						}
					}
				}
		res
	}

	def match6ObjectInitializer(actions: List[Action]): Boolean = {
		var res = false
				val arrActions = actions.toArray(new Array[Action](actions.size))
				if (nodeClassName(arrActions(0).getNode.getParent) == "VariableDeclarationFragment" || 
				nodeClassName(arrActions(0).getNode.getParent) == "Block") {
					for (ac <- actions) {
						if (actionName(ac) != "Insert") return false
								if (nodeClassName(ac.getNode) == "ClassInstanceCreation") {
									res = true
								}
					}
				}
		res
	}

	def match7AddNullChecker(actions: List[Action]): Boolean = {
		var res = false
				val arrActions = actions.toArray(Array.ofDim[Action](actions.size))
				if (nodeClassName(arrActions(0).getNode.getParent) == "Block") {
					if (nodeClassName(arrActions(0).getNode) == "IfStatement") {
						if (actionName(arrActions(0)) != "Insert") return false
								for (ac <- actions if nodeClassName(ac.getNode) == "InfixExpression" if nodeClassName(ac.getNode.getChild(1)) == "NullLiteral") res = true
					}
				}
		res
	}

	def match8AddRangeChecker(actions: List[Action]): Boolean = {
		var res = false
				var lowerCheck = false
				var upperCheck = false
				val arrActions = actions.toArray(Array.ofDim[Action](actions.size))
				if (nodeClassName(arrActions(0).getNode.getParent) == "Block") {
					if (nodeClassName(arrActions(0).getNode) == "IfStatement") {
						if (actionName(arrActions(0)) != "Insert") return false
								for (ac <- actions if nodeClassName(ac.getNode) == "InfixExpression") {
									if (ac.getNode.getShortLabel == "<" || ac.getNode.getShortLabel == "<=") {
										upperCheck = true
									}
									if (ac.getNode.getShortLabel == ">=" || ac.getNode.getShortLabel == ">") {
										lowerCheck = true
									}
									if (lowerCheck && upperCheck) {
										res = true
									}
								}
					}
				}
		res
	}

	def match9AddColectionSizeChecker(actions: List[Action]): Boolean = {
		var res = false
				val arrActions = actions.toArray(Array.ofDim[Action](actions.size))
				if (nodeClassName(arrActions(0).getNode.getParent) == "Block") {
					if (nodeClassName(arrActions(0).getNode) == "IfStatement") {
						if (actionName(arrActions(0)) != "Insert") return false
								for (ac <- actions if nodeClassName(ac.getNode) == "InfixExpression") {
									if (ac.getNode.getShortLabel == "<" || ac.getNode.getShortLabel == "<=") {
										res = true
									}
									if (ac.getNode.getShortLabel == ">" || ac.getNode.getShortLabel == ">=") {
										res = if (res) false else true
									}
								}
					}
				}
		res
	}

	def match10AddCastChecker(actions: List[Action]): Boolean = {
		var res = false
				val arrActions = actions.toArray(Array.ofDim[Action](actions.size))
				if (nodeClassName(arrActions(0).getNode.getParent) == "Block") {
					if (nodeClassName(arrActions(0).getNode) == "IfStatement") {
						if (actionName(arrActions(0)) != "Insert") return false
								for (ac <- actions if nodeClassName(ac.getNode) == "InstanceofExpression" 
								if nodeClassName(ac.getNode.getChild(1)) == "SimpleType") res = true
					}
				}
		res
	}
	 */

}

