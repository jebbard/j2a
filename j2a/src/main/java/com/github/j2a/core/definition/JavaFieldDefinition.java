/**
 *
 * {@link JavaFieldDefinition}.java
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
 * {@link JavaFieldDefinition} represents a field declaration in Java.
 */
public class JavaFieldDefinition extends AbstractScopedJavaElementDefinition {
	private final boolean isVolatile;
	private final boolean isTransient;
	private final boolean isSynthetic;
	private final JavaClassReference type;

	public JavaFieldDefinition(boolean isFinal, String name, List<JavaClassReference> annotations,
		JavaElementVisibility visibility, boolean isStatic, boolean isVolatile, boolean isTransient,
		JavaClassReference type, boolean isSynthetic) {
		super(isFinal, name, visibility, isStatic);
		this.isVolatile = isVolatile;
		this.isTransient = isTransient;
		this.type = type;
		this.isSynthetic = isSynthetic;
		setAnnotations(annotations);
	}

	public JavaFieldDefinition(JavaFieldDefinition other, String adaptedName, Class<?> adaptedType) {
		this(other.isFinal(), adaptedName, other.getAnnotations(), other.getVisibility(), other.isStatic(),
			other.isVolatile, other.isTransient(),
			new JavaClassDefinition(adaptedType, false, "", null, false, null, false, "", false, false, false, false),
			other.isSynthetic());
	}

	public JavaClassReference getType() {
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
