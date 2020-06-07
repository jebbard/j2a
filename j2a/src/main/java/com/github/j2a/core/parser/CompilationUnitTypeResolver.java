/**
 *
 * {@link CompilationUnitTypeResolver}.java
 *
 * @author Jens Ebert
 *
 * @date 02.05.2020
 *
 */
package com.github.j2a.core.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.github.j2a.core.utils.FullyQualifiedJavaTypeReference;
import com.github.j2a.core.utils.TypeReferenceHelper;

/**
 * {@link CompilationUnitTypeResolver} resolves Java Parser types into
 * {@link FullyQualifiedJavaTypeReference}s.
 */
public class CompilationUnitTypeResolver {
	private static final String VARARGS_ELLIPSIS = "...";
	private final Set<String> importDeclarations;
	private final Set<String> unqualifiedRawCompilationUnitTypes;

	private final String compilationUnitPackage;

	/**
	 * Creates a new {@link CompilationUnitTypeResolver}.
	 *
	 * @param importDeclarations
	 * @param compilationUnitTypes
	 * @param compilationUnitPackage
	 */
	public CompilationUnitTypeResolver(Set<String> importDeclarations, Set<String> compilationUnitTypes,
		String compilationUnitPackage) {
		this.importDeclarations = importDeclarations;
		unqualifiedRawCompilationUnitTypes = compilationUnitTypes.stream()
			.map(cut -> TypeReferenceHelper.getClassNameFromFullyQualified(TypeReferenceHelper.getRawType(cut)))
			.collect(Collectors.toSet());
		this.compilationUnitPackage = compilationUnitPackage;
	}

	public FullyQualifiedJavaTypeReference resolveType(String type, Set<String> typeParametersInScope) {
		String typeExpression = type;

		if (typeExpression.endsWith(CompilationUnitTypeResolver.VARARGS_ELLIPSIS)) {
			typeExpression = typeExpression.replace(CompilationUnitTypeResolver.VARARGS_ELLIPSIS,
				TypeReferenceHelper.EMPTY_ARRAY_BRACKETS);
		}

		List<JavaTypeReference> typeArgumentRefs = getTypeArguments(typeExpression).stream()
			.map(ta -> resolveType(ta, typeParametersInScope)).collect(Collectors.toList());

		if (TypeReferenceHelper.isTypeReferenceFullyQualified(typeExpression)) {
			return new FullyQualifiedJavaTypeReference(typeExpression, typeArgumentRefs);
		}

		if (TypeReferenceHelper.isPrimitive(TypeReferenceHelper.getElementType(typeExpression))) {
			return new FullyQualifiedJavaTypeReference(typeExpression, TypeReferenceHelper.JAVA_LANG, typeArgumentRefs,
				true);
		}

		String typeFromImportList = getFromImportList(
			TypeReferenceHelper.getRawType(TypeReferenceHelper.getElementType(typeExpression)));

		if (typeFromImportList != null) {
			return new FullyQualifiedJavaTypeReference(typeFromImportList, typeArgumentRefs);
		}

		if (isTypeDeclaredInCompilationUnit(typeExpression)) {
			return new FullyQualifiedJavaTypeReference(typeExpression, compilationUnitPackage, typeArgumentRefs, false);
		}

		if (typeParametersInScope.contains(TypeReferenceHelper.getElementType(typeExpression))) {
			return new FullyQualifiedJavaTypeReference(typeExpression, compilationUnitPackage, typeArgumentRefs, false);
		}

		if (TypeReferenceHelper.isJavaLangClass(typeExpression)
			|| TypeReferenceHelper.isJavaLangClass(TypeReferenceHelper.getElementType(typeExpression))) {
			return new FullyQualifiedJavaTypeReference(typeExpression, TypeReferenceHelper.JAVA_LANG, typeArgumentRefs,
				false);
		}

		return new FullyQualifiedJavaTypeReference(typeExpression, compilationUnitPackage, typeArgumentRefs, false);

	}

	private String getFromImportList(String typeName) {
		for (String importDeclaration : importDeclarations) {
			if (importDeclaration.endsWith(TypeReferenceHelper.PACKAGE_SEGMENT_SEPARATOR + typeName)) {
				return importDeclaration;
			}
		}

		return null;
	}

	private List<String> getTypeArguments(String typeReference) {
		if (typeReference.contains(Character.toString(TypeReferenceHelper.TYPE_ARGUMENT_START_BRACKET))) {
			String typeArgs = typeReference.substring(
				typeReference.indexOf(TypeReferenceHelper.TYPE_ARGUMENT_START_BRACKET) + 1,
				typeReference.indexOf(TypeReferenceHelper.TYPE_ARGUMENT_END_BRACKET));
			return Arrays.stream(typeArgs.split(",")).map(ta -> ta.trim()).collect(Collectors.toList());
		}

		return new ArrayList<>();
	}

	private boolean isTypeDeclaredInCompilationUnit(String type) {
		for (String typeDeclaration : unqualifiedRawCompilationUnitTypes) {
			if (typeDeclaration.equals(TypeReferenceHelper.getRawType(TypeReferenceHelper.getElementType(type)))) {
				return true;
			}
		}

		return false;
	}
}
