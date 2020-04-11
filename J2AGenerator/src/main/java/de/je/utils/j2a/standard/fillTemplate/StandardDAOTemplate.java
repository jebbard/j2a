package de.je.utils.j2a.standard.fillTemplate;

import java.io.File;

import de.je.utils.j2a.fillTemplate.AbstractTOWithFieldsTemplate;

public class StandardDAOTemplate extends AbstractTOWithFieldsTemplate {

	public final static String PARAM_NAME_DAO_NAME = "{$DAO-Name}";
	public final static String PARAM_NAME_VESIONFIELD_TYPE = "{$VersionField-Type}";
	public final static String PARAM_NAME_IDFIELD_TYPE = "{$IdField-Type}";
	public final static String PARAM_NAME_TO_IMPORT = "{$TO-Package}";
	public final static String PARAM_NAME_ENTITY_IMPORT = "{$Entity-Package}";
	public final static String PARAM_NAME_TO_NAME = "{$TO-Name}";
	public final static String PARAM_NAME_ENTITY_NAME = "{$Entity-Name}";
	public final static String PARAM_PERSISTENCE_UNIT = "{$Persistence-Unit}";

	public StandardDAOTemplate(File templateFile) {
		super(templateFile, new String[] {},
			new String[] { PARAM_NAME_DAO_NAME, PARAM_NAME_VESIONFIELD_TYPE, PARAM_NAME_IDFIELD_TYPE,
				PARAM_NAME_TO_IMPORT, PARAM_NAME_ENTITY_IMPORT, PARAM_NAME_TO_NAME, PARAM_NAME_ENTITY_NAME,
				PARAM_PERSISTENCE_UNIT, });
	}
}