/**
 *
 * {@link Utf8TextPositionTest}.java
 *
 * @author Jens Ebert
 *
 * @date 18.02.2020
 *
 */
package com.github.j2a.core.template;

import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * {@link UTF8TextPosition} represents a fixed position in a UTF8-encoded
 * string, including various representation to facilitate debugging. Note that
 * line numbers are starting with 1. This class only considers LF and CR+LF as
 * line breaks. Note that this is a constant class and to obtain further
 * {@link UTF8TextPosition}s based on this one, you have to use the
 * {@link #advanceByString(String)} method.
 */
public class UTF8TextPosition {
	private final int characterPosition;
	private final int bytePosition;
	private final int lineNumber;
	private final int byteColumn;
	private final int characterColumn;

	/**
	 * Creates a new {@link UTF8TextPosition} initialized to the beginning of a
	 * string.
	 */
	public UTF8TextPosition() {
		characterPosition = 0;
		bytePosition = 0;
		lineNumber = 1;
		byteColumn = 0;
		characterColumn = 0;
	}

	public UTF8TextPosition(int characterPosition, int bytePosition, int lineNumber, int byteColumn,
		int characterColumn) {
		this.characterPosition = characterPosition;
		this.bytePosition = bytePosition;
		this.lineNumber = lineNumber;
		this.byteColumn = byteColumn;
		this.characterColumn = characterColumn;
	}

	/**
	 * Advances this {@link UTF8TextPosition} by the given character, taking into
	 * account single-character line breaks.
	 *
	 * @param character The character to use to advance the
	 *                  {@link UTF8TextPosition}.
	 * @return An advanced version of the {@link UTF8TextPosition}.
	 */
	public UTF8TextPosition advanceByCharacter(char character) {
		return advanceByString(Character.valueOf(character).toString());
	}

	/**
	 * Advances this {@link UTF8TextPosition} by the given text content, taking into
	 * account characters and line breaks in the given string. If the string is
	 * null, the original {@link UTF8TextPosition} is returned, otherwise a new
	 * {@link UTF8TextPosition} is returned. This method never modifies
	 * {@link UTF8TextPosition} object it is called on.
	 *
	 * @param textContent The text content to use to advance the
	 *                    {@link UTF8TextPosition}.
	 * @return An advanced version of the {@link UTF8TextPosition}.
	 */
	public UTF8TextPosition advanceByString(String textContent) {
		if (textContent == null) {
			return this;
		}

		int lineCount = (int) textContent.lines().count();

		String lastLine = "";

		if (lineCount == 0) {
			lineCount = 1;
		} else {
			if (textContent.endsWith("\n") || textContent.endsWith("\r\n")) {
				lineCount++;
			}
			lastLine = textContent.lines().skip(lineCount - 1).collect(Collectors.joining());
		}

		int byteLength = getByteLengthOfUTF8String(textContent);

		int newCharacterPosition = characterPosition + textContent.length();
		int newBytePosition = bytePosition + byteLength;
		int newLineNumber = lineNumber + lineCount - 1;
		int newByteColumn = lineCount == 1 ? byteColumn + byteLength : getByteLengthOfUTF8String(lastLine);
		int newCharacterColumn = lineCount == 1 ? characterColumn + textContent.length() : lastLine.length();

		return new UTF8TextPosition(newCharacterPosition, newBytePosition, newLineNumber, newByteColumn,
			newCharacterColumn);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		UTF8TextPosition other = (UTF8TextPosition) obj;
		if (byteColumn != other.byteColumn) {
			return false;
		}
		if (bytePosition != other.bytePosition) {
			return false;
		}
		if (characterColumn != other.characterColumn) {
			return false;
		}
		if (characterPosition != other.characterPosition) {
			return false;
		}
		if (lineNumber != other.lineNumber) {
			return false;
		}
		return true;
	}

	public int getByteColumn() {
		return byteColumn;
	}

	public int getBytePosition() {
		return bytePosition;
	}

	public int getCharacterColumn() {
		return characterColumn;
	}

	public int getCharacterPosition() {
		return characterPosition;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + byteColumn;
		result = prime * result + bytePosition;
		result = prime * result + characterColumn;
		result = prime * result + characterPosition;
		result = prime * result + lineNumber;
		return result;
	}

	@Override
	public String toString() {
		return "UTF8TextPosition [characterPosition=" + characterPosition + ", bytePosition=" + bytePosition
			+ ", lineNumber=" + lineNumber + ", byteColumn=" + byteColumn + ", characterColumn=" + characterColumn
			+ "]";
	}

	private int getByteLengthOfUTF8String(String textContent) {
		return textContent.getBytes(StandardCharsets.UTF_8).length;
	}
}
