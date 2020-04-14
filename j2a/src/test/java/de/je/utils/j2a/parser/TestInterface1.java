/**
 *
 * {@link TestInterface1}.java
 *
 * @author Jens Ebert
 *
 * @date 10.02.2020
 *
 */
package de.je.utils.j2a.parser;

/**
 * {@link TestInterface1} is just a meaningless test interface to test parsing.
 */
public interface TestInterface1 {
	default void doStuff(@SuppressWarnings("unused") String... x) {
		// do nothing
	}
}
