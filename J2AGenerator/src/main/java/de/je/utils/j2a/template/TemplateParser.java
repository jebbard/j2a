/**
 *
 * {@link TemplateParser}.java
 *
 * @author Jens Ebert
 *
 * @date 27.02.2020
 *
 */
package de.je.utils.j2a.template;

import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * {@link TemplateParser}
 *
 */
public class TemplateParser {
	private static final Set<AbstractTemplateSectionParser<?>> PARSERS = new HashSet<>();
	static {
		// TODO create parsers
	}

	public List<AbstractTemplateSection> parseTemplate(Reader templateReader) {
		TemplateTokenizer tokenizer = new TemplateTokenizer(templateReader);
		List<AbstractTemplateSection> topLevelSections = new ArrayList<>();

		while (tokenizer.hasNext()) {
			TemplateToken templateToken = tokenizer.next();

			AbstractTemplateSectionParser<?> templateSectionParser = TemplateParser.PARSERS.stream()
				.filter(p -> p.isIndicatingTemplateSection(templateToken)).findFirst().orElse(null);

			if (templateSectionParser != null) {
				topLevelSections.add(templateSectionParser.parseTemplateSection(templateToken, tokenizer));
			} else {
				throw new IllegalArgumentException();
			}
		}

		return topLevelSections;
	}
}
