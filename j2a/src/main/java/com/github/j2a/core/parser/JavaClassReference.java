/**
 *
 * {@link JavaClassReference}.java
 *
 * @author Jens Ebert
 *
 * @date 30.04.2020
 *
 */
package com.github.j2a.core.parser;

/**
 * {@link JavaClassReference}
 *
 */
public interface JavaClassReference {
	String getFullyQualifiedName();

	String getName();

	String getPackageName();
}
