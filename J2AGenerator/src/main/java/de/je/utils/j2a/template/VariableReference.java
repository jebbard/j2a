/**
 *
 * {@link VariableReference}.java
 *
 * @author Jens Ebert
 *
 * @date 03.03.2020
 *
 */
package de.je.utils.j2a.template;

/**
 * {@link VariableReference}
 *
 */
public class VariableReference extends AbstractTemplateSection {

	private final String localName;
	private final String namespace;

	public VariableReference(UTF8TextPosition templateStartPosition, UTF8TextPosition templateEndPosition,
		String localName, String namespace) {
		super(templateStartPosition, templateEndPosition);
		this.localName = localName;
		this.namespace = namespace;
	}

	public String getLocalName() {
		return localName;
	}

	public String getNamespace() {
		return namespace;
	}
}
