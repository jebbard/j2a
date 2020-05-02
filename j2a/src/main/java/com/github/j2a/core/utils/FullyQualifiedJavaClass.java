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

import com.github.j2a.core.parser.JavaClassReference;

/**
 * {@link FullyQualifiedJavaClass}
 *
 */
public class FullyQualifiedJavaClass implements JavaClassReference {
	private final String name;
	private final String packageName;

	public FullyQualifiedJavaClass(String fullyQualifiedClassName) {
		if (!IdentifierHelper.isValidJavaFullyQualifiedIdentifier(fullyQualifiedClassName)) {
			throw new IllegalArgumentException("Not a valid Java fully qualified class name");
		}

		if (fullyQualifiedClassName.equals(IdentifierHelper.VOID)) {
			name = IdentifierHelper.VOID;
			packageName = "java.lang";
		} else if (fullyQualifiedClassName.equals("String[]")) {
			name = "String[]";
			packageName = "java.lang";
		} else if (fullyQualifiedClassName.equals("SuppressWarnings")) {
			name = "SuppressWarnings";
			packageName = "java.lang";
		} else {
			String[] packageSegments = fullyQualifiedClassName.split("\\.");

			name = packageSegments[packageSegments.length - 1];
			packageName = Arrays.stream(packageSegments).limit(packageSegments.length - 1)
				.collect(Collectors.joining("."));
		}
	}

	/**
	 * Creates a new {@link FullyQualifiedJavaClass}.
	 *
	 * @param className
	 * @param packageName
	 */
	public FullyQualifiedJavaClass(String className, String packageName) {
		name = className;
		this.packageName = packageName;
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
}
