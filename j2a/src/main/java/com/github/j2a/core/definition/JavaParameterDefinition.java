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

import com.github.j2a.core.parser.JavaClassReference;

/**
 * {@link JavaParameterDefinition} represents a Java method parameter
 * declaration
 *
 */
public class JavaParameterDefinition extends AbstractJavaElementDefinition {
	private final JavaClassReference type;

	public JavaParameterDefinition(boolean isFinal, String name, List<JavaClassReference> annotations,
		JavaClassReference type) {
		super(isFinal, name);
		this.type = type;
		setAnnotations(annotations);
	}

	public JavaClassReference getType() {
		return type;
	}

}
