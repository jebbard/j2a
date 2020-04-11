/**
 *
 * {@link UTF8TextPosition}.java
 *
 * @author Jens Ebert
 *
 * @date 18.02.2020
 *
 */
package de.je.utils.j2a.template;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link UTF8TextPosition} tests the {@link UTF8TextPosition} class.
 */
public class UTF8TextPositionTest {

	/**
	 * Tests {@link UTF8TextPosition#advanceByString(String)}.
	 */
	@Test
	public void advance_forBlankTextNoLinebreaks_advancesByNumberOfWhitespaceChars() {
		UTF8TextPosition textPosition = new UTF8TextPosition(10, 12, 1, 12, 10);

		Assertions.assertEquals(new UTF8TextPosition(13, 15, 1, 15, 13), textPosition.advanceByString(" \t\f"));
	}

	/**
	 * Tests {@link UTF8TextPosition#advanceByString(String)}.
	 */
	@Test
	public void advance_forEmptyText_returnsSamePosition() {
		UTF8TextPosition textPosition = new UTF8TextPosition(10, 12, 1, 12, 10);

		Assertions.assertEquals(textPosition, textPosition.advanceByString(""));
	}

	/**
	 * Tests {@link UTF8TextPosition#advanceByString(String)}.
	 */
	@Test
	public void advance_forLineBreaksOnly_advancesByNCharsAndNLines() {
		UTF8TextPosition textPosition = new UTF8TextPosition(10, 12, 1, 12, 10);

		Assertions.assertEquals(new UTF8TextPosition(11, 13, 2, 0, 0), textPosition.advanceByString("\n"));
		Assertions.assertEquals(new UTF8TextPosition(13, 15, 4, 0, 0), textPosition.advanceByString("\n\n\n"));
		Assertions.assertEquals(new UTF8TextPosition(12, 14, 2, 0, 0), textPosition.advanceByString("\r\n"));
		Assertions.assertEquals(new UTF8TextPosition(16, 18, 4, 0, 0), textPosition.advanceByString("\r\n\r\n\r\n"));
		Assertions.assertEquals(new UTF8TextPosition(17, 19, 6, 0, 0), textPosition.advanceByString("\r\n\n\r\n\n\n"));
		Assertions.assertEquals(new UTF8TextPosition(19, 21, 7, 0, 0),
			textPosition.advanceByString("\r\n\n\r\n\n\n\r\n"));
	}

	/**
	 * Tests {@link UTF8TextPosition#advanceByString(String)}.
	 */
	@Test
	public void advance_forNullText_returnsSamePosition() {
		UTF8TextPosition textPosition = new UTF8TextPosition(10, 12, 1, 12, 10);

		Assertions.assertEquals(textPosition, textPosition.advanceByString(null));
	}

	/**
	 * Tests {@link UTF8TextPosition#advanceByString(String)}.
	 */
	@Test
	public void advance_forTextWithNonAsciiAndLinebreaks_advancesByNumberOfCharsAndLinebreaks() {
		UTF8TextPosition textPosition = new UTF8TextPosition(10, 12, 1, 12, 10);

		String testText = "J2A täst\n t\next \r\n1ä234%&//";

		Assertions.assertEquals(
			new UTF8TextPosition(textPosition.getCharacterPosition() + testText.length(),
				textPosition.getBytePosition() + testText.length() + 2, 4, 10, 9),
			textPosition.advanceByString(testText));
	}

	/**
	 * Tests {@link UTF8TextPosition#advanceByString(String)}.
	 */
	@Test
	public void advance_forTextWithNonAsciiNoLinebreaks_advancesByNumberOfCharsAndBytes() {
		UTF8TextPosition textPosition = new UTF8TextPosition(10, 12, 1, 12, 10);

		// Contains German umlauts which translate to two UTF8 bytes
		String testText = "J2A täestµ text 1ö234%&//";

		Assertions.assertEquals(new UTF8TextPosition(textPosition.getCharacterPosition() + testText.length(),
			textPosition.getBytePosition() + testText.length() + 3, 1,
			textPosition.getByteColumn() + testText.length() + 3,
			textPosition.getCharacterColumn() + testText.length()), textPosition.advanceByString(testText));
	}

	/**
	 * Tests {@link UTF8TextPosition#advanceByString(String)}.
	 */
	@Test
	public void advance_forTextWithOnlyAsciiLinebreaksAtEnd_advancesByNumberOfCharsAndLinebreaks() {
		UTF8TextPosition textPosition = new UTF8TextPosition(10, 12, 1, 12, 10);

		String testText = "J2A test text \n\r\n";

		Assertions.assertEquals(new UTF8TextPosition(textPosition.getCharacterPosition() + testText.length(),
			textPosition.getBytePosition() + testText.length(), 3, 0, 0), textPosition.advanceByString(testText));
	}

	/**
	 * Tests {@link UTF8TextPosition#advanceByString(String)}.
	 */
	@Test
	public void advance_forTextWithOnlyAsciiLinebreaksInTheMiddle_advancesByNumberOfCharsAndLinebreaks() {
		UTF8TextPosition textPosition = new UTF8TextPosition(10, 12, 1, 12, 10);

		String testText = "J2A test \n\r\n text";

		Assertions.assertEquals(new UTF8TextPosition(textPosition.getCharacterPosition() + testText.length(),
			textPosition.getBytePosition() + testText.length(), 3, 5, 5), textPosition.advanceByString(testText));

		testText = "J2A test \ntest\r\n text";

		Assertions.assertEquals(new UTF8TextPosition(textPosition.getCharacterPosition() + testText.length(),
			textPosition.getBytePosition() + testText.length(), 3, 5, 5), textPosition.advanceByString(testText));

		testText = "Plain Text %%% Comment\n\t %%% Next line of comment\nPlain text again";

		Assertions.assertEquals(new UTF8TextPosition(textPosition.getCharacterPosition() + testText.length(),
			textPosition.getBytePosition() + testText.length(), 3, 16, 16), textPosition.advanceByString(testText));
	}

	/**
	 * Tests {@link UTF8TextPosition#advanceByString(String)}.
	 */
	@Test
	public void advance_forTextWithOnlyAsciiNoLinebreaks_advancesByNumberOfChars() {
		UTF8TextPosition textPosition = new UTF8TextPosition(10, 12, 1, 12, 10);

		String testText = "J2A test text 1234%&//";

		Assertions.assertEquals(new UTF8TextPosition(textPosition.getCharacterPosition() + testText.length(),
			textPosition.getBytePosition() + testText.length(), 1, textPosition.getByteColumn() + testText.length(),
			textPosition.getCharacterColumn() + testText.length()), textPosition.advanceByString(testText));
	}
}
