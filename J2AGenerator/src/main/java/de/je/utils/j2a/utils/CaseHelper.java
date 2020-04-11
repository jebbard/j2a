/**
 *
 * {@link CaseHelper}.java
 *
 * @author Jens Ebert
 *
 * @date 04.04.2020
 *
 */
package de.je.utils.j2a.utils;

/**
 * {@link CaseHelper}
 *
 */
public class CaseHelper {
	public static String toCapitalCase(String string) {
		return Character.toUpperCase(string.charAt(0)) + string.substring(1);
	}
}
