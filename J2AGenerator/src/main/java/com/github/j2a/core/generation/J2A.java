/**
 *
 * {@link J2A}.java
 *
 * @author Jens Ebert
 *
 * @date 04.04.2020
 *
 */
package com.github.j2a.core.generation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import com.github.j2a.core.config.J2AConfiguration;
import com.github.j2a.core.parser.ReflectionJavaClassParser;
import com.github.j2a.core.parser.declaration.JavaClassDeclaration;

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
