/**
 *
 * {@link J2ATest}.java
 *
 * @author Jens Ebert
 *
 * @date 05.05.2020
 *
 */
package de.je.utils.j2a.testutils;

import org.junit.jupiter.api.Test;

import com.github.j2a.core.generation.Generator;
import com.github.j2a.core.generation.J2A;

/**
 * {@link J2ATest}
 *
 */
public class J2ATest {
	/**
	 * Tests {@link J2A#getAllRegisteredGenerators()}.
	 */
	@Test
	public void _testContent() {
		for (Generator gen : new J2A().getAllRegisteredGenerators()) {
			System.out.println(gen.getName());
		}
	}

}
