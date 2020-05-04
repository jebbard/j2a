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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.github.j2a.core.definition.JavaClassDefinition;
import com.github.j2a.core.definition.JavaClassType;
import com.github.j2a.core.definition.JavaElementVisibility;
import com.github.j2a.core.definition.JavaFieldDefinition;
import com.github.j2a.core.definition.JavaMethodDefinition;
import com.github.j2a.core.definition.JavaParameterDefinition;
import com.github.j2a.core.utils.FullyQualifiedJavaTypeReference;
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
import com.github.javaparser.ast.nodeTypes.NodeWithTypeParameters;
import com.github.javaparser.ast.type.TypeParameter;

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

		Set<String> allTypesInCompilationUnit = getAllTypesBelowNode(compilationUnit);

		PackageDeclaration packDecl = compilationUnit.getPackageDeclaration().get();

		CompilationUnitTypeResolver typeResolver = new CompilationUnitTypeResolver(
			compilationUnit.getImports().stream().map(i -> i.getNameAsString()).collect(Collectors.toSet()),
			allTypesInCompilationUnit, packDecl.getNameAsString());

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
				fieldDeclaration.getVariable(0).getNameAsString(),
				parseAnnotations(fieldDeclaration.getAnnotations(), typeResolver),
				JavaElementVisibility.fromAccessSpecifier(fieldDeclaration.getAccessSpecifier()),
				fieldDeclaration.isStatic(), fieldDeclaration.isVolatile(), fieldDeclaration.isTransient(),
				resolveType(fieldDeclaration.getVariable(0), fieldDeclaration.getVariable(0).getTypeAsString(),
					typeResolver),
				false);

			fieldDefinitions.add(fieldDef);
		}

		javaClassDefinition.setFields(fieldDefinitions);

		javaClassDefinition.setAnnotations(parseAnnotations(primaryType.getAnnotations(), typeResolver));

		List<JavaMethodDefinition> methodDefinitions = primaryType
			.getConstructors().stream().map(constructorDeclaration -> parseCallableDeclaration(constructorDeclaration,
				javaClassDefinition, javaClassDefinition, true, false, false, false, false, typeResolver))
			.collect(Collectors.toList());

		methodDefinitions.addAll(primaryType.getMethods().stream()
			.map(methodDeclaration -> parseCallableDeclaration(methodDeclaration, javaClassDefinition,
				resolveType(methodDeclaration, methodDeclaration.getTypeAsString(), typeResolver), false,
				methodDeclaration.isSynchronized(), methodDeclaration.isNative(), false, methodDeclaration.isDefault(),
				typeResolver))
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

	private Set<String> getAllTypesBelowNode(Node node) {
		Set<String> typesBelowNode = new HashSet<>();

		for (Node child : node.getChildNodes()) {
			if (child instanceof TypeDeclaration) {
				String type = ((TypeDeclaration<?>) child).getNameAsString();
				typesBelowNode.add(type);
			}

			typesBelowNode.addAll(getAllTypesBelowNode(child));
		}

		return typesBelowNode;
	}

	/**
	 * @param annotations
	 * @param annotationRefs
	 */
	private List<JavaTypeReference> parseAnnotations(NodeList<AnnotationExpr> annotations,
		CompilationUnitTypeResolver typeResolver) {
		List<JavaTypeReference> annotationRefs = new ArrayList<>();
		for (AnnotationExpr annotationExpr : annotations) {
			annotationRefs.add(resolveType(annotationExpr, annotationExpr.getNameAsString(), typeResolver));
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
		JavaClassDefinition enclosingClassDefinition, JavaTypeReference returnType, boolean isConstructor,
		boolean isSynchronized, boolean isNative, boolean isBridge, boolean isDefault,
		CompilationUnitTypeResolver typeResolver) {
		// TODO isBridge? isSynthetic?

		boolean isVarArgs = callableDeclaration.getParameters().stream().anyMatch(p -> p.isVarArgs());

		List<JavaParameterDefinition> parameters = callableDeclaration.getParameters().stream()
			.map(parameter -> new JavaParameterDefinition(parameter.isFinal(), parameter.getNameAsString(),
				parseAnnotations(parameter.getAnnotations(), typeResolver),
				resolveType(parameter, parameter.getTypeAsString() + (isVarArgs ? "..." : ""), typeResolver)))
			.collect(Collectors.toList());

		JavaElementVisibility visibility = JavaElementVisibility
			.fromAccessSpecifier(callableDeclaration.getAccessSpecifier());

		if (enclosingClassDefinition.getClassType() == JavaClassType.INTERFACE && isDefault) {
			visibility = JavaElementVisibility.PUBLIC;
		}

		return new JavaMethodDefinition(callableDeclaration.isFinal(), callableDeclaration.getNameAsString(),
			parseAnnotations(callableDeclaration.getAnnotations(), typeResolver), visibility,
			callableDeclaration.isStatic(), isConstructor, isSynchronized, isNative, callableDeclaration.isAbstract(),
			callableDeclaration.isStrictfp(), isBridge, false, isDefault, isVarArgs, returnType, parameters);
	}

	private FullyQualifiedJavaTypeReference resolveType(Node node, String type,
		CompilationUnitTypeResolver typeResolver) {

		Set<String> typeParametersInScope = new HashSet<>();

		Optional<Node> parentNodeOptional = node.getParentNode();

		while (parentNodeOptional.isPresent()) {
			Node parentNode = parentNodeOptional.get();

			if (parentNode instanceof NodeWithTypeParameters) {
				NodeWithTypeParameters<?> nodeWithTypeParams = (NodeWithTypeParameters<?>) parentNode;

				for (TypeParameter typeParam : nodeWithTypeParams.getTypeParameters()) {
					typeParametersInScope.add(typeParam.getNameAsString());
				}
			}

			if (parentNode instanceof CallableDeclaration) {
				// Only non-static methods can use the type parameters of their enclosing class
				if (((CallableDeclaration<?>) parentNode).isStatic()) {
					break;
				}
			}

			parentNodeOptional = parentNode.getParentNode();
		}

		return typeResolver.resolveType(type, typeParametersInScope);
	}
}
