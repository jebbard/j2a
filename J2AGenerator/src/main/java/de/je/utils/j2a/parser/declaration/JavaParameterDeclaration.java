/**
 *
 * {@link JavaParameterDeclaration}.java
 *
 * @author Jens Ebert
 *
 * @date 10.02.2020
 *
 */
package de.je.utils.j2a.parser.declaration;

import java.util.List;

/**
 * {@link JavaParameterDeclaration} represents a Java method parameter
 * declaration
 *
 */
public class JavaParameterDeclaration extends AbstractJavaElementDeclaration {
	private final JavaClassDeclaration type;

	public JavaParameterDeclaration(boolean isFinal, String name, List<JavaClassDeclaration> annotations,
		JavaClassDeclaration type) {
		super(isFinal, name);
		this.type = type;
		setAnnotations(annotations);
	}

	public JavaClassDeclaration getType() {
		return type;
	}

}
