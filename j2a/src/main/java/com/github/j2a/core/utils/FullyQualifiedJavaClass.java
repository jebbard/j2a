/**
 *
 * {@link FullyQualifiedJavaClass}.java
 *
 * @author Jens Ebert
 *
 * @date 15.04.2020
 *
 */
package com.github.j2a.core.utils;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * {@link FullyQualifiedJavaClass}
 *
 */
public class FullyQualifiedJavaClass {
	private final String className;
	private final String packageName;

	public FullyQualifiedJavaClass(String fullyQualifiedClassName) {
		if (!IdentifierHelper.isValidJavaFullyQualifiedIdentifier(fullyQualifiedClassName)) {
			throw new IllegalArgumentException("Not a valid Java fully qualified class name");
		}

		String[] packageSegments = fullyQualifiedClassName.split("\\.");

		className = packageSegments[packageSegments.length - 1];
		packageName = Arrays.stream(packageSegments).limit(packageSegments.length - 1).collect(Collectors.joining("."));
	}

	/**
	 * Creates a new {@link FullyQualifiedJavaClass}.
	 *
	 * @param className
	 * @param packageName
	 */
	public FullyQualifiedJavaClass(String className, String packageName) {
		this.className = className;
		this.packageName = packageName;
	}

	public String getClassName() {
		return className;
	}

	public String getFullyQualifiedName() {
		return packageName + "." + className;
	}

	public String getPackageName() {
		return packageName;
	}
}
