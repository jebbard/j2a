/**
 *
 * {@link JavaTypeReference}.java
 *
 * @author Jens Ebert
 *
 * @date 30.04.2020
 *
 */
package com.github.j2a.core.parser;

import java.util.List;

/**
 * {@link JavaTypeReference}
 *
 */
public interface JavaTypeReference {
	String getFullyQualifiedName();

	String getName();

	String getPackageName();

	List<JavaTypeReference> getTypeArguments();
}
