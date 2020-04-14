/**
 *
 * {@link TemplateTokenTypeTest}.java
 *
 * @author Jens Ebert
 *
 * @date 26.02.2020
 *
 */
package de.je.utils.j2a.template;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.j2a.core.template.TemplateTokenType;

/**
 * {@link TemplateTokenTypeTest} checks the utility methods of the
 * {@link TemplateTokenType} class.
 */
public class TemplateTokenTypeTest {
	/**
	 * Tests {@link TemplateTokenType#getBestMatchingTokenTypeFor(String)}.
	 */
	@Test
	public void getBestMatchingTokenTypeFor_forMatchingString_returnsLongestMatchingType() {
		Assertions.assertEquals(TemplateTokenType.BLOCK_DIRECTIVE_END,
			TemplateTokenType.getBestMatchingTokenTypeFor("<"));
		Assertions.assertEquals(TemplateTokenType.BLOCK_DIRECTIVE_END,
			TemplateTokenType.getBestMatchingTokenTypeFor("<%end PLAIN TEXT"));
		Assertions.assertEquals(TemplateTokenType.BLOCK_DIRECTIVE_END,
			TemplateTokenType.getBestMatchingTokenTypeFor("<%end"));
		Assertions.assertEquals(TemplateTokenType.DIRECTIVE, TemplateTokenType.getBestMatchingTokenTypeFor("<%test"));
		Assertions.assertEquals(TemplateTokenType.BLOCK_DIRECTIVE_END,
			TemplateTokenType.getBestMatchingTokenTypeFor("<%"));
	}

	/**
	 * Tests {@link TemplateTokenType#getBestMatchingTokenTypeFor(String)}.
	 */
	@Test
	public void getBestMatchingTokenTypeFor_forNonMatchingString_returnsPlainText() {
		Assertions.assertEquals(TemplateTokenType.PLAIN_TEXT, TemplateTokenType.getBestMatchingTokenTypeFor(""));
		Assertions.assertEquals(TemplateTokenType.PLAIN_TEXT,
			TemplateTokenType.getBestMatchingTokenTypeFor("PLAIN TEXT"));
		Assertions.assertEquals(TemplateTokenType.PLAIN_TEXT, TemplateTokenType.getBestMatchingTokenTypeFor("}"));
		Assertions.assertEquals(TemplateTokenType.PLAIN_TEXT, TemplateTokenType.getBestMatchingTokenTypeFor(">"));
	}

	/**
	 * Tests {@link TemplateTokenType#hasTokenTypeStartingWith(String)}.
	 */
	@Test
	public void hasTokenTypeStartingWith_forMatchingString_returnsTrue() {
		Assertions.assertTrue(TemplateTokenType.hasTokenTypeStartingWith("<%"));
		Assertions.assertTrue(TemplateTokenType.hasTokenTypeStartingWith("<%end PLAIN TEXT"));
		Assertions.assertTrue(TemplateTokenType.hasTokenTypeStartingWith("<%end"));
		Assertions.assertTrue(TemplateTokenType.hasTokenTypeStartingWith("${var"));
		Assertions.assertTrue(TemplateTokenType.hasTokenTypeStartingWith("%%%"));

	}

	/**
	 * Tests {@link TemplateTokenType#hasTokenTypeStartingWith(String)}.
	 */
	@Test
	public void hasTokenTypeStartingWith_forNonMatchingString_returnsFalse() {
		Assertions.assertFalse(TemplateTokenType.hasTokenTypeStartingWith("<"));
		Assertions.assertFalse(TemplateTokenType.hasTokenTypeStartingWith("x<%end PLAIN TEXT"));
		Assertions.assertFalse(TemplateTokenType.hasTokenTypeStartingWith("$"));
		Assertions.assertFalse(TemplateTokenType.hasTokenTypeStartingWith("%%"));

	}

	/**
	 * Tests {@link TemplateTokenType#isEndOf(String)}.
	 */
	@Test
	public void isEndOf_forMatchingString_returnsTrue() {
		Assertions.assertTrue(TemplateTokenType.BLOCK_DIRECTIVE_END.isEndOf("%>"));
		Assertions.assertTrue(TemplateTokenType.BLOCK_DIRECTIVE_END.isEndOf("TEST%>"));
		Assertions.assertTrue(TemplateTokenType.VARIABLE_REFERENCE.isEndOf("${var}"));
		Assertions.assertTrue(TemplateTokenType.VARIABLE_REFERENCE.isEndOf("} t}"));
	}

	/**
	 * Tests {@link TemplateTokenType#isEndOf(String)}.
	 */
	@Test
	public void isEndOf_forNonMatchingString_returnsFalse() {
		Assertions.assertFalse(TemplateTokenType.BLOCK_DIRECTIVE_END.isEndOf(""));
		Assertions.assertFalse(TemplateTokenType.BLOCK_DIRECTIVE_END.isEndOf("TEST"));
		Assertions.assertFalse(TemplateTokenType.VARIABLE_REFERENCE.isEndOf("${"));
		Assertions.assertFalse(TemplateTokenType.VARIABLE_REFERENCE.isEndOf("} t"));
		Assertions.assertFalse(TemplateTokenType.END_OF_STRING.isEndOf(""));
	}

}
