/**
 *
 * {@link AbstractTemplateSection}.java
 *
 * @author Jens Ebert
 *
 * @date 27.02.2020
 *
 */
package de.je.utils.j2a.template;

/**
 * {@link AbstractTemplateSection}
 *
 */
public abstract class AbstractTemplateSection {

	private final UTF8TextPosition templateStartPosition;
	private final UTF8TextPosition templateEndPosition;

	public AbstractTemplateSection(UTF8TextPosition templateStartPosition, UTF8TextPosition templateEndPosition) {
		this.templateStartPosition = templateStartPosition;
		this.templateEndPosition = templateEndPosition;
	}

	public UTF8TextPosition getTemplateEndPosition() {
		return templateEndPosition;
	}

	public UTF8TextPosition getTemplateStartPosition() {
		return templateStartPosition;
	}

	// TODO define
//	public String expand(GenerationContext context);
}
