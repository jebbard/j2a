package de.je.fhunii.fillTemplate;

import java.io.File;

import de.je.utils.j2a.fillTemplate.AbstractTOWithFieldsTemplate;

public class FhuniiDataTOTemplate extends AbstractTOWithFieldsTemplate {
	public final static String PARAM_NAME_DATA_TO_NAME = "{$DataTO-Name}";

	public FhuniiDataTOTemplate(File templateFile) {
		super(templateFile, new String[] {}, new String[] { PARAM_NAME_DATA_TO_NAME });
	}
}