package cn.edu.pku.sei.plde.qacrashfix.jdt;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jdt.core.dom.*;

import cn.edu.pku.sei.plde.qacrashfix.jdt.nodes.*;
/**
 * ASTVisit that helps to generate a tree
 * @author Hansheng Zhang
 */
class TreeGenerateVisitor extends ASTVisitor{
	private JDTTreeNode _tree = null;
	private final ASTNode _root;
	private final  Map<ASTNode, JDTTreeNode> _map = new HashMap<ASTNode, JDTTreeNode>();
	private final LinkedList<ASTNode> _nodes= new LinkedList<ASTNode>();
	private final Logger logger = LogManager.getLogger();
	
	public TreeGenerateVisitor(ASTNode root){
		_root = root;
		_root.accept(this);
		for (ASTNode node : _nodes){
			if (!_map.containsKey(node)){
				logger.warn("unspecified node convertion: " + node.getClass().getSimpleName());
				_map.put(node, new JDTTreeNode(node));
			}
			JDTTreeNode treeNode = _map.get(node);
			if (treeNode.getASTNode()==_root)
				_tree = treeNode;
			else _map.get(treeNode.getASTNode().getParent()).appendChild(treeNode);
		}
	}
	
	public JDTTreeNode getTree(){
		return _tree;
	}
	
	
	@Override
	public void preVisit(ASTNode node){
		_nodes.add(node);
	}
	
	@Override
	public boolean visit(UnionType node) {
		return addToMap(node, new JDTUnionType(node));
	}
	
	@Override
	public boolean visit(TypeDeclarationStatement node) {
		return addToMap(node, new JDTTypeDeclarationStatement(node));
	}
	
	@Override
	public boolean visit(TextElement node) {
		return false;
	}
	
	@Override
	public boolean visit(TagElement node) {
		return false;
	}
	
	@Override
	public boolean visit(SuperFieldAccess node) {
		return addToMap(node, new JDTSuperFieldAccess(node));
	}
	
	@Override
	public boolean visit(QualifiedType node) {
		return addToMap(node, new JDTQualifiedType(node));
	}
	
	@Override
	public boolean visit(NormalAnnotation node) {
		return addToMap(node, new JDTNormalAnnotation(node));
	}
	
	@Override
	public boolean visit(MethodRefParameter node) {
		return false;
	}
	
	@Override
	public boolean visit(MethodRef node) {
		return false;
	}
	
	@Override
	public boolean visit(MemberValuePair node) {
		return addToMap(node, new JDTMemberValuePair(node));
	}
	
	@Override
	public boolean visit(MemberRef node) {
		return false;
	}
	
	@Override
	public boolean visit(LabeledStatement node) {
		return addToMap(node, new JDTLabeledStatement(node));
	}
	
	@Override
	public boolean visit(LineComment node) {
		return false;
	}
	
	@Override
	public boolean visit(Javadoc node) {
		return false;
	}
	
	@Override
	public boolean visit(EnumDeclaration node) {
		return addToMap(node, new JDTEnumDeclaration(node));
	}
	
	@Override
	public boolean visit(EnumConstantDeclaration node) {
		return addToMap(node, new JDTEnumConstantDeclaration(node));
	}
	
	@Override
	public boolean visit(BlockComment node) {
		return false;
	}
	
	@Override
	public boolean visit(AssertStatement node) {
		return addToMap(node, new JDTAssertStatement(node));
	}
	
	@Override
	public boolean visit(AnnotationTypeMemberDeclaration node) {
		return addToMap(node, new JDTAnnotationTypeMemberDeclaration(node));
	}
	
	@Override
	public boolean visit(AnnotationTypeDeclaration node) {
		return addToMap(node, new JDTAnnotationTypeDeclaration(node));
	}
	
	@Override
	public boolean visit(ConstructorInvocation node) {
		return addToMap(node, new JDTConstructorInvocation(node));
	}
	
	@Override
	public boolean visit(ContinueStatement node) {
		return addToMap(node, new JDTContinueStatement(node));
	}
	
	@Override
	public boolean visit(WhileStatement node) {
		return addToMap(node, new JDTWhileStatement(node));
	}
	
	@Override
	public boolean visit(SynchronizedStatement node) {
		return addToMap(node, new JDTSynchronizedStatement(node));
	}
	
	@Override
	public boolean visit(EmptyStatement node) {
		return addToMap(node, new JDTEmptyStatement(node));
	}
	
	@Override
	public boolean visit(TypeParameter node) {
		return addToMap(node, new JDTTypeParameter(node));
	}
	
	@Override
	public boolean visit(ThrowStatement node) {
		return addToMap(node, new JDTThrowStatement(node));
	}
	
	@Override
	public boolean visit(WildcardType node) {
		return addToMap(node, new JDTWildcardType(node));
	}
	
	@Override
	public boolean visit(ArrayAccess node) {
		return addToMap(node, new JDTArrayAccess(node));
	}
	
	@Override
	public boolean visit(DoStatement node) {
		return addToMap(node, new JDTDoStatement(node));
	}
	
	@Override
	public boolean visit(Initializer node) {
		return addToMap(node, new JDTInitializer(node));
	}
	
	@Override
	public boolean visit(TypeLiteral node) {
		return addToMap(node, new JDTTypeLiteral(node));
	}
	
	@Override
	public boolean visit(SingleMemberAnnotation node) {
		return addToMap(node, new JDTSingleMemberAnnotation(node));
	}
	
	@Override
	public boolean visit(InstanceofExpression node) {
		return addToMap(node, new JDTInstanceofExpression(node));
	}
	
	@Override
	public boolean visit(PostfixExpression node) {
		return addToMap(node, new JDTPostfixExpression(node));
	}
	
	@Override
	public boolean visit(VariableDeclarationExpression node) {
		return addToMap(node, new JDTVariableDeclarationExpression(node));
	}
	
	@Override
	public boolean visit(ForStatement node) {
		return addToMap(node, new JDTForStatement(node));
	}
	
	@Override
	public boolean visit(BreakStatement node) {
		return addToMap(node, new JDTBreakStatement(node));
	}
	
	@Override
	public boolean visit(SwitchCase node) {
		return addToMap(node, new JDTSwitchCase(node));
	}
	
	@Override
	public boolean visit(SwitchStatement node) {
		return addToMap(node, new JDTSwitchStatement(node));
	}
	
	@Override
	public boolean visit(AnonymousClassDeclaration node) {
		return addToMap(node, new JDTAnonymousClassDeclaration(node));
	}
	
	@Override
	public boolean visit(SuperConstructorInvocation node) {
		return addToMap(node, new JDTSuperConstructorInvocation(node));
	}
	
	@Override
	public boolean visit(ParameterizedType node) {
		return addToMap(node, new JDTParameterizedType(node));
	}
	
	@Override
	public boolean visit(EnhancedForStatement node) {
		return addToMap(node, new JDTEnhancedForStatement(node));
	}
	
	@Override
	public boolean visit(ParenthesizedExpression node) {
		return addToMap(node, new JDTParenthesizedExpression(node));
	}
	
	@Override
	public boolean visit(CharacterLiteral node) {
		return addToMap(node, new JDTCharacterLiteral(node));
	}
	
	@Override
	public boolean visit(BooleanLiteral node) {
		return addToMap(node, new JDTBooleanLiteral(node));
	}
	
	@Override
	public boolean visit(ConditionalExpression node) {
		return addToMap(node, new JDTConditionalExpression(node));
	}
	
	@Override
	public boolean visit(FieldAccess node) {
		return addToMap(node, new JDTFieldAccess(node));
	}
	
	@Override
	public boolean visit(ArrayInitializer node) {
		return addToMap(node, new JDTArrayInitializer(node));
	}
	
	@Override
	public boolean visit(ImportDeclaration node) {
		return addToMap(node, new JDTImportDeclaration(node));
	}
	
	@Override
	public boolean visit(PackageDeclaration node) {
		return addToMap(node, new JDTPackageDeclaration(node));
	}
	
	@Override
	public boolean visit(FieldDeclaration node) {
		return addToMap(node , new JDTFieldDeclaration(node));
	}
	
	@Override
	public boolean visit(ReturnStatement node) {
		return addToMap(node, new JDTReturnStatement(node));
	}
	
	@Override
	public boolean visit(PrefixExpression node) {
		return addToMap(node, new JDTPrefixExpression(node));
	}
	
	@Override
	public boolean visit(ThisExpression node) {
		return addToMap(node , new JDTThisExpression(node) );
	}
	
	@Override
	public boolean visit(MarkerAnnotation node) {
		return addToMap(node, new JDTMarkerAnnotation(node));
	}
	
	@Override
	public boolean visit(CastExpression node) {
		return addToMap(node, new JDTCastExpression(node));
	}
	
	@Override
	public boolean visit(Modifier node) {
		return addToMap(node , new JDTModifier(node));
	}
	
	@Override
	public boolean visit(TypeDeclaration node) {
		return addToMap(node, new JDTTypeDeclaration(node));
	}
	
	
	@Override
	public boolean visit(VariableDeclarationFragment node) {
		return addToMap(node, new JDTVariableDeclarationFragment(node));
	}
	
	@Override
	public boolean visit(VariableDeclarationStatement node) {
		return addToMap(node, new JDTVariableDeclarationStatement(node));
	}
	
	@Override
	public boolean visit(CompilationUnit node) {
		return addToMap(node, new JDTCompilationUnit(node));
	}
	
	@Override
	public boolean visit(NumberLiteral node) {
		return addToMap(node, new JDTNumberLiteral(node));
	}
	
	@Override
	public boolean visit(PrimitiveType node) {
		// TODO Auto-generated method stub
		return addToMap(node, new JDTPrimitiveType(node));
	}
	
	@Override
	public boolean visit(ArrayType node) {
		return addToMap(node, new JDTArrayType(node));
	}
	
	@Override
	public boolean visit(ArrayCreation node) {
		return addToMap(node, new JDTArrayCreation(node));
	}
	
	@Override
	public boolean visit(QualifiedName node) {
		return addToMap(node, new JDTQualifiedName(node));
	}
	
	@Override
	public boolean visit(ClassInstanceCreation node) {
		return addToMap(node, new JDTClassInstanceCreation(node));
	}
	
	@Override
	public boolean visit(Assignment node) {
		return addToMap(node, new JDTAssignment(node));
	}
	
	@Override
	public boolean visit(MethodDeclaration node){
		return addToMap(node, new JDTMethodDeclaration(node));
	}
	
	@Override 
	public boolean visit(SimpleType node){
		return addToMap(node, new JDTSimpleType(node));
	}
	
	@Override
	public boolean visit(SingleVariableDeclaration node){
		return addToMap(node, new JDTSingleVariableDeclaration(node));
	}
	
	@Override
	public boolean visit(CatchClause node){
		return addToMap(node, new JDTCatchClause(node));
	}
	
	@Override
	public boolean visit(NullLiteral node) {
		return addToMap(node, new JDTNullLiteral(node));
	}
	
	@Override
	public boolean visit(Block node){
		return addToMap(node, new JDTBlock(node));
	}
	
	@Override
	public boolean visit(ExpressionStatement node){
		return addToMap(node, new JDTExpressionStatement(node));
	}
	
	@Override
	public boolean visit(IfStatement node){
		return addToMap(node, new JDTIfStatement(node));
	}
	
	@Override
	public boolean visit(InfixExpression node){
		return addToMap(node, new JDTInfixExpression(node));
	}
	
	@Override
	public boolean visit(SimpleName node){
		return addToMap(node, new JDTSimpleName(node));
	}
	
	@Override
	public boolean visit(SuperMethodInvocation node){
		return addToMap(node, new JDTSuperMethodInvocation(node));
	}
	
	@Override
	public boolean visit(MethodInvocation node){
		return addToMap(node, new JDTMethodInvocation(node));
	}
	
	@Override
	public boolean visit(StringLiteral node){
		return addToMap(node, new JDTStringLiteral(node));
	}
	
	@Override
	public boolean visit(TryStatement node){
		return addToMap(node, new JDTTryStatement(node));
	}
	
	private boolean addToMap(ASTNode astNode, JDTTreeNode treeNode){
		_map.put(astNode, treeNode);
		return true;
	}
}
