/**
 *
 * {@link CaseHelper}.java
 *
 * @author Jens Ebert
 *
 * @date 04.04.2020
 *
 */
package com.github.j2a.core.utils;

/**
 * {@link CaseHelper}
 *
 */
public class CaseHelper {
	public static String toCapitalCase(String string) {
		return Character.toUpperCase(string.charAt(0)) + string.substring(1);
	}
}
