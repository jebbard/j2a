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
import com.github.j2a.core.utils.IdentifierHelper;

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
			.map(cut -> IdentifierHelper.getClassNameFromFullyQualified(getRawType(cut))).collect(Collectors.toSet());
		this.compilationUnitPackage = compilationUnitPackage;
	}

	public FullyQualifiedJavaTypeReference resolveType(String type, Set<String> typeParametersInScope) {
		String typeExpression = type;

		if (typeExpression.endsWith(CompilationUnitTypeResolver.VARARGS_ELLIPSIS)) {
			typeExpression = typeExpression.replace(CompilationUnitTypeResolver.VARARGS_ELLIPSIS,
				IdentifierHelper.EMPTY_ARRAY_BRACKETS);
		}

		List<JavaTypeReference> typeArgumentRefs = getTypeArguments(typeExpression).stream()
			.map(ta -> resolveType(ta, typeParametersInScope)).collect(Collectors.toList());

		if (IdentifierHelper.isTypeReferenceFullyQualified(typeExpression)) {
			return new FullyQualifiedJavaTypeReference(typeExpression, typeArgumentRefs);
		}

		String typeFromImportList = getFromImportList(typeExpression);

		if (typeFromImportList != null) {
			return new FullyQualifiedJavaTypeReference(typeFromImportList, typeArgumentRefs);
		}

		if (isTypeDeclaredInCompilationUnit(typeExpression)) {
			return new FullyQualifiedJavaTypeReference(typeExpression, compilationUnitPackage, typeArgumentRefs);
		}

		if (typeParametersInScope.contains(getElementType(typeExpression))) {
			return new FullyQualifiedJavaTypeReference(typeExpression, compilationUnitPackage, typeArgumentRefs);
		}

		if (IdentifierHelper.isJavaLangClass(typeExpression)) {
			return new FullyQualifiedJavaTypeReference(typeExpression, IdentifierHelper.JAVA_LANG, typeArgumentRefs);
		}

		return new FullyQualifiedJavaTypeReference(typeExpression, compilationUnitPackage, typeArgumentRefs);

	}

	private String getArrayType(String typeReference) {
		if (typeReference.endsWith(IdentifierHelper.EMPTY_ARRAY_BRACKETS)) {
			return typeReference;
		}

		return typeReference + IdentifierHelper.EMPTY_ARRAY_BRACKETS;
	}

	private String getElementType(String typeReference) {
		if (typeReference.endsWith(IdentifierHelper.EMPTY_ARRAY_BRACKETS)) {
			return typeReference.substring(0, typeReference.indexOf(IdentifierHelper.ARRAY_START_BRACKET));
		}

		return typeReference;
	}

	private String getFromImportList(String typeName) {
		for (String importDeclaration : importDeclarations) {
			if (importDeclaration.endsWith(IdentifierHelper.PACKAGE_SEGMENT_SEPARATOR + typeName)) {
				return importDeclaration;
			}
		}

		return null;
	}

	private String getRawType(String typeReference) {
		if (typeReference.contains(Character.toString(IdentifierHelper.TYPE_ARGUMENT_START_BRACKET))) {
			return typeReference.substring(0, typeReference.indexOf(IdentifierHelper.TYPE_ARGUMENT_START_BRACKET));
		}

		return typeReference;
	}

	private List<String> getTypeArguments(String typeReference) {
		if (typeReference.contains(Character.toString(IdentifierHelper.TYPE_ARGUMENT_START_BRACKET))) {
			String typeArgs = typeReference.substring(
				typeReference.indexOf(IdentifierHelper.TYPE_ARGUMENT_START_BRACKET) + 1,
				typeReference.indexOf(IdentifierHelper.TYPE_ARGUMENT_END_BRACKET));
			return Arrays.stream(typeArgs.split(",")).map(ta -> ta.trim()).collect(Collectors.toList());
		}

		return new ArrayList<>();
	}

	private boolean isTypeDeclaredInCompilationUnit(String type) {
		for (String typeDeclaration : unqualifiedRawCompilationUnitTypes) {
			if (typeDeclaration.equals(getRawType(getElementType(type)))) {
				return true;
			}
		}

		return false;
	}
}
