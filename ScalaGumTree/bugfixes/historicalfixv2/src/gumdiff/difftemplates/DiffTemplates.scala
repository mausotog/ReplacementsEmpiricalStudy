package gumdiff.difftemplates

import java.io.File
import java.util.ArrayList
import java.util.List
import gumdiff.handlejdt.{SimpleNameVisitor, JdtHandling}
import gumdiff.jdtgum.MyJdtTreeGenerator
import myLib.MyScalaLib
import org.eclipse.jdt.core.dom._
import com.github.gumtreediff.actions.ActionGenerator
import com.github.gumtreediff.actions.model.Action
import com.github.gumtreediff.client.Run
import com.github.gumtreediff.gen.jdt.JdtTreeGenerator
import com.github.gumtreediff.matchers.{MappingStore, Matchers}
import com.github.gumtreediff.tree.{TreeContext, ITree, Tree}
import org.w3c.dom.NodeList
import scala.util.control.Breaks._
import scala.collection.mutable.ListBuffer


import scala.collection.mutable.ArrayBuffer

//remove if not needed
import scala.collection.JavaConversions._

object DiffTemplates {

  def main(args: Array[String]) {
    /*val (fixFile,oldFile) =
      //SettingsAndUtils.getFixOldFiles("/home/dxble/Desktop/data_bigquery/alldata_raw/myresults_results-20150622-223455_1.5t_1t.csv/zeroturnaround_zt-zip/modifiedFiles/2")
      //SettingsAndUtils.getFixOldFiles("/home/dxble/Desktop/data_bigquery/alldata_raw/myresults_results-20150622-223146_5t_3t.csv/jooby-project_jooby/modifiedFiles/2/")
      //SettingsAndUtils.getFixOldFiles("/home/dxble/Desktop/data_bigquery/alldata_raw/myresults_results-20150622-223146_5t_3t.csv/jenkinsci_perforce-plugin/modifiedFiles/2")
      SettingsAndUtils.getFixOldFiles("/home/dxble/Desktop/data_bigquery/alldata_withUnit/all/takari_io.takari.incrementalbuild/modifiedFiles/4")
    val diff = new DiffTemplates
    val (actions,stc,dtc) = diff.getDiffActions(oldFile.getCanonicalPath,fixFile.getCanonicalPath)
    diff.printActions(actions,stc,dtc)*/
    //diff.printMappings(diff.getDiffMappings(oldFile.getCanonicalPath,fixFile.getCanonicalPath))
    //println(ActionHandling.needRootOnly(actions))
    //val gs=ActionHandling.serializeDiff2GSpanGraph(actions,0)
    //for(line <- gs){
    //  println(line)
    //}

    //println(SettingsAndUtils.isSatisfiedDataCondtion("/home/dxble/Desktop/data_bigquery/alldata_raw/myresults_results-20150622-223146_5t_3t.csv/mdeverdelhan_ta4j/modifiedFiles/2/"))
    //println(diff.matchExpressionReplacer(actions))
    //println(diff.matchExpressionAdderRemover(actions))
    //test()
    matchAllTempaltes(args(0), args(1))
    //test2()
  }

  def test2() = {
    val diff = new DiffTemplates()
    val file1=new File("/home/dxble/MyWorkSpace/diffExamples/src/main/java/Expresion_base2.java")
    val file2=new File("/home/dxble/MyWorkSpace/diffExamples/src/main/java/Expresion_base2_test1.java")
    //val (file2,file1) = SettingsAndUtils.getFixOldFiles("/home/dxble/Desktop/data_bigquery/alldata_withUnit/all/ModeShape_modeshape/modifiedFiles/4")
    val (actions,stc,dtc) = diff.getDiffActions(file1.getCanonicalPath, file2.getCanonicalPath)
    println("Action size: " + actions.size)
    diff.gatherAllInfor(actions,stc,dtc)
    //diff.printActions(actions,stc.getCompUnit)
    val buf = ActionHandling.serializeDiff2GSpanGraph(actions, 0, "", diff)
    for (line <- buf) {
      MyScalaLib.appendFile(SettingsAndUtils.GSPAN_INPUT, line)
      println(line)
    }

  }

  def test() = {
    val diff = new DiffTemplates()
    val file1=new File("/home/xuanbach/workspace/bugfiles/reports/ec/3187/bugFiles/PartStack.java")
    val file2=new File("/home/xuanbach/workspace/bugfiles/reports/ec/3187/fixedFiles/PartStack.java")
    //val (file2,file1) = SettingsAndUtils.getFixOldFiles("/home/dxble/Desktop/data_bigquery/alldata_withUnit/all/ModeShape_modeshape/modifiedFiles/4")
    val (actions,stc,dtc) = diff.getDiffActions(file1.getCanonicalPath, file2.getCanonicalPath)
    println("Action size: " + actions)
    diff.gatherAllInfor(actions,stc,dtc)
    //diff.printActions(actions,stc.getCompUnit)
    val buf = ActionHandling.serializeDiff2GSpanGraph(actions, 0, "", diff)
    for (line <- buf) {
      MyScalaLib.appendFile(SettingsAndUtils.GSPAN_INPUT_2, line)
      println(line)
    }

  }
/*
  def matchAllTempaltes_old(file1: String, file2: String) ={
    val results = new ArrayBuffer[Int]()
    val diff = new DiffTemplates()
    val (actions,srcTC,dstTC) = diff.getDiffActions(file1, file2)
    if(actions.isEmpty)
      println("Bachle: Warning in match Template: actions size is zero!!!")
    results.add(diff.match1ParameterReplacer(actions))
    results.add(diff.match2MethodReplacer(actions))
    results.add(diff.match3ParameterAdderRemover(actions))
    results.add(diff.match4ExpressionReplacer(actions))
    results.add(diff.match5ExpressionAdderRemover(actions))
    results.add(diff.match6ObjectInitializer(actions))
    results.add(diff.match7AddNullChecker(actions))
    results.add(diff.match8AddRangeChecker(actions))
    results.add(diff.match9AddColectionSizeChecker(actions))
    results.add(diff.match10AddCastChecker(actions))
    results
  }*/

  def matchAllTempaltes(file1: String, file2: String) ={
    val results = new ArrayBuffer[Int]()
    val diff = new DiffTemplates()
    val (actions,srcTC,dstTC) = diff.getDiffActions(file1, file2)
    diff.gatherAllInforX(actions,srcTC, dstTC)
    for(action <- actions){
      //println("ACTION: " + action)
	//println("Node: "+ diff.nodeClassName(action.getNode))
        //println("NodeParent: "+ diff.nodeClassName(action.getNode.getParent))
	//println()
    }
    if(actions.isEmpty){
      //println("Bachle: Warning in match Template: actions size is zero!!!")
    }

    var res1 = diff.match1AddNullChecker(actions)
    var res2 = diff.match2ParameterReplacer(actions)
    var res3 = diff.match3MethodReplacer(actions)
    var res4 = diff.match4ParameterAdderRemover(actions)
    var res5 = diff.match5ObjectInitializer(actions)
    var res7 = diff.match7AddRangeChecker(actions)
    var res8 = diff.match8AddColectionSizeChecker(actions)
    var res9 = diff.match9LowerBoundSetter(actions)
    var res10 = diff.match10UpperBoundSetter(actions)
    var res11 = diff.match11OffByOneMutator(actions)
    var res12 = diff.match12AddCastChecker(actions)
    var res13 = diff.match13CasterMutator(actions)
    var res14 = diff.match14CasteeMutator(actions)
    var res15 = diff.match15ExpressionChanger(actions)
    var res16 = diff.match16ExpressionAdder(actions)
    var res6 = diff.match6SequenceExchanger(actions)
 


    println(res1+": 1AddNullChecker")
    println(res2+": 2ParameterReplacer")
    println(res3+": 3MethodReplacer")
    println(res4+": 4ParameterAdderRemover")
    println(res5+": 5ObjectInitializer")
    println(res6+": 6SequenceExchanger")
    println(res7+": 7AddRangeChecker")
    println(res8+": 8AddColectionSizeChecker")
    println(res9+": 9LowerBoundSetter")
    println(res10+": 10UpperBoundSetter")
    println(res11+": 11OffByOneMutator")
    println(res12+": 12AddCastChecker")
    println(res13+": 13CasterMutator")
    println(res14+": 14CasteeMutator")
    println(res15+": 15ExpressionChanger")
    println(res16+": 16ExpressionAdder")
   
    results
  }
}


class DiffTemplates {
  var allInfor = new ArrayBuffer[(String, String, ArrayBuffer[String])] // var name, method name, args name
  var classInstanceInfor = new ArrayBuffer[(String, ArrayBuffer[String])] // class name, args name
  var arrayAccess = new ArrayBuffer[(String, ArrayBuffer[String])] // var name, index name


  var res1:Int = 0
  var res2:Int = 0
  var res3:Int = 0
  var res4:Int = 0
  var res5:Int = 0
  var res6:Int = 0
  var res7:Int = 0
  var res8:Int = 0
  var res9:Int = 0
  var res10:Int = 0
  var res11:Int = 0
  var res12:Int = 0
  var res13:Int = 0
  var res14:Int = 0
  var res15:Int = 0
  var res16:Int = 0

  def printActions(actions: List[Action], cu: CompilationUnit): Unit ={
    for(ac<-actions) {
      val jdtNode = JdtHandling.getJDTNodeOfITree(cu,ac.getNode)
      println("***JDT node "+ jdtNode)
      if(jdtNode!=null) {
        jdtNode.accept(new SimpleNameVisitor(cu))
        if(jdtNode.isInstanceOf[MethodInvocation]){
          println("Method invoc: "+jdtNode.asInstanceOf[MethodInvocation].getName)
          println("Method invoc var: "+jdtNode.asInstanceOf[MethodInvocation])
          println("Method invoc params: "+jdtNode.asInstanceOf[MethodInvocation].arguments())
        }
      }
      println(ac.toString)
      //println(ac.getNode.toTreeString)
      println("Current: " +nodeClassName(ac.getNode))
      if(nodeClassName(ac.getNode) == "SimpleName"){
        val realName = extractRealSimpleName(ac.getNode.getShortLabel)
        println("RealName "+realName)
      }
      //println(ac.getNode.areDescendantsMatched())
      //println(ac.getNode.toTreeString)
      //println(ASTNode.nodeClassForType(42))
      println("Parent: "+ nodeClassName(ac.getNode.getParent))
      println("Parent Parent: "+ nodeClassName(ac.getNode.getParent.getParent))
      for(child <- ac.getNode.getParent.getChildren) {
        println("Child of Parent: " + nodeClassName(child)+" "+child.getShortLabel)
      }
      //println(nodeClassName(ac.getNode.getParent.getParent))
    }
    //cu.accept(new SimpleNameVisitor(cu))
  }

  def printMappings(mappings: MappingStore) ={
    for(m <- mappings){
      println("*********")
      println(m.getFirst)
      println(m.getSecond)
      println("*********")
    }
  }

  def getDiffMappings(file1: String, file2: String): MappingStore ={
    Run.initGenerators()
    val srcTC = new JdtTreeGenerator().generateFromFile(file1)
    val dstTC = new JdtTreeGenerator().generateFromFile(file2)
    val src = srcTC.getRoot
    val dst = dstTC.getRoot
    val m = Matchers.getInstance.getMatcher(src, dst)
    m.`match`()
    return m.getMappings()
  }

  var srcCp:ITree =_ 
  var dstCp:ITree =_

  def getDiffActions(file1: String, file2: String): (List[Action], MyJdtTreeGenerator, MyJdtTreeGenerator) = {
    val srcTG = new MyJdtTreeGenerator()
    val srcTC = srcTG.generateFromFile(file1)
    val dstTG = new MyJdtTreeGenerator()
    val dstTC = dstTG.generateFromFile(file2)
    val src = srcTC.getRoot
    val dst = dstTC.getRoot
    srcCp = srcTC.getRoot
    dstCp = dstTC.getRoot
    val m = Matchers.getInstance.getMatcher(src, dst)
    m.`match`()
    val g = new ActionGenerator(src, dst, m.getMappings)
    g.generate()
    val actions = g.getActions
    (actions,srcTG,dstTG)
  }

  def getDiffActionsOnly(file1: String, file2: String): List[Action] = {
    val srcTG = new JdtTreeGenerator()
    val srcTC = srcTG.generateFromFile(file1)
    val dstTG = new JdtTreeGenerator()
    val dstTC = dstTG.generateFromFile(file2)
    val src = srcTC.getRoot
    val dst = dstTC.getRoot
    val m = Matchers.getInstance.getMatcher(src, dst)
    m.`match`()
    val g = new ActionGenerator(src, dst, m.getMappings)
    g.generate()
    val actions = g.getActions
    actions
  }

  def nodeClassName(node: ITree): String = {
    ASTNode.nodeClassForType(node.getType).getSimpleName
  }

  def actionName(ac: Action): String = ac.getClass.getSimpleName

  def isExpressionReplacer(actions: List[Action]): Unit ={

  }

  private def gatherMethodIvocationInfor(jdtNode: ASTNode, compilationUnit: CompilationUnit) ={
    //import scala.util.control.Breaks._
    val methodIvocNode = jdtNode.asInstanceOf[MethodInvocation]
    val visitSimpleNames = new SimpleNameVisitor(compilationUnit)
    methodIvocNode.accept(visitSimpleNames)
    val methodName=methodIvocNode.getName.getFullyQualifiedName
    val arguments=methodIvocNode.arguments().foldLeft(new ArrayBuffer[String]())( (buf,arg) => {
      val expVisitor = new SimpleNameVisitor(compilationUnit)
      arg.asInstanceOf[Expression].accept(expVisitor)
      buf.appendAll(expVisitor.allSimpleName); buf
    })
    //for(arg <- arguments){
      //println("Arg: "+arg)
    //}
    var varName = ""
    for(simpleName <- visitSimpleNames.allSimpleName){
      if(!simpleName.equals(methodName) && !arguments.exists(arg => arg.contains(simpleName))){ // may be change to equals, for now just use contains...
        varName = simpleName
        //println("Found: "+simpleName)
        //break
      }
    }
    //println(" Exited!")
    //println(varName + methodName + arguments)
    val res = new  ArrayBuffer[(String, String, ArrayBuffer[String])]()
    res.append((varName, methodName, arguments))
    res
  }

  private def gatherInstanceofExpInfor(jdtNode: ASTNode, compilationUnit: CompilationUnit) ={
    val methodInvocNode = jdtNode.asInstanceOf[InstanceofExpression]
    val visitSimpleNames = new SimpleNameVisitor(compilationUnit)
    val left = methodInvocNode.getLeftOperand
    if(left.isInstanceOf[MethodInvocation]){
      val infor=gatherMethodIvocationInfor(left, compilationUnit)
      infor
    }else{
      left.accept(visitSimpleNames)
      val args = new ArrayBuffer[String]()
      args.append(methodInvocNode.getRightOperand.toString)
      val res = new  ArrayBuffer[(String, String, ArrayBuffer[String])]()
      if(visitSimpleNames.allSimpleName.size > 0) {
        res.append((visitSimpleNames.allSimpleName(0), "JDTInstanceofExp", args))
        res
      }else
        null
    }

  }

  private def gatherAssignExpInfor(jdtNode: ASTNode, compilationUnit: CompilationUnit) ={
    /*val methodInvocNode = jdtNode.asInstanceOf[Assignment]
    val expLeft = methodInvocNode.getLeftHandSide
    val expRight = methodInvocNode.getRightHandSide
    def helper(exp: Expression) = {
      if (exp.isInstanceOf[MethodInvocation]) {
        val infor = gatherMethodIvocationInfor(exp, compilationUnit)
        infor
      } else {
        val visitSimpleNames = new SimpleNameVisitor(compilationUnit)
        exp.accept(visitSimpleNames)
        val args = new ArrayBuffer[String]()
        val res = new  ArrayBuffer[(String, String, ArrayBuffer[String])]()
        if(!visitSimpleNames.allSimpleName.isEmpty)
          res.append((visitSimpleNames.allSimpleName(0), "JDTAssignExp", args))
        res
      }
    }
    val res = new ArrayBuffer[(String, String, ArrayBuffer[String])]()
    res.appendAll(helper(expLeft))
    res.appendAll(helper(expRight))
    res*/
    gatherExpInfor(jdtNode, compilationUnit)
  }

  private def gatherExpInfor(jdtNode: ASTNode, compilationUnit: CompilationUnit) ={
    val methodInvocNode = jdtNode.asInstanceOf[Expression]

    def helper(exp: Expression): ArrayBuffer[(String, String, ArrayBuffer[String])] = {
      if (exp.isInstanceOf[MethodInvocation]) {
        val infor = gatherMethodIvocationInfor(exp, compilationUnit)
        infor
      } else if(exp.isInstanceOf[SimpleName]){
        val visitSimpleNames = new SimpleNameVisitor(compilationUnit)
        exp.accept(visitSimpleNames)
        val args = new ArrayBuffer[String]()
        val res = new  ArrayBuffer[(String, String, ArrayBuffer[String])]()
        if(!visitSimpleNames.allSimpleName.isEmpty)
         res.append((visitSimpleNames.allSimpleName(0), "JDTInfixExp", args))
        //println("simple "+res)
        res
      } else if(exp.isInstanceOf[InfixExpression]){
        val res =  new  ArrayBuffer[(String, String, ArrayBuffer[String])]()
        res.appendAll(helper(exp.asInstanceOf[InfixExpression].getLeftOperand))
        res.appendAll(helper(exp.asInstanceOf[InfixExpression].getRightOperand))
        //println("infix")
        res
      } else if(exp.isInstanceOf[ParenthesizedExpression]){
        helper(exp.asInstanceOf[ParenthesizedExpression].getExpression)
      } else if(exp.isInstanceOf[PostfixExpression]){
        helper(exp.asInstanceOf[PostfixExpression].getOperand)
      } else if(exp.isInstanceOf[PrefixExpression]){
        helper(exp.asInstanceOf[PrefixExpression].getOperand)
      } else if(exp.isInstanceOf[ConditionalExpression]){
        helper(exp.asInstanceOf[ConditionalExpression].getExpression)
      } else if(exp.isInstanceOf[CastExpression]){
        val res =  new  ArrayBuffer[(String, String, ArrayBuffer[String])]()
        res.append((exp.asInstanceOf[CastExpression].getType.toString,"CAST_EXP", new ArrayBuffer[String]()))
        res.appendAll(helper(exp.asInstanceOf[CastExpression].getExpression))
        res
      } else if(exp.isInstanceOf[Assignment]){
        val res =  new  ArrayBuffer[(String, String, ArrayBuffer[String])]()
        res.appendAll(helper(exp.asInstanceOf[Assignment].getLeftHandSide))
        res.appendAll(helper(exp.asInstanceOf[Assignment].getRightHandSide))
        res
      }
      else {
        val res =  new  ArrayBuffer[(String, String, ArrayBuffer[String])]()
        res
      }
    }
    //println("Exited EXP")
    helper(methodInvocNode)
  }

  def extractRealSimpleName(simpleName: String): String ={
    for(methodInfor <- allInfor){
      methodInfor match {
        case (varName, methodName, arguments) =>{
          //println(varName +" "+ methodName+" "+arguments)
          if(simpleName.equals(varName)) {
            if(methodName.equals("FIELD_ACCESS"))
              return "VarName"
            if(methodName.equals("CAST_EXP"))
              return "ClassName"
            if (!methodName.equals("JDT_CLASS_INST_CREATE"))
              return "VarName"
            else
              return "ClassName"
          }
          else if(simpleName.equals(methodName)) {
            if(!arguments.isEmpty)
              if(arguments.get(0).equals("VARDEC"))
                return "ClassName"

            return "MethodName"
          }
          else if(arguments.exists(sp => sp.equals(simpleName)))
            return "ArgumentName"
        }
        case null =>{}
      }
    }

    return null
  }

  def typeOfRealSimpleName(simpleName: String): Int ={
    if(simpleName == null)
      return 103
    else if(simpleName.equals("VarName"))
      return 100
    else if(simpleName.equals("MethodName"))
      return 101
    else if(simpleName.equals("ArgumentName"))
      return 102
    else if(simpleName.equals("ClassName"))
      return 105
    else
      return 104
  }

  def extractTypeOfNode(node: ITree): Int ={
    if(nodeClassName(node) == "SimpleName")
      typeOfRealSimpleName(extractRealSimpleName(node.getShortLabel))
    else
      node.getType
  }

  def gatherAllInfor(actions: List[Action],stg: MyJdtTreeGenerator, dtg: MyJdtTreeGenerator ): Unit = {
    this.allInfor.appendAll(gatherAllInforX(actions,stg,dtg))
    //println("All infor: "+allInfor)
  }

  private def gatherClassInstance(jdtNode: ASTNode, compilationUnit: CompilationUnit): ArrayBuffer[(String,String,ArrayBuffer[String])] = {
    val classInstNode = jdtNode.asInstanceOf[ClassInstanceCreation]
    val className = classInstNode.getType.toString
    val visitSimpleNames = new SimpleNameVisitor(compilationUnit)
    val arguments=classInstNode.arguments().foldLeft(new ArrayBuffer[String]())( (buf,arg) => {
      val expVisitor = new SimpleNameVisitor(compilationUnit)
      arg.asInstanceOf[Expression].accept(expVisitor)
      buf.appendAll(expVisitor.allSimpleName); buf
    })
    val res = new ArrayBuffer[(String, String ,ArrayBuffer[String])]()
    res.append((className,"JDT_CLASS_INST_CREATE",arguments))
    res
  }

  private def gatherVarDec(jdtNode: ASTNode, compilationUnit: CompilationUnit): ArrayBuffer[(String,String,ArrayBuffer[String])] = {
    val varDecNode = jdtNode.asInstanceOf[SingleVariableDeclaration]
    val res = new ArrayBuffer[(String, String ,ArrayBuffer[String])]()
    val args = new ArrayBuffer[String]()
    args.append("VARDEC")
    res.append((varDecNode.getName.getFullyQualifiedName,varDecNode.getType.toString,args))
    res
  }

  private def gatherSuperConstructor(jdtNode: ASTNode, compilationUnit: CompilationUnit): ArrayBuffer[(String,String,ArrayBuffer[String])] = {
    val varDecNode = jdtNode.asInstanceOf[SuperConstructorInvocation]
    val res = new ArrayBuffer[(String, String ,ArrayBuffer[String])]()
    val args = new ArrayBuffer[String]()
    val expInfor=gatherExpInfor(varDecNode.getExpression, compilationUnit)
    val arguments=varDecNode.arguments().foldLeft(new ArrayBuffer[String]())( (buf,arg) => {
      val expVisitor = new SimpleNameVisitor(compilationUnit)
      arg.asInstanceOf[Expression].accept(expVisitor)
      buf.appendAll(expVisitor.allSimpleName); buf
    })
    res.appendAll(expInfor)
    res.append(("","",arguments))
    res
  }

  private def gatherSuperMethod(jdtNode: ASTNode, compilationUnit: CompilationUnit): ArrayBuffer[(String,String,ArrayBuffer[String])] = {
    val varDecNode = jdtNode.asInstanceOf[SuperMethodInvocation]
    val res = new ArrayBuffer[(String, String ,ArrayBuffer[String])]()
    val args = new ArrayBuffer[String]()
    val arguments=varDecNode.arguments().foldLeft(new ArrayBuffer[String]())( (buf,arg) => {
      val expVisitor = new SimpleNameVisitor(compilationUnit)
      arg.asInstanceOf[Expression].accept(expVisitor)
      buf.appendAll(expVisitor.allSimpleName); buf
    })
    res.append(("",varDecNode.getName.getFullyQualifiedName,arguments))
    res
  }

  private def gatherArrayAccess(jdtNode: ASTNode, compilationUnit: CompilationUnit): ArrayBuffer[(String,String,ArrayBuffer[String])] = {
    val arrayNode = jdtNode.asInstanceOf[ArrayAccess]
    val arrName = gatherExpInfor(arrayNode.getArray,compilationUnit)
    val indexName = gatherExpInfor(arrayNode.getIndex,compilationUnit)
    val res = new ArrayBuffer[(String, String ,ArrayBuffer[String])]()
    res.appendAll(arrName)
    res.appendAll(indexName)
    //println("Res: "+ res)
    res
  }

  private def gatherFieldAccess(jdtNode: ASTNode, compilationUnit: CompilationUnit): ArrayBuffer[(String,String,ArrayBuffer[String])] = {
    val arrayNode = jdtNode.asInstanceOf[FieldAccess]
    val arrName = gatherExpInfor(arrayNode.getExpression,compilationUnit)
    val fieldName = arrayNode.getName.getFullyQualifiedName
    val res = new ArrayBuffer[(String, String ,ArrayBuffer[String])]()
    res.appendAll(arrName)
    res.append((fieldName,"FIELD_ACCESS", new ArrayBuffer[String]()))
    //println("Res: "+ res)
    res
  }

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
    currentInfor
  }

  def match1AddNullChecker(actions: List[Action]): Int = {
    var res = 0
    for (ac <- actions) {
          if(nodeClassName(ac.getNode) == "IfStatement"){
	    if (actionName(ac) == "Insert"){
	      if((nodeClassName(ac.getNode.getChildren.get(0)) == "InfixExpression") && (nodeClassName(ac.getNode.getChildren.get(0).getChildren.get(1)) == "NullLiteral")){
		res += 1
	      }
            }
	  }
    }
    res1=res
    res
  }

def closestMethodInvocation(acParam: ITree): ITree = {
  var ac = acParam.getParent
  while(!ac.isRoot){
    if(nodeClassName(ac) == "MethodInvocation"){
      return ac
    }else{
      ac=ac.getParent
    }
  }
ac
}

var pos2, pos3, pos4, pos5, pos6, pos7, pos8, pos9 = false

def cleanPosVars(): Boolean = {
pos2 = false
pos3 = false
pos4 = false
pos5 = false
pos6 = false
pos7 = false
pos8 = false
pos9 = false
true
}

def alreadyCounted(pos: Int): Boolean = {
  var ac = false
  pos match {
  case 2  => return pos2
  case 3  => return pos3
  case 4  => return pos4
  case 5  => return pos5
  case 6  => return pos6
  case 7  => return pos7
  case 8  => return pos8
  case 9  => return pos9
  case _  => 
  }
ac
}


def isVerySimilar(node: ITree, node2: ITree): Boolean = {
  var ret = true
  if(!node.isSimilar(node2))ret=false
//  if(!node.isRoot && !node2.isRoot){
//println(nodeClassName(node.getParent) + " " + nodeClassName(node2.getParent))
//    if(node.getParent.toShortString!=node2.getParent.toShortString)ret=false
//  }  
//if(node.hasLabel!=node2.hasLabel)ret=false
//  if(node.getChildren.size>0 && node2.getChildren.size>0){
//println("Comparing " + node.getChildren.get(0).toShortString + " with " + node2.getChildren.get(0).toShortString)
  if(node.getChildren.size>0 && node2.getChildren.size>0 && node.getChildren.get(0).toShortString!=node2.getChildren.get(0).toShortString){
    ret=false
  }
  if(node.getChildren.size>1 && node2.getChildren.size>1 && node.getChildren.get(1).toShortString!=node2.getChildren.get(1).toShortString){
    ret=false
  }
  if(node.getShortLabel!=node2.getShortLabel)ret=false
  //if(node.getSize!=node2.getSize)ret=false
  if(node.isLeaf!=node2.isLeaf)ret=false
//  if(node.isMatched!=node2.isMatched)ret=false
  if(node.isRoot!=node2.isRoot)ret=false
//  if(node.positionInParent!=node2.positionInParent)ret=false
//  if(node.getPos>node2.getPos+100 || node.getPos<node2.getPos-100)ret=false

ret
}



  def match2ParameterReplacer(actions: List[Action]): Int = {
    var res = 0
    var childrenNode:Int=0
    var childrenNode2:Int=0
    
    if(actions.size<=3 && actions.size>0){
//println("actions.size" + actions.size)
    var target = closestMethodInvocation(actions.get(0).getNode)


      for(node<-srcCp.breadthFirst){
        if(isVerySimilar(node,target) ){
	  childrenNode=node.getChildren.length
        }
      }
      for(node<-dstCp.breadthFirst){
        if(isVerySimilar(node,target) ){
	  childrenNode2=node.getChildren.length
        }
      }
      if(childrenNode==childrenNode2){
	if (actionName(actions.get(0)) == "Insert") {
	  res+=1
	}
      }

    }else{

    cleanPosVars()
    for (ac <- actions) {

      var target = closestMethodInvocation(ac.getNode)
      if(!target.isRoot){
        if(statementAlreadyExisted(target, actions)){ //parent already existed

	  for (ac2 <- actions.reverse) {
	    var target2 = closestMethodInvocation(ac2.getNode)
	    if(target.isSimilar(target2)){

	      var childAC = target.getChildren.size
	      var childAC2 = target2.getChildren.size
	      if(childAC==childAC2 && statementWasNotRemoved(target2,actions)){

		      var position = target2.getChildPosition(ac2.getNode)
		      if(position > 1 && !alreadyCounted(position)){
		        position match {
	  	          case 2  => pos2=true
			  case 3  => pos3=true
			  case 4  => pos4=true
			  case 5  => pos5=true
			  case 6  => pos6=true
			  case 7  => pos7=true
			  case 8  => pos8=true
			  case 9  => pos9=true
			}
		        res+=1

		  
		       }
	      }
	    
	  }
	  }
	  
        }
      }
    }
    }
    cleanPosVars()
    res2=res
    res

/*

        if (nodeClassName(ac.getNode.getParent) == "MethodInvocation") {
          if(statementAlreadyExisted(ac.getNode.getParent, actions)){ //parent already existed
	  var exp = nodeClassName(ac.getNode)
          if (isExpression(exp)) {
            if (actionName(ac) == "Update"){
	       if(ac.getNode.getParent.getChildren.size >1 && ac.getNode.getParent.getChildren.get(1)!=ac.getNode && ac.getNode.getParent.getChildren.get(0)!=ac.getNode){
		res += 1
	       }
	     }else if( actionName(ac) == "Insert" ) {
	      if(ac.getNode.getParent.getChildren.get(1).toShortString != ac.getNode.toShortString){
		for (ac2 <- actions) {
                if(actionName(ac2) == "Delete" && ac.getNode.getParent.getChildren.size > 1 && ac2.getNode.getParent.getChildren.size > 1 && ac.getNode.getParent.getChildren.get(1).toShortString == ac2.getNode.getParent.getChildren.get(1).toShortString){
		   res += 1
	        }
	      }
	       
	      }
	    }else if(actionName(ac) == "Delete"){
	      if(nodeClassName(ac.getNode) == "MethodInvocation"){
		res += 1
	      }
  	    }
          } 
        }
      }
*/



  }

  def match3MethodReplacer(actions: List[Action]): Int = {
    var res = 0
    for (ac <- actions){
      if (nodeClassName(ac.getNode) == "SimpleName"){
	if (nodeClassName(ac.getNode.getParent) == "MethodInvocation"){
          if (actionName(ac) == "Update") {
	    if(ac.getNode.getParent.getChildren.size >1 && ac.getNode.getParent.getChildren.get(1)==ac.getNode){
		var itChangedTheCallerObject = false		
		for (ac2 <- actions) {
                  if(actionName(ac2) == "Update" && ac2.getNode == ac.getNode.getParent.getChildren.get(0) ){
		    itChangedTheCallerObject = true	
		  }
		}
		if(!itChangedTheCallerObject){
        	  res += 1
		}
	    }
	  }
	}
      }
    }
    res3=res
    res
  }




  def match4ParameterAdderRemover(actions: List[Action]): Int = {
    var res = 0
    var childrenNode:Int=0
    var childrenNode2:Int=0
    
    if(actions.size<=3 && actions.size>0){
    var target = closestMethodInvocation(actions.get(0).getNode)
      for(node<-srcCp.preOrder){
        if(isVerySimilar(node,target) ){
	  childrenNode=node.getChildren.size
        }
      }

      for(node<-dstCp.preOrder){
        if(isVerySimilar(node,target) ){
	  childrenNode2=node.getChildren.size
        }
      }

      if(childrenNode!=childrenNode2){
	if (actionName(actions.get(0)) == "Insert" || actionName(actions.get(0)) == "Delete") {
	  res+=1
	}
      }

    }else{

    for (ac <- actions) {

      var target = closestMethodInvocation(ac.getNode)
      if (!target.isRoot) {
        if(statementAlreadyExisted(target, actions)){ 
	  breakable{
	  for (ac2 <- actions.reverse) {
	    var target2 = closestMethodInvocation(ac2.getNode)
	    if(isVerySimilar(target,target2)){
	      var childAC = target.getChildren.size
	      var childAC2 = target2.getChildren.size
	      if(childAC!=childAC2){
		res+=1
		break
	      }
	    }
	  }
	  }
        }
      }
    }
    }
    res4=res
    res
/*
      if (nodeClassName(ac.getNode.getParent) == "MethodInvocation") {
        if(statementAlreadyExisted(ac.getNode.getParent, actions)){ //parent already existed
 	  if (isExpression(nodeClassName(ac.getNode))) {          
	    if (actionName(ac) == "Insert" || actionName(ac) == "Move"){
	      var foundMatchingDelete = false
	      var itIsAParameterReplacer=false
  	      for (ac2 <- actions) {
                if(ac.getNode.getParent.getChildren.size > 1 && ac2.getNode.getParent.getChildren.size > 1 && ac.getNode.getParent.getChildren.get(1).toShortString == ac2.getNode.getParent.getChildren.get(1).toShortString && (actionName(ac2) == "Delete" || actionName(ac2) == "Update")){
		  foundMatchingDelete = true
	        }
	      }
	      for (ac2 <- actions) {
                if(ac.getNode.getParent.getChildren.size > 1 && ac.getNode.toShortString == ac2.getNode.getParent.toShortString && actionName(ac2) == "Move"){
		  itIsAParameterReplacer = true
	        }
	      }
	      if(!foundMatchingDelete && !itIsAParameterReplacer){
                res += 1
	      }
            }else if (actionName(ac) == "Delete") {            
	      var foundMatchingInsert = false
  	      for (ac2 <- actions) {

                if(ac.getNode.getParent.getChildren.size > 1 && ac2.getNode.getParent.getChildren.size > 1 && ac.getNode.getParent.getChildren.get(1).toShortString == ac2.getNode.getParent.getChildren.get(1).toShortString && (actionName(ac2) == "Insert" || actionName(ac2) == "Move") ){
		  foundMatchingInsert = true
	        }
	      }
	      if(!foundMatchingInsert){
		  res += 1
	      }
            }
          }
        }
      }
*/





  }

  def match5ObjectInitializer(actions: List[Action]): Int = {
    var res = 0
    for (ac <- actions) {
        if (nodeClassName(ac.getNode.getParent) == "VariableDeclarationFragment") {
          if(nodeClassName(ac.getNode) == "ClassInstanceCreation" || nodeClassName(ac.getNode) == "ArrayCreation"){
	    if (actionName(ac) == "Insert"){ 
              for (ac1 <- actions) {
		if(actionName(ac1) == "Delete"){
                  if (nodeClassName(ac1.getNode.getParent) == "VariableDeclarationFragment") {
                    if(nodeClassName(ac1.getNode) == "NullLiteral"){
                      res += 1
		    }
		  }
  	        }
	      }
            }
	  }
	}
    }
    res5=res
    res
  }


  def noOtherTemplateWasCounted(): Boolean = {
    return (res1==0 && res2==0 && res3==0 && res4==0 && res5==0 && res6==0 && res7==0 && res8==0 && res9==0 && res10==0 && res11==0 && res12==0 && res13==0 && res14==0 && res15==0 && res16==0)
  }

  def match6SequenceExchanger(actions: List[Action]): Int = {
    var res = 0
    if(noOtherTemplateWasCounted()){
    for (ac <- actions) {
      var target = ac.getNode
      breakable {
      for (ac1 <- actions) {
	var target1 = ac1.getNode	
	//look up a statement of the same kind as target
	while(!target1.isRoot && nodeClassName(target) != nodeClassName(target1)){
	  target1 = target1.getParent
	}

 	if(!target1.isRoot && !target1.isLeaf && target1.getHeight>1){ //the ones of height 1 are usually considered in the other templates
	  if(actionName(ac) == "Insert"){
	  //if(nodeClassName(ac.getNode.getParent) == nodeClassName(ac1.getNode.getParent)){	

	  if(target.getSize==target1.getSize && target.hasLabel==target1.hasLabel && target.getLabel==target1.getLabel){
	      if(numberOfChildren(ac.getNode.getChildren)==numberOfChildren(ac1.getNode.getChildren)){
	         res += 1 
	         break
	      }
          }
	  //}
	  }
	}
      }
      }
    }
    }
    res6=res
    res
  }

  def match7AddRangeChecker(actions: List[Action]): Int = {

    var res = 0
    for (ac <- actions) {

          if(nodeClassName(ac.getNode) == "IfStatement"){
	    if (actionName(ac) == "Insert"){
	      val condition = ac.getNode.getChildren.get(0)
	      if((nodeClassName(condition) == "InfixExpression")){ 
		//only checks one bound
		if(nodeClassName(condition.getChildren.get(0)) != "InfixExpression" ){
		  /*if(nodeClassName(condition.getChildren.get(0)) == "SimpleName" ){
		    if(condition.getShortLabel == "<" || condition.getShortLabel == "<=" || condition.getShortLabel == ">" || condition.getShortLabel == ">="){
		      res += 1
		    }
		  }*/
		//checks both bounds
		}else{
		  if(nodeClassName(condition.getChildren.get(0).getChildren.get(0)) == "SimpleName" 
			|| nodeClassName(condition.getChildren.get(0).getChildren.get(0)) =="ArrayAccess" 
			|| nodeClassName(condition.getChildren.get(0).getChildren.get(0)) == "MethodInvocation" 
			|| nodeClassName(condition.getChildren.get(0).getChildren.get(0)) == "QualifiedName"){
		    if(condition.getChildren.get(0).getShortLabel == "<" 
			|| condition.getChildren.get(0).getShortLabel == "<=" 
			|| condition.getChildren.get(0).getShortLabel == ">" 
			|| condition.getChildren.get(0).getShortLabel == ">="){
		      if(nodeClassName(condition.getChildren.get(1).getChildren.get(0)) == "SimpleName" 
			|| nodeClassName(condition.getChildren.get(1).getChildren.get(0)) =="ArrayAccess" 
			|| nodeClassName(condition.getChildren.get(1).getChildren.get(0)) == "MethodInvocation"
			|| nodeClassName(condition.getChildren.get(1).getChildren.get(0)) == "QualifiedName"){
		        if(condition.getChildren.get(0).getShortLabel == "<" 
			  || condition.getChildren.get(0).getShortLabel == "<=" 
			  || condition.getChildren.get(0).getShortLabel == ">" 
			  || condition.getChildren.get(0).getShortLabel == ">="){
		          res += 1
		        }
		      }
		    }
		  }
		}
	      }
            }
	  }
    }
    res7=res
    res
  }

  def match8AddColectionSizeChecker(actions: List[Action]): Int = {

    var res = 0
    for (ac <- actions) {
          if(nodeClassName(ac.getNode) == "IfStatement"){
	    if (actionName(ac) == "Insert"){
	      val condition = ac.getNode.getChildren.get(0)
	      if((nodeClassName(condition) == "InfixExpression")){ 

		  if(nodeClassName(condition.getChildren.get(0)) == "SimpleName" || 
		    nodeClassName(condition.getChildren.get(0)) == "ArrayAccess" || 
		    nodeClassName(condition.getChildren.get(0)) == "MethodInvocation" ||
		    nodeClassName(condition.getChildren.get(0)) == "QualifiedName"){
		    if(nodeClassName(condition.getChildren.get(1)) == "MethodInvocation" || nodeClassName(condition.getChildren.get(1)) == "QualifiedName"){
		      if(condition.getShortLabel == "<" || condition.getShortLabel == "<=" || condition.getShortLabel == ">" || condition.getShortLabel == ">="){
		        res += 1
		      }
		    }
		  }
		
	      }
            }
	  }
    }
    res8=res
    res
  }


  def match9LowerBoundSetter(actions: List[Action]): Int = {
    var res = 0
    for (ac <- actions) {
	if(nodeClassName(ac.getNode) == "IfStatement"){
	    if (actionName(ac) == "Insert"){
	      val condition = ac.getNode.getChildren.get(0)
	      if((nodeClassName(condition) == "InfixExpression")){ 
		  if(nodeClassName(condition.getChildren.get(0)) == "SimpleName" ||
		  nodeClassName(condition.getChildren.get(0)) == "ArrayAccess" ||
		  nodeClassName(condition.getChildren.get(0)) == "MethodInvocation" ||
		  nodeClassName(condition.getChildren.get(0)) == "QualifiedName"){
		        if(condition.getShortLabel == "<" || condition.getShortLabel == "<="){
		            val ifBody = ac.getNode.getChildren.get(1)
			     if((nodeClassName(ifBody.getChildren.get(0).getChildren.get(0)) == "Assignment")){ 
			       val assig = ifBody.getChildren.get(0).getChildren.get(0)
			       if(nodeClassName(assig.getChildren.get(0)) == "SimpleName" ||
				nodeClassName(assig.getChildren.get(0)) == "ArrayAccess"){
			       if(nodeClassName(condition.getChildren.get(1)) == nodeClassName(assig.getChildren.get(1))){
				  res += 1
		       	        }
		 	      }
	     		    }
		        }		      
		  }
	      }
            }
	  }
    }
    res9=res
    res
  }

  def match10UpperBoundSetter(actions: List[Action]): Int = {
    var res = 0
    for (ac <- actions) {
	if(nodeClassName(ac.getNode) == "IfStatement"){
	    if (actionName(ac) == "Insert"){
	      val condition = ac.getNode.getChildren.get(0)
	      if((nodeClassName(condition) == "InfixExpression")){ 
		  if(nodeClassName(condition.getChildren.get(0)) == "SimpleName"||
		  nodeClassName(condition.getChildren.get(0)) == "ArrayAccess" ||
		  nodeClassName(condition.getChildren.get(0)) == "MethodInvocation" ||
		  nodeClassName(condition.getChildren.get(0)) == "QualifiedName"){
		        if(condition.getShortLabel == ">" || condition.getShortLabel == ">="){
		            val ifBody = ac.getNode.getChildren.get(1)
			    if((nodeClassName(ifBody.getChildren.get(0).getChildren.get(0)) == "Assignment")){ 
			       val assig = ifBody.getChildren.get(0).getChildren.get(0)
			       if(nodeClassName(assig.getChildren.get(0)) == "SimpleName"||
				nodeClassName(assig.getChildren.get(0)) == "ArrayAccess"){
			       if(nodeClassName(condition.getChildren.get(1)) == nodeClassName(assig.getChildren.get(1))){
				  res += 1
		       	        }
		 	      }
	     		    }
		        }		      
		  }
	      }
            }
	  }
    }
    res10=res
    res
  }

  def match11OffByOneMutator(actions: List[Action]): Int = {
    var res = 0
    for (ac <- actions) {
      if (nodeClassName(ac.getNode.getParent) == "ArrayAccess") {
        if(statementAlreadyExisted(ac.getNode.getParent, actions)){ //parent already existed
          if (actionName(ac) == "Insert"){
	    if(nodeClassName(ac.getNode) == "PostfixExpression"){ //array[i++]
	      if((nodeClassName(ac.getNode.getChildren.get(0)) == "SimpleName")){ 
		 res += 1 
	      }
	    }else if(nodeClassName(ac.getNode) == "InfixExpression"){ //array[i+1]
	      if(ac.getNode.getShortLabel == "+" || ac.getNode.getShortLabel == "-"){
		if(nodeClassName(ac.getNode.getChildren.get(1))=="NumberLiteral"){
		  if(ac.getNode.getChildren.get(1).hasLabel && ac.getNode.getChildren.get(1).getLabel == "1"){
	            for (ac2 <- actions) {
                      if(actionName(ac2) == "Delete" 
			&& ac.getNode.getParent.getChildren.size > 1 
			&& ac2.getNode.getParent.getChildren.size > 1 
			&& ac.getNode.getParent.getChildren.get(0).toShortString == ac2.getNode.getParent.getChildren.get(0).toShortString){
		        res += 1
	              }
	            }
		  }
		}
	       }
	     }
 	   }

	}


	}
    }
    res11=res
    res
  }

  def match12AddCastChecker(actions: List[Action]): Int = {
    var res = 0
    for (ac <- actions) {

          if(nodeClassName(ac.getNode) == "IfStatement"){
	    if (actionName(ac) == "Insert"){
	      val condition = ac.getNode.getChildren.get(0)
	      if((nodeClassName(condition) == "InstanceofExpression")){ 
		  if(isExpression(nodeClassName(condition.getChildren.get(0)))){
		      if(nodeClassName(condition.getChildren.get(1)) == "SimpleType" ){
		        res += 1
		      }
		  }
	      }
            }
	  }
    }
    res12=res
    res
  }

  def match13CasterMutator(actions: List[Action]): Int = {
    var res = 0
    for (ac <- actions) {
      if(nodeClassName(ac.getNode) == "SimpleType"){
	if(nodeClassName(ac.getNode.getParent) == "CastExpression"){	
	    if (actionName(ac) == "Update"){
	      if((nodeClassName(ac.getNode.getChildren.get(0)) == "SimpleName")){ 
		 res += 1 
	      }
            }
	  }
	}else if(nodeClassName(ac.getNode) == "PrimitiveType"){
	  if(nodeClassName(ac.getNode.getParent) == "CastExpression"){	
	    if (actionName(ac) == "Update"){ 
		 res += 1 
            }
	  }
	}
    }
    res13=res
    res
  }


  def match14CasteeMutator(actions: List[Action]): Int = {
    var res = 0
    for (ac <- actions) {
      if (actionName(ac) == "Insert"){
      if(isExpression(nodeClassName(ac.getNode)) 
	&& !(nodeClassName(ac.getNode) == "SimpleType") 
	&& !(nodeClassName(ac.getNode) == "PrimitiveType") 
	&& !(nodeClassName(ac.getNode.getParent) == "SimpleType") 
	&& !(nodeClassName(ac.getNode.getParent.getParent) == "PrimitiveType")){

	  if((nodeClassName(ac.getNode) == "SimpleName" && nodeClassName(ac.getNode.getParent) == "CastExpression") 
		|| (nodeClassName(ac.getNode) == "ArrayAccess" && nodeClassName(ac.getNode.getParent) == "CastExpression") 
		|| (nodeClassName(ac.getNode) == "MethodInvocation" && nodeClassName(ac.getNode.getParent) == "CastExpression")){	
	    res += 1 
	  }
	}
      }
    }
    res14=res
    res
  }

def numberOfChildren(ac: List[ITree]): Int = {
  var ret = 0
  for(des<-ac){
    if(nodeClassName(des) == "InfixExpression" && des.getHeight >= 2){
      ret += numberOfChildren(des.getChildren)
    }else if(nodeClassName(des) == "Block"){

    }else{
      //println(nodeClassName(des)+" "+des.toShortString)  
      ret += 1
    }
  }
return ret
}

def childrenPos(ac: List[ITree],lookForThis: ITree): Int = {
  var ret = 0
  breakable{
  for(des<-ac){
    if(nodeClassName(des) == "InfixExpression" && des.getHeight >= 2){
      ret += numberOfChildren(des.getChildren)
    }else if(nodeClassName(des) == "Block"){

    }else{
      //println(nodeClassName(des)+" "+des.toShortString)  
      ret += 1
      if(des==lookForThis){
	break
      }
    }
  }
  }
return ret
}


def childrenInCondition(ac: List[ITree]): List[ITree] = {
  val ret = new ListBuffer[ITree]()
  for(des<-ac){
    if(nodeClassName(des) == "InfixExpression" && des.getHeight >= 2){
      ret ++= childrenInCondition(des.getChildren)
    }else if(nodeClassName(des) == "Block"){

    }else{
      //println(nodeClassName(des)+" "+des.toShortString)  
      ret += des
    }
  }
return ret.toList
}

def howManyChildrenAreDifferent(l1: List[ITree], l2: List[ITree]): Int = {
  var ret = 0
  for(i <- 0 to (l1.length-1)){
    if(!l1(i).isSimilar(l2(i))){
      ret+=1
    }
  }
ret
}

def closestIf(acParam: ITree): ITree = {
  var ac = acParam
  while(!ac.isRoot){
    if(nodeClassName(ac) == "IfStatement"){
      return ac
    }else{
      ac=ac.getParent
    }
  }
ac
}

var posIf2, posIf3, posIf4, posIf5, posIf6, posIf7, posIf8, posIf9 = false
def cleanIfPosVars(): Boolean = {
posIf2 = false
posIf3 = false
posIf4 = false
posIf5 = false
posIf6 = false
posIf7 = false
posIf8 = false
posIf9 = false
true
}

def alreadyCountedIf(pos: Int): Boolean = {
  var ac = false
  pos match {
  case 2  => return posIf2
  case 3  => return posIf3
  case 4  => return posIf4
  case 5  => return posIf5
  case 6  => return posIf6
  case 7  => return posIf7
  case 8  => return posIf8
  case 9  => return posIf9
  case _  =>  
  }
ac
}


  def match15ExpressionChanger(actions: List[Action]): Int = {
    var res = 0
    cleanIfPosVars()
    for (ac <- actions) {
	var target = closestIf(ac.getNode)

	if(!target.isRoot){
	  if(statementAlreadyExisted(target, actions)){ 
	    if(ac.getNode==target.getChildren.get(0) 
		|| ac.getNode.getParent==target.getChildren.get(0) 
		||ac.getNode.getParent.getParent==target.getChildren.get(0) 
		||ac.getNode.getParent.getParent.getParent==target.getChildren.get(0) 
		||ac.getNode.getParent.getParent.getParent.getParent==target.getChildren.get(0)){
	        if (actionName(ac) == "Update") {
                  res += 1
	        }else if (actionName(ac) == "Insert" || actionName(ac) == "Delete"){ 
		  breakable {
	          for (ac2 <- actions.reverse) {
		  var target2 = closestIf(ac2.getNode)

	            if(target.isSimilar(target2) && ac!=ac2){
		
		      var childrenEnd = childrenInCondition(target.getChildren) 
		      var childrenBeginning = childrenInCondition(target2.getChildren) 

		      if(childrenBeginning.length == childrenEnd.length){
			if(howManyChildrenAreDifferent(childrenBeginning,childrenEnd) != 0){
			  var position = childrenPos(target.getChildren, ac.getNode)
		          if(position > 1 && !alreadyCountedIf(position)){
		            position match {
	  	            case 2  => posIf2=true
			    case 3  => posIf3=true
			    case 4  => posIf4=true
			    case 5  => posIf5=true
			    case 6  => posIf6=true
			    case 7  => posIf7=true
			    case 8  => posIf8=true
			    case 9  => posIf9=true
			    case _  =>  
			  }
		          res+=1
		        }
			}
		      }
	            }
	          }
		  }
 	      }
	    }
	  }
        }
    }
    cleanIfPosVars()
    res15=res
    res






/*
    var res = 0
    for (ac <- actions) {
	if (nodeClassName(ac.getNode.getParent) == "IfStatement" || 
          nodeClassName(ac.getNode.getParent) == "WhileStatement" || 
          nodeClassName(ac.getNode.getParent) == "DoStatement") {  
          if(statementAlreadyExisted(ac.getNode.getParent, actions)){ //parent already existed
	      var exp = nodeClassName(ac.getNode)
	      if(isExpression(exp)){
	        if (actionName(ac) == "Update") { // (var == null) -> (var != null)
                  res += 1
	        }else if (actionName(ac) == "Insert"){ 
		  
		  var addOne = false
  
		  var foundMatchingDelete = false
	          var itIsAExpressionChanger=false
  	          for (ac2 <- actions) {

                    if(actionName(ac2) == "Delete" && ((ac.getNode.getParent.getId == ac2.getNode.getParent.getId) || (ac.getNode.getParent.getPos == ac2.getNode.getParent.getPos))){
		      addOne = true
	            }
		    if(ac.getNode.toShortString == ac2.getNode.getParent.toShortString && actionName(ac2) == "Move"){
		      addOne = true
	            }
	          }
		//println(ac.getNode.getParent.getChildren.get(0).getChildren.size)
		//println(ac.getNode.getParent.getChildren.get(0).getChildren.get(0))
		//println(ac.getNode.getParent.getChildren.get(0).getChildren.get(1))
		if(ac.getNode.getParent.getChildren.get(0).getChildren.size >= 2){
		    addOne = false
		   }

	          
	          if(addOne){
                    res += 1
	          }


/*
	          for (ac2 <- actions) {
		    if(actionName(ac2) == "Delete" && ac.getNode.getParent.getPos == ac2.getNode.getParent.getPos){
		      res += 1
	            }
		    if(ac.getNode.toShortString == ac2.getNode.getParent.toShortString && actionName(ac2) == "Move"){
		      res += 1
	            }
	          } 
*/

	        }
 	      }
            }	  
        }
    }
    res*/
  }


  def match16ExpressionAdder(actions: List[Action]): Int = {
    var res = 0
    for (ac <- actions) {
        if (nodeClassName(ac.getNode.getParent) == "IfStatement" || 
          nodeClassName(ac.getNode.getParent) == "WhileStatement" || 
          nodeClassName(ac.getNode.getParent) == "DoStatement") {
	  if(statementAlreadyExisted(ac.getNode.getParent, actions)){ 

              var exp = nodeClassName(ac.getNode)
	      if(isExpression(exp)){
	        if (actionName(ac) == "Insert"){ 
		  breakable {
	          for (ac2 <- actions.reverse) {
	            if(ac.getNode.getParent.isSimilar(ac2.getNode.getParent) && ac!=ac2){
		
		      var numberOfChildrenEnd = numberOfChildren(ac.getNode.getParent.getChildren) 
		      var numberOfChildrenBeginning = numberOfChildren(ac2.getNode.getParent.getChildren) 
 
		      if(numberOfChildrenBeginning < numberOfChildrenEnd){
		        res += (numberOfChildrenEnd - numberOfChildrenBeginning)
		        break
		      }
	            }
	          }
		  }
 	      }
	    }
	  }
        }
    }
    res16=res
    res
  }


  def statementAlreadyExisted(nodeToLookFor: ITree, actions: List[Action]): Boolean = {
    var res = true
    for (ac <- actions) {
      if (actionName(ac) == "Insert"){
        if(ac.getNode == nodeToLookFor){
	  return false
	}
      }
    }
    res
  }

def statementWasNotRemoved(nodeToLookFor: ITree, actions: List[Action]): Boolean = {
    var res = true
    for (ac <- actions) {
      if (actionName(ac) == "Delete"){
        if(ac.getNode == nodeToLookFor){
	  return false
	}
      }
    }
    res
  }

  def isExpression(exp: String): Boolean = {
    return (exp == "SimpleName" || exp == "MethodInvocation" || exp == "Annotation" || exp == "ArrayAccess" || exp == "ArrayCreation" || exp == "ArrayInitializer" || exp == "Assignment" || exp == "BooleanLiteral" || exp == "CastExpression" || exp == "CharacterLiteral" || exp == "ClassInstanceCreation" || exp == "ConditionalExpression" || exp == "FieldAccess" || exp == "InfixExpression" || exp == "InstanceofExpression" || exp == "LambdaExpression" || exp == "MethodReference" || exp == "Name" || exp == "NullLiteral" || exp == "NumberLiteral" || exp == "ParenthesizedExpression" || exp == "PostfixExpression" || exp == "PrefixExpression" || exp == "StringLiteral" || exp == "SuperFieldAccess" || exp == "SuperMethodInvocation" || exp == "ThisExpression" || exp == "TypeLiteral" || exp == "VariableDeclarationExpression" || exp == "MarkerAnnotation" || exp == "NormalAnnotation" || exp == "SingleMemberAnnotation" || exp == "CreationReference" || exp == "ExpressionMethodReference" || exp == "SuperMethodReference" || exp == "TypeMethodReference" || exp == "QualifiedName")
  }


}
