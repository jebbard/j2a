/**
 *
 * {@link JavaParameterDefinition}.java
 *
 * @author Jens Ebert
 *
 * @date 10.02.2020
 *
 */
package com.github.j2a.core.definition;

import java.util.List;

/**
 * {@link JavaParameterDefinition} represents a Java method parameter
 * declaration
 *
 */
public class JavaParameterDefinition extends AbstractJavaElementDefinition {
	private final JavaClassDefinition type;

	public JavaParameterDefinition(boolean isFinal, String name, List<JavaClassDefinition> annotations,
		JavaClassDefinition type) {
		super(isFinal, name);
		this.type = type;
		setAnnotations(annotations);
	}

	public JavaClassDefinition getType() {
		return type;
	}

}
