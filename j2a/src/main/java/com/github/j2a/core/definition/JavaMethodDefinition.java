/**
 *
 * {@link JavaMethodDefinition}.java
 *
 * @author Jens Ebert
 *
 * @date 10.02.2020
 *
 */
package com.github.j2a.core.definition;

import java.util.List;

import com.github.j2a.core.parser.JavaTypeReference;

/**
 * {@link JavaMethodDefinition} represents a method declaration in Java.
 */
public class JavaMethodDefinition extends AbstractScopedJavaElementDefinition {
	private final boolean isConstructor;
	private final boolean isSynchronized;
	private final boolean isNative;
	private final boolean isAbstract;
	private final boolean isStrictFp;
	private final boolean isBridge;
	private final boolean isSynthetic;
	private final boolean isDefault;
	private final boolean isVarArgs;

	private final JavaTypeReference returnType;

	private final List<JavaParameterDefinition> parameterDeclarations;

	public JavaMethodDefinition(boolean isFinal, String name, List<JavaTypeReference> annotations,
		JavaElementVisibility visibility, boolean isStatic, boolean isConstructor, boolean isSynchronized,
		boolean isNative, boolean isAbstract, boolean isStrictFp, boolean isBridge, boolean isSynthetic,
		boolean isDefault, boolean isVarArgs, JavaTypeReference returnType,
		List<JavaParameterDefinition> parameterDeclarations) {
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

	public List<JavaParameterDefinition> getParameters() {
		return parameterDeclarations;
	}

	public JavaTypeReference getReturnType() {
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
