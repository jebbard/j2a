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

import com.github.j2a.core.definition.JavaClassDefinition;
import com.github.j2a.core.generation.GenerationContext;
import com.github.j2a.core.generation.Generator;
import com.github.j2a.core.generation.GeneratorResult;
import com.github.j2a.core.generation.J2A;
import com.github.j2a.core.utils.FullyQualifiedJavaTypeReference;
import com.github.j2a.core.utils.JavaClassBuilder;

/**
 * {@link MapStructMapperFromEntityGenerator}
 *
 */
public class MapStructMapperFromEntityGenerator implements Generator {

	public static void main(String[] args) {
		J2A j2a = new J2A();

		// TODO use Java parser for getting Java Class Decl
		j2a.generateOutputFromClass("org.mycollection.games.server", ClassText.GAME_RELEASE, Paths.get("."),
			new MapStructMapperFromEntityGenerator());
	}

	/**
	 * @see com.github.j2a.core.generation.Generator#canGenerateResult(com.github.j2a.core.definition.JavaClassDefinition)
	 */
	@Override
	public boolean canGenerateResult(JavaClassDefinition classDefinition) {
		return false;
	}

	/**
	 * @see com.github.j2a.core.generation.Generator#generateResult(com.github.j2a.core.definition.JavaClassDefinition,
	 *      com.github.j2a.core.generation.GenerationContext)
	 */
	@Override
	public GeneratorResult generateResult(JavaClassDefinition classDefinition, GenerationContext context) {
		String entitySimpleName = classDefinition.getName();
		JavaClassBuilder builder = JavaClassBuilder.createInterface(new FullyQualifiedJavaTypeReference(
			entitySimpleName + "Mapper", "org.mycollection.games.server.games.impl.facade"));

		// TODO derive TO package from entity package
		String toName = entitySimpleName + "TO";

		builder
			.withClassJavadoc(
				"A MapStruct mapper for mapping {@link " + entitySimpleName + "} entities to {@link " + toName + "}s.")
			.extending(new FullyQualifiedJavaTypeReference("org.mycollection.games.server.utils.MGCMapper"),
				new FullyQualifiedJavaTypeReference(toName, classDefinition.getPackageName()),
				new FullyQualifiedJavaTypeReference(entitySimpleName, classDefinition.getPackageName()))
			.withAnnotation(new FullyQualifiedJavaTypeReference("org.mapstruct.Mapper"))
			.withBodyComment("Intentionally empty");

		System.out.println(builder.build());

		return new GeneratorResult(null, null);
	}

	/**
	 * @see com.github.j2a.core.generation.Generator#getDescription()
	 */
	@Override
	public String getDescription() {
		return null;
	}

	/**
	 * @see com.github.j2a.core.generation.Generator#getName()
	 */
	@Override
	public String getName() {
		return "Generate MapStruct Mapper from JPA Entity";
	}
}
