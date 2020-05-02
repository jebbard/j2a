/**
 *
 * {@link AbstractJavaElementDefinition}.java
 *
 * @author Jens Ebert
 *
 * @date 10.02.2020
 *
 */
package com.github.j2a.core.definition;

import java.util.ArrayList;
import java.util.List;

import com.github.j2a.core.parser.JavaClassReference;

/**
 * {@link AbstractJavaElementDefinition} is the base class of all Java element
 * declarations, it contains the properties common to any Java declaration.
 */
public abstract class AbstractJavaElementDefinition {
	private final boolean isFinal;
	private final String name;
	private List<JavaClassReference> annotations = new ArrayList<>();

	public AbstractJavaElementDefinition(boolean isFinal, String name) {
		this.isFinal = isFinal;
		this.name = name;
	}

	public List<JavaClassReference> getAnnotations() {
		return annotations;
	}

	public String getName() {
		return name;
	}

	public boolean hasAnnotationByRegex(String annotationNameRegex) {
		for (JavaClassReference annotation : annotations) {
			if (annotation.getFullyQualifiedName().matches(annotationNameRegex)) {
				return true;
			}
		}

		return false;
	}

	public boolean isFinal() {
		return isFinal;
	}

	public void setAnnotations(List<JavaClassReference> annotations) {
		this.annotations = annotations;
	}
}
