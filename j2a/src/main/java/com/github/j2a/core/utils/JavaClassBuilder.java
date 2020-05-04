/**
 *
 * {@link JavaClassBuilder}.java
 *
 * @author Jens Ebert
 *
 * @date 05.04.2020
 *
 */
package com.github.j2a.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.github.j2a.core.definition.JavaFieldDefinition;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier.Keyword;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.AssignExpr.Operator;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.expr.ThisExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.VoidType;

/**
 * {@link JavaClassBuilder}
 *
 */
public class JavaClassBuilder {
	public static JavaClassBuilder createClass(FullyQualifiedJavaTypeReference fullyQualifiedJavaClass) {
		CompilationUnit unit = new CompilationUnit(fullyQualifiedJavaClass.getPackageName());

		return new JavaClassBuilder(unit, unit.addClass(fullyQualifiedJavaClass.getName(), Keyword.PUBLIC));
	}

	public static JavaClassBuilder createInterface(FullyQualifiedJavaTypeReference fullyQualifiedJavaClass) {
		CompilationUnit unit = new CompilationUnit(fullyQualifiedJavaClass.getPackageName());

		return new JavaClassBuilder(unit, unit.addInterface(fullyQualifiedJavaClass.getName(), Keyword.PUBLIC));
	}

	private final List<MethodDeclaration> methods = new ArrayList<>();

	private final List<String> importStatements = new ArrayList<>();

	private final ClassOrInterfaceDeclaration jpDeclaration;

	private final CompilationUnit unit;

	private JavaClassBuilder(CompilationUnit unit, ClassOrInterfaceDeclaration jpDeclaration) {
		this.jpDeclaration = jpDeclaration;
		this.unit = unit;
	}

	public String build() {
		// Add property getters and setters after all fields only
		methods.forEach(m -> jpDeclaration.addMember(m));

		// Add import statements
		for (String importStatement : importStatements) {
			String removedOwnPackage = importStatement
				.replaceAll(Pattern.quote(unit.getPackageDeclaration().get().getNameAsString() + "."), "");
			if (removedOwnPackage.length() == importStatement.length() || removedOwnPackage.contains(".")) {
				unit.addImport(importStatement);
			}
		}

		return unit.toString();
	}

	public JavaClassBuilder extending(FullyQualifiedJavaTypeReference extendedClass,
		FullyQualifiedJavaTypeReference... extendedClassTypeArgs) {
		jpDeclaration.addExtendedType(extendedClass.getName());
		importStatements.add(extendedClass.getFullyQualifiedName());

		if (extendedClassTypeArgs.length > 0) {
			List<Type> typeArguments = new ArrayList<>();

			for (FullyQualifiedJavaTypeReference extendedClassTypeArg : extendedClassTypeArgs) {
				typeArguments
					.add(new JavaParser().parseClassOrInterfaceType(extendedClassTypeArg.getName()).getResult().get());
				importStatements.add(extendedClassTypeArg.getFullyQualifiedName());
			}
			jpDeclaration.getExtendedTypes(0).setTypeArguments(new NodeList<>(typeArguments));
		}

		return this;
	}

	public JavaClassBuilder withAnnotation(FullyQualifiedJavaTypeReference annotation) {
		jpDeclaration.addAnnotation(annotation.getName());
		importStatements.add(annotation.getFullyQualifiedName());

		return this;
	}

	public JavaClassBuilder withBodyComment(String comment) {
		jpDeclaration.addOrphanComment(new LineComment(comment));

		return this;
	}

	public JavaClassBuilder withClassJavadoc(String javadoc) {
		jpDeclaration.setJavadocComment(javadoc);

		return this;
	}

	public JavaClassBuilder withProperty(JavaFieldDefinition fieldForProperty) {
		jpDeclaration.addField(fieldForProperty.getType().getName(), fieldForProperty.getName(),
			fieldForProperty.getVisibility().toJavaParserKeyword());
		importStatements.add(fieldForProperty.getType().getFullyQualifiedName());
		methods.add(createGetter(fieldForProperty));
		methods.add(createSetter(fieldForProperty));

		return this;
	}

	public JavaClassBuilder withSingleParamPublicMethod(String returnType, String methodName, String singleParamType,
		String singleParamName, NodeList<Statement> statements) {
		MethodDeclaration setter = new MethodDeclaration();
		setter.setName(new SimpleName(methodName));
		setter.setModifiers(Keyword.PUBLIC);
		setter.addParameter(singleParamType, singleParamName);
		setter.setType(returnType);

		if (statements != null) {
			setter.setBody(new BlockStmt(statements));
		}

		jpDeclaration.addMember(setter);

		return this;
	}

	private MethodDeclaration createGetter(JavaFieldDefinition fieldDefinition) {

		MethodDeclaration getter = new MethodDeclaration();
		getter.setName(new SimpleName("get" + CaseHelper.toCapitalCase(fieldDefinition.getName())));
		getter.setModifiers(Keyword.PUBLIC);
		getter.setType(fieldDefinition.getType().getName());
		importStatements.add(fieldDefinition.getType().getFullyQualifiedName());
		getter.setBody(new BlockStmt(new NodeList<>(new ReturnStmt(fieldDefinition.getName()))));

		return getter;
	}

	private MethodDeclaration createSetter(JavaFieldDefinition fieldDefinition) {

		MethodDeclaration setter = new MethodDeclaration();
		setter.setName(new SimpleName("set" + CaseHelper.toCapitalCase(fieldDefinition.getName())));
		setter.setModifiers(Keyword.PUBLIC);
		setter.addParameter(fieldDefinition.getType().getName(), fieldDefinition.getName());
		importStatements.add(fieldDefinition.getType().getFullyQualifiedName());
		setter.setType(new VoidType());
		setter.setBody(new BlockStmt(new NodeList<>(
			new ExpressionStmt(new AssignExpr(new FieldAccessExpr(new ThisExpr(), fieldDefinition.getName()),
				new NameExpr(fieldDefinition.getName()), Operator.ASSIGN)))));

		return setter;
	}
}
