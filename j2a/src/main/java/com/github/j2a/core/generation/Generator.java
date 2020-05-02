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

import com.github.j2a.core.definition.JavaClassDefinition;

/**
 * {@link AbstractOutputGenerator}
 *
 */
public interface Generator {
	boolean canGenerateResult(JavaClassDefinition classDefinition);

	GeneratorResult generateResult(JavaClassDefinition classDefinition, GenerationContext context);

	String getDescription();

	String getName();
}
