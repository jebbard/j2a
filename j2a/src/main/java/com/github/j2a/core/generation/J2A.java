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
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;

import com.github.j2a.core.config.J2AConfiguration;
import com.github.j2a.core.definition.JavaClassDefinition;
import com.github.j2a.core.parser.JavaParserBasedClassParser;
import com.github.j2a.core.parser.ReflectionJavaClassParser;

/**
 * {@link J2A}
 *
 */
public class J2A {
	public void generateOutputFromClass(String appBasePackage, Class<?> sourceClass, Path outputBasePath,
		Generator... generators) {
		JavaClassDefinition classDefinition = new ReflectionJavaClassParser(
			new J2AConfiguration(Set.of(appBasePackage))).parse(sourceClass);

		for (Generator outputGenerator : generators) {
			GeneratorResult result = outputGenerator.generateResult(classDefinition,
				new GenerationContext(appBasePackage));

			Path targetOutputPath = outputBasePath.resolve(result.getOutputRelativeTargetPath());

			try {
				Files.writeString(targetOutputPath, result.getOutput());
			} catch (IOException e) {
				throw new RuntimeException("", e);
			}
		}
	}

	public void generateOutputFromClass(String appBasePackage, String classSource, Path outputBasePath,
		Generator... generators) {
		JavaClassDefinition classDefinition = new JavaParserBasedClassParser().parse(classSource);

		for (Generator outputGenerator : generators) {
			GeneratorResult result = outputGenerator.generateResult(classDefinition,
				new GenerationContext(appBasePackage));

			System.out.println(result.getOutput());
//			Path targetOutputPath = outputBasePath.resolve(result.getOutputRelativeTargetPath());
//
//			try {
//				Files.writeString(targetOutputPath, result.getOutput());
//			} catch (IOException e) {
//				throw new RuntimeException("", e);
//			}
		}
	}

	public List<Generator> getAllRegisteredGenerators() {
		List<Generator> generators = new ArrayList<>();

		for (Generator generator : ServiceLoader.load(Generator.class)) {
			generators.add(generator);
		}

		return generators;
	}
}
