/**
 *
 * {@link JavaParserBasedClassParser}.java
 *
 * @author Jens Ebert
 *
 * @date 29.04.2020
 *
 */
package com.github.j2a.core.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.github.j2a.core.definition.JavaClassDefinition;
import com.github.j2a.core.definition.JavaClassType;
import com.github.j2a.core.definition.JavaElementVisibility;
import com.github.j2a.core.definition.JavaFieldDefinition;
import com.github.j2a.core.definition.JavaMethodDefinition;
import com.github.j2a.core.definition.JavaParameterDefinition;
import com.github.j2a.core.utils.FullyQualifiedJavaClass;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier.Keyword;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithType;
import com.github.javaparser.ast.type.Type;

/**
 * {@link JavaParserBasedClassParser}
 *
 */
public class JavaParserBasedClassParser implements JavaClassParser<String> {

	/**
	 * @see com.github.j2a.core.parser.JavaClassParser#parse(java.lang.Object)
	 */
	@Override
	public JavaClassDefinition parse(String inputClass) {

		ParseResult<CompilationUnit> cu = new JavaParser().parse(inputClass);

		CompilationUnit compilationUnit = cu.getResult().get();
		PackageDeclaration packDecl = compilationUnit.getPackageDeclaration().get();

		TypeDeclaration<?> primaryType = compilationUnit.getType(0);

		JavaClassType classType = null;

		if (primaryType.isEnumDeclaration()) {
			classType = JavaClassType.ENUM;
		} else {
			ClassOrInterfaceDeclaration ci = (ClassOrInterfaceDeclaration) primaryType;

			if (ci.isInterface()) {
				classType = JavaClassType.INTERFACE;
			} else {
				classType = JavaClassType.CLASS;
			}
		}

		JavaClassDefinition javaClassDefinition = new JavaClassDefinition(null, primaryType.hasModifier(Keyword.FINAL),
			primaryType.getNameAsString(), JavaElementVisibility.fromAccessSpecifier(primaryType.getAccessSpecifier()),
			primaryType.hasModifier(Keyword.STATIC), classType, false, packDecl.getNameAsString(),
			primaryType.hasModifier(Keyword.ABSTRACT) || classType == JavaClassType.INTERFACE,
			primaryType.hasModifier(Keyword.STRICTFP), true, false);

		List<FieldDeclaration> fieldDeclarations = primaryType.getFields();

		List<JavaFieldDefinition> fieldDefinitions = new ArrayList<>();

		for (FieldDeclaration fieldDeclaration : fieldDeclarations) {
			// TODO get whether field is synthetic?
			JavaFieldDefinition fieldDef = new JavaFieldDefinition(fieldDeclaration.isFinal(),
				fieldDeclaration.getVariable(0).getNameAsString(), parseAnnotations(fieldDeclaration.getAnnotations()),
				JavaElementVisibility.fromAccessSpecifier(fieldDeclaration.getAccessSpecifier()),
				fieldDeclaration.isStatic(), fieldDeclaration.isVolatile(), fieldDeclaration.isTransient(),
				resolveType(fieldDeclaration.getVariable(0), false), false);

			fieldDefinitions.add(fieldDef);
		}

		javaClassDefinition.setFields(fieldDefinitions);

		javaClassDefinition.setAnnotations(parseAnnotations(primaryType.getAnnotations()));

		List<JavaMethodDefinition> methodDefinitions = primaryType
			.getConstructors().stream().map(constructorDeclaration -> parseCallableDeclaration(constructorDeclaration,
				javaClassDefinition, javaClassDefinition, true, false, false, false, false))
			.collect(Collectors.toList());

		methodDefinitions.addAll(primaryType.getMethods().stream()
			.map(methodDeclaration -> parseCallableDeclaration(methodDeclaration, javaClassDefinition,
				resolveType(methodDeclaration, false), false, methodDeclaration.isSynchronized(),
				methodDeclaration.isNative(), false, methodDeclaration.isDefault()))
			.collect(Collectors.toList()));

		javaClassDefinition.setMethods(methodDefinitions);

		// TODO get nested classes???

		return javaClassDefinition;
	}

	/**
	 * @see com.github.j2a.core.parser.JavaClassParser#supportsCompileRetentionAnnotations()
	 */
	@Override
	public boolean supportsCompileRetentionAnnotations() {
		return true;
	}

	/**
	 * @param annotations
	 * @param annotationRefs
	 */
	private List<JavaClassReference> parseAnnotations(NodeList<AnnotationExpr> annotations) {
		List<JavaClassReference> annotationRefs = new ArrayList<>();
		for (AnnotationExpr annotationExpr : annotations) {
			annotationRefs.add(new FullyQualifiedJavaClass(annotationExpr.getNameAsString()));
		}

		return annotationRefs;
	}

	/**
	 * @param callableDeclaration
	 * @param enclosingClassDefinition
	 * @param returnType               TODO
	 * @param isSynchronized
	 * @param isNative
	 * @param isBridge
	 * @param isDefault
	 */
	private JavaMethodDefinition parseCallableDeclaration(CallableDeclaration<?> callableDeclaration,
		JavaClassDefinition enclosingClassDefinition, JavaClassReference returnType, boolean isConstructor,
		boolean isSynchronized, boolean isNative, boolean isBridge, boolean isDefault) {
		// TODO isBridge? isSynthetic?

		boolean isVarArgs = callableDeclaration.getParameters().stream().anyMatch(p -> p.isVarArgs());

		List<JavaParameterDefinition> parameters = callableDeclaration.getParameters().stream()
			.map(parameter -> new JavaParameterDefinition(parameter.isFinal(), parameter.getNameAsString(),
				parseAnnotations(parameter.getAnnotations()), resolveType(parameter, isVarArgs)))
			.collect(Collectors.toList());

		JavaElementVisibility visibility = JavaElementVisibility
			.fromAccessSpecifier(callableDeclaration.getAccessSpecifier());

		if (enclosingClassDefinition.getClassType() == JavaClassType.INTERFACE && isDefault) {
			visibility = JavaElementVisibility.PUBLIC;
		}

		return new JavaMethodDefinition(callableDeclaration.isFinal(), callableDeclaration.getNameAsString(),
			parseAnnotations(callableDeclaration.getAnnotations()), visibility, callableDeclaration.isStatic(),
			isConstructor, isSynchronized, isNative, callableDeclaration.isAbstract(), callableDeclaration.isStrictfp(),
			isBridge, false, isDefault, isVarArgs, returnType, parameters);
	}

	private <N extends Node> FullyQualifiedJavaClass resolveType(NodeWithType<N, Type> nodeWithType,
		boolean isVarArgs) {
		return new FullyQualifiedJavaClass(
			isVarArgs ? nodeWithType.getTypeAsString() + "[]" : nodeWithType.getTypeAsString());
	}
}
