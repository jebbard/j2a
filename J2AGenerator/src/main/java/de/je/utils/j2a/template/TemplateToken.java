/**
 *
 * {@link TemplateToken}.java
 *
 * @author Jens Ebert
 *
 * @date 18.02.2020
 *
 */
package de.je.utils.j2a.template;

/**
 * {@link TemplateToken}
 *
 */
public class TemplateToken {
	private final TemplateTokenType tokenType;
	private final String tokenTextUntilNext;
	private final UTF8TextPosition startPosition;

	public TemplateToken(TemplateTokenType tokenType, String upcomingText, UTF8TextPosition startPosition) {
		this.tokenType = tokenType;
		tokenTextUntilNext = upcomingText;
		this.startPosition = startPosition;
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
		TemplateToken other = (TemplateToken) obj;
		if (startPosition == null) {
			if (other.startPosition != null) {
				return false;
			}
		} else if (!startPosition.equals(other.startPosition)) {
			return false;
		}
		if (tokenTextUntilNext == null) {
			if (other.tokenTextUntilNext != null) {
				return false;
			}
		} else if (!tokenTextUntilNext.equals(other.tokenTextUntilNext)) {
			return false;
		}
		if (tokenType != other.tokenType) {
			return false;
		}
		return true;
	}

	public UTF8TextPosition getStartPosition() {
		return startPosition;
	}

	public String getTokenTextUntilNext() {
		return tokenTextUntilNext;
	}

	public TemplateTokenType getTokenType() {
		return tokenType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (startPosition == null ? 0 : startPosition.hashCode());
		result = prime * result + (tokenTextUntilNext == null ? 0 : tokenTextUntilNext.hashCode());
		result = prime * result + (tokenType == null ? 0 : tokenType.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "TemplateToken [tokenType=" + tokenType + ", tokenTextUntilNext=" + tokenTextUntilNext
			+ ", startPosition=" + startPosition + "]";
	}
}
