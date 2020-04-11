package de.je.fhunii.fillTemplate;

import java.io.File;

import de.je.utils.j2a.fillTemplate.AbstractClassTemplate;

public class FhuniiCTOTemplate extends AbstractClassTemplate {

	public final static String PARAM_NAME_DATA_TO_NAME = "{$DataTO-Name}";
	public final static String PARAM_NAME_PKEY_TO_NAME = "{$PkeyTO-Name}";
	public final static String PARAM_NAME_CTO_NAME = "{$CTO-Name}";

	public FhuniiCTOTemplate(File templateFile) {
		super(templateFile, new String[] {}, new String[] {},
			new String[] { PARAM_NAME_DATA_TO_NAME, PARAM_NAME_PKEY_TO_NAME, PARAM_NAME_CTO_NAME });
	}
}