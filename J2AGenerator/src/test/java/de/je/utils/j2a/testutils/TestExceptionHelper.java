/**
 *
 * {@link TestExceptionHelper}.java
 *
 * @author Jens Ebert
 *
 * @date 03.03.2020
 *
 */
package de.je.utils.j2a.testutils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;

import com.github.j2a.core.template.InvalidTemplateException;
import com.github.j2a.core.template.UTF8TextPosition;

/**
 * {@link TestExceptionHelper}
 *
 */
public class TestExceptionHelper {

	public static void assertThrowsInvalidTemplateExceptionWithCode(InvalidTemplateException.ExceptionCode expectedCode,
		UTF8TextPosition expectedPosition, Executable executable) {
		try {
			executable.execute();
		} catch (Throwable e) {
			Assertions.assertEquals(InvalidTemplateException.class, e.getClass());
			Assertions.assertEquals(expectedCode, ((InvalidTemplateException) e).getCode());
			Assertions.assertEquals(expectedPosition, ((InvalidTemplateException) e).getPosition());
		}
	}

	/**
	 * Creates a new {@link TestExceptionHelper}.
	 */
	private TestExceptionHelper() {
	}

}
