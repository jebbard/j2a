/**
 *
 * {@link TemplateTokenizer}.java
 *
 * @author Jens Ebert
 *
 * @date 18.02.2020
 *
 */
package com.github.j2a.core.template;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

import com.github.j2a.core.template.InvalidTemplateException.ExceptionCode;

/**
 * {@link TemplateTokenizer} splits up a template string into its top-level
 * components, i.e. directives, plain text and variables.
 */
public class TemplateTokenizer implements Iterator<TemplateToken> {
	private final Reader reader;
	private UTF8TextPosition nextTokenPosition = new UTF8TextPosition();
	private String readAheadBuffer = "";
	private boolean hasNext = true;

	public TemplateTokenizer(Reader reader) {
		this.reader = reader;
	}

	public UTF8TextPosition getNextTokenPosition() {
		return nextTokenPosition;
	}

	/**
	 * Returns true if and only if the {@link TemplateTokenType#END_OF_STRING} token
	 * has not yet been read.
	 *
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public boolean hasNext() {
		return hasNext;
	}

	/**
	 * Reads the next {@link TemplateToken} from the provider {@link Reader}. If the
	 * end of string (EOS) is reached during parsing, this method first returns the
	 * last token before EOS, and the next call to this method will return a token
	 * of type {@link TemplateTokenType#END_OF_STRING}.
	 *
	 * In case that the start of a token was identified but the end delimiter was
	 * not found before EOS, this method throws an {@link InvalidTemplateException}.
	 * This exception is also thrown in case of any {@link IOException} emitted by
	 * the underlying Java IO libraries.
	 *
	 * As {@link TemplateTokenType#PLAIN_TEXT} tokens are only delimited by the
	 * start delimiters of other tokens or EOS, this method might need to read ahead
	 * the next token already to be able to return a token of type
	 * {@link TemplateTokenType#PLAIN_TEXT}.
	 *
	 * @return The next {@link TemplateToken} in the template {@link Reader}.
	 */
	@Override
	public TemplateToken next() {
		// This is the full read buffer that contains all text parsed so far since the
		// last token read or start of string
		StringBuilder fullReadBuffer = new StringBuilder(readAheadBuffer);
		// This contains any identified token start string, so it might be shorter as
		// the full read buffer
		StringBuilder currentTokenStartMatchCandidateBuffer = new StringBuilder(readAheadBuffer);

		TemplateToken nextToken = null;

		// Holds a character-correct current position of parsing for exact error
		// location reporting
		UTF8TextPosition currentParsePosition = nextTokenPosition.advanceByString(readAheadBuffer);

		try {
			//
			TemplateTokenType startMatchCandidate = null;
			boolean exactStartMatch = false;

			while (nextToken == null) {
				int nextCharacterAsInt = reader.read();

				if (nextCharacterAsInt == -1) {
					nextToken = handleEndOfString(currentParsePosition, startMatchCandidate, exactStartMatch,
						fullReadBuffer.toString());
				} else {
					char nextCharacter = (char) nextCharacterAsInt;

					fullReadBuffer.append(nextCharacter);
					currentTokenStartMatchCandidateBuffer.append(nextCharacter);

					currentParsePosition = currentParsePosition.advanceByCharacter(nextCharacter);
				}

				final String tokenStartMatchCandidate = currentTokenStartMatchCandidateBuffer.toString();

				startMatchCandidate = TemplateTokenType.getBestMatchingTokenTypeFor(tokenStartMatchCandidate);
				exactStartMatch = TemplateTokenType.hasTokenTypeStartingWith(tokenStartMatchCandidate);

				if (startMatchCandidate == TemplateTokenType.PLAIN_TEXT) {
					currentTokenStartMatchCandidateBuffer = new StringBuilder();
				} else if (exactStartMatch) {
					if (tokenStartMatchCandidate.length() != fullReadBuffer.length()) {
						// There is some plain text before the current match candidate that we have to
						// return first
						String plainText = fullReadBuffer.substring(0,
							fullReadBuffer.length() - tokenStartMatchCandidate.length());

						nextToken = createTokenAndAdvancePosition(TemplateTokenType.PLAIN_TEXT, plainText,
							tokenStartMatchCandidate);
					} else if (startMatchCandidate.isEndOf(tokenStartMatchCandidate)) {
						nextToken = createTokenAndAdvancePosition(startMatchCandidate, tokenStartMatchCandidate, "");
					}
				}
			}
		} catch (IOException e) {
			throw new InvalidTemplateException(ExceptionCode.IO_EXCEPTION, getNextTokenPosition());
		}

		return nextToken;
	}

	private TemplateToken createTokenAndAdvancePosition(TemplateTokenType type, String tokenContent,
		String newReadAheadBufferValue) {
		TemplateToken templateToken = new TemplateToken(type, tokenContent, nextTokenPosition);
		nextTokenPosition = nextTokenPosition.advanceByString(tokenContent);
		readAheadBuffer = newReadAheadBufferValue;
		return templateToken;
	}

	/**
	 * Determines the next {@link TemplateToken} in the event that end of template
	 * string has been reached.
	 *
	 * @param eosPosition         The current parse {@link UTF8TextPosition} the EOS
	 *                            has been detected at.
	 * @param startMatchCandidate The previously found start match candidate string
	 *                            or null if no such thing
	 * @param exactStartMatch     True if there has been an exact start match
	 *                            before, false otherwise
	 * @param readBufferContent   The overall read characters for the next token
	 *                            determination
	 * @return A {@link TemplateToken} found before EOS or null if there is none
	 */
	private TemplateToken handleEndOfString(UTF8TextPosition eosPosition, TemplateTokenType startMatchCandidate,
		boolean exactStartMatch, String readBufferContent) {
		TemplateToken nextToken = null;
		if (exactStartMatch) {
			switch (startMatchCandidate) {
				case DIRECTIVE:
					throw new InvalidTemplateException(ExceptionCode.PREMATURE_EOS_IN_DIRECTIVE, eosPosition);
				case BLOCK_DIRECTIVE_END:
					throw new InvalidTemplateException(ExceptionCode.PREMATURE_EOS_IN_END_DIRECTIVE, eosPosition);
				case VARIABLE_REFERENCE:
					throw new InvalidTemplateException(ExceptionCode.PREMATURE_EOS_IN_VARIABLE, eosPosition);
				case SINGLE_LINE_COMMENT:
					nextToken = createTokenAndAdvancePosition(TemplateTokenType.SINGLE_LINE_COMMENT, readBufferContent,
						"");
				default:
				break;
			}

		} else if (readBufferContent.length() > 0) {
			nextToken = createTokenAndAdvancePosition(TemplateTokenType.PLAIN_TEXT, readBufferContent, "");
		} else {
			nextToken = createTokenAndAdvancePosition(TemplateTokenType.END_OF_STRING, "", "");
			hasNext = false;
		}
		return nextToken;
	}
}
