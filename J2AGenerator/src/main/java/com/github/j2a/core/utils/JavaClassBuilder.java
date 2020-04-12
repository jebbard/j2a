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

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.github.j2a.core.parser.declaration.JavaFieldDeclaration;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
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
	public static JavaClassBuilder createClass(String packageName, String className) {
		CompilationUnit unit = new CompilationUnit(packageName);

		return new JavaClassBuilder(unit, unit.addClass(className, Keyword.PUBLIC));
	}

	public static JavaClassBuilder createInterface(String packageName, String className) {
		CompilationUnit unit = new CompilationUnit(packageName);

		return new JavaClassBuilder(unit, unit.addInterface(className, Keyword.PUBLIC));
	}

	private final List<MethodDeclaration> methods = new ArrayList<>();

	private final ClassOrInterfaceDeclaration jpDeclaration;

	private final CompilationUnit unit;

	private JavaClassBuilder(CompilationUnit unit, ClassOrInterfaceDeclaration jpDeclaration) {
		this.jpDeclaration = jpDeclaration;
		this.unit = unit;
	}

	public String build() {
		// Add property getters and setters after all fields only
		methods.forEach(m -> jpDeclaration.addMember(m));

		// Clean up imports: Any import that is referring to the same package as the compilation unit is unnecessary and
		// removed
		NodeList<ImportDeclaration> imports = unit.getImports();

		List<ImportDeclaration> importsToRemove = new ArrayList<>();

		for (ImportDeclaration importDec : imports) {
			String nameAsString = importDec.getNameAsString();

			String removedOwnPackage = nameAsString
				.replaceAll(Pattern.quote(unit.getPackageDeclaration().get().getNameAsString() + "."), "");
			if (removedOwnPackage.length() != nameAsString.length() && !removedOwnPackage.contains(".")) {
				importsToRemove.add(importDec);
			}
		}

		importsToRemove.forEach(i -> unit.remove(i));

		return unit.toString();
	}

	public JavaClassBuilder extending(Class<?> extendedClass, String... extendedClassTypeArgs) {
		jpDeclaration.addExtendedType(extendedClass);

		if (extendedClassTypeArgs.length > 0) {
			List<Type> typeArguments = new ArrayList<>();

			for (String extendedClassTypeArg : extendedClassTypeArgs) {
				typeArguments.add(new JavaParser().parseClassOrInterfaceType(extendedClassTypeArg).getResult().get());
			}
			jpDeclaration.getExtendedTypes(0).setTypeArguments(new NodeList<>(typeArguments));
		}

		return this;
	}

	public JavaClassBuilder withAnnotation(Class<? extends Annotation> annotation) {
		jpDeclaration.addAnnotation(annotation);

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

	public JavaClassBuilder withProperty(JavaFieldDeclaration fieldForProperty) {
		jpDeclaration.addField(fieldForProperty.getType().getSourceClass(), fieldForProperty.getName(),
			fieldForProperty.getVisibility().toJavaParserKeyword());
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

	private MethodDeclaration createGetter(JavaFieldDeclaration fieldDefinition) {

		MethodDeclaration getter = new MethodDeclaration();
		getter.setName(new SimpleName("get" + CaseHelper.toCapitalCase(fieldDefinition.getName())));
		getter.setModifiers(Keyword.PUBLIC);
		getter.setType(fieldDefinition.getType().getSourceClass());
		getter.setBody(new BlockStmt(new NodeList<>(new ReturnStmt(fieldDefinition.getName()))));

		return getter;
	}

	private MethodDeclaration createSetter(JavaFieldDeclaration fieldDefinition) {

		MethodDeclaration setter = new MethodDeclaration();
		setter.setName(new SimpleName("set" + CaseHelper.toCapitalCase(fieldDefinition.getName())));
		setter.setModifiers(Keyword.PUBLIC);
		setter.addParameter(fieldDefinition.getType().getSourceClass(), fieldDefinition.getName());
		setter.setType(new VoidType());
		setter.setBody(new BlockStmt(new NodeList<>(
			new ExpressionStmt(new AssignExpr(new FieldAccessExpr(new ThisExpr(), fieldDefinition.getName()),
				new NameExpr(fieldDefinition.getName()), Operator.ASSIGN)))));

		return setter;
	}
}
