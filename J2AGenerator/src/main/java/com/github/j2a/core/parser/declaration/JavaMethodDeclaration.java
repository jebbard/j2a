/**
 *
 * {@link JavaMethodDeclaration}.java
 *
 * @author Jens Ebert
 *
 * @date 10.02.2020
 *
 */
package com.github.j2a.core.parser.declaration;

import java.util.List;

/**
 * {@link JavaMethodDeclaration} represents a method declaration in Java.
 */
public class JavaMethodDeclaration extends AbstractScopedJavaElementDeclaration {
	private final boolean isConstructor;
	private final boolean isSynchronized;
	private final boolean isNative;
	private final boolean isAbstract;
	private final boolean isStrictFp;
	private final boolean isBridge;
	private final boolean isSynthetic;
	private final boolean isDefault;
	private final boolean isVarArgs;

	private final JavaClassDeclaration returnType;

	private final List<JavaParameterDeclaration> parameterDeclarations;

	public JavaMethodDeclaration(boolean isFinal, String name, List<JavaClassDeclaration> annotations,
		JavaElementVisibility visibility, boolean isStatic, boolean isConstructor, boolean isSynchronized,
		boolean isNative, boolean isAbstract, boolean isStrictFp, boolean isBridge, boolean isSynthetic,
		boolean isDefault, boolean isVarArgs, JavaClassDeclaration returnType,
		List<JavaParameterDeclaration> parameterDeclarations) {
		super(isFinal, name, visibility, isStatic);
		this.isConstructor = isConstructor;
		this.isSynchronized = isSynchronized;
		this.isNative = isNative;
		this.isAbstract = isAbstract;
		this.isStrictFp = isStrictFp;
		this.isBridge = isBridge;
		this.isSynthetic = isSynthetic;
		this.isDefault = isDefault;
		this.isVarArgs = isVarArgs;
		this.returnType = returnType;
		this.parameterDeclarations = parameterDeclarations;
		setAnnotations(annotations);
	}

	public List<JavaParameterDeclaration> getParameters() {
		return parameterDeclarations;
	}

	public JavaClassDeclaration getReturnType() {
		return returnType;
	}

	public boolean isAbstract() {
		return isAbstract;
	}

	public boolean isBridge() {
		return isBridge;
	}

	public boolean isConstructor() {
		return isConstructor;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public boolean isNative() {
		return isNative;
	}

	public boolean isStrictFp() {
		return isStrictFp;
	}

	public boolean isSynchronized() {
		return isSynchronized;
	}

	public boolean isSynthetic() {
		return isSynthetic;
	}

	public boolean isVarArgs() {
		return isVarArgs;
	}
}
