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

import com.github.j2a.core.parser.JavaTypeReference;

/**
 * {@link JavaFieldDefinition} represents a field declaration in Java.
 */
public class JavaFieldDefinition extends AbstractScopedJavaElementDefinition {
	private final boolean isVolatile;
	private final boolean isTransient;
	private final boolean isSynthetic;
	private final JavaTypeReference type;

	public JavaFieldDefinition(boolean isFinal, String name, List<JavaTypeReference> annotations,
		JavaElementVisibility visibility, boolean isStatic, boolean isVolatile, boolean isTransient,
		JavaTypeReference type, boolean isSynthetic) {
		super(isFinal, name, visibility, isStatic);
		this.isVolatile = isVolatile;
		this.isTransient = isTransient;
		this.type = type;
		this.isSynthetic = isSynthetic;
		setAnnotations(annotations);
	}

	public JavaFieldDefinition(JavaFieldDefinition other, String adaptedName, Class<?> adaptedType,
		List<JavaTypeReference> adaptedAnnotations) {
		this(other.isFinal(), adaptedName, adaptedAnnotations, other.getVisibility(), other.isStatic(),
			other.isVolatile, other.isTransient(), new JavaClassDefinition(null, false, adaptedType.getSimpleName(),
				null, false, null, false, adaptedType.getPackageName(), false, false, false, false),
			other.isSynthetic());
	}

	public JavaTypeReference getType() {
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
