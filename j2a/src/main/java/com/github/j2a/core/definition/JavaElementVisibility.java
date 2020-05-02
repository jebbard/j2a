/**
 *
 * {@link JavaElementVisibility}.java
 *
 * @author Jens Ebert
 *
 * @date 10.02.2020
 *
 */
package com.github.j2a.core.definition;

import java.lang.reflect.Modifier;

import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.ast.Modifier.Keyword;

/**
 * {@link JavaElementVisibility}
 *
 */
public enum JavaElementVisibility {
	PUBLIC, PACKAGE_PRIVATE, PROTECTED, PRIVATE;

	/**
	 * @param accessSpecifier
	 * @return
	 */
	public static JavaElementVisibility fromAccessSpecifier(AccessSpecifier accessSpecifier) {
		switch (accessSpecifier) {
			case PACKAGE_PRIVATE:
				return PACKAGE_PRIVATE;
			case PRIVATE:
				return PRIVATE;
			case PROTECTED:
				return PROTECTED;
			case PUBLIC:
				return PUBLIC;
			default:
				return null;
		}
	}

	public static JavaElementVisibility fromModifier(int modifiers) {
		return Modifier.isPublic(modifiers) ? JavaElementVisibility.PUBLIC
			: Modifier.isProtected(modifiers) ? JavaElementVisibility.PROTECTED
				: Modifier.isPrivate(modifiers) ? JavaElementVisibility.PRIVATE : JavaElementVisibility.PACKAGE_PRIVATE;
	}

	public Keyword toJavaParserKeyword() {
		switch (this) {
			case PACKAGE_PRIVATE:
				return Keyword.DEFAULT;
			case PRIVATE:
				return Keyword.PRIVATE;
			case PROTECTED:
				return Keyword.PROTECTED;
			case PUBLIC:
				return Keyword.PUBLIC;
			default:
				return null;
		}
	}
}
