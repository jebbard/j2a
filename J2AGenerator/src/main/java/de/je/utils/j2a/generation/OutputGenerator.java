/**
 *
 * {@link AbstractOutputGenerator}.java
 *
 * @author Jens Ebert
 *
 * @date 04.04.2020
 *
 */
package de.je.utils.j2a.generation;

import de.je.utils.j2a.parser.declaration.JavaClassDeclaration;

/**
 * {@link AbstractOutputGenerator}
 *
 */
public interface OutputGenerator {
	GeneratorResult generateOutput(JavaClassDeclaration classDefinition);

	boolean needsToGenerate(JavaClassDeclaration classDefinition);
}
