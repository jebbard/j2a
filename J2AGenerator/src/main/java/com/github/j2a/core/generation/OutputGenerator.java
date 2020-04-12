/**
 *
 * {@link AbstractOutputGenerator}.java
 *
 * @author Jens Ebert
 *
 * @date 04.04.2020
 *
 */
package com.github.j2a.core.generation;

import com.github.j2a.core.parser.declaration.JavaClassDeclaration;

/**
 * {@link AbstractOutputGenerator}
 *
 */
public interface OutputGenerator {
	GeneratorResult generateOutput(JavaClassDeclaration classDefinition);

	boolean needsToGenerate(JavaClassDeclaration classDefinition);
}
