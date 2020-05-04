/**
 *
 * {@link IdentifierHelper}.java
 *
 * @author Jens Ebert
 *
 * @date 15.04.2020
 *
 */
package com.github.j2a.core.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * {@link IdentifierHelper}
 *
 */
public class IdentifierHelper {
	public static final char ARRAY_START_BRACKET = '[';
	public static final char ARRAY_END_BRACKET = ']';
	public static final char TYPE_ARGUMENT_START_BRACKET = '<';
	public static final char TYPE_ARGUMENT_END_BRACKET = '>';

	public static final String EMPTY_ARRAY_BRACKETS = Character.toString(IdentifierHelper.ARRAY_START_BRACKET)
		+ Character.toString(IdentifierHelper.ARRAY_END_BRACKET);

	public static final String EMPTY_TYPE_ARGUMENT_BRACKETS = Character.toString(
		IdentifierHelper.TYPE_ARGUMENT_START_BRACKET) + Character.toString(IdentifierHelper.TYPE_ARGUMENT_END_BRACKET);

	public static final char PACKAGE_SEGMENT_SEPARATOR = '.';

	public static final String JAVA_LANG = "java" + IdentifierHelper.PACKAGE_SEGMENT_SEPARATOR + "lang";

	public static final String VOID = "void";

	public static final String BOOLEAN = "boolean";
	public static final String CHAR = "char";
	public static final String BYTE = "byte";
	public static final String SHORT = "short";
	public static final String INT = "int";
	public static final String LONG = "long";
	public static final String DOUBLE = "double";
	public static final String FLOAT = "float";

	private static final Set<String> PRIMITIVES = Set.of(IdentifierHelper.VOID, IdentifierHelper.CHAR,
		IdentifierHelper.DOUBLE, IdentifierHelper.INT, IdentifierHelper.FLOAT, IdentifierHelper.BOOLEAN,
		IdentifierHelper.LONG, IdentifierHelper.SHORT, IdentifierHelper.BYTE);

	private static final Set<Class<?>> JAVA_LANG_CLASSES = new HashSet<>();

	static {
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.AbstractMethodError.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.Appendable.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.ArithmeticException.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.ArrayIndexOutOfBoundsException.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.ArrayStoreException.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.AssertionError.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.AutoCloseable.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.Boolean.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.BootstrapMethodError.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.Byte.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.Character.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.CharSequence.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.Class.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.ClassCastException.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.ClassCircularityError.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.ClassFormatError.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.ClassLoader.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.ClassNotFoundException.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.ClassValue.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.Cloneable.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.CloneNotSupportedException.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.Comparable.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.Compiler.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.Deprecated.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.Double.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.Enum.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.EnumConstantNotPresentException.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.Error.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.Exception.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.ExceptionInInitializerError.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.Float.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.FunctionalInterface.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.IllegalAccessError.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.IllegalAccessException.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.IllegalArgumentException.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.IllegalCallerException.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.IllegalMonitorStateException.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.IllegalStateException.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.IllegalThreadStateException.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.IncompatibleClassChangeError.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.IllegalAccessError.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.IndexOutOfBoundsException.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.InheritableThreadLocal.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.InstantiationError.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.Integer.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.InternalError.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.InstantiationException.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.InterruptedException.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.LayerInstantiationException.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.LinkageError.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.Long.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.Math.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.Module.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.ModuleLayer.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.NegativeArraySizeException.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.NoClassDefFoundError.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.NoSuchFieldError.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.NoSuchFieldException.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.NoSuchMethodError.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.NoSuchMethodException.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.NullPointerException.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.Number.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.NumberFormatException.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.Object.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.OutOfMemoryError.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.Override.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.Package.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.Process.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.ProcessBuilder.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.ProcessHandle.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.Readable.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.Record.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.ReflectiveOperationException.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.Runnable.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.Runtime.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.RuntimeException.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.RuntimePermission.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.SafeVarargs.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.SecurityException.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.SecurityManager.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.Short.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.StackOverflowError.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.StackTraceElement.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.StackWalker.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.StrictMath.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.String.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.StringBuffer.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.StringBuilder.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.StringIndexOutOfBoundsException.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.SuppressWarnings.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.System.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.Thread.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.ThreadDeath.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.ThreadGroup.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.ThreadLocal.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.Throwable.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.TypeNotPresentException.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.UnknownError.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.UnsatisfiedLinkError.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.UnsupportedClassVersionError.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.UnsupportedOperationException.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.VerifyError.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.VirtualMachineError.class);
		IdentifierHelper.JAVA_LANG_CLASSES.add(java.lang.Void.class);
	}

	private static Set<String> JAVA_KEYWORDS = Set.of("abstract", "continue", "for", "new", "switch", "assert",
		"default", "if", "package", "synchronized", IdentifierHelper.BOOLEAN, "do", "goto", "private", "this", "break",
		IdentifierHelper.DOUBLE, "implements", "protected", "throw", IdentifierHelper.BYTE, "else", "import", "public",
		"throws", "case", "enum", "instanceof", "return", "transient", "catch", "extends", IdentifierHelper.INT,
		IdentifierHelper.SHORT, "try", IdentifierHelper.CHAR, "final", "interface", "static", IdentifierHelper.VOID,
		"class", "finally", IdentifierHelper.LONG, "strictfp", "volatile", "const", IdentifierHelper.FLOAT, "native",
		"super", "while", "_");

	public static String getClassNameFromFullyQualified(String fullyQualifiedTypeName) {
		String[] packageSegments = IdentifierHelper.getPackageSegmentsFromFullyQualified(fullyQualifiedTypeName);

		return packageSegments[packageSegments.length - 1];
	}

	public static String getPackageNameFromFullyQualified(String fullyQualifiedTypeName) {
		String[] packageSegments = IdentifierHelper.getPackageSegmentsFromFullyQualified(fullyQualifiedTypeName);

		return Arrays.stream(packageSegments).limit(packageSegments.length - 1)
			.collect(Collectors.joining(Character.toString(IdentifierHelper.PACKAGE_SEGMENT_SEPARATOR)));
	}

	public static String[] getPackageSegmentsFromFullyQualified(String fullyQualifiedTypeName) {
		return fullyQualifiedTypeName.split("\\" + IdentifierHelper.PACKAGE_SEGMENT_SEPARATOR);
	}

	public static boolean isJavaLangClass(String simpleClassName) {
		return IdentifierHelper.JAVA_LANG_CLASSES.stream().anyMatch(cl -> cl.getSimpleName().equals(simpleClassName));
	}

	public static boolean isPrimitive(String className) {
		return IdentifierHelper.PRIMITIVES.contains(className);
	}

	public static boolean isTypeReferenceFullyQualified(String typeReference) {
		return IdentifierHelper.isValidJavaTypeReference(typeReference)
			&& typeReference.contains(Character.toString(IdentifierHelper.PACKAGE_SEGMENT_SEPARATOR));
	}

	public static boolean isValidJavaTypeReference(String typeReference) {
		if (typeReference == null) {
			throw new IllegalArgumentException();
		}

		if (IdentifierHelper.PRIMITIVES.contains(typeReference)) {
			return true;
		}

		for (int i = 0; i < typeReference.length(); ++i) {
			char ch = typeReference.charAt(i);

			if (!Character.isLetter(ch) && ch != '_' && ch != '$' && ch != IdentifierHelper.PACKAGE_SEGMENT_SEPARATOR
				&& ch != IdentifierHelper.ARRAY_START_BRACKET && ch != IdentifierHelper.ARRAY_END_BRACKET
				&& ch != IdentifierHelper.TYPE_ARGUMENT_START_BRACKET
				&& ch != IdentifierHelper.TYPE_ARGUMENT_END_BRACKET) {
				return false;
			}
		}

		String[] packageSegments = IdentifierHelper.getPackageSegmentsFromFullyQualified(typeReference);

		for (String segment : packageSegments) {
			if (IdentifierHelper.JAVA_KEYWORDS.contains(segment)) {
				return false;
			}
		}

		return true;
	}
}
