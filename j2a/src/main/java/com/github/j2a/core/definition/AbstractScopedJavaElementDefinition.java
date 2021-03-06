/**
 *
 * {@link AbstractScopedJavaElementDefinition}.java
 *
 * @author Jens Ebert
 *
 * @date 10.02.2020
 *
 */
package com.github.j2a.core.definition;

/**
 * {@link AbstractScopedJavaElementDefinition} represents Java declarations
 * containing an explicit visibility and class or instance scope such as field
 * or method declarations.
 *
 */
public class AbstractScopedJavaElementDefinition extends AbstractJavaElementDefinition {

	private final JavaElementVisibility visibility;
	private final boolean isStatic;

	public AbstractScopedJavaElementDefinition(boolean isFinal, String name, JavaElementVisibility visibility,
		boolean isStatic) {
		super(isFinal, name);
		this.visibility = visibility;
		this.isStatic = isStatic;
	}

	public JavaElementVisibility getVisibility() {
		return visibility;
	}

	public boolean isStatic() {
		return isStatic;
	}
}
