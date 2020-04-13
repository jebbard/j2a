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
	GeneratorResult generateResult(JavaClassDefinition classDefinition);

	String getDescription();

	String getName();

	boolean canGenerateResult(JavaClassDefinition classDefinition);
}
