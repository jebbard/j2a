/**
 *
 * {@link AbstractScopedJavaElementDeclaration}.java
 *
 * @author Jens Ebert
 *
 * @date 10.02.2020
 *
 */
package de.je.utils.j2a.parser.declaration;

/**
 * {@link AbstractScopedJavaElementDeclaration} represents Java declarations
 * containing an explicit visibility and class or instance scope such as field
 * or method declarations.
 *
 */
public class AbstractScopedJavaElementDeclaration extends AbstractJavaElementDeclaration {

	private final JavaElementVisibility visibility;
	private final boolean isStatic;

	public AbstractScopedJavaElementDeclaration(boolean isFinal, String name, JavaElementVisibility visibility,
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
