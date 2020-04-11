/**
 *
 * {@link J2A}.java
 *
 * @author Jens Ebert
 *
 * @date 04.04.2020
 *
 */
package de.je.utils.j2a.generation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import de.je.utils.j2a.config.J2AConfiguration;
import de.je.utils.j2a.parser.ReflectionJavaClassParser;
import de.je.utils.j2a.parser.declaration.JavaClassDeclaration;

/**
 * {@link J2A}
 *
 */
public class J2A {
	public void generateOutputFromClass(String appBasePackage, Class<?> sourceClass, Path outputBasePath,
		OutputGenerator... generators) {
		JavaClassDeclaration classDefinition = new ReflectionJavaClassParser(
			new J2AConfiguration(Set.of(appBasePackage))).parse(sourceClass);

		for (OutputGenerator outputGenerator : generators) {
			GeneratorResult result = outputGenerator.generateOutput(classDefinition);

			Path targetOutputPath = outputBasePath.resolve(result.getOutputRelativeTargetPath());

			try {
				Files.writeString(targetOutputPath, result.getOutput());
			} catch (IOException e) {
				throw new RuntimeException("", e);
			}
		}
	}
}
