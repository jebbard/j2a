package de.je.fhunii.fillTemplate;

import java.io.File;

import de.je.utils.j2a.fillTemplate.AbstractTOWithFieldsTemplate;

public class FhuniiPkeyTOTemplate extends AbstractTOWithFieldsTemplate {

	public final static String PARAM_NAME_PKEY_TO_NAME = "{$PkeyTO-Name}";
	public final static String PARAM_NAME_IDFIELD_NAME = "{$IdField-Name}";
	public final static String PARAM_NAME_IDFIELD_TYPE = "{$IdField-Type}";

	public FhuniiPkeyTOTemplate(File templateFile) {
		super(templateFile, new String[] {},
			new String[] { PARAM_NAME_PKEY_TO_NAME, PARAM_NAME_IDFIELD_NAME, PARAM_NAME_IDFIELD_TYPE });
	}
}