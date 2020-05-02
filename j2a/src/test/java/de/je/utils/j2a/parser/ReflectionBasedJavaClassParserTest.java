/**
 *
 * {@link ReflectionBasedJavaClassParserTest}.java
 *
 * @author Jens Ebert
 *
 * @date 30.04.2020
 *
 */
package de.je.utils.j2a.parser;

import java.util.Set;

import com.github.j2a.core.config.J2AConfiguration;
import com.github.j2a.core.parser.JavaClassParser;
import com.github.j2a.core.parser.ReflectionJavaClassParser;

/**
 * {@link ReflectionBasedJavaClassParserTest}
 *
 */
public class ReflectionBasedJavaClassParserTest extends AbstractJavaClassParserTest<Class<?>> {

	/**
	 * @see de.je.utils.j2a.parser.AbstractJavaClassParserTest#getComplexInputClass()
	 */
	@Override
	protected Class<?> getComplexInputClass() {
		return TestClass.class;
	}

	/**
	 * @see de.je.utils.j2a.parser.AbstractJavaClassParserTest#getExpectedMethodParamName(java.lang.String,
	 *      int, boolean)
	 */
	@Override
	protected String getExpectedMethodParamName(String method, int index, boolean simple) {
		return "arg" + index;
	}

	/**
	 * @see de.je.utils.j2a.parser.AbstractJavaClassParserTest#getParserToTest()
	 */
	@Override
	protected JavaClassParser<Class<?>> getParserToTest() {
		return new ReflectionJavaClassParser(new J2AConfiguration(Set.of(TestClass.class.getPackageName())));
	}

	/**
	 * @see de.je.utils.j2a.parser.AbstractJavaClassParserTest#getSimpleInputClass()
	 */
	@Override
	protected Class<?> getSimpleInputClass() {
		return TestInterface1.class;
	}

}
