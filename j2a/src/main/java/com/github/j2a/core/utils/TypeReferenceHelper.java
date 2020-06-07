/**
 *
 * {@link TypeReferenceHelper}.java
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
 * {@link TypeReferenceHelper}
 *
 */
public class TypeReferenceHelper {
	public static final char ARRAY_START_BRACKET = '[';
	public static final char ARRAY_END_BRACKET = ']';
	public static final char TYPE_ARGUMENT_START_BRACKET = '<';
	public static final char TYPE_ARGUMENT_END_BRACKET = '>';

	public static final String EMPTY_ARRAY_BRACKETS = Character.toString(TypeReferenceHelper.ARRAY_START_BRACKET)
		+ Character.toString(TypeReferenceHelper.ARRAY_END_BRACKET);

	public static final String EMPTY_TYPE_ARGUMENT_BRACKETS = Character
		.toString(TypeReferenceHelper.TYPE_ARGUMENT_START_BRACKET)
		+ Character.toString(TypeReferenceHelper.TYPE_ARGUMENT_END_BRACKET);

	public static final char PACKAGE_SEGMENT_SEPARATOR = '.';

	public static final String JAVA_LANG = "java" + TypeReferenceHelper.PACKAGE_SEGMENT_SEPARATOR + "lang";

	public static final String VOID = "void";

	public static final String BOOLEAN = "boolean";
	public static final String CHAR = "char";
	public static final String BYTE = "byte";
	public static final String SHORT = "short";
	public static final String INT = "int";
	public static final String LONG = "long";
	public static final String DOUBLE = "double";
	public static final String FLOAT = "float";

	private static final Set<String> PRIMITIVES = Set.of(TypeReferenceHelper.VOID, TypeReferenceHelper.CHAR,
		TypeReferenceHelper.DOUBLE, TypeReferenceHelper.INT, TypeReferenceHelper.FLOAT, TypeReferenceHelper.BOOLEAN,
		TypeReferenceHelper.LONG, TypeReferenceHelper.SHORT, TypeReferenceHelper.BYTE);

	private static final Set<Class<?>> JAVA_LANG_CLASSES = new HashSet<>();

	static {
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.AbstractMethodError.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.Appendable.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.ArithmeticException.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.ArrayIndexOutOfBoundsException.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.ArrayStoreException.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.AssertionError.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.AutoCloseable.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.Boolean.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.BootstrapMethodError.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.Byte.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.Character.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.CharSequence.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.Class.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.ClassCastException.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.ClassCircularityError.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.ClassFormatError.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.ClassLoader.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.ClassNotFoundException.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.ClassValue.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.Cloneable.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.CloneNotSupportedException.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.Comparable.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.Compiler.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.Deprecated.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.Double.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.Enum.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.EnumConstantNotPresentException.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.Error.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.Exception.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.ExceptionInInitializerError.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.Float.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.FunctionalInterface.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.IllegalAccessError.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.IllegalAccessException.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.IllegalArgumentException.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.IllegalCallerException.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.IllegalMonitorStateException.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.IllegalStateException.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.IllegalThreadStateException.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.IncompatibleClassChangeError.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.IllegalAccessError.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.IndexOutOfBoundsException.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.InheritableThreadLocal.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.InstantiationError.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.Integer.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.InternalError.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.InstantiationException.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.InterruptedException.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.LayerInstantiationException.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.LinkageError.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.Long.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.Math.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.Module.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.ModuleLayer.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.NegativeArraySizeException.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.NoClassDefFoundError.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.NoSuchFieldError.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.NoSuchFieldException.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.NoSuchMethodError.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.NoSuchMethodException.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.NullPointerException.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.Number.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.NumberFormatException.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.Object.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.OutOfMemoryError.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.Override.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.Package.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.Process.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.ProcessBuilder.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.ProcessHandle.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.Readable.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.ReflectiveOperationException.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.Runnable.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.Runtime.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.RuntimeException.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.RuntimePermission.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.SafeVarargs.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.SecurityException.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.SecurityManager.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.Short.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.StackOverflowError.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.StackTraceElement.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.StackWalker.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.StrictMath.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.String.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.StringBuffer.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.StringBuilder.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.StringIndexOutOfBoundsException.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.SuppressWarnings.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.System.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.Thread.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.ThreadDeath.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.ThreadGroup.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.ThreadLocal.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.Throwable.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.TypeNotPresentException.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.UnknownError.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.UnsatisfiedLinkError.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.UnsupportedClassVersionError.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.UnsupportedOperationException.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.VerifyError.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.VirtualMachineError.class);
		TypeReferenceHelper.JAVA_LANG_CLASSES.add(java.lang.Void.class);
	}

	private static Set<String> JAVA_KEYWORDS = Set.of("abstract", "continue", "for", "new", "switch", "assert",
		"default", "if", "package", "synchronized", TypeReferenceHelper.BOOLEAN, "do", "goto", "private", "this",
		"break", TypeReferenceHelper.DOUBLE, "implements", "protected", "throw", TypeReferenceHelper.BYTE, "else",
		"import", "public", "throws", "case", "enum", "instanceof", "return", "transient", "catch", "extends",
		TypeReferenceHelper.INT, TypeReferenceHelper.SHORT, "try", TypeReferenceHelper.CHAR, "final", "interface",
		"static", TypeReferenceHelper.VOID, "class", "finally", TypeReferenceHelper.LONG, "strictfp", "volatile",
		"const", TypeReferenceHelper.FLOAT, "native", "super", "while", "_");

	public static String getArrayType(String typeReference) {
		if (typeReference.endsWith(TypeReferenceHelper.EMPTY_ARRAY_BRACKETS)) {
			return typeReference;
		}

		return typeReference + TypeReferenceHelper.EMPTY_ARRAY_BRACKETS;
	}

	public static String getClassNameFromFullyQualified(String fullyQualifiedTypeName) {
		String[] packageSegments = TypeReferenceHelper.getPackageSegmentsFromFullyQualified(fullyQualifiedTypeName);

		return packageSegments[packageSegments.length - 1];
	}

	public static String getElementType(String typeReference) {
		if (typeReference.endsWith(TypeReferenceHelper.EMPTY_ARRAY_BRACKETS)) {
			return typeReference.substring(0, typeReference.indexOf(TypeReferenceHelper.ARRAY_START_BRACKET));
		}

		return typeReference;
	}

	public static String getPackageNameFromFullyQualified(String fullyQualifiedTypeName) {
		String[] packageSegments = TypeReferenceHelper.getPackageSegmentsFromFullyQualified(fullyQualifiedTypeName);

		return Arrays.stream(packageSegments).limit(packageSegments.length - 1)
			.collect(Collectors.joining(Character.toString(TypeReferenceHelper.PACKAGE_SEGMENT_SEPARATOR)));
	}

	public static String[] getPackageSegmentsFromFullyQualified(String fullyQualifiedTypeName) {
		return fullyQualifiedTypeName.split("\\" + TypeReferenceHelper.PACKAGE_SEGMENT_SEPARATOR);
	}

	public static String getRawType(String typeReference) {
		if (typeReference.contains(Character.toString(TypeReferenceHelper.TYPE_ARGUMENT_START_BRACKET))) {
			return typeReference.substring(0, typeReference.indexOf(TypeReferenceHelper.TYPE_ARGUMENT_START_BRACKET));
		}

		return typeReference;
	}

	public static boolean isJavaLangClass(String simpleClassName) {
		return TypeReferenceHelper.JAVA_LANG_CLASSES.stream()
			.anyMatch(cl -> cl.getSimpleName().equals(simpleClassName));
	}

	public static boolean isPrimitive(String className) {

		if (TypeReferenceHelper.PRIMITIVES.contains(className)) {
			return true;
		}

		if (className.startsWith(TypeReferenceHelper.JAVA_LANG) && TypeReferenceHelper.PRIMITIVES.contains(
			className.replaceAll(TypeReferenceHelper.JAVA_LANG + TypeReferenceHelper.PACKAGE_SEGMENT_SEPARATOR, ""))) {
			return true;
		}

		return false;
	}

	public static boolean isTypeReferenceFullyQualified(String typeReference) {
		return TypeReferenceHelper.isValidJavaTypeReference(typeReference)
			&& typeReference.contains(Character.toString(TypeReferenceHelper.PACKAGE_SEGMENT_SEPARATOR));
	}

	public static boolean isValidJavaTypeReference(String typeReference) {
		if (typeReference == null) {
			throw new IllegalArgumentException();
		}

		if (TypeReferenceHelper.isPrimitive(typeReference)) {
			return true;
		}

		for (int i = 0; i < typeReference.length(); ++i) {
			char ch = typeReference.charAt(i);

			if (!Character.isLetter(ch) && !Character.isDigit(ch) && ch != '_' && ch != '$'
				&& ch != TypeReferenceHelper.PACKAGE_SEGMENT_SEPARATOR && ch != TypeReferenceHelper.ARRAY_START_BRACKET
				&& ch != TypeReferenceHelper.ARRAY_END_BRACKET && ch != TypeReferenceHelper.TYPE_ARGUMENT_START_BRACKET
				&& ch != TypeReferenceHelper.TYPE_ARGUMENT_END_BRACKET) {
				return false;
			}
		}

		String[] packageSegments = TypeReferenceHelper.getPackageSegmentsFromFullyQualified(typeReference);

		for (String segment : packageSegments) {
			if (TypeReferenceHelper.JAVA_KEYWORDS.contains(segment)) {
				return false;
			}

			if (Character.isDigit(segment.charAt(0))) {
				return false;
			}
		}

		return true;
	}
}
