/**
 *
 * {@link JavaFieldDeclaration}.java
 *
 * @author Jens Ebert
 *
 * @date 10.02.2020
 *
 */
package com.github.j2a.core.parser.declaration;

import java.util.List;

/**
 * {@link JavaFieldDeclaration} represents a field declaration in Java.
 */
public class JavaFieldDeclaration extends AbstractScopedJavaElementDeclaration {
	private final boolean isVolatile;
	private final boolean isTransient;
	private final boolean isSynthetic;
	private final JavaClassDeclaration type;

	public JavaFieldDeclaration(boolean isFinal, String name, List<JavaClassDeclaration> annotations,
		JavaElementVisibility visibility, boolean isStatic, boolean isVolatile, boolean isTransient,
		JavaClassDeclaration type, boolean isSynthetic) {
		super(isFinal, name, visibility, isStatic);
		this.isVolatile = isVolatile;
		this.isTransient = isTransient;
		this.type = type;
		this.isSynthetic = isSynthetic;
		setAnnotations(annotations);
	}

	public JavaFieldDeclaration(JavaFieldDeclaration other, String adaptedName, Class<?> adaptedType) {
		this(other.isFinal(), adaptedName, other.getAnnotations(), other.getVisibility(), other.isStatic(),
			other.isVolatile, other.isTransient(),
			new JavaClassDeclaration(adaptedType, false, "", null, false, null, false, "", false, false, false, false),
			other.isSynthetic());
	}

	public JavaClassDeclaration getType() {
		return type;
	}

	public boolean isTransient() {
		return isTransient;
	}

	public boolean isVolatile() {
		return isVolatile;
	}

	boolean isSynthetic() {
		return isSynthetic;
	}
}
