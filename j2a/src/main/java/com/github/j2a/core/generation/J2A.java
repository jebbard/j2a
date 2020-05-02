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
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;

import com.github.j2a.core.config.J2AConfiguration;
import com.github.j2a.core.definition.JavaClassDefinition;
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

	public Generator getGenerator(String fullyQualifiedClassName, Path[] classpath) {
		Function<? super Path, ? extends URL> pathToUrl = cp -> {
			try {
				return cp.toUri().toURL();
			} catch (MalformedURLException e) {
				throw new RuntimeException("", e);
			}
		};
		URLClassLoader classLoader = new URLClassLoader(
			Arrays.stream(classpath).map(pathToUrl).toArray(size -> new URL[size]));

		try {
			Class<Generator> generatorClass = (Class<Generator>) classLoader.loadClass(fullyQualifiedClassName);

			return generatorClass.getConstructor().newInstance();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("", e);
		} catch (InstantiationException e) {
			throw new RuntimeException("", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("", e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("", e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("", e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("", e);
		} catch (SecurityException e) {
			throw new RuntimeException("", e);
		}
	}

	public GeneratorGroup getGeneratorGroup(String fullyQualifiedClassName, Path[] classpath) {
		return null;
	}
}
