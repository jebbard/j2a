/**
 *
 * {@link StandardTOGeneratorTest}.java
 *
 * @author Jens Ebert
 *
 * @date 10.02.2020
 *
 */
package de.je.utils.j2a.standard.generation;

import java.io.IOException;
import java.util.Properties;

import org.junit.Test;

/**
 * {@link StandardTOGeneratorTest}
 *
 */
public class StandardTOGeneratorTest {

	@Test
	public void generate_withDefaultSettingsAndSourceClasses_generatesExpectedOutput() {
		StandardTOGenerator gen = new StandardTOGenerator();

		Properties properties = new Properties();
		try {
			properties.load(getClass().getResourceAsStream("standardGeneration.properties"));
		} catch (IOException e) {
			throw new RuntimeException("Could not find properties file", e);
		}

		gen.generate(properties);
	}

}
