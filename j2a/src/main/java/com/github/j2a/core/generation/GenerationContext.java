/**
 *
 * {@link GenerationContext}.java
 *
 * @author Jens Ebert
 *
 * @date 30.04.2020
 *
 */
package com.github.j2a.core.generation;

import com.github.j2a.core.parser.JavaTypeReference;

/**
 * {@link GenerationContext}
 *
 */
public class GenerationContext {

	private final String applicationPackage;

	/**
	 * Creates a new {@link GenerationContext}.
	 * 
	 * @param applicationPackage
	 */
	public GenerationContext(String applicationPackage) {
		this.applicationPackage = applicationPackage;
	}

	public boolean isApplicationClass(JavaTypeReference reference) {
		return reference.getPackageName().startsWith(applicationPackage);
	}
}
