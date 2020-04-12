/**
 *
 * {@link TemplateTokenType}.java
 *
 * @author Jens Ebert
 *
 * @date 18.02.2020
 *
 */
package com.github.j2a.core.template;

import java.util.Arrays;

// TODO unit tests for static methods
/**
 * {@link TemplateTokenType} represents all tokens that may occur in a valid J2A
 * template.
 */
public enum TemplateTokenType {
	/**
	 * Indicates a single-line directive or start of a block directive
	 */
	DIRECTIVE("<%", "%>"),
	/**
	 * The end delimiter of a block directive
	 */
	BLOCK_DIRECTIVE_END("<%end", "%>"),
	/**
	 * A variable reference
	 */
	VARIABLE_REFERENCE("${", "}"),
	/**
	 * A single line comment
	 */
	SINGLE_LINE_COMMENT("%%%", "\n"),
	/**
	 * Everything else is plain text (just delimited by start of string, end of
	 * string and other tokens)
	 */
	PLAIN_TEXT("", ""),
	/**
	 * Signals end of string
	 */
	END_OF_STRING("", "");

	/**
	 * Returns a {@link TemplateTokenType} that best matches the given token start
	 * match string or null if there is no such {@link TemplateTokenType}. "Best
	 * match" means:
	 * <ol>
	 * <li>If the given match candidate string is the beginning of a
	 * {@link TemplateTokenType}'s start delimiter, the {@link TemplateTokenType}
	 * with the longest start delimiter is taken. There cannot be multiple start
	 * delimiters for different types having the same length and matching, as this
	 * would mean they are equal.</li>
	 * <li>If the given match candidate is not the beginning of a
	 * {@link TemplateTokenType}'s start delimiter, but vice versa, i.e. the
	 * candidate string starts with exactly any {@link TemplateTokenType}'s start
	 * delimiter, the {@link TemplateTokenType} with the longest start delimiter is
	 * returned.</li>
	 * <li>This method returns {@link TemplateTokenType#PLAIN_TEXT} for an empty
	 * string</li>
	 * </ol>
	 *
	 * @param tokenStartMatchCandidate A string that needs to be checked for
	 *                                 (potentially) indicating the start of a token
	 * @return A {@link TemplateTokenType} that best matches the given token start
	 *         match string or null if there is no such {@link TemplateTokenType}
	 */
	public static TemplateTokenType getBestMatchingTokenTypeFor(String tokenStartMatchCandidate) {
		if (tokenStartMatchCandidate.isEmpty()) {
			return PLAIN_TEXT;
		}

		return Arrays.stream(TemplateTokenType.values())
			// Sort the template token start delimited by string length descending
			.sorted((tt2, tt1) -> Integer.compare(tt1.getStartDelimiter().length(), tt2.getStartDelimiter().length()))
			.filter(
				tt -> !tt.getStartDelimiter().isEmpty() && (tokenStartMatchCandidate.startsWith(tt.getStartDelimiter())
					|| tt.getStartDelimiter().startsWith(tokenStartMatchCandidate)))
			.findFirst().orElse(PLAIN_TEXT);
	}

	/**
	 * Returns true if the given start match candidate string is actually starting
	 * with at least one token type's start delimiter and false otherwise.
	 *
	 * @param tokenStartMatchCandidate A string that needs to be checked for being
	 *                                 the exact start of a token
	 * @return true if the given start match candidate string is actually starting
	 *         with at least one token type's start delimiter and false otherwise
	 */
	public static boolean hasTokenTypeStartingWith(String tokenStartMatchCandidate) {
		return Arrays.stream(TemplateTokenType.values()).anyMatch(
			tt -> !tt.getStartDelimiter().isEmpty() && tokenStartMatchCandidate.startsWith(tt.getStartDelimiter()));
	}

	private final String startDelimiter;

	private final String endDelimiter;

	private TemplateTokenType(String startDelimiter, String endDelimiter) {
		this.startDelimiter = startDelimiter;
		this.endDelimiter = endDelimiter;
	}

	public String getEndDelimiter() {
		return endDelimiter;
	}

	public String getStartDelimiter() {
		return startDelimiter;
	}

	/**
	 * Returns true if the given match candidate string ends with this
	 * {@link TemplateTokenType}'s end delimiter, false otherwise.
	 *
	 * @param tokenStartMatchCandidate The match candidate string
	 * @return true if the given match candidate string ends with this
	 *         {@link TemplateTokenType}'s end delimiter, false otherwise
	 */
	public boolean isEndOf(String tokenStartMatchCandidate) {
		return !getEndDelimiter().isEmpty() && tokenStartMatchCandidate.endsWith(getEndDelimiter());
	}

}
