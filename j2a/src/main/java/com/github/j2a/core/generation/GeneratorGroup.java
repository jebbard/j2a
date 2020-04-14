/**
 *
 * {@link GeneratorGroup}.java
 *
 * @author Jens Ebert
 *
 * @date 12.04.2020
 *
 */
package com.github.j2a.core.generation;

import java.util.List;

/**
 * {@link GeneratorGroup}
 *
 */
public interface GeneratorGroup {
	String getDescription();

	List<Generator> getGenerators();

	String getName();
}
