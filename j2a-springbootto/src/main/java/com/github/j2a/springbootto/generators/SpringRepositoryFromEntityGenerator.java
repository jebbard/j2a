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
 * {@link SpringRepositoryFromEntityGenerator}
 *
 */
public class SpringRepositoryFromEntityGenerator implements Generator {

	public static void main(String[] args) {
		J2A j2a = new J2A();

		j2a.generateOutputFromClass("org.mycollection.games.server", ClassText.GAME_RELEASE, Paths.get("."),
			new SpringRepositoryFromEntityGenerator());
	}

	/**
	 * @see com.github.j2a.core.generation.Generator#canGenerateResult(com.github.j2a.core.definition.JavaClassDefinition)
	 */
	public boolean canGenerateResult(JavaClassDefinition classDefinition) {
		return false;
	}

	/**
	 * @see com.github.j2a.core.generation.Generator#generateResult(com.github.j2a.core.definition.JavaClassDefinition,
	 *      com.github.j2a.core.generation.GenerationContext)
	 */
	public GeneratorResult generateResult(JavaClassDefinition classDefinition, GenerationContext context) {
		JavaClassBuilder builder = JavaClassBuilder.createInterface(new FullyQualifiedJavaTypeReference(
			classDefinition.getName() + "Repository", "org.mycollection.games.server.games.impl.data"));

		builder.withClassJavadoc("A repository for " + classDefinition.getName() + " entities.")
			.extending(new FullyQualifiedJavaTypeReference("org.springframework.data.jpa.repository.JpaRepository"),
				new FullyQualifiedJavaTypeReference(classDefinition.getName(), classDefinition.getPackageName()),
				new FullyQualifiedJavaTypeReference("java.lang.Long"))
			.withAnnotation(new FullyQualifiedJavaTypeReference("org.springframework.stereotype.Repository"))
			.withBodyComment("Intentionally empty");

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