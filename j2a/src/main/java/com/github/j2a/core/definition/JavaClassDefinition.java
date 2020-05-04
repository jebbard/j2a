/**
 *
 * {@link JavaClassDefinition}.java
 *
 * @author Jens Ebert
 *
 * @date 10.02.2020
 *
 */
package com.github.j2a.core.definition;

import java.util.ArrayList;
import java.util.List;

import com.github.j2a.core.parser.JavaTypeReference;

/**
 * {@link JavaClassDefinition} represents a Java class, interface, enum or
 * annotation declaration.
 */
public class JavaClassDefinition extends AbstractScopedJavaElementDefinition implements JavaTypeReference {
	private final Class<?> sourceClass;
	private final JavaClassType classType;
	private final boolean isInnerClass;
	private final String packageName;
	private final boolean isAbstract;
	private final boolean isStrictFp;
	private final boolean isApplicationClass;
	private final boolean isPrimitive;

	private JavaTypeReference baseClassOrInterface;

	private List<JavaTypeReference> implementedInterfaces = new ArrayList<>();
	private List<JavaTypeReference> typeArguments = new ArrayList<>();
	private List<JavaMethodDefinition> methods = new ArrayList<>();
	private List<JavaFieldDefinition> fields = new ArrayList<>();
	private List<JavaClassDefinition> nestedClasses = new ArrayList<>();

	public JavaClassDefinition(Class<?> sourceClass, boolean isFinal, String name, JavaElementVisibility visibility,
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

	public JavaTypeReference getBaseClassOrInterface() {
		return baseClassOrInterface;
	}

	public JavaClassType getClassType() {
		return classType;
	}

	public List<JavaFieldDefinition> getFields() {
		return fields;
	}

	@Override
	public String getFullyQualifiedName() {
		return packageName + "." + getName();
	}

	public List<JavaTypeReference> getImplementedInterfaces() {
		return implementedInterfaces;
	}

	public List<JavaMethodDefinition> getMethods() {
		return methods;
	}

	public List<JavaClassDefinition> getNestedClasses() {
		return nestedClasses;
	}

	@Override
	public String getPackageName() {
		return packageName;
	}

	public Class<?> getSourceClass() {
		return sourceClass;
	}

	/**
	 * @see com.github.j2a.core.parser.JavaTypeReference#getTypeArguments()
	 */
	@Override
	public List<JavaTypeReference> getTypeArguments() {
		return typeArguments;
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

	public void setBaseClassOrInterface(JavaTypeReference baseClass) {
		baseClassOrInterface = baseClass;
	}

	public void setFields(List<JavaFieldDefinition> fields) {
		this.fields = fields;
	}

	public void setImplementedInterfaces(List<JavaTypeReference> implementedInterfaces) {
		this.implementedInterfaces = implementedInterfaces;
	}

	public void setMethods(List<JavaMethodDefinition> methods) {
		this.methods = methods;
	}

	public void setNestedClasses(List<JavaClassDefinition> nestedClasses) {
		this.nestedClasses = nestedClasses;
	}

}
