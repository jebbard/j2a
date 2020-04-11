/**
 *
 * {@link InvalidTemplateException}.java
 *
 * @author Jens Ebert
 *
 * @date 18.02.2020
 *
 */
package de.je.utils.j2a.template;

/**
 * {@link InvalidTemplateException} is thrown during template parsing in case
 * that the template is invalid.
 */
public class InvalidTemplateException extends RuntimeException {
	public enum ExceptionCode {
		PREMATURE_EOS_IN_DIRECTIVE("Premature end of line during parsing directive found in template at %1$s"),
		PREMATURE_EOS_IN_VARIABLE("Premature end of line during parsing variable found in template at %1$s"),
		PREMATURE_EOS_IN_END_DIRECTIVE("Premature end of line during parsing end directive found in template at %1$s"),
		IO_EXCEPTION("I/O exception during template parsing at or near %1$s");

		private final String message;

		private ExceptionCode(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}
	}

	private static final long serialVersionUID = -6359518918721602425L;

	private final ExceptionCode code;
	private final UTF8TextPosition position;

	public InvalidTemplateException(ExceptionCode code, UTF8TextPosition position) {
		super(code + ": " + String.format(code.getMessage(), position));
		this.code = code;
		this.position = position;
	}

	public ExceptionCode getCode() {
		return code;
	}

	public UTF8TextPosition getPosition() {
		return position;
	}
}
