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

import java.util.Set;

/**
 * {@link IdentifierHelper}
 *
 */
public class IdentifierHelper {

	public static final String VOID = "void";
	public static final String BOOLEAN = "boolean";
	public static final String CHAR = "char";
	public static final String BYTE = "byte";
	public static final String SHORT = "short";
	public static final String INT = "int";
	public static final String LONG = "long";
	public static final String DOUBLE = "double";
	public static final String FLOAT = "float";

	public static final Set<String> PRIMITIVES = Set.of(IdentifierHelper.VOID, IdentifierHelper.CHAR,
		IdentifierHelper.DOUBLE, IdentifierHelper.INT, IdentifierHelper.FLOAT, IdentifierHelper.BOOLEAN,
		IdentifierHelper.LONG, IdentifierHelper.SHORT, IdentifierHelper.BYTE);

	private static Set<String> JAVA_KEYWORDS = Set.of("abstract", "continue", "for", "new", "switch", "assert",
		"default", "if", "package", "synchronized", IdentifierHelper.BOOLEAN, "do", "goto", "private", "this", "break",
		IdentifierHelper.DOUBLE, "implements", "protected", "throw", IdentifierHelper.BYTE, "else", "import", "public",
		"throws", "case", "enum", "instanceof", "return", "transient", "catch", "extends", IdentifierHelper.INT,
		IdentifierHelper.SHORT, "try", IdentifierHelper.CHAR, "final", "interface", "static", IdentifierHelper.VOID,
		"class", "finally", IdentifierHelper.LONG, "strictfp", "volatile", "const", IdentifierHelper.FLOAT, "native",
		"super", "while", "_");

	public static boolean isValidJavaFullyQualifiedIdentifier(String fullyQualifiedIdentifier) {
		if (fullyQualifiedIdentifier == null) {
			throw new IllegalArgumentException();
		}

		if (IdentifierHelper.PRIMITIVES.contains(fullyQualifiedIdentifier)) {
			return true;
		}

		for (int i = 0; i < fullyQualifiedIdentifier.length(); ++i) {
			char ch = fullyQualifiedIdentifier.charAt(i);

			if (!Character.isLetter(ch) && ch != '_' && ch != '$' && ch != '.' && ch != '[' && ch != ']') {
				return false;
			}
		}

		String[] packageSegments = fullyQualifiedIdentifier.split("\\.");

		for (String segment : packageSegments) {
			if (IdentifierHelper.JAVA_KEYWORDS.contains(segment)) {
				return false;
			}
		}

		return true;
	}
}
