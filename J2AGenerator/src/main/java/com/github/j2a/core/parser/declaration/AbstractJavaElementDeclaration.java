/**
 *
 * {@link AbstractJavaElementDeclaration}.java
 *
 * @author Jens Ebert
 *
 * @date 10.02.2020
 *
 */
package com.github.j2a.core.parser.declaration;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link AbstractJavaElementDeclaration} is the base class of all Java element declarations, it contains the properties
 * common to any Java declaration.
 */
public abstract class AbstractJavaElementDeclaration {
	private final boolean isFinal;
	private final String name;
	private List<JavaClassDeclaration> annotations = new ArrayList<>();

	public AbstractJavaElementDeclaration(boolean isFinal, String name) {
		this.isFinal = isFinal;
		this.name = name;
	}

	public List<JavaClassDeclaration> getAnnotations() {
		return annotations;
	}

	public String getName() {
		return name;
	}

	public boolean hasAnnotationByRegex(String annotationNameRegex) {
		for (JavaClassDeclaration annotation : annotations) {
			if (annotation.getSourceClass().getName().matches(annotationNameRegex)) {
				return true;
			}
		}

		return false;
	}

	public boolean isFinal() {
		return isFinal;
	}

	public void setAnnotations(List<JavaClassDeclaration> annotations) {
		this.annotations = annotations;
	}
}
