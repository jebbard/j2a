/**
 *
 * {@link JavaParserBasedJavaClassParserTest}.java
 *
 * @author Jens Ebert
 *
 * @date 30.04.2020
 *
 */
package de.je.utils.j2a.parser;

import java.util.HashMap;
import java.util.Map;

import com.github.j2a.core.parser.JavaClassParser;
import com.github.j2a.core.parser.JavaParserBasedClassParser;

/**
 * {@link JavaParserBasedJavaClassParserTest}
 *
 */
public class JavaParserBasedJavaClassParserTest extends AbstractJavaClassParserTest<String> {

	public static final String SIMPLE_CLASS = "/**\n" + " *\n" + " * {@link TestInterface1}.java\n" + " *\n"
		+ " * @author Jens Ebert\n" + " *\n" + " * @date 10.02.2020\n" + " *\n" + " */\n"
		+ "package de.je.utils.j2a.parser;\n" + "\n" + "/**\n"
		+ " * {@link TestInterface1} is just a meaningless test interface to test parsing.\n" + " */\n"
		+ "public interface TestInterface1 {\n" + "	default void doStuff(@SuppressWarnings(\"unused\") String... x) {\n"
		+ "		// do nothing\n" + "	}\n" + "}";

	public static final String COMPLEX_CLASS = "/**\n" + " *\n" + " * {@link TestClass}.java\n" + " *\n"
		+ " * @author Jens Ebert\n" + " *\n" + " * @date 10.02.2020\n" + " *\n" + " */\n"
		+ "package de.je.utils.j2a.parser;\n" + "\n" + "import java.util.ArrayList;\n" + "import java.util.List;\n"
		+ "import java.util.concurrent.atomic.AtomicLong;\n" + "\n" + "import org.junit.jupiter.api.Disabled;\n" + "\n"
		+ "/**\n" + " * {@link TestClass} is a meaningless class to test parsing. It was tried to\n"
		+ " * include all possible stuff of possible java declarations.\n" + " */\n" + "@Disabled\n"
		+ "public abstract class TestClass implements TestInterface1, TestInterface2 {\n"
		+ "	public enum TestEnum {\n" + "		X, Y, Z;\n" + "	}\n" + "\n"
		+ "	final static strictfp class InnerTestClass extends ArrayList<Long> {\n"
		+ "		private static final long serialVersionUID = 1L;\n" + "		int myInnerProp;\n" + "\n"
		+ "		void myInnerMethod() {\n" + "			// do nothing\n" + "		}\n" + "	}\n" + "\n"
		+ "	static Boolean TEST;\n" + "\n" + "	public static final String TEST_2 = \"TEST_2\";\n" + "\n"
		+ "	@SafeVarargs\n" + "	@SuppressWarnings(\"unused\")\n"
		+ "	public static <T> List<T> myGenericMethod(String x, final int j, long... k) {\n"
		+ "		return new ArrayList<>();\n" + "	}\n" + "\n" + "	private final transient int myInt;\n" + "\n"
		+ "	protected volatile AtomicLong x;\n" + "\n" + "	public TestClass(int myInt, AtomicLong x) {\n"
		+ "		super();\n" + "		this.myInt = myInt;\n" + "		this.x = x;\n" + "	}\n" + "\n" + "	TestClass() {\n"
		+ "		super();\n" + "		myInt = 0;\n" + "		x = null;\n" + "	}\n" + "\n" + "	@Override\n"
		+ "	public synchronized void doStuff(String... y) {\n" + "		// Still does nothing\n" + "	}\n" + "\n"
		+ "	protected native Long myStupidNativeMethod(int t);\n" + "\n" + "	int getMyInt() {\n"
		+ "		return myInt;\n" + "	}\n" + "}\n" + "";

	private static final Map<String, String> EXPECTED_METHOD_PARAM_NAMES_COMPLEX = new HashMap<>();
	static {
		JavaParserBasedJavaClassParserTest.EXPECTED_METHOD_PARAM_NAMES_COMPLEX.put("myGenericMethod0", "x");
		JavaParserBasedJavaClassParserTest.EXPECTED_METHOD_PARAM_NAMES_COMPLEX.put("myGenericMethod1", "j");
		JavaParserBasedJavaClassParserTest.EXPECTED_METHOD_PARAM_NAMES_COMPLEX.put("myGenericMethod2", "k");
		JavaParserBasedJavaClassParserTest.EXPECTED_METHOD_PARAM_NAMES_COMPLEX
			.put(TestClass.class.getSimpleName() + "0", "myInt");
		JavaParserBasedJavaClassParserTest.EXPECTED_METHOD_PARAM_NAMES_COMPLEX
			.put(TestClass.class.getSimpleName() + "1", "x");
		JavaParserBasedJavaClassParserTest.EXPECTED_METHOD_PARAM_NAMES_COMPLEX.put("doStuff0", "y");
		JavaParserBasedJavaClassParserTest.EXPECTED_METHOD_PARAM_NAMES_COMPLEX.put("myStupidNativeMethod0", "t");
	}

	private static final Map<String, String> EXPECTED_METHOD_PARAM_NAMES_SIMPLE = new HashMap<>();
	static {
		JavaParserBasedJavaClassParserTest.EXPECTED_METHOD_PARAM_NAMES_SIMPLE.put("doStuff0", "x");
	}

	/**
	 * @see de.je.utils.j2a.parser.AbstractJavaClassParserTest#getComplexInputClass()
	 */
	@Override
	protected String getComplexInputClass() {
		return JavaParserBasedJavaClassParserTest.COMPLEX_CLASS;
	}

	/**
	 * @see de.je.utils.j2a.parser.AbstractJavaClassParserTest#getExpectedMethodParamName(java.lang.String,
	 *      int, boolean)
	 */
	@Override
	protected String getExpectedMethodParamName(String method, int index, boolean simple) {
		if (simple) {
			return JavaParserBasedJavaClassParserTest.EXPECTED_METHOD_PARAM_NAMES_SIMPLE.get(method + index);
		}
		return JavaParserBasedJavaClassParserTest.EXPECTED_METHOD_PARAM_NAMES_COMPLEX.get(method + index);
	}

	/**
	 * @see de.je.utils.j2a.parser.AbstractJavaClassParserTest#getParserToTest()
	 */
	@Override
	protected JavaClassParser<String> getParserToTest() {
		return new JavaParserBasedClassParser();
	}

	/**
	 * @see de.je.utils.j2a.parser.AbstractJavaClassParserTest#getSimpleInputClass()
	 */
	@Override
	protected String getSimpleInputClass() {
		return JavaParserBasedJavaClassParserTest.SIMPLE_CLASS;
	}

}
