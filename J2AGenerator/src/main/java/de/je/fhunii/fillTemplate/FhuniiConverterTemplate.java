package de.je.fhunii.fillTemplate;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.je.fhunii.generation.FhuniiClassGenerationInfo;
import de.je.utils.j2a.fillTemplate.AbstractClassTemplate;
import de.je.utils.j2a.generation.FieldInfo;
import de.je.utils.j2a.generation.RepeatBlock;

public class FhuniiConverterTemplate extends AbstractClassTemplate {
	private static final String CONVERSION_PARAM_NAME = "poToConvert";

	public final static String PARAM_NAME_CONVERTER_NAME = "{$Converter-Name}";

	private final static String REPEAT_BLOCK_CTO_CONV = "CTO";
	private final static String REPEAT_BLOCK_DATA_TO_CONV = "DataTO";
	private final static String REPEAT_BLOCK_INFO_TO_CONV = "InfoTO";
	private final static String REPEAT_BLOCK_LIST_CONV = "ListConvert";

	private final static String PARAM_NAME_DATA_TO_NAME = "{$DataTO-Name}";
	private final static String PARAM_NAME_PKEY_TO_NAME = "{$PkeyTO-Name}";
	private final static String PARAM_NAME_INFO_TO_NAME = "{$InfoTO-Name}";
	private final static String PARAM_NAME_CTO_NAME = "{$CTO-Name}";
	private final static String PARAM_NAME_PO_NAME = "{$PO-Name}";
	private final static String PARAM_NAME_ANY_TO_NAME = "{$AnyTO-Name}";
	private final static String PARAM_NAME_ELEM_CONV = "{$Elem-Conversion}";
	private final static String PARAM_NAME_PKEY_PARAMS = "{$Pkey-Params}";
	private final static String PARAM_NAME_DATA_PARAMS = "{$Data-Params}";
	private final static String PARAM_NAME_PARAM_NAME = "{$Param-Name}";

	private static boolean handleListConversion(Map<Class<?>, FhuniiClassGenerationInfo> referencedGenerationInfos,
		Map<String, String> parameters, List<FieldInfo> fieldInfos) {
		for (int j = 0; j < fieldInfos.size(); ++j) {
			FieldInfo element = fieldInfos.get(j);

			List<Class<?>> typeArgs = element.getActualTypeArguments();

			if (FhuniiConverterTemplate.isParameterizedListField(element)) {
				Class<?> thePO = typeArgs.get(0);

				if (referencedGenerationInfos.containsKey(thePO)) {
					FhuniiClassGenerationInfo genInfo = referencedGenerationInfos.get(thePO);
					String toName = "";
					String elemConv = "";
					if (genInfo.isGenerateAsInfoTO()) {
						toName = genInfo.getInfoToName();
						elemConv = "toInfoTO";
					}

					else {
						toName = genInfo.getCtoName();
						elemConv = "toCTO";

					}

					parameters.put(FhuniiConverterTemplate.PARAM_NAME_PO_NAME, thePO.getSimpleName());
					parameters.put(FhuniiConverterTemplate.PARAM_NAME_ANY_TO_NAME, toName);
					parameters.put(FhuniiConverterTemplate.PARAM_NAME_ELEM_CONV, elemConv);

					return true;
				}
			}
		}
		return false;
	}

	private static boolean isParameterizedListField(FieldInfo field) {
		List<Class<?>> typeArgs = field.getActualTypeArguments();

		return typeArgs.size() == 1 && field.getFieldType() == List.class;
	}

	public FhuniiConverterTemplate(File templateFile) {
		super(templateFile,
			new String[] { FhuniiConverterTemplate.REPEAT_BLOCK_DATA_TO_CONV,
				FhuniiConverterTemplate.REPEAT_BLOCK_CTO_CONV, FhuniiConverterTemplate.REPEAT_BLOCK_LIST_CONV,
				FhuniiConverterTemplate.REPEAT_BLOCK_INFO_TO_CONV },
			new String[] {}, new String[] { FhuniiConverterTemplate.PARAM_NAME_CONVERTER_NAME });
	}

	public void addEntity(FhuniiClassGenerationInfo generationInfo,
		Map<Class<?>, FhuniiClassGenerationInfo> referencedGenerationInfos) {
		checkState();

		if (referencedGenerationInfos == null || generationInfo == null) {
			throw new IllegalArgumentException("Parameters 'generationInfos' and 'poClass' may not be null");
		}

		Map<String, String> parameters = new HashMap<>();

		parameters.put(FhuniiConverterTemplate.PARAM_NAME_DATA_TO_NAME, generationInfo.getDataTOName());
		parameters.put(FhuniiConverterTemplate.PARAM_NAME_PKEY_TO_NAME, generationInfo.getPkeyTOName());
		parameters.put(FhuniiConverterTemplate.PARAM_NAME_INFO_TO_NAME, generationInfo.getInfoTOName());
		parameters.put(FhuniiConverterTemplate.PARAM_NAME_CTO_NAME, generationInfo.getCtoName());
		parameters.put(FhuniiConverterTemplate.PARAM_NAME_PO_NAME, generationInfo.getSourceClass().getSimpleName());
		parameters.put(FhuniiConverterTemplate.PARAM_NAME_PARAM_NAME, FhuniiConverterTemplate.CONVERSION_PARAM_NAME);

		List<FieldInfo> fieldInfos = generationInfo.getFieldInfos();

		String pkeyFieldString = generatePkeyParams(fieldInfos, null);
		String dataToFieldString = generateDataParams(generationInfo, referencedGenerationInfos);

		parameters.put(FhuniiConverterTemplate.PARAM_NAME_PKEY_PARAMS, pkeyFieldString);
		parameters.put(FhuniiConverterTemplate.PARAM_NAME_DATA_PARAMS, dataToFieldString);

		boolean mayExpand = true;

		for (int i = 0; i < getRepeatBlocks().size(); i++) {
			RepeatBlock repeatBlock = getRepeatBlocks().get(i);

			if (repeatBlock.getName().equals(FhuniiConverterTemplate.REPEAT_BLOCK_LIST_CONV)) {
				mayExpand = FhuniiConverterTemplate.handleListConversion(referencedGenerationInfos, parameters,
					fieldInfos);
			} else if (repeatBlock.getName().equals(FhuniiConverterTemplate.REPEAT_BLOCK_CTO_CONV)) {
				mayExpand = !generationInfo.isGenerateAsInfoTO();
			} else if (repeatBlock.getName().equals(FhuniiConverterTemplate.REPEAT_BLOCK_DATA_TO_CONV)) {
				mayExpand = !generationInfo.isGenerateAsInfoTO();
			} else if (repeatBlock.getName().equals(FhuniiConverterTemplate.REPEAT_BLOCK_INFO_TO_CONV)) {
				mayExpand = generationInfo.isGenerateAsInfoTO();
			}

			if (mayExpand) {
				String replicatedBlock = repeatBlock.expand(parameters, null, null);

				replaceContent(repeatBlock.getCompleteBlock(), replicatedBlock);
			}
		}
	}

	private String createGetStatement(String prefix, FieldInfo fieldInfo) {
		return prefix + "." + fieldInfo.getGetterName();
	}

	private String generateDataParams(FhuniiClassGenerationInfo myGenerationInfo,
		Map<Class<?>, FhuniiClassGenerationInfo> generationInfos) {
		String dataFieldString = "";

		for (int i = 0; i < myGenerationInfo.getFieldInfos().size(); ++i) {
			FieldInfo fieldInfo = myGenerationInfo.getFieldInfos().get(i);

			if (!fieldInfo.isUnique() && !fieldInfo.isPkey() && !fieldInfo.isVersion()) {
				dataFieldString += dataFieldString.isEmpty() ? "" : ", ";

				// Replace with conversion method for referenced PO
				if (fieldInfo.isFkey() && generationInfos.containsKey(fieldInfo.getFieldType())) {
					FhuniiClassGenerationInfo referencedGenerationInfo = generationInfos.get(fieldInfo.getFieldType());

					if (myGenerationInfo.getFieldsAsIds().contains(fieldInfo)) {
						dataFieldString += "new " + referencedGenerationInfo.getPkeyTOName() + "("
							+ generatePkeyParams(referencedGenerationInfo.getFieldInfos(), fieldInfo.getGetterName())
							+ ")";
					} else if (referencedGenerationInfo.isGenerateAsInfoTO()) {
						dataFieldString += "toInfoTO("
							+ createGetStatement(FhuniiConverterTemplate.CONVERSION_PARAM_NAME, fieldInfo) + ")";
					} else {
						dataFieldString += "toDataTO("
							+ createGetStatement(FhuniiConverterTemplate.CONVERSION_PARAM_NAME, fieldInfo) + ")";
					}
				}

				else if (FhuniiConverterTemplate.isParameterizedListField(fieldInfo)) {
					dataFieldString += "convert" + fieldInfo.getActualTypeArguments().get(0).getSimpleName() + "List("
						+ createGetStatement(FhuniiConverterTemplate.CONVERSION_PARAM_NAME, fieldInfo) + ")";
				} else {
					dataFieldString += createGetStatement(FhuniiConverterTemplate.CONVERSION_PARAM_NAME, fieldInfo);
				}
			}
		}
		return dataFieldString;
	}

	private String generatePkeyParams(List<FieldInfo> fieldInfos, String getterName) {
		String pkeyFieldString = "";
		String prefix = FhuniiConverterTemplate.CONVERSION_PARAM_NAME + (getterName != null ? "." + getterName : "");

		// First parameter is always the pkey
		for (int i = 0; i < fieldInfos.size(); ++i) {
			FieldInfo fieldInfo = fieldInfos.get(i);

			if (fieldInfo.isPkey()) {
				pkeyFieldString += createGetStatement(prefix, fieldInfo);
			}
		}

		for (int i = 0; i < fieldInfos.size(); ++i) {
			FieldInfo fieldInfo = fieldInfos.get(i);

			if (fieldInfo.isUnique()) {
				pkeyFieldString += ", " + createGetStatement(prefix, fieldInfo);
			}
		}
		return pkeyFieldString;
	}
}