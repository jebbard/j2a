package de.je.utils.j2a.standard.fillTemplate;

import java.io.File;

import de.je.utils.j2a.fillTemplate.AbstractTOWithFieldsTemplate;
import de.je.utils.j2a.generation.ConditionalBlock;
import de.je.utils.j2a.generation.FieldInfo;
import de.je.utils.j2a.generation.RepeatBlock;

public class StandardTOTemplate extends AbstractTOWithFieldsTemplate {

	public final static String PARAM_NAME_TO_NAME = "{$TO-Name}";
	public final static String PARAM_NAME_VESIONFIELD_TYPE = "{$VersionField-Type}";
	public final static String PARAM_NAME_IDFIELD_TYPE = "{$IdField-Type}";

	private final static String COND_BLOCK_NOT_NULLABLE = "NotNullable";
	private final static String COND_BLOCK_NULLABLE = "Nullable";

	public StandardTOTemplate(File templateFile) {
		super(templateFile, new String[] { COND_BLOCK_NOT_NULLABLE, COND_BLOCK_NULLABLE },
			new String[] { PARAM_NAME_TO_NAME, PARAM_NAME_VESIONFIELD_TYPE, PARAM_NAME_IDFIELD_TYPE });
	}

	@Override
	public boolean mayExpandConditionalBlock(FieldInfo fi, ConditionalBlock condBlock,
		RepeatBlock enclosingRepeatBlock) {

		if (condBlock.getName().equals(COND_BLOCK_NOT_NULLABLE))
			return !fi.isNullable();

		else if (condBlock.getName().equals(COND_BLOCK_NULLABLE))
			return fi.isNullable();

		return false;
	}
}