/**
 *
 * {@link VariableReferenceParserTest}.java
 *
 * @author Jens Ebert
 *
 * @date 03.03.2020
 *
 */
package de.je.utils.j2a.template;

import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.github.j2a.core.template.TemplateToken;
import com.github.j2a.core.template.TemplateTokenType;
import com.github.j2a.core.template.UTF8TextPosition;
import com.github.j2a.core.template.VariableReference;
import com.github.j2a.core.template.VariableReferenceParser;
import com.github.j2a.core.template.InvalidTemplateException.ExceptionCode;

import de.je.utils.j2a.testutils.TestExceptionHelper;

/**
 * {@link VariableReferenceParserTest} checks the {@link VariableReferenceParser} class for validity.
 */
@Disabled
public class VariableReferenceParserTest {
	/**
	 * Tests {@link VariableReferenceParser#isIndicatingTemplateSection(TemplateToken)}.
	 */
	@Test
	public void isIndicatingTemplateSection_forNonVariableToken_returnsFalse() {
		VariableReferenceParser parser = new VariableReferenceParser();

		Assertions.assertFalse(parser
			.isIndicatingTemplateSection(new TemplateToken(TemplateTokenType.DIRECTIVE, "", new UTF8TextPosition())));
		Assertions.assertFalse(parser.isIndicatingTemplateSection(
			new TemplateToken(TemplateTokenType.END_OF_STRING, "", new UTF8TextPosition())));
	}

	/**
	 * Tests {@link VariableReferenceParser#isIndicatingTemplateSection(TemplateToken)}.
	 */
	@Test
	public void isIndicatingTemplateSection_forVariableToken_returnsTrue() {
		VariableReferenceParser parser = new VariableReferenceParser();

		Assertions.assertTrue(parser.isIndicatingTemplateSection(createDefaultVariableNoNamespace()));
	}

	/**
	 * Tests {@link VariableReferenceParser#parseTemplateSection(TemplateToken, java.util.Iterator)}.
	 */
	@Test
	public void parseTemplateSection_forNonVariableToken_throwsException() {
		assertThrowsIllegalArgumentExceptionForInvalidVariable(
			new TemplateToken(TemplateTokenType.DIRECTIVE, "", new UTF8TextPosition()));
	}

	/**
	 * Tests {@link VariableReferenceParser#parseTemplateSection(TemplateToken, java.util.Iterator)}.
	 */
	@Test
	public void parseTemplateSection_forValidVariableSectionNoNamespace_parsesCorrectly() {
		assertDefaultVariableIsParsed(createDefaultVariableNoNamespace(), 6, "", "var");
	}

	/**
	 * Tests {@link VariableReferenceParser#parseTemplateSection(TemplateToken, java.util.Iterator)}.
	 */
	@Test
	public void parseTemplateSection_forValidVariableSectionWithNamespace_parsesCorrectly() {
		assertDefaultVariableIsParsed(createDefaultVariableWithNamespace(), 9, "ns", "var");
	}

	/**
	 * Tests {@link VariableReferenceParser#parseTemplateSection(TemplateToken, java.util.Iterator)}.
	 */
	@Test
	public void parseTemplateSection_forVariableTokenWithInvalidContent_throwsException() {
		assertThrowsInvalidTemplateExceptionWithCode("${ns:name with\n white\tspace}", ExceptionCode.IO_EXCEPTION,
			new UTF8TextPosition(2, 2, 1, 2, 2));
		assertThrowsInvalidTemplateExceptionWithCode("${ns:namewith\twspace}", ExceptionCode.IO_EXCEPTION,
			new UTF8TextPosition(2, 2, 1, 2, 2));
		assertThrowsInvalidTemplateExceptionWithCode("${ns:name${t}", ExceptionCode.IO_EXCEPTION,
			new UTF8TextPosition(2, 2, 1, 2, 2));
		assertThrowsInvalidTemplateExceptionWithCode("${ns:9}", ExceptionCode.IO_EXCEPTION,
			new UTF8TextPosition(2, 2, 1, 2, 2));
		assertThrowsInvalidTemplateExceptionWithCode("${ns:9te}", ExceptionCode.IO_EXCEPTION,
			new UTF8TextPosition(2, 2, 1, 2, 2));
		assertThrowsInvalidTemplateExceptionWithCode("${ns:öart}", ExceptionCode.IO_EXCEPTION,
			new UTF8TextPosition(2, 2, 1, 2, 2));
		assertThrowsInvalidTemplateExceptionWithCode("${ns:/art}", ExceptionCode.IO_EXCEPTION,
			new UTF8TextPosition(2, 2, 1, 2, 2));
		assertThrowsInvalidTemplateExceptionWithCode("${ns:tr&r}", ExceptionCode.IO_EXCEPTION,
			new UTF8TextPosition(2, 2, 1, 2, 2));
		assertThrowsInvalidTemplateExceptionWithCode("${name with\n white\tspace}", ExceptionCode.IO_EXCEPTION,
			new UTF8TextPosition(2, 2, 1, 2, 2));
		assertThrowsInvalidTemplateExceptionWithCode("${namewith\twspace}", ExceptionCode.IO_EXCEPTION,
			new UTF8TextPosition(2, 2, 1, 2, 2));
		assertThrowsInvalidTemplateExceptionWithCode("${name${t}", ExceptionCode.IO_EXCEPTION,
			new UTF8TextPosition(2, 2, 1, 2, 2));
		assertThrowsInvalidTemplateExceptionWithCode("${9}", ExceptionCode.IO_EXCEPTION,
			new UTF8TextPosition(2, 2, 1, 2, 2));
		assertThrowsInvalidTemplateExceptionWithCode("${9te}", ExceptionCode.IO_EXCEPTION,
			new UTF8TextPosition(2, 2, 1, 2, 2));
		assertThrowsInvalidTemplateExceptionWithCode("${öart}", ExceptionCode.IO_EXCEPTION,
			new UTF8TextPosition(2, 2, 1, 2, 2));
		assertThrowsInvalidTemplateExceptionWithCode("${/art}", ExceptionCode.IO_EXCEPTION,
			new UTF8TextPosition(2, 2, 1, 2, 2));
		assertThrowsInvalidTemplateExceptionWithCode("${tr&r}", ExceptionCode.IO_EXCEPTION,
			new UTF8TextPosition(2, 2, 1, 2, 2));
		assertThrowsInvalidTemplateExceptionWithCode("${ns:ar:t}", ExceptionCode.IO_EXCEPTION,
			new UTF8TextPosition(2, 2, 1, 2, 2));
		assertThrowsInvalidTemplateExceptionWithCode("${:name}", ExceptionCode.IO_EXCEPTION,
			new UTF8TextPosition(2, 2, 1, 2, 2));

		assertThrowsInvalidTemplateExceptionWithCode("${ns with whitespace:name}", ExceptionCode.IO_EXCEPTION,
			new UTF8TextPosition(2, 2, 1, 2, 2));
		assertThrowsInvalidTemplateExceptionWithCode("${n\ts:name}", ExceptionCode.IO_EXCEPTION,
			new UTF8TextPosition(2, 2, 1, 2, 2));
		assertThrowsInvalidTemplateExceptionWithCode("${n$s:name}", ExceptionCode.IO_EXCEPTION,
			new UTF8TextPosition(2, 2, 1, 2, 2));
		assertThrowsInvalidTemplateExceptionWithCode("${näs:name}", ExceptionCode.IO_EXCEPTION,
			new UTF8TextPosition(2, 2, 1, 2, 2));
		assertThrowsInvalidTemplateExceptionWithCode("${9ns:t}", ExceptionCode.IO_EXCEPTION,
			new UTF8TextPosition(2, 2, 1, 2, 2));
		assertThrowsInvalidTemplateExceptionWithCode("${//ns:te}", ExceptionCode.IO_EXCEPTION,
			new UTF8TextPosition(2, 2, 1, 2, 2));

		assertThrowsInvalidTemplateExceptionWithCode("${nswithoutvar:}", ExceptionCode.IO_EXCEPTION,
			new UTF8TextPosition(2, 2, 1, 2, 2));
	}

	/**
	 * Tests {@link VariableReferenceParser#parseTemplateSection(TemplateToken, java.util.Iterator)}.
	 */
	@Test
	public void parseTemplateSection_forVariableTokenWithoutDelimiters_throwsException() {
		assertThrowsIllegalArgumentExceptionForInvalidVariable(
			new TemplateToken(TemplateTokenType.VARIABLE_REFERENCE, "var}", new UTF8TextPosition()));
		assertThrowsIllegalArgumentExceptionForInvalidVariable(
			new TemplateToken(TemplateTokenType.VARIABLE_REFERENCE, "v${ar}", new UTF8TextPosition()));
		assertThrowsIllegalArgumentExceptionForInvalidVariable(
			new TemplateToken(TemplateTokenType.VARIABLE_REFERENCE, "${var", new UTF8TextPosition()));
		assertThrowsIllegalArgumentExceptionForInvalidVariable(
			new TemplateToken(TemplateTokenType.VARIABLE_REFERENCE, "${va{r", new UTF8TextPosition()));
	}

	private void assertDefaultVariableIsParsed(TemplateToken token, int expectedEndPosition, String expectedNamespace,
		String expectedLocalName) {
		VariableReferenceParser parser = new VariableReferenceParser();

		Assertions.assertTrue(parser.isIndicatingTemplateSection(token));

		VariableReference ref = parser.parseTemplateSection(token, new ArrayList<TemplateToken>().iterator());

		Assertions.assertNotNull(ref);
		Assertions.assertEquals(new UTF8TextPosition(), ref.getTemplateStartPosition());
		Assertions.assertEquals(
			new UTF8TextPosition(expectedEndPosition, expectedEndPosition, 1, expectedEndPosition, expectedEndPosition),
			ref.getTemplateEndPosition());
		Assertions.assertEquals(expectedLocalName, ref.getLocalName());
		Assertions.assertEquals(expectedNamespace, ref.getNamespace());
	}

	private void assertThrowsIllegalArgumentExceptionForInvalidVariable(TemplateToken token) {
		VariableReferenceParser parser = new VariableReferenceParser();

		Assertions.assertThrows(IllegalArgumentException.class,
			() -> parser.parseTemplateSection(token, new ArrayList<TemplateToken>().iterator()));
	}

	private void assertThrowsInvalidTemplateExceptionWithCode(String variableContent, ExceptionCode expectedCode,
		UTF8TextPosition expectedPosition) {
		TestExceptionHelper.assertThrowsInvalidTemplateExceptionWithCode(expectedCode, expectedPosition, () -> {
			VariableReferenceParser parser = new VariableReferenceParser();
			parser.parseTemplateSection(
				new TemplateToken(TemplateTokenType.VARIABLE_REFERENCE, variableContent, new UTF8TextPosition()),
				new ArrayList<TemplateToken>().iterator());
		});
	}

	private TemplateToken createDefaultVariableNoNamespace() {
		return new TemplateToken(TemplateTokenType.VARIABLE_REFERENCE, "${var}", new UTF8TextPosition());
	}

	private TemplateToken createDefaultVariableWithNamespace() {
		return new TemplateToken(TemplateTokenType.VARIABLE_REFERENCE, "${ns:var}", new UTF8TextPosition());
	}

}
