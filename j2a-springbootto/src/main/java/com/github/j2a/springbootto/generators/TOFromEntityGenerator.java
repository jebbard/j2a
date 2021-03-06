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
import java.util.ArrayList;

import com.github.j2a.core.definition.JavaClassDefinition;
import com.github.j2a.core.definition.JavaFieldDefinition;
import com.github.j2a.core.generation.GenerationContext;
import com.github.j2a.core.generation.Generator;
import com.github.j2a.core.generation.GeneratorResult;
import com.github.j2a.core.generation.J2A;
import com.github.j2a.core.parser.JavaTypeReference;
import com.github.j2a.core.utils.FullyQualifiedJavaTypeReference;
import com.github.j2a.core.utils.JavaClassBuilder;

/**
 * {@link TOFromEntityGenerator}
 *
 */
public class TOFromEntityGenerator implements Generator {

	private static final String errorText = "package d;\n" + "\n" + "public class EntExample {\n"
		+ "	private String y;\n" + "	private int x;\n" + "	private Long test;\n" + "}\n";

	public static void main(String[] args) {
		J2A j2a = new J2A();

//		j2a.generateOutputFromClass("org.mycollection.games.server", ClassText.GAME_RELEASE, Paths.get("."),
//			new TOFromEntityGenerator());

		j2a.generateOutputFromClass("org.mycollection.games.server", TOFromEntityGenerator.errorText, Paths.get("."),
			new TOFromEntityGenerator());
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
		JavaClassBuilder builder = JavaClassBuilder.createClass(new FullyQualifiedJavaTypeReference(
			classDefinition.getName() + "TO", "org.mycollection.games.server.games.api", false));

		builder.withClassJavadoc("A transport object for " + classDefinition.getName() + " entities.")
			.extending(new FullyQualifiedJavaTypeReference("org.mycollection.games.server.utils.AbstractTO"));

		for (JavaFieldDefinition fieldDefinition : classDefinition.getFields()) {
			if (!fieldDefinition.isFinal() && !fieldDefinition.isStatic() && !fieldDefinition.isTransient()
				&& !fieldDefinition.hasAnnotationByRegex(".*Transient")) {
				if (fieldDefinition.hasAnnotationByRegex(".*ManyToOne|.*OneToMany|.*ManyToMany|.*OneToOne")) {
					JavaTypeReference fieldType = fieldDefinition.getType();

					if (context.isApplicationClass(fieldType)) {
						JavaFieldDefinition adaptedFieldDefinition = new JavaFieldDefinition(fieldDefinition,
							fieldDefinition.getName() + "Id", Long.class, new ArrayList<>());

						builder.withProperty(adaptedFieldDefinition);
					}
				} else {
					builder.withProperty(fieldDefinition);
				}
			}
		}

		String result = builder.build();
		System.out.println(result);

		return new GeneratorResult(result, null);
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
		return "Generate flat Transport Object from JPA Entity";
	}
}
