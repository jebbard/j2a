/**
 *
 * {@link JavaClassDeclaration}.java
 *
 * @author Jens Ebert
 *
 * @date 10.02.2020
 *
 */
package com.github.j2a.core.parser.declaration;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link JavaClassDeclaration} represents a Java class, interface, enum or annotation declaration.
 */
public class JavaClassDeclaration extends AbstractScopedJavaElementDeclaration {
	private final Class<?> sourceClass;
	private final JavaClassType classType;
	private final boolean isInnerClass;
	private final String packageName;
	private final boolean isAbstract;
	private final boolean isStrictFp;
	private final boolean isApplicationClass;
	private final boolean isPrimitive;

	private JavaClassDeclaration baseClass;
	private List<JavaClassDeclaration> implementedInterfaces = new ArrayList<>();
	private List<JavaMethodDeclaration> methods = new ArrayList<>();
	private List<JavaFieldDeclaration> fields = new ArrayList<>();
	private List<JavaClassDeclaration> nestedClasses = new ArrayList<>();

	public JavaClassDeclaration(Class<?> sourceClass, boolean isFinal, String name, JavaElementVisibility visibility,
		boolean isStatic, JavaClassType classType, boolean isInnerClass, String packageName, boolean isAbstract,
		boolean isStrictFp, boolean isApplicationClass, boolean isPrimitive) {
		super(isFinal, name, visibility, isStatic);
		this.sourceClass = sourceClass;
		this.classType = classType;
		this.isInnerClass = isInnerClass;
		this.packageName = packageName;
		this.isAbstract = isAbstract;
		this.isStrictFp = isStrictFp;
		this.isApplicationClass = isApplicationClass;
		this.isPrimitive = isPrimitive;
	}

	public JavaClassDeclaration getBaseClass() {
		return baseClass;
	}

	public JavaClassDeclaration getBaseClassOrInterface() {
		return baseClass;
	}

	public JavaClassType getClassType() {
		return classType;
	}

	public List<JavaFieldDeclaration> getFields() {
		return fields;
	}

	public List<JavaClassDeclaration> getImplementedInterfaces() {
		return implementedInterfaces;
	}

	public List<JavaMethodDeclaration> getMethods() {
		return methods;
	}

	public List<JavaClassDeclaration> getNestedClasses() {
		return nestedClasses;
	}

	public String getPackageName() {
		return packageName;
	}

	public Class<?> getSourceClass() {
		return sourceClass;
	}

	public boolean isAbstract() {
		return isAbstract;
	}

	public boolean isApplicationClass() {
		return isApplicationClass;
	}

	public boolean isInnerClass() {
		return isInnerClass;
	}

	public boolean isPrimitive() {
		return isPrimitive;
	}

	public boolean isStrictFp() {
		return isStrictFp;
	}

	public void setBaseClass(JavaClassDeclaration baseClass) {
		this.baseClass = baseClass;
	}

	public void setFields(List<JavaFieldDeclaration> fields) {
		this.fields = fields;
	}

	public void setImplementedInterfaces(List<JavaClassDeclaration> implementedInterfaces) {
		this.implementedInterfaces = implementedInterfaces;
	}

	public void setMethods(List<JavaMethodDeclaration> methods) {
		this.methods = methods;
	}

	public void setNestedClasses(List<JavaClassDeclaration> nestedClasses) {
		this.nestedClasses = nestedClasses;
	}

}
