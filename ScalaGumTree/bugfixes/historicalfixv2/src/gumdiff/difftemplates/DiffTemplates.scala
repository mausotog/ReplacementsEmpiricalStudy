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
      println("ACTION: " + action)
	println("Node: "+ diff.nodeClassName(action.getNode))
        println("NodeParent: "+ diff.nodeClassName(action.getNode.getParent))
	println()
    }
    if(actions.isEmpty)
      println("Bachle: Warning in match Template: actions size is zero!!!")

    println(diff.match1AddNullChecker(actions)+": 1AddNullChecker")
    println(diff.match2ParameterReplacer(actions)+": 2ParameterReplacer")
    println(diff.match3MethodReplacer(actions)+": 3MethodReplacer")
    println(diff.match4ParameterAdderRemover(actions)+": 4ParameterAdderRemover")
    println(diff.match5ObjectInitializer(actions)+": 5ObjectInitializer")
    println(diff.match6SequenceExchanger(actions)+": 6SequenceExchanger")
    println(diff.match7AddRangeChecker(actions)+": 7AddRangeChecker")
    println(diff.match8AddColectionSizeChecker(actions)+": 8AddColectionSizeChecker")
    println(diff.match9LowerBoundSetter(actions)+": 9LowerBoundSetter")
    println(diff.match10UpperBoundSetter(actions)+": 10UpperBoundSetter")
    println(diff.match11OffByOneMutator(actions)+": 11OffByOneMutator")
    println(diff.match12AddCastChecker(actions)+": 12AddCastChecker")
    println(diff.match13CasterMutator(actions)+": 13CasterMutator")
    println(diff.match14CasteeMutator(actions)+": 14CasteeMutator")
    println(diff.match15ExpressionChanger(actions)+": 15ExpressionChanger")
    println(diff.match16ExpressionAdder(actions)+": 16ExpressionAdder")
    
    
    
    
    
    results
  }
}

class DiffTemplates {
  var allInfor = new ArrayBuffer[(String, String, ArrayBuffer[String])] // var name, method name, args name
  var classInstanceInfor = new ArrayBuffer[(String, ArrayBuffer[String])] // class name, args name
  var arrayAccess = new ArrayBuffer[(String, ArrayBuffer[String])] // var name, index name

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

  def getDiffActions(file1: String, file2: String): (List[Action], MyJdtTreeGenerator, MyJdtTreeGenerator) = {
    val srcTG = new MyJdtTreeGenerator()
    val srcTC = srcTG.generateFromFile(file1)
    val dstTG = new MyJdtTreeGenerator()
    val dstTC = dstTG.generateFromFile(file2)
    val src = srcTC.getRoot
    val dst = dstTC.getRoot
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
/*
  def match1ParameterReplacer(actions: List[Action],stg: JdtTreeGenerator, dtg: JdtTreeGenerator ) : Boolean ={
    for(ac <- actions){
      if(nodeClassName(ac.getNode) == "SimpleName"){
        val realName = extractRealSimpleName(ac.getNode.getShortLabel)
      }
    }
    true
  }
*/

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
    res

/*
    var res = false
    val arrActions = actions.toArray(Array.ofDim[Action](actions.size))
    if (nodeClassName(arrActions(0).getNode.getParent) == "Block") {
      if (nodeClassName(arrActions(0).getNode) == "IfStatement") {
        if (actionName(arrActions(0)) != "Insert") return false
        for (ac <- actions if nodeClassName(ac.getNode) == "InfixExpression" if nodeClassName(ac.getNode.getChild(1)) == "NullLiteral") res = true
      }
    }
    res*/
  }

  def match2ParameterReplacer(actions: List[Action]): Int = {
    var res = 0
    for (ac <- actions) {
        if (nodeClassName(ac.getNode.getParent) == "MethodInvocation") {
          if(statementAlreadyExisted(ac.getNode.getParent, actions)){ //parent already existed
	  var exp = nodeClassName(ac.getNode)
          if (isExpression(exp)) {
            if (actionName(ac) == "Update" || actionName(ac) == "Insert" ) {
	      if(ac.getNode.getParent.getChildren.get(1).toShortString != ac.getNode.toShortString){
	        res += 1
	      }
	    }else if(actionName(ac) == "Delete"){
	      if(nodeClassName(ac.getNode) == "MethodInvocation"){
		res += 1
	      }
  	    }
          } 
        }
      }
    }
    res
  }

  def match3MethodReplacer(actions: List[Action]): Int = {
    var res = 0
    for (ac <- actions){
      if (nodeClassName(ac.getNode) == "SimpleName"){
	if (nodeClassName(ac.getNode.getParent) == "MethodInvocation"){
          if (actionName(ac) == "Update") {
            res += 1
	  }
	}
      }
    }
    res
  }

  def match4ParameterAdderRemover(actions: List[Action]): Int = {
    var res = 0
    for (ac <- actions) {
      if (nodeClassName(ac.getNode.getParent) == "MethodInvocation") {
        if(statementAlreadyExisted(ac.getNode.getParent, actions)){ //parent already existed
 	  if (isExpression(nodeClassName(ac.getNode))) {          
	    if (actionName(ac) == "Insert" || actionName(ac) == "Move"){
	      var foundMatchingDelete = false
  	      for (ac2 <- actions) {
                if(ac.getNode.getParent.getChildren.get(1).toShortString == ac2.getNode.getParent.getChildren.get(1).toShortString && actionName(ac2) == "Delete"){
		  foundMatchingDelete = true
	        }
	      }
	      if(!foundMatchingDelete){
                res += 1
	      }
            }else if (actionName(ac) == "Delete") {            
	      var foundMatchingInsert = false
  	      for (ac2 <- actions) {
		//println("Comparing: " + ac.getNode.getParent.getChildren.get(1).toShortString + " with " + ac2.getNode.getParent.getChildren.get(1).toShortString)
                if(ac.getNode.getParent.getChildren.get(1).toShortString == ac2.getNode.getParent.getChildren.get(1).toShortString && (actionName(ac2) == "Insert" || actionName(ac2) == "Move") ){
		  //println("Match found!")
		  foundMatchingInsert = true
		 // break
	        }
	      }
	      if(!foundMatchingInsert){
		//println("ac.getNode.getParent.getChildren.get(1).toShortString: " + ac.getNode.getParent.getChildren.get(1).toShortString + " ac.getNode.toShortString: " + ac.getNode.toShortString)
		//if(ac.getNode.getParent.getChildren.get(1).toShortString != ac.getNode.toShortString){                
		  res += 1
		//}
	      }
            }
          }
        }
      }
    }
    res
  }

  def match5ObjectInitializer(actions: List[Action]): Int = {
    var res = 0
    for (ac <- actions) {
	for (ac1 <- actions) {
        if (nodeClassName(ac.getNode.getParent) == "VariableDeclarationFragment") {
          if(nodeClassName(ac.getNode) == "ClassInstanceCreation"){
	    if (actionName(ac) == "Insert" && actionName(ac1) == "Delete"){
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
    res




   /* var res = false
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
    res*/
  }


  def match6SequenceExchanger(actions: List[Action]): Int = {
    var res = 0
    for (ac <- actions) {
      for (ac1 <- actions) {
	if(actionName(ac) == "Insert" && actionName(ac1) == "Delete"){
	  if(nodeClassName(ac.getNode.getParent) == nodeClassName(ac1.getNode.getParent)){	
	    if(nodeClassName(ac.getNode) == nodeClassName(ac1.getNode)){
	       res += 1 
            }
	  }
	}
      }
    }
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
		  if(nodeClassName(condition.getChildren.get(0).getChildren.get(0)) == "SimpleName" ){
		    if(condition.getChildren.get(0).getShortLabel == "<" || condition.getChildren.get(0).getShortLabel == "<=" || condition.getChildren.get(0).getShortLabel == ">" || condition.getChildren.get(0).getShortLabel == ">="){
		      if(nodeClassName(condition.getChildren.get(1).getChildren.get(0)) == "SimpleName" ){
		        if(condition.getChildren.get(0).getShortLabel == "<" || condition.getChildren.get(0).getShortLabel == "<=" || condition.getChildren.get(0).getShortLabel == ">" || condition.getChildren.get(0).getShortLabel == ">="){
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
    res
  }

  def match8AddColectionSizeChecker(actions: List[Action]): Int = {

    var res = 0
    for (ac <- actions) {

          if(nodeClassName(ac.getNode) == "IfStatement"){
	    if (actionName(ac) == "Insert"){
	      val condition = ac.getNode.getChildren.get(0)
	      if((nodeClassName(condition) == "InfixExpression")){ 
		//only checks one bound
		if(nodeClassName(condition.getChildren.get(0)) != "InfixExpression" ){
		  if(nodeClassName(condition.getChildren.get(0)) == "SimpleName" && nodeClassName(condition.getChildren.get(1)) == "MethodInvocation"){
		    if(condition.getShortLabel == "<" || condition.getShortLabel == "<=" || condition.getShortLabel == ">" || condition.getShortLabel == ">="){
		        res += 1
		      
		    }
		  }
		}
	      }
            }
	  }
    }
    res
  }


  def match9LowerBoundSetter(actions: List[Action]): Int = {
    var res = 0
    for (ac <- actions) {
	if(nodeClassName(ac.getNode) == "IfStatement"){
	    if (actionName(ac) == "Insert"){
	      val condition = ac.getNode.getChildren.get(0)
	      if((nodeClassName(condition) == "InfixExpression")){ 
		  if(nodeClassName(condition.getChildren.get(0)) == "SimpleName"){
		        if(condition.getShortLabel == "<" || condition.getShortLabel == "<="){
		            val ifBody = ac.getNode.getChildren.get(1)
			     if((nodeClassName(ifBody.getChildren.get(0).getChildren.get(0)) == "Assignment")){ 
			       val assig = ifBody.getChildren.get(0).getChildren.get(0)
			       if(nodeClassName(assig.getChildren.get(0)) == "SimpleName"){
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
    res
  }

  def match10UpperBoundSetter(actions: List[Action]): Int = {
    var res = 0
    for (ac <- actions) {
	if(nodeClassName(ac.getNode) == "IfStatement"){
	    if (actionName(ac) == "Insert"){
	      val condition = ac.getNode.getChildren.get(0)
	      if((nodeClassName(condition) == "InfixExpression")){ 
		  if(nodeClassName(condition.getChildren.get(0)) == "SimpleName"){
		        if(condition.getShortLabel == ">" || condition.getShortLabel == ">="){
		            val ifBody = ac.getNode.getChildren.get(1)
			    if((nodeClassName(ifBody.getChildren.get(0).getChildren.get(0)) == "Assignment")){ 
			       val assig = ifBody.getChildren.get(0).getChildren.get(0)
			       if(nodeClassName(assig.getChildren.get(0)) == "SimpleName"){
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
    res
  }

  def match11OffByOneMutator(actions: List[Action]): Int = {
    var res = 0
    for (ac <- actions) {
      if(nodeClassName(ac.getNode) == "PostfixExpression"){
	if(nodeClassName(ac.getNode.getParent) == "ArrayAccess"){	
	    if (actionName(ac) == "Insert"){
	      if((nodeClassName(ac.getNode.getChildren.get(0)) == "SimpleName")){ 
		 res += 1 
	      }
            }
	  }
	}
    }
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
    res
  }

  def match14CasteeMutator(actions: List[Action]): Int = {
    var res = 0
    for (ac <- actions) {
      if(isExpression(nodeClassName(ac.getNode))){
	if(nodeClassName(ac.getNode.getParent) == "CastExpression"){	
	    if (actionName(ac) == "Insert"){
	      //if((nodeClassName(ac.getNode.getChildren.get(0)) == "SimpleName")){ 
		 res += 1 
	      //}
            }
	  }
	}else if(nodeClassName(ac.getNode) == "SimpleName"){
	  if(nodeClassName(ac.getNode.getParent) == "CastExpression"){	
	    if (actionName(ac) == "Update"){ 
		 res += 1 
            }
	  }
	}
    }
    res
  }

  def match15ExpressionChanger(actions: List[Action]): Int = {
    var res = 0
    for (ac <- actions) {   
	if (nodeClassName(ac.getNode.getParent) == "IfStatement" || 
          nodeClassName(ac.getNode.getParent) == "WhileStatement" || 
          nodeClassName(ac.getNode.getParent) == "DoStatement") {
	    if(statementAlreadyExisted(ac.getNode.getParent, actions)){ //parent already existed
	      if (actionName(ac) == "Update") { // (var == null) -> (var != null)
		if (nodeClassName(ac.getNode) == "InfixExpression") {
                  res += 1
		}
	      }else if (actionName(ac) == "Insert"){ //or Delete, but if we count both it would count them twice
	        var exp = nodeClassName(ac.getNode)
	        if(isExpression(exp)){	  
	          //if (nodeClassName(ac.getNode.getParent) == "InfixExpression" && nodeClassName(ac.getNode.getParent.getParent) != "InfixExpression") { // (var == null) -> (var == var2)
                    res += 1
	          //}
	        }
	      }
            }	  
        }
   
    }
    res
  }

  def match16ExpressionAdder(actions: List[Action]): Int = {
    var res = 0
    for (ac <- actions) {
      if (nodeClassName(ac.getNode.getParent) == "InfixExpression") {
	//println("nodeClassName(ac.getNode.getParent.getParent): "+nodeClassName(ac.getNode.getParent.getParent))
        if (nodeClassName(ac.getNode.getParent.getParent) == "IfStatement") {
	  if(statementAlreadyExisted(ac.getNode.getParent.getParent, actions)){ //parent already existed
	    if(!statementAlreadyExisted(ac.getNode.getParent, actions)){ //parent already existed
              var exp = nodeClassName(ac.getNode)
	      if(isExpression(exp)){	  
	        if (actionName(ac) == "Insert"){
		println("ac.getNode.getChildren.get(0): "+ac.getNode.getChildren.get(0))
		 // if (ac.getNode.getChildren.get(0) != null && ac.getNode.getChildren.get(1) != null && nodeClassName(ac.getNode.getChildren.get(0)) == "InfixExpression" && nodeClassName(ac.getNode.getChildren.get(1)) == "InfixExpression") {
                  res += 1
		  //}
              }
	    }
          }}
        }
      }
    }
    res






/*
    var res = 0
    for (ac <- actions) {
        if (nodeClassName(ac.getNode.getParent) == "IfStatement") {
          if(statementAlreadyExisted(ac.getNode.getParent, actions)){ //parent already existed
            if(nodeClassName(ac.getNode) == "InfixExpression"){
	      if (actionName(ac) == "Insert"){
		  if (ac.getNode.getChildren.get(0) != null && ac.getNode.getChildren.get(1) != null && nodeClassName(ac.getNode.getChildren.get(0)) == "InfixExpression" && nodeClassName(ac.getNode.getChildren.get(1)) == "InfixExpression") {
                  res += 1
		  }
              }
	    }
          }
        }
    }
    res*/
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

  def isExpression(exp: String): Boolean = {
    return (exp == "SimpleName" || exp == "MethodInvocation" || exp == "Annotation" || exp == "ArrayAccess" || exp == "ArrayCreation" || exp == "ArrayInitializer" || exp == "Assignment" || exp == "BooleanLiteral" || exp == "CastExpression" || exp == "CharacterLiteral" || exp == "ClassInstanceCreation" || exp == "ConditionalExpression" || exp == "FieldAccess" || exp == "InfixExpression" || exp == "InstanceofExpression" || exp == "LambdaExpression" || exp == "MethodReference" || exp == "Name" || exp == "NullLiteral" || exp == "NumberLiteral" || exp == "ParenthesizedExpression" || exp == "PostfixExpression" || exp == "PrefixExpression" || exp == "StringLiteral" || exp == "SuperFieldAccess" || exp == "SuperMethodInvocation" || exp == "ThisExpression" || exp == "TypeLiteral" || exp == "VariableDeclarationExpression" || exp == "MarkerAnnotation" || exp == "NormalAnnotation" || exp == "SingleMemberAnnotation" || exp == "CreationReference" || exp == "ExpressionMethodReference" || exp == "SuperMethodReference" || exp == "TypeMethodReference" || exp == "QualifiedName")
  }


}
