/**
 *
 * {@link TOFromEntityGenerator}.java
 *
 * @author Jens Ebert
 *
 * @date 04.04.2020
 *
 */
package com.github.j2a.springbootto.generators;

import java.nio.file.Paths;

import org.mycollection.games.server.games.impl.data.GameRelease;

import com.github.j2a.core.definition.JavaClassDefinition;
import com.github.j2a.core.generation.Generator;
import com.github.j2a.core.generation.GeneratorResult;
import com.github.j2a.core.generation.J2A;
import com.github.j2a.core.utils.FullyQualifiedJavaClass;
import com.github.j2a.core.utils.JavaClassBuilder;

/**
 * {@link MapStructMapperFromEntityGenerator}
 *
 */
public class MapStructMapperFromEntityGenerator implements Generator {

	public static void main(String[] args) {
		J2A j2a = new J2A();

		// TODO use Java parser for getting Java Class Decl
		j2a.generateOutputFromClass("org.mycollection.games.server", GameRelease.class, Paths.get("."),
			new MapStructMapperFromEntityGenerator());
	}

	/**
	 * @see com.github.j2a.core.generation.Generator#canGenerateResult(com.github.j2a.core.definition.JavaClassDefinition)
	 */
	public boolean canGenerateResult(JavaClassDefinition classDefinition) {
		return false;
	}

	/**
	 * @see com.github.j2a.core.generation.Generator#generateResult(com.github.j2a.core.definition.JavaClassDefinition)
	 */
	public GeneratorResult generateResult(JavaClassDefinition classDefinition) {
		String entitySimpleName = classDefinition.getName();
		JavaClassBuilder builder = JavaClassBuilder.createInterface(new FullyQualifiedJavaClass(
			entitySimpleName + "Mapper", "org.mycollection.games.server.games.impl.facade"));

		// TODO derive TO package from entity package
		String toName = entitySimpleName + "TO";

		builder
			.withClassJavadoc(
				"A MapStruct mapper for mapping {@link " + entitySimpleName + "} entities to {@link " + toName + "}s.")
			.extending(new FullyQualifiedJavaClass("org.mycollection.games.server.utils.MGCMapper"),
				new FullyQualifiedJavaClass(toName, classDefinition.getPackageName()),
				new FullyQualifiedJavaClass(entitySimpleName, classDefinition.getPackageName()))
			.withAnnotation(new FullyQualifiedJavaClass("org.mapstruct.Mapper")).withBodyComment("Intentionally empty");

		System.out.println(builder.build());

		return new GeneratorResult(null, null);
	}

	/**
	 * @see com.github.j2a.core.generation.Generator#getDescription()
	 */
	public String getDescription() {
		return null;
	}

	/**
	 * @see com.github.j2a.core.generation.Generator#getName()
	 */
	public String getName() {
		return null;
	}
}
