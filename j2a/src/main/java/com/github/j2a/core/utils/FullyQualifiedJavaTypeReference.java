/**
 *
 * {@link FullyQualifiedJavaTypeReference}.java
 *
 * @author Jens Ebert
 *
 * @date 15.04.2020
 *
 */
package com.github.j2a.core.utils;

import java.util.ArrayList;
import java.util.List;

import com.github.j2a.core.parser.JavaTypeReference;

/**
 * {@link FullyQualifiedJavaTypeReference}
 *
 */
public class FullyQualifiedJavaTypeReference implements JavaTypeReference {
	private final String name;
	private final String packageName;
	private final List<JavaTypeReference> typeArguments = new ArrayList<>();

	public FullyQualifiedJavaTypeReference(Class<?> klass) {
		this(klass.getSimpleName(), klass.getPackageName(), new ArrayList<>());
	}

	/**
	 * Creates a new {@link FullyQualifiedJavaTypeReference} based of a fully
	 * qualified name of the class, without type arguments.
	 *
	 * @param fullyQualifiedClassName
	 */
	public FullyQualifiedJavaTypeReference(String fullyQualifiedClassName) {
		this(fullyQualifiedClassName, new ArrayList<>());
	}

	/**
	 * Creates a new {@link FullyQualifiedJavaTypeReference} including type
	 * arguments.
	 *
	 * @param fullyQualifiedClassName
	 * @param typeArguments
	 */
	public FullyQualifiedJavaTypeReference(String fullyQualifiedClassName, List<JavaTypeReference> typeArguments) {
		if (!TypeReferenceHelper.isValidJavaTypeReference(fullyQualifiedClassName)) {
			throw new IllegalArgumentException("Not a valid Java fully qualified class name");
		}

		name = TypeReferenceHelper
			.getRawType(TypeReferenceHelper.getClassNameFromFullyQualified(fullyQualifiedClassName));

		if (TypeReferenceHelper.isPrimitive(fullyQualifiedClassName)) {
			packageName = TypeReferenceHelper.JAVA_LANG;
		} else {
			packageName = TypeReferenceHelper.getPackageNameFromFullyQualified(fullyQualifiedClassName);
		}

		typeArguments.addAll(typeArguments);
	}

	/**
	 * Creates a new {@link FullyQualifiedJavaTypeReference} without type arguments.
	 *
	 * @param className
	 * @param packageName
	 */
	public FullyQualifiedJavaTypeReference(String className, String packageName) {
		this(className, packageName, new ArrayList<>());
	}

	/**
	 * Creates a new {@link FullyQualifiedJavaTypeReference} including type
	 * arguments.
	 *
	 * @param className
	 * @param packageName
	 * @param typeArguments
	 */
	public FullyQualifiedJavaTypeReference(String className, String packageName,
		List<JavaTypeReference> typeArguments) {
		name = TypeReferenceHelper.getRawType(className);
		this.packageName = packageName;
		this.typeArguments.addAll(typeArguments);
	}

	@Override
	public String getFullyQualifiedName() {
		return packageName + "." + name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getPackageName() {
		return packageName;
	}

	/**
	 * @see com.github.j2a.core.parser.JavaTypeReference#getTypeArguments()
	 */
	@Override
	public List<JavaTypeReference> getTypeArguments() {
		return typeArguments;
	}
}
