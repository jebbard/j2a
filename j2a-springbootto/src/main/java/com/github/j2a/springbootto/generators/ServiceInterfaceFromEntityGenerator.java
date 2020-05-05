/**
 *
 * {@link ServiceInterfaceFromEntityGenerator}.java
 *
 * @author Jens Ebert
 *
 * @date 05.04.2020
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
 * {@link ServiceInterfaceFromEntityGenerator}
 *
 */
public class ServiceInterfaceFromEntityGenerator implements Generator {

	public static void main(String[] args) {
		J2A j2a = new J2A();

		j2a.generateOutputFromClass("org.mycollection.games.server", ClassText.GAME_RELEASE, Paths.get("."),
			new ServiceInterfaceFromEntityGenerator());
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
			entitySimpleName + "Service", "org.mycollection.games.server.games.api"));

		String toSimpleName = entitySimpleName + "TO";
		builder
			.withClassJavadoc(
				"A service for managing " + entitySimpleName + " entities using {@link " + entitySimpleName + "TO}s.")
			.withSingleParamPublicMethod(toSimpleName, "find" + entitySimpleName + "ById", "Long",
				entitySimpleName + "Id", null)
			.withSingleParamPublicMethod(toSimpleName, "save" + entitySimpleName, toSimpleName,
				entitySimpleName.toLowerCase(), null);

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
		return "Generate Service Interface from JPA Entity";
	}
}
