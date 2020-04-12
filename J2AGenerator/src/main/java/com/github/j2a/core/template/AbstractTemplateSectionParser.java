/**
 *
 * {@link AbstractTemplateSectionParser}.java
 *
 * @author Jens Ebert
 *
 * @date 27.02.2020
 *
 */
package com.github.j2a.core.template;

import java.util.Iterator;

/**
 * {@link AbstractTemplateSectionParser}
 *
 */
public abstract class AbstractTemplateSectionParser<T extends AbstractTemplateSection> {

	public abstract boolean isIndicatingTemplateSection(TemplateToken nextToken);

	public abstract T parseTemplateSection(TemplateToken nextToken, Iterator<TemplateToken> followUpTokenIterator);
}
