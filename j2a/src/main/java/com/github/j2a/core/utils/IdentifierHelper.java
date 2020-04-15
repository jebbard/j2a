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

	private static Set<String> JAVA_KEYWORDS = Set.of("abstract", "continue", "for", "new", "switch", "assert",
		"default", "if", "package", "synchronized", "boolean", "do", "goto", "private", "this", "break", "double",
		"implements", "protected", "throw", "byte", "else", "import", "public", "throws", "case", "enum", "instanceof",
		"return", "transient", "catch", "extends", "int", "short", "try", "char", "final", "interface", "static",
		"void", "class", "finally", "long", "strictfp", "volatile", "const", "float", "native", "super", "while", "_");

	public static boolean isValidJavaFullyQualifiedIdentifier(String fullyQualifiedIdentifier) {
		if (fullyQualifiedIdentifier == null) {
			throw new IllegalArgumentException();
		}

		for (int i = 0; i < fullyQualifiedIdentifier.length(); ++i) {
			char ch = fullyQualifiedIdentifier.charAt(i);

			if (!Character.isLetter(ch) && ch != '_' && ch != '$' && ch != '.') {
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
