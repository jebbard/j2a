/**
 *
 * {@link AbstractTemplateSectionParser}.java
 *
 * @author Jens Ebert
 *
 * @date 27.02.2020
 *
 */
package de.je.utils.j2a.template;

import java.util.Iterator;

/**
 * {@link AbstractTemplateSectionParser}
 *
 */
public abstract class AbstractTemplateSectionParser<T extends AbstractTemplateSection> {

	public abstract boolean isIndicatingTemplateSection(TemplateToken nextToken);

	public abstract T parseTemplateSection(TemplateToken nextToken, Iterator<TemplateToken> followUpTokenIterator);
}
