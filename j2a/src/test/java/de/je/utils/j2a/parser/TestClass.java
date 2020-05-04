/**
 *
 * {@link TestClass}.java
 *
 * @author Jens Ebert
 *
 * @date 10.02.2020
 *
 */
package de.je.utils.j2a.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.jupiter.api.Disabled;

/**
 * {@link TestClass} is a meaningless class to test parsing. It was tried to
 * include all possible stuff of possible java declarations.
 */
@Disabled
public abstract class TestClass implements TestInterface1, TestInterface2 {
	public enum TestEnum {
		X, Y, Z;
	}

	final static strictfp class InnerTestClass extends ArrayList<Long> {
		private static final long serialVersionUID = 1L;
		int myInnerProp;

		void myInnerMethod() {
			// do nothing
		}
	}

	static Boolean TEST;

	public static final String TEST_2 = "TEST_2";

	@SafeVarargs
	@SuppressWarnings("unused")
	public static <T> List<T> myGenericMethod(String x, final int j, T z, long... k) {
		return new ArrayList<>();
	}

	private final transient int myInt;

	protected volatile AtomicLong x;

	public TestClass(int myInt, AtomicLong x) {
		super();
		this.myInt = myInt;
		this.x = x;
	}

	TestClass() {
		super();
		myInt = 0;
		x = null;
	}

	@Override
	public synchronized void doStuff(String... y) {
		// Still does nothing
	}

	protected native Long myStupidNativeMethod(int t);

	int getMyInt() {
		return myInt;
	}
}
