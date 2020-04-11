package de.je.utils.j2a.fillTemplate;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import de.je.utils.j2a.generation.ConditionalBlock;
import de.je.utils.j2a.generation.FieldInfo;
import de.je.utils.j2a.generation.RepeatBlock;

public abstract class AbstractTOWithFieldsTemplate extends AbstractClassTemplate {

	private static final String RBINST_FIELD_CONSTR_CHECK = "constrCheck";

	public AbstractTOWithFieldsTemplate(File templateFile, String[] conditionalBlockNames, String[] allowedParamNames) {
		super(templateFile, new String[] { RBN_FIELD, RBN_HASHCODE, RBN_EQUALS }, conditionalBlockNames,
			allowedParamNames);
	}

	private final static String RBN_FIELD = "Field";
	private final static String RBN_EQUALS = "Equals";
	private final static String RBN_HASHCODE = "HashCode";

	private final static String RBINST_HASHCODE_LONG = "hashCodeLong";
	private final static String RBINST_HASHCODE_PRIM = "hashCodePrimitiveTypes";
	private final static String RBINST_HASHCODE_BOOL = "hashCodeBool";
	private final static String RBINST_HASHCODE_OBJ = "hashCodeObjects";
	private final static String RBINST_HASHCODE_FLOAT = "hashCodeFloat";
	private final static String RBINST_HASHCODE_DOUBLE = "hashCodeDouble";
	private final static String RBINST_EQUALS_OBJ = "equalsObjects";
	private final static String RBINST_EQUALS_PRIM = "equalsPrimitiveTypes";
	private final static String RBINST_EQUALS_FLOAT = "equalsFloat";
	private final static String RBINST_EQUALS_DOUBLE = "equalsDouble";

	private final static String PARAM_NAME_FIELD_NAME = "{$Field-Name}";
	private final static String PARAM_NAME_FIELD_NAME_U = "{$Field-Name-U}";
	private final static String PARAM_NAME_FIELD_TYPE = "{$Field-Type}";
	private final static String PARAM_NAME_TMP_VAR = "{$TMP-var}";

	public void startNewTemplate() {
		super.startNewTemplate();
		TMP_VAR_COUNTER = 0;
	}

	public void addField(FieldInfo fi) {
		checkState();
		if (fi == null)
			throw new IllegalArgumentException("Parameter 'fi' may not be null");

		Map<String, String> parameters = new HashMap<String, String>();

		String fieldNameUppercase = fi.getFieldNameFirstUppercase();

		parameters.put(PARAM_NAME_FIELD_NAME, fi.getFieldName());
		parameters.put(PARAM_NAME_FIELD_NAME_U, fieldNameUppercase);

		String fieldType = "";

		// The field type is a parameterized type with actual arguments
		if (!fi.getActualTypeArguments().isEmpty()) {
			fieldType = fi.getFieldType().getSimpleName() + "<";

			for (int i = 0; i < fi.getActualTypeArguments().size(); ++i) {
				Class<?> typeArgument = fi.getActualTypeArguments().get(i);

				Map<Class<?>, String> actualTypeArgumentSurrogates = fi.getActualTypeArgumentSurrogates();
				if (actualTypeArgumentSurrogates.containsKey(typeArgument))
					fieldType += actualTypeArgumentSurrogates.get(typeArgument);

				else
					fieldType += typeArgument.getSimpleName();

				if (i < fi.getActualTypeArguments().size() - 1)
					fieldType += ",";
			}

			fieldType += ">";
		}

		else
			fieldType = fi.getTypeSurrogate() != null ? fi.getTypeSurrogate() : fi.getFieldType().getSimpleName();

		parameters.put(PARAM_NAME_FIELD_TYPE, fieldType);

		for (int i = 0; i < getRepeatBlocks().size(); i++) {
			RepeatBlock repeatBlock = getRepeatBlocks().get(i);

			boolean expand = mayExpandRepeatBlock(fi, repeatBlock);

			if (expand) {
				if (repeatBlock.getName().equals(RBN_HASHCODE)
					&& repeatBlock.getInstanceId().equals(RBINST_HASHCODE_DOUBLE))
					parameters.put(PARAM_NAME_TMP_VAR, "tmp" + TMP_VAR_COUNTER++);

				String replicatedBlock = repeatBlock.expand(parameters, this, fi);

				replaceContent(repeatBlock.getCompleteBlock(), replicatedBlock);
			}
		}
	}

	private boolean mayExpandRepeatBlock(FieldInfo fi, RepeatBlock repeatBlock) {
		String rbn = repeatBlock.getName();
		String rbinst = repeatBlock.getInstanceId();

		if (rbn.equals(RBN_FIELD) && rbinst.equals(RBINST_FIELD_CONSTR_CHECK))
			return !fi.isPrimitive() && !fi.isNullable();

		if (rbn.equals(RBN_HASHCODE) && rbinst.equals(RBINST_HASHCODE_DOUBLE)
			|| rbn.equals(RBN_EQUALS) && rbinst.equals(RBINST_EQUALS_DOUBLE))
			return FieldInfo.isDouble(fi.getFieldType());

		else if (rbn.equals(RBN_HASHCODE) && rbinst.equals(RBINST_HASHCODE_FLOAT)
			|| rbn.equals(RBN_EQUALS) && rbinst.equals(RBINST_EQUALS_FLOAT))
			return FieldInfo.isFloat(fi.getFieldType());

		else if (rbn.equals(RBN_HASHCODE) && rbinst.equals(RBINST_HASHCODE_BOOL))
			return FieldInfo.isBool(fi.getFieldType());

		else if (rbn.equals(RBN_HASHCODE) && rbinst.equals(RBINST_HASHCODE_LONG))
			return FieldInfo.isLong(fi.getFieldType()) && fi.isPrimitive();

		else if (rbn.equals(RBN_HASHCODE) && rbinst.equals(RBINST_HASHCODE_PRIM)
			|| rbn.equals(RBN_EQUALS) && rbinst.equals(RBINST_EQUALS_PRIM))
			return fi.isPrimitive() && !(FieldInfo.isLong(fi.getFieldType()) || FieldInfo.isBool(fi.getFieldType())
				|| FieldInfo.isFloat(fi.getFieldType()) || FieldInfo.isDouble(fi.getFieldType()));

		if (rbn.equals(RBN_FIELD))
			return true;

		if (rbn.equals(RBN_HASHCODE) && rbinst.equals(RBINST_HASHCODE_OBJ)
			|| rbn.equals(RBN_EQUALS) && rbinst.equals(RBINST_EQUALS_OBJ))
			return !(fi.isPrimitive() || FieldInfo.isFloat(fi.getFieldType()) || FieldInfo.isDouble(fi.getFieldType()));

		return false;
	}

	public boolean mayExpandConditionalBlock(FieldInfo fi, ConditionalBlock condBlock,
		RepeatBlock enclosingRepeatBlock) {
		return false;
	}

	private static int TMP_VAR_COUNTER = 0;

}
