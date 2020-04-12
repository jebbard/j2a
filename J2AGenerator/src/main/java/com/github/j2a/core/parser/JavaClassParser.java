/**
 *
 * {@link JavaClassParser}.java
 *
 * @author Jens Ebert
 *
 * @date 11.02.2020
 *
 */
package com.github.j2a.core.parser;

import com.github.j2a.core.parser.declaration.JavaClassDeclaration;

/**
 * {@link JavaClassParser} parses Java classes in diverse forms and returns a
 * {@link JavaClassDeclaration} instance.
 *
 * @param <I> The type of input form the Java class is represented as
 */
public interface JavaClassParser<I> {
	JavaClassDeclaration parse(I inputClass);
}