/**
 *
 * {@link TemplateTokenizerTest}.java
 *
 * @author Jens Ebert
 *
 * @date 18.02.2020
 *
 */
package de.je.utils.j2a.template;

import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.j2a.core.template.TemplateToken;
import com.github.j2a.core.template.TemplateTokenType;
import com.github.j2a.core.template.TemplateTokenizer;
import com.github.j2a.core.template.UTF8TextPosition;
import com.github.j2a.core.template.InvalidTemplateException.ExceptionCode;

import de.je.utils.j2a.testutils.TestExceptionHelper;

/**
 * {@link TemplateTokenizerTest} checks the {@link TemplateTokenizer}
 * implementation class.
 */
public class TemplateTokenizerTest {

	/**
	 * Tests {@link TemplateTokenizer#next()}.
	 */
	@Test
	public void next_forCommentTemplate_returnsCommentEndAndEOSTokens() {
		assertOnlySingleTokenAndEOS("%%% This is a comment", TemplateTokenType.SINGLE_LINE_COMMENT);
	}

	/**
	 * Tests {@link TemplateTokenizer#next()}.
	 */
	@Test
	public void next_forComplexTemplate_returnsAllExpectedTokens() {
		// @formatter:off
		String complexTemplate = "<%template name='myName' description='a test template'%>\n"
			+ "\n"
			+ "<%output mode='CREATE'%>\n"
			+ "Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum ${variable}\n"
			+ "TEST <%anyExample%>Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum \n"
			+ "\n"
			+ "\n"
			+ "\n"
			+ "Lorem ipsum Lorem ipsum <%end anyExample%>\n"
			+ "${var2}\n"
			+ "<%temp ${var}%>\n";
		// @formatter:on

		TemplateTokenizer tt = new TemplateTokenizer(new StringReader(complexTemplate));

		int currentPosition = assertNextToken(tt, 0, "<%template name='myName' description='a test template'%>",
			TemplateTokenType.DIRECTIVE, 1, 0);
		currentPosition = assertNextToken(tt, currentPosition, "\n\n", TemplateTokenType.PLAIN_TEXT, 1,
			currentPosition);
		currentPosition = assertNextToken(tt, currentPosition, "<%output mode='CREATE'%>", TemplateTokenType.DIRECTIVE,
			3, 0);
		currentPosition = assertNextToken(tt, currentPosition, "\nLorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum ",
			TemplateTokenType.PLAIN_TEXT, 3, 24);
		currentPosition = assertNextToken(tt, currentPosition, "${variable}", TemplateTokenType.VARIABLE_REFERENCE, 4,
			48);
		currentPosition = assertNextToken(tt, currentPosition, "\nTEST ", TemplateTokenType.PLAIN_TEXT, 4, 59);
		currentPosition = assertNextToken(tt, currentPosition, "<%anyExample%>", TemplateTokenType.DIRECTIVE, 5, 5);
		currentPosition = assertNextToken(tt, currentPosition,
			"Lorem ipsum Lorem ipsum Lorem ipsum Lorem ipsum \n\n\n\nLorem ipsum Lorem ipsum ",
			TemplateTokenType.PLAIN_TEXT, 5, 19);
		currentPosition = assertNextToken(tt, currentPosition, "<%end anyExample%>",
			TemplateTokenType.BLOCK_DIRECTIVE_END, 9, 24);
		currentPosition = assertNextToken(tt, currentPosition, "\n", TemplateTokenType.PLAIN_TEXT, 9, 42);
		currentPosition = assertNextToken(tt, currentPosition, "${var2}", TemplateTokenType.VARIABLE_REFERENCE, 10, 0);
		currentPosition = assertNextToken(tt, currentPosition, "\n", TemplateTokenType.PLAIN_TEXT, 10, 7);
		currentPosition = assertNextToken(tt, currentPosition, "<%temp ${var}%>", TemplateTokenType.DIRECTIVE, 11, 0);
		currentPosition = assertNextToken(tt, currentPosition, "\n", TemplateTokenType.PLAIN_TEXT, 11, 15);
		assertEndOfString(tt, currentPosition, 12, 0);
	}

	/**
	 * Tests {@link TemplateTokenizer#next()}.
	 */
	@Test
	public void next_forDirectiveEndTemplate_returnsDirectiveEndAndEOSTokens() {
		assertOnlySingleTokenAndEOS("<%end directive %>", TemplateTokenType.BLOCK_DIRECTIVE_END);
	}

	/**
	 * Tests {@link TemplateTokenizer#next()}.
	 */
	@Test
	public void next_forDirectiveEndTemplateContainingLineBreaks_returnsDirectiveEndAndEOSTokens() {
		assertOnlySingleTokenAndEOS("<%end directive \r\n attribute=\n'value'%>", TemplateTokenType.BLOCK_DIRECTIVE_END,
			3, 9);
	}

	/**
	 * Tests {@link TemplateTokenizer#next()}.
	 */
	@Test
	public void next_forDirectiveEndTemplateContainingLineBreaksAndVariable_returnsDirectiveEndAndEOSTokens() {
		assertOnlySingleTokenAndEOS("<%end directive \r\n attribute=\n'${var}'%>",
			TemplateTokenType.BLOCK_DIRECTIVE_END, 3, 10);
	}

	/**
	 * Tests {@link TemplateTokenizer#next()}.
	 */
	@Test
	public void next_forDirectiveTemplate_returnsDirectiveAndEOSTokens() {
		assertOnlySingleTokenAndEOS("<%directive %>", TemplateTokenType.DIRECTIVE);
	}

	/**
	 * Tests {@link TemplateTokenizer#next()}.
	 */
	@Test
	public void next_forDirectiveTemplateContainingLineBreaks_returnsDirectiveAndEOSTokens() {
		assertOnlySingleTokenAndEOS("<%directive \r\n attribute=\n'value'%>", TemplateTokenType.DIRECTIVE, 3, 9);
	}

	/**
	 * Tests {@link TemplateTokenizer#next()}.
	 */
	@Test
	public void next_forDirectiveTemplateContainingLineBreaksAndVariable_returnsDirectiveAndEOSTokens() {
		assertOnlySingleTokenAndEOS("<%directive \r\n attribute=\n'${var}'%>", TemplateTokenType.DIRECTIVE, 3, 10);
	}

	/**
	 * Tests {@link TemplateTokenizer#next()}.
	 */
	@Test
	public void next_forEmptyTemplate_returnsEOSTokenOnly() {
		TemplateTokenizer tt = new TemplateTokenizer(new StringReader(""));
		assertNextToken(tt, 0, "", TemplateTokenType.END_OF_STRING, 1, 0);
	}

	/**
	 * Tests {@link TemplateTokenizer#next()}.
	 */
	@Test
	public void next_forMultipleCommentsBetweenPlainText_returnsAllExpectedTokens() {
		String asciiOnlyTemplate = "Plain Text %%% Comment\n\t %%% Next line of comment\nPlain text again";
		TemplateTokenizer tt = new TemplateTokenizer(new StringReader(asciiOnlyTemplate));

		int currentPosition = assertNextToken(tt, 0, "Plain Text ", TemplateTokenType.PLAIN_TEXT, 1, 0);
		currentPosition = assertNextToken(tt, currentPosition, "%%% Comment\n", TemplateTokenType.SINGLE_LINE_COMMENT,
			1, 11);
		currentPosition = assertNextToken(tt, currentPosition, "\t ", TemplateTokenType.PLAIN_TEXT, 2, 0);
		currentPosition = assertNextToken(tt, currentPosition, "%%% Next line of comment\n",
			TemplateTokenType.SINGLE_LINE_COMMENT, 2, 2);
		currentPosition = assertNextToken(tt, currentPosition, "Plain text again", TemplateTokenType.PLAIN_TEXT, 3, 0);
		assertEndOfString(tt, currentPosition, 3, 16);
	}

	/**
	 * Tests {@link TemplateTokenizer#next()}.
	 */
	@Test
	public void next_forMultipleTokensAfterPlainText_returnsVariableAndEOSTokens() {
		String asciiOnlyTemplate = "Plain< T<%end mydir%>ext ${var} 098";
		TemplateTokenizer tt = new TemplateTokenizer(new StringReader(asciiOnlyTemplate));

		int currentPosition = assertNextToken(tt, 0, "Plain< T", TemplateTokenType.PLAIN_TEXT, 1, 0);
		currentPosition = assertNextToken(tt, currentPosition, "<%end mydir%>", TemplateTokenType.BLOCK_DIRECTIVE_END,
			1, 8);
		currentPosition = assertNextToken(tt, currentPosition, "ext ", TemplateTokenType.PLAIN_TEXT, 1, 21);
		currentPosition = assertNextToken(tt, currentPosition, "${var}", TemplateTokenType.VARIABLE_REFERENCE, 1, 25);
		currentPosition = assertNextToken(tt, currentPosition, " 098", TemplateTokenType.PLAIN_TEXT, 1, 31);
		assertEndOfString(tt, currentPosition, 1, asciiOnlyTemplate.length());
	}

	/**
	 * Tests {@link TemplateTokenizer#next()}.
	 */
	@Test
	public void next_forPlainTextTemplateNoLinebreaks_returnsPlainTextAndEOSTokens() {
		assertOnlySingleTokenAndEOS("<plain$text", TemplateTokenType.PLAIN_TEXT);
	}

	/**
	 * Tests {@link TemplateTokenizer#next()}.
	 */
	@Test
	public void next_forPlainTextTemplateOnlyWhitespace_returnsPlainTextAndEOSTokens() {
		assertOnlySingleTokenAndEOS("   \t\t \r\n \n  ", TemplateTokenType.PLAIN_TEXT, 3, 2);
	}

	/**
	 * Tests {@link TemplateTokenizer#next()}.
	 */
	@Test
	public void next_forPlainTextTemplateWithEndDelimiters_returnsPlainTextAndEOSTokens() {
		assertOnlySingleTokenAndEOS("<plain$text}and%>anyText", TemplateTokenType.PLAIN_TEXT);
	}

	/**
	 * Tests {@link TemplateTokenizer#next()}.
	 */
	@Test
	public void next_forPlainTextTemplateWithLineBreaks_returnsPlainTextAndEOSTokens() {
		assertOnlySingleTokenAndEOS("<plain$text}and%>\nany\r\nText", TemplateTokenType.PLAIN_TEXT, 3, 4);
	}

	/**
	 * Tests {@link TemplateTokenizer#next()}.
	 */
	@Test
	public void next_forRealLifeTemplate_returnsAllExpectedTokens() {
		TemplateTokenizer tt = new TemplateTokenizer(new InputStreamReader(
			TemplateTokenizerTest.class.getResourceAsStream("dto_template.java.j2a"), StandardCharsets.UTF_8));

		int currentPosition = assertNextToken(tt, 0,
			"<%template name='DTO from Entity' description='Contains a DTO (data transfer object) \n"
				+ "\tJava class that is created from a JPA entity. A DTO basically just has a default \n"
				+ "\tconstructor (not explicitly given), the same properties as the entity - except JPA \n"
				+ "\trelations which are replaced by foreign key references - and getters and setters for them.'%>",
			TemplateTokenType.DIRECTIVE, 1, 0);
		currentPosition = assertNextToken(tt, currentPosition, "\n\n", TemplateTokenType.PLAIN_TEXT, 4, 94);
		currentPosition = assertNextToken(tt, currentPosition, "${dto:targetClass}",
			TemplateTokenType.VARIABLE_REFERENCE, 6, 0);
		currentPosition = assertNextToken(tt, currentPosition, ":=", TemplateTokenType.PLAIN_TEXT, 6, 18);
		currentPosition = assertNextToken(tt, currentPosition, "${j2a:source.name}",
			TemplateTokenType.VARIABLE_REFERENCE, 6, 20);
		currentPosition = assertNextToken(tt, currentPosition, "DTO\n\n", TemplateTokenType.PLAIN_TEXT, 6, 38);
		currentPosition = assertNextToken(tt, currentPosition,
			"<%output path='${j2a:source.package}/../../api/dto' file='${dto:targetClass}.java' mode='CREATE'%>",
			TemplateTokenType.DIRECTIVE, 8, 0);
		currentPosition = assertNextToken(tt, currentPosition, "\n\n", TemplateTokenType.PLAIN_TEXT, 8, 98);
		currentPosition = assertNextToken(tt, currentPosition, "${dto:targetPackage}",
			TemplateTokenType.VARIABLE_REFERENCE, 10, 0);
		currentPosition = assertNextToken(tt, currentPosition, ":=", TemplateTokenType.PLAIN_TEXT, 10, 20);
		currentPosition = assertNextToken(tt, currentPosition, "${package(j2a:source.package)/../../api.dto}",
			TemplateTokenType.VARIABLE_REFERENCE, 10, 22);
		currentPosition = assertNextToken(tt, currentPosition, "\n\npackage ", TemplateTokenType.PLAIN_TEXT, 10, 66);
		currentPosition = assertNextToken(tt, currentPosition, "${dto:targetPackage}",
			TemplateTokenType.VARIABLE_REFERENCE, 12, 8);
		currentPosition = assertNextToken(tt, currentPosition, ";\n\npublic class ", TemplateTokenType.PLAIN_TEXT, 12,
			28);
		currentPosition = assertNextToken(tt, currentPosition, "${dto:targetClass}",
			TemplateTokenType.VARIABLE_REFERENCE, 14, 13);
		currentPosition = assertNextToken(tt, currentPosition, " extends ", TemplateTokenType.PLAIN_TEXT, 14, 31);
		currentPosition = assertNextToken(tt, currentPosition, "${dto:targetBaseClass}",
			TemplateTokenType.VARIABLE_REFERENCE, 14, 40);
		currentPosition = assertNextToken(tt, currentPosition, " {\n\n\t", TemplateTokenType.PLAIN_TEXT, 14, 62);
		currentPosition = assertNextToken(tt, currentPosition, "%%% The field declarations\n",
			TemplateTokenType.SINGLE_LINE_COMMENT, 16, 1);
		currentPosition = assertNextToken(tt, currentPosition, "\t", TemplateTokenType.PLAIN_TEXT, 17, 0);
		currentPosition = assertNextToken(tt, currentPosition, "<%foreach field f in ${j2a:source.fields}%>",
			TemplateTokenType.DIRECTIVE, 17, 1);
		currentPosition = assertNextToken(tt, currentPosition, "\n\t\t", TemplateTokenType.PLAIN_TEXT, 17, 44);
		currentPosition = assertNextToken(tt, currentPosition, "%%% JPA relationships are mapped differently\n",
			TemplateTokenType.SINGLE_LINE_COMMENT, 18, 2);
		currentPosition = assertNextToken(tt, currentPosition, "\t\t", TemplateTokenType.PLAIN_TEXT, 19, 0);
		currentPosition = assertNextToken(tt, currentPosition, "<%if (f.getType().isApplicationClass())%>",
			TemplateTokenType.DIRECTIVE, 19, 2);
		currentPosition = assertNextToken(tt, currentPosition, "\n\tprivate ", TemplateTokenType.PLAIN_TEXT, 19, 43);
		currentPosition = assertNextToken(tt, currentPosition,
			"${f.getType().getFields()[f -> f.getAnnotations()[a -> a.getName().equals('javax.persistence.@Id')]].getType()}",
			TemplateTokenType.VARIABLE_REFERENCE, 20, 9);
		currentPosition = assertNextToken(tt, currentPosition, " ", TemplateTokenType.PLAIN_TEXT, 20, 120);
		currentPosition = assertNextToken(tt, currentPosition, "${f.getName()}", TemplateTokenType.VARIABLE_REFERENCE,
			20, 121);
		currentPosition = assertNextToken(tt, currentPosition, ";\n\t\t", TemplateTokenType.PLAIN_TEXT, 20, 135);
		currentPosition = assertNextToken(tt, currentPosition, "<%else%>", TemplateTokenType.DIRECTIVE, 21, 2);
		currentPosition = assertNextToken(tt, currentPosition, "\n\tprivate ", TemplateTokenType.PLAIN_TEXT, 21, 10);
		currentPosition = assertNextToken(tt, currentPosition, "${f.getType().getName()}",
			TemplateTokenType.VARIABLE_REFERENCE, 22, 9);
		currentPosition = assertNextToken(tt, currentPosition, " ", TemplateTokenType.PLAIN_TEXT, 22, 33);
		currentPosition = assertNextToken(tt, currentPosition, "${f.getName()}", TemplateTokenType.VARIABLE_REFERENCE,
			22, 34);
		currentPosition = assertNextToken(tt, currentPosition, ";\n\t\t", TemplateTokenType.PLAIN_TEXT, 22, 48);
		currentPosition = assertNextToken(tt, currentPosition, "<%end if%>", TemplateTokenType.BLOCK_DIRECTIVE_END, 23,
			2);
		currentPosition = assertNextToken(tt, currentPosition, "\n\t", TemplateTokenType.PLAIN_TEXT, 23, 12);
		currentPosition = assertNextToken(tt, currentPosition, "<%end foreach%>", TemplateTokenType.BLOCK_DIRECTIVE_END,
			24, 1);
		currentPosition = assertNextToken(tt, currentPosition, "\n\n\t", TemplateTokenType.PLAIN_TEXT, 24, 16);
		currentPosition = assertNextToken(tt, currentPosition, "%%% The getter and setter declarations\n",
			TemplateTokenType.SINGLE_LINE_COMMENT, 26, 1);
		currentPosition = assertNextToken(tt, currentPosition, "\t", TemplateTokenType.PLAIN_TEXT, 27, 0);
		currentPosition = assertNextToken(tt, currentPosition, "<%foreach field f in ${j2a:source.fields}%>",
			TemplateTokenType.DIRECTIVE, 27, 1);
		currentPosition = assertNextToken(tt, currentPosition, "\n\tpublic ", TemplateTokenType.PLAIN_TEXT, 27, 44);
		currentPosition = assertNextToken(tt, currentPosition, "${f.getType().getName()}",
			TemplateTokenType.VARIABLE_REFERENCE, 28, 8);
		currentPosition = assertNextToken(tt, currentPosition, " get", TemplateTokenType.PLAIN_TEXT, 28, 32);
		currentPosition = assertNextToken(tt, currentPosition, "${uppercaseFirst(f.getName())}",
			TemplateTokenType.VARIABLE_REFERENCE, 28, 36);
		currentPosition = assertNextToken(tt, currentPosition, "() {\n\t\treturn ", TemplateTokenType.PLAIN_TEXT, 28,
			66);
		currentPosition = assertNextToken(tt, currentPosition, "${f.getName()}", TemplateTokenType.VARIABLE_REFERENCE,
			29, 9);
		currentPosition = assertNextToken(tt, currentPosition, ";\n\t}\n\n\tpublic void set",
			TemplateTokenType.PLAIN_TEXT, 29, 23);
		currentPosition = assertNextToken(tt, currentPosition, "${uppercaseFirst(f.getName())}",
			TemplateTokenType.VARIABLE_REFERENCE, 32, 16);
		currentPosition = assertNextToken(tt, currentPosition, "(", TemplateTokenType.PLAIN_TEXT, 32, 46);
		currentPosition = assertNextToken(tt, currentPosition, "${f.getType().getName()}",
			TemplateTokenType.VARIABLE_REFERENCE, 32, 47);
		currentPosition = assertNextToken(tt, currentPosition, " ", TemplateTokenType.PLAIN_TEXT, 32, 71);
		currentPosition = assertNextToken(tt, currentPosition, "${f.getName()}", TemplateTokenType.VARIABLE_REFERENCE,
			32, 72);
		currentPosition = assertNextToken(tt, currentPosition, ") {\n\t\tthis.", TemplateTokenType.PLAIN_TEXT, 32, 86);
		currentPosition = assertNextToken(tt, currentPosition, "${f.getName()}", TemplateTokenType.VARIABLE_REFERENCE,
			33, 7);
		currentPosition = assertNextToken(tt, currentPosition, " = ", TemplateTokenType.PLAIN_TEXT, 33, 21);
		currentPosition = assertNextToken(tt, currentPosition, "${f.getName()}", TemplateTokenType.VARIABLE_REFERENCE,
			33, 24);
		currentPosition = assertNextToken(tt, currentPosition, ";\n\t}\n\t", TemplateTokenType.PLAIN_TEXT, 33, 38);
		currentPosition = assertNextToken(tt, currentPosition, "<%end foreach%>", TemplateTokenType.BLOCK_DIRECTIVE_END,
			35, 1);
		currentPosition = assertNextToken(tt, currentPosition, "\n}", TemplateTokenType.PLAIN_TEXT, 35, 16);
		assertEndOfString(tt, currentPosition, 36, 1);
	}

	/**
	 * Tests {@link TemplateTokenizer#next()}.
	 */
	@Test
	public void next_forVariableTemplate_returnsVariableAndEOSTokens() {
		assertOnlySingleTokenAndEOS("${var}", TemplateTokenType.VARIABLE_REFERENCE);
	}

	/**
	 * Tests {@link TemplateTokenizer#next()}.
	 */
	@Test
	public void next_forVariableTemplateAfterPlainText_returnsVariableAndEOSTokens() {
		String asciiOnlyTemplate = "Plain Text${var}";
		TemplateTokenizer tt = new TemplateTokenizer(new StringReader(asciiOnlyTemplate));

		int currentPosition = assertNextToken(tt, 0, "Plain Text", TemplateTokenType.PLAIN_TEXT, 1, 0);
		currentPosition = assertNextToken(tt, currentPosition, "${var}", TemplateTokenType.VARIABLE_REFERENCE, 1, 10);
		assertEndOfString(tt, currentPosition, 1, asciiOnlyTemplate.length());
	}

	/**
	 * Tests {@link TemplateTokenizer#next()}.
	 */
	@Test
	public void next_forVariableTemplateContainingLineBreaks_returnsVariableAndEOSTokens() {
		assertOnlySingleTokenAndEOS("${v\r\na\nr}", TemplateTokenType.VARIABLE_REFERENCE, 3, 2);
	}

	/**
	 * Tests {@link TemplateTokenizer#next()}.
	 */
	@Test
	public void next_forVariableTemplateContainingLineBreaksAndDirective_returnsVariableAndEOSTokens() {
		assertOnlySingleTokenAndEOS("${v\r\na\nr<%end test %>}", TemplateTokenType.VARIABLE_REFERENCE, 3, 15);
	}

	/**
	 * Tests {@link TemplateTokenizer#next()}.
	 */
	@Test
	public void next_withPrematureEOSInDirective_throwsException() {
		String templateWithError = "TEST<%directive %";

		TemplateTokenizer tt = new TemplateTokenizer(new StringReader(templateWithError));

		assertNextToken(tt, 0, "TEST", TemplateTokenType.PLAIN_TEXT, 1, 0);

		TestExceptionHelper.assertThrowsInvalidTemplateExceptionWithCode(ExceptionCode.PREMATURE_EOS_IN_DIRECTIVE,
			new UTF8TextPosition(17, 17, 1, 17, 17), () -> tt.next());
	}

	/**
	 * Tests {@link TemplateTokenizer#next()}.
	 */
	@Test
	public void next_withPrematureEOSInEndDirective_throwsException() {
		String templateWithError = "TEST<%end foreac\nh ";

		TemplateTokenizer tt = new TemplateTokenizer(new StringReader(templateWithError));

		assertNextToken(tt, 0, "TEST", TemplateTokenType.PLAIN_TEXT, 1, 0);

		TestExceptionHelper.assertThrowsInvalidTemplateExceptionWithCode(ExceptionCode.PREMATURE_EOS_IN_END_DIRECTIVE,
			new UTF8TextPosition(19, 19, 2, 2, 2), () -> tt.next());
	}

	/**
	 * Tests {@link TemplateTokenizer#next()}.
	 */
	@Test
	public void next_withPrematureEOSInVariable_throwsException() {
		String templateWithError = "TEST${v\nr";

		TemplateTokenizer tt = new TemplateTokenizer(new StringReader(templateWithError));

		assertNextToken(tt, 0, "TEST", TemplateTokenType.PLAIN_TEXT, 1, 0);

		TestExceptionHelper.assertThrowsInvalidTemplateExceptionWithCode(ExceptionCode.PREMATURE_EOS_IN_VARIABLE,
			new UTF8TextPosition(9, 9, 2, 1, 1), () -> tt.next());
	}

	private void assertEndOfString(TemplateTokenizer tt, int currentContentLength, int expectedLine,
		int expectedColumn) {
		assertNextToken(tt, currentContentLength, "", TemplateTokenType.END_OF_STRING, expectedLine, expectedColumn);

		Assertions.assertFalse(tt.hasNext());
	}

	private int assertNextToken(TemplateTokenizer tt, int currentContentLength, String expectedAsciiContent,
		TemplateTokenType expectedTokenType, int expectedLine, int expectedColumn) {

		Assertions.assertTrue(tt.hasNext());

		Assertions.assertEquals(new TemplateToken(expectedTokenType, expectedAsciiContent, new UTF8TextPosition(
			currentContentLength, currentContentLength, expectedLine, expectedColumn, expectedColumn)), tt.next());

		return currentContentLength + expectedAsciiContent.length();
	}

	/**
	 * Verifies that, for the given ASCII character only template, that it contains
	 * only a single token and an end of string token.
	 *
	 * @param asciiOnlyTemplate The template string, must only contain ASCII
	 *                          characters, and expected to contain only plain text
	 *                          and no other tokens until its end
	 * @param expectedTokenType The expected type of the first token
	 */
	private void assertOnlySingleTokenAndEOS(String asciiOnlyTemplate, TemplateTokenType expectedTokenType) {
		assertOnlySingleTokenAndEOS(asciiOnlyTemplate, expectedTokenType, 1, asciiOnlyTemplate.length());
	}

	/**
	 * Verifies that, for the given ASCII character only template - potentially
	 * containing multiple lines -, that it contains only a single token and an end
	 * of string token.
	 *
	 * @param asciiOnlyTemplate The template string, must only contain ASCII
	 *                          characters, and expected to contain only plain text
	 *                          and no other tokens until its end
	 * @param expectedTokenType The expected type of the first token
	 * @param expectedLine      The expected line of the EOS token
	 * @param expectedColumn    The expected byte and character column of the EOS
	 *                          token
	 */
	private void assertOnlySingleTokenAndEOS(String asciiOnlyTemplate, TemplateTokenType expectedTokenType,
		int expectedLine, int expectedColumn) {
		TemplateTokenizer tt = new TemplateTokenizer(new StringReader(asciiOnlyTemplate));

		int currentPosition = assertNextToken(tt, 0, asciiOnlyTemplate, expectedTokenType, 1, 0);
		assertEndOfString(tt, currentPosition, expectedLine, expectedColumn);
	}
}
