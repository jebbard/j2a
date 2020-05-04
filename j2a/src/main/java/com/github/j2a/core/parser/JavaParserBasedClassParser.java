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
import com.github.j2a.core.utils.TypeReferenceHelper;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier.Keyword;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithTypeParameters;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
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

		return parseTypeDeclaration(primaryType, packDecl, typeResolver, false);
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

		List<JavaParameterDefinition> parameters = null;

		if (isVarArgs) {
			parameters = callableDeclaration.getParameters().stream()
				.limit(callableDeclaration.getParameters().size() - 1)
				.map(parameter -> new JavaParameterDefinition(parameter.isFinal(), parameter.getNameAsString(),
					parseAnnotations(parameter.getAnnotations(), typeResolver),
					resolveType(parameter, parameter.getTypeAsString(), typeResolver)))
				.collect(Collectors.toList());

			parameters
				.addAll(
					callableDeclaration.getParameters().stream().skip(callableDeclaration.getParameters().size() - 1)
						.map(parameter -> new JavaParameterDefinition(parameter.isFinal(), parameter.getNameAsString(),
							parseAnnotations(parameter.getAnnotations(), typeResolver), resolveType(parameter,
								parameter.getTypeAsString() + (isVarArgs ? "..." : ""), typeResolver)))
						.collect(Collectors.toList()));
		} else {
			parameters = callableDeclaration.getParameters().stream()
				.map(parameter -> new JavaParameterDefinition(parameter.isFinal(), parameter.getNameAsString(),
					parseAnnotations(parameter.getAnnotations(), typeResolver),
					resolveType(parameter, parameter.getTypeAsString(), typeResolver)))
				.collect(Collectors.toList());

		}

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

	/**
	 * @param primaryType
	 * @param packDecl
	 * @param typeResolver
	 * @param isInnerClass TODO
	 * @return
	 */
	private JavaClassDefinition parseTypeDeclaration(TypeDeclaration<?> primaryType, PackageDeclaration packDecl,
		CompilationUnitTypeResolver typeResolver, boolean isInnerClass) {
		JavaClassType classType = null;

		JavaTypeReference baseClass = primaryType.isEnumDeclaration() ? new FullyQualifiedJavaTypeReference(Enum.class)
			: new FullyQualifiedJavaTypeReference(Object.class);

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

		if (!primaryType.isEnumDeclaration()) {
			ClassOrInterfaceDeclaration ci = (ClassOrInterfaceDeclaration) primaryType;

			if (ci.getExtendedTypes().size() == 1) {
				ClassOrInterfaceType baseClassType = ci.getExtendedTypes(0);

				baseClass = resolveType(baseClassType, baseClassType.getNameAsString(), typeResolver);
			}
		}

		List<ClassOrInterfaceType> implementedTypes = new ArrayList<>();

		if (primaryType.isEnumDeclaration()) {
			EnumDeclaration e = (EnumDeclaration) primaryType;

			implementedTypes.addAll(e.getImplementedTypes());
		} else {
			ClassOrInterfaceDeclaration ci = (ClassOrInterfaceDeclaration) primaryType;

			implementedTypes.addAll(ci.getImplementedTypes());
		}

		List<JavaTypeReference> implementedInterfaces = new ArrayList<>();

		for (ClassOrInterfaceType classOrInterfaceType : implementedTypes) {
			implementedInterfaces
				.add(resolveType(classOrInterfaceType, classOrInterfaceType.getNameAsString(), typeResolver));
		}

		JavaClassDefinition javaClassDefinition = new JavaClassDefinition(null,
			primaryType.isEnumDeclaration() || primaryType.hasModifier(Keyword.FINAL), primaryType.getNameAsString(),
			JavaElementVisibility.fromAccessSpecifier(primaryType.getAccessSpecifier()),
			primaryType.isEnumDeclaration() || primaryType.hasModifier(Keyword.STATIC), classType, isInnerClass,
			packDecl.getNameAsString(),
			primaryType.hasModifier(Keyword.ABSTRACT) || classType == JavaClassType.INTERFACE,
			primaryType.hasModifier(Keyword.STRICTFP), true, false);

		javaClassDefinition.setBaseClassOrInterface(baseClass);

		javaClassDefinition.setImplementedInterfaces(implementedInterfaces);

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

		if (primaryType.isEnumDeclaration()) {
			List<Node> children = primaryType.getChildNodes();

			for (Node node : children) {
				if (node instanceof EnumConstantDeclaration) {

					EnumConstantDeclaration enumDecl = (EnumConstantDeclaration) node;

					fieldDefinitions.add(new JavaFieldDefinition(true, enumDecl.getNameAsString(),
						parseAnnotations(enumDecl.getAnnotations(), typeResolver), JavaElementVisibility.PUBLIC, true,
						false, false, javaClassDefinition, false));
				}
			}

			fieldDefinitions
				.add(new JavaFieldDefinition(true, "ENUM$VALUES", new ArrayList<>(), JavaElementVisibility.PRIVATE,
					true, false, false, resolveType(primaryType,
						javaClassDefinition.getName() + TypeReferenceHelper.EMPTY_ARRAY_BRACKETS, typeResolver),
					false));
		}

		javaClassDefinition.setFields(fieldDefinitions);

		javaClassDefinition.setAnnotations(parseAnnotations(primaryType.getAnnotations(), typeResolver));

		List<JavaMethodDefinition> methodDefinitions = primaryType
			.getConstructors().stream().map(constructorDeclaration -> parseCallableDeclaration(constructorDeclaration,
				javaClassDefinition, javaClassDefinition, true, false, false, false, false, typeResolver))
			.collect(Collectors.toList());

		// Add default constructor
		if (primaryType.getConstructors().isEmpty() && classType != JavaClassType.INTERFACE) {
			methodDefinitions.add(new JavaMethodDefinition(false, primaryType.getNameAsString(), new ArrayList<>(),
				JavaElementVisibility.PUBLIC, false, true, false, false, false, false, false, false, false, false,
				javaClassDefinition, new ArrayList<>()));
		}

		methodDefinitions.addAll(primaryType.getMethods().stream()
			.map(methodDeclaration -> parseCallableDeclaration(methodDeclaration, javaClassDefinition,
				resolveType(methodDeclaration, methodDeclaration.getTypeAsString(), typeResolver), false,
				methodDeclaration.isSynchronized(), methodDeclaration.isNative(), false, methodDeclaration.isDefault(),
				typeResolver))
			.collect(Collectors.toList()));

		if (primaryType.isEnumDeclaration()) {
			methodDefinitions.add(new JavaMethodDefinition(false, "valueOf", new ArrayList<>(),
				JavaElementVisibility.PUBLIC, false, false, false, false, false, false, false, false, false, false,
				javaClassDefinition, new ArrayList<>()));

			methodDefinitions
				.add(new JavaMethodDefinition(false, "values", new ArrayList<>(), JavaElementVisibility.PUBLIC, false,
					false, false, false, false, false, false, false, false, false, resolveType(primaryType,
						javaClassDefinition.getName() + TypeReferenceHelper.EMPTY_ARRAY_BRACKETS, typeResolver),
					new ArrayList<>()));
		}

		javaClassDefinition.setMethods(methodDefinitions);

		List<Node> children = primaryType.getChildNodes();

		List<JavaClassDefinition> nestedClasses = new ArrayList<>();

		for (Node node : children) {
			if (node instanceof TypeDeclaration) {
				nestedClasses.add(parseTypeDeclaration((TypeDeclaration<?>) node, packDecl, typeResolver, true));
			}
		}

		javaClassDefinition.setNestedClasses(nestedClasses);

		return javaClassDefinition;
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
