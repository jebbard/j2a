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

import com.github.j2a.core.utils.FullyQualifiedJavaClass;
import com.github.j2a.core.utils.IdentifierHelper;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.type.Type;

/**
 * {@link CompilationUnitTypeResolver} resolves Java Parser types into
 * {@link FullyQualifiedJavaClass}s.
 */
public class CompilationUnitTypeResolver {
	private final NodeList<ImportDeclaration> importDeclarations;
	private final NodeList<TypeDeclaration<?>> compilationUnitTypes;

	public CompilationUnitTypeResolver(CompilationUnit compilationUnit) {
		importDeclarations = compilationUnit.getImports();
		compilationUnitTypes = compilationUnit.getTypes();
	}

	public FullyQualifiedJavaClass resolveType(Type type, boolean isVarArgsParam) {
		String typeAsString = type.asString();

		if (IdentifierHelper.isValidJavaFullyQualifiedIdentifier(typeAsString)) {
			return new FullyQualifiedJavaClass(typeAsString);
		}

		// TODO:
		/*
		 * 2) Is in import list 3) Is Type in CU 4) Is type param in element's context?
		 * 5) Is in java.lang?
		 */

		return new FullyQualifiedJavaClass(isVarArgsParam ? typeAsString + "[]" : typeAsString);
	}
}
