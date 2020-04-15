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
import org.mycollection.games.server.utils.AbstractTO;

import com.github.j2a.core.definition.JavaClassDefinition;
import com.github.j2a.core.definition.JavaFieldDefinition;
import com.github.j2a.core.generation.Generator;
import com.github.j2a.core.generation.GeneratorResult;
import com.github.j2a.core.generation.J2A;
import com.github.j2a.core.utils.JavaClassBuilder;

/**
 * {@link TOFromEntityGenerator}
 *
 */
public class TOFromEntityGenerator implements Generator {

	public static void main(String[] args) {
		J2A j2a = new J2A();

		j2a.generateOutputFromClass("org.mycollection.games.server", GameRelease.class, Paths.get("."),
			new TOFromEntityGenerator());
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
		JavaClassBuilder builder = JavaClassBuilder.createClass("org.mycollection.games.server.games.api",
			classDefinition.getName() + "TO");

		builder.withClassJavadoc("A transport object for " + classDefinition.getName() + " entities.")
			.extending(AbstractTO.class);

		for (JavaFieldDefinition fieldDefinition : classDefinition.getFields()) {
			if (!fieldDefinition.isFinal() && !fieldDefinition.isStatic() && !fieldDefinition.isTransient()
				&& !fieldDefinition.hasAnnotationByRegex(".*Transient")) {
				if (fieldDefinition.hasAnnotationByRegex(".*ManyToOne|.*OneToMany|.*ManyToMany|.*OneToOne")) {
					JavaClassDefinition fieldType = fieldDefinition.getType();

					if (fieldType.isApplicationClass()) {
						JavaFieldDefinition adaptedFieldDefinition = new JavaFieldDefinition(fieldDefinition,
							fieldDefinition.getName() + "Id", Long.class);

						builder.withProperty(adaptedFieldDefinition);
					}
				} else {
					builder.withProperty(fieldDefinition);
				}
			}
		}

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
