/**
 *
 * {@link VariableReferenceParser}.java
 *
 * @author Jens Ebert
 *
 * @date 03.03.2020
 *
 */
package com.github.j2a.core.template;

import java.util.Iterator;

/**
 * {@link VariableReferenceParser}
 *
 */
public class VariableReferenceParser extends AbstractTemplateSectionParser<VariableReference> {

	/**
	 * Creates a new {@link VariableReferenceParser}.
	 */
	public VariableReferenceParser() {
	}

	/**
	 * @see com.github.j2a.core.template.AbstractTemplateSectionParser#isIndicatingTemplateSection(com.github.j2a.core.template.TemplateToken)
	 */
	@Override
	public boolean isIndicatingTemplateSection(TemplateToken nextToken) {
		return nextToken.getTokenType() == TemplateTokenType.VARIABLE_REFERENCE;
	}

	/**
	 * @see com.github.j2a.core.template.AbstractTemplateSectionParser#parseTemplateSection(com.github.j2a.core.template.TemplateToken,
	 *      java.util.Iterator)
	 */
	@Override
	public VariableReference parseTemplateSection(TemplateToken variableToken,
		Iterator<TemplateToken> followUpTokenIterator) {
		if (!isIndicatingTemplateSection(variableToken)) {
			throw new IllegalArgumentException(
				"This method may only be called if the given token indicates the presence of this TemplateSection, token: "
					+ variableToken);
		}

		String variableReferenceText = variableToken.getTokenTextUntilNext();

		if (!variableReferenceText.startsWith(TemplateTokenType.VARIABLE_REFERENCE.getStartDelimiter())) {
			throw new IllegalArgumentException(
				"Got variable reference token which does not start with VARIABLE_REFERENCE start delimiter: "
					+ variableToken);
		}

		if (!variableReferenceText.endsWith(TemplateTokenType.VARIABLE_REFERENCE.getEndDelimiter())) {
			throw new IllegalArgumentException(
				"Got variable reference token which does not end with VARIABLE_REFERENCE end delimiter: "
					+ variableToken);
		}

		variableReferenceText = variableReferenceText.substring(
			TemplateTokenType.VARIABLE_REFERENCE.getStartDelimiter().length(),
			variableReferenceText.length() - TemplateTokenType.VARIABLE_REFERENCE.getEndDelimiter().length());

		// TODO check for non-allowed characters
		// TODO split namespace and var name
		// TODO create Var Ref

		/*
		 * Tests: - Parse correct variable w/o namespace - Parse correct variable with
		 * namespace - Error when parsing variable with incorrect characters (e.g.
		 * whitespace) - Error when parsing variable with too much : - Error when
		 * parsing variable not correctly delimited
		 */

		return null;
	}

}
