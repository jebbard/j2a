package de.je.utils.j2a.standard.fillTemplate;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.je.utils.j2a.fillTemplate.AbstractClassTemplate;
import de.je.utils.j2a.generation.FieldInfo;
import de.je.utils.j2a.generation.RepeatBlock;
import de.je.utils.j2a.standard.generation.StandardClassGenerationInfo;

public class StandardConverterTemplate extends AbstractClassTemplate {
	private static final String CONVERSION_PARAM_NAME = "entity";

	public final static String PARAM_NAME_CONVERTER_NAME = "{$Converter-Name}";

	private final static String REPEAT_BLOCK_TO_CONV = "entityToTO";
	private final static String REPEAT_BLOCK_LIST_CONV = "ListConvert";
	private final static String REPEAT_BLOCK_PACKAGES = "packages";

	private final static String PARAM_NAME_TO_IMPORT = "{$TO-Package}";
	private final static String PARAM_NAME_ENTITY_IMPORT = "{$Entity-Package}";
	private final static String PARAM_NAME_TO_NAME = "{$TO-Name}";
	private final static String PARAM_NAME_ENTITY_NAME = "{$Entity-Name}";
	private final static String PARAM_NAME_ELEM_CONV = "{$Elem-Conversion}";
	private final static String PARAM_NAME_DATA_PARAMS = "{$Data-Params}";
	private final static String PARAM_NAME_PARAM_NAME = "{$Param-Name}";

	private static boolean handleListConversion(Map<Class<?>, StandardClassGenerationInfo> referencedGenerationInfos,
		Map<String, String> parameters, List<FieldInfo> fieldInfos) {
		for (int j = 0; j < fieldInfos.size(); ++j) {
			FieldInfo element = fieldInfos.get(j);

			List<Class<?>> typeArgs = element.getActualTypeArguments();

			if (StandardConverterTemplate.isParameterizedListField(element)) {
				Class<?> theEntity = typeArgs.get(0);

				if (referencedGenerationInfos.containsKey(theEntity)) {
					StandardClassGenerationInfo genInfo = referencedGenerationInfos.get(theEntity);
					String toName = genInfo.getToName();
					String elemConv = "entityToTO";

					parameters.put(StandardConverterTemplate.PARAM_NAME_ENTITY_NAME, theEntity.getSimpleName());
					parameters.put(StandardConverterTemplate.PARAM_NAME_TO_NAME, toName);
					parameters.put(StandardConverterTemplate.PARAM_NAME_ELEM_CONV, elemConv);

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

	public StandardConverterTemplate(File templateFile) {
		super(templateFile,
			new String[] { StandardConverterTemplate.REPEAT_BLOCK_TO_CONV,
				StandardConverterTemplate.REPEAT_BLOCK_LIST_CONV, StandardConverterTemplate.REPEAT_BLOCK_PACKAGES },
			new String[] {}, new String[] { StandardConverterTemplate.PARAM_NAME_CONVERTER_NAME });
	}

	public void addEntity(StandardClassGenerationInfo generationInfo,
		Map<Class<?>, StandardClassGenerationInfo> referencedGenerationInfos) {
		checkState();

		if (referencedGenerationInfos == null || generationInfo == null) {
			throw new IllegalArgumentException("Parameters 'generationInfos' and 'poClass' may not be null");
		}

		Map<String, String> parameters = new HashMap<>();

		parameters.put(StandardConverterTemplate.PARAM_NAME_TO_IMPORT, generationInfo.getTargetPackageName());
		parameters.put(StandardConverterTemplate.PARAM_NAME_TO_NAME, generationInfo.getToName());
		parameters.put(StandardConverterTemplate.PARAM_NAME_ENTITY_IMPORT,
			generationInfo.getSourceClass().getPackage().getName());
		parameters.put(StandardConverterTemplate.PARAM_NAME_ENTITY_NAME,
			generationInfo.getSourceClass().getSimpleName());
		parameters.put(StandardConverterTemplate.PARAM_NAME_PARAM_NAME,
			StandardConverterTemplate.CONVERSION_PARAM_NAME);

		List<FieldInfo> fieldInfos = generationInfo.getFieldInfos();

		String dataToFieldString = generateDataParams(generationInfo, referencedGenerationInfos);

		parameters.put(StandardConverterTemplate.PARAM_NAME_DATA_PARAMS, dataToFieldString);

		boolean mayExpand = true;

		for (int i = 0; i < getRepeatBlocks().size(); i++) {
			RepeatBlock repeatBlock = getRepeatBlocks().get(i);

			if (repeatBlock.getName().equals(StandardConverterTemplate.REPEAT_BLOCK_LIST_CONV)) {
				mayExpand = StandardConverterTemplate.handleListConversion(referencedGenerationInfos, parameters,
					fieldInfos);
			} else {
				mayExpand = true;
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

	private String generateDataParams(StandardClassGenerationInfo myGenerationInfo,
		Map<Class<?>, StandardClassGenerationInfo> generationInfos) {
		String dataFieldString = "";

		for (int i = 0; i < myGenerationInfo.getFieldInfos().size(); ++i) {
			FieldInfo fieldInfo = myGenerationInfo.getFieldInfos().get(i);

			if (!fieldInfo.isUnique() && !fieldInfo.isPkey() && !fieldInfo.isVersion()) {
				dataFieldString += dataFieldString.isEmpty() ? "" : ", ";

				// Replace with conversion method for referenced PO
				if (fieldInfo.isFkey() && generationInfos.containsKey(fieldInfo.getFieldType())) {
					StandardClassGenerationInfo referencedGenerationInfo = generationInfos
						.get(fieldInfo.getFieldType());

//					if (myGenerationInfo.getFieldsAsPkeyTOs().contains(fieldInfo))
//						dataFieldString += "new " + referencedGenerationInfo.getPkeyTOName() + "(" + generatePkeyParams(referencedGenerationInfo.getFieldInfos(), fieldInfo.getGetterName()) + ")";
//					else
//						if (referencedGenerationInfo.isGenerateAsInfoTO())
//							dataFieldString += "toInfoTO(" + createGetStatement(CONVERSION_PARAM_NAME, fieldInfo) + ")";
//
//					else
					dataFieldString += "entityToTO("
						+ createGetStatement(StandardConverterTemplate.CONVERSION_PARAM_NAME, fieldInfo) + ")";
				}

				else if (StandardConverterTemplate.isParameterizedListField(fieldInfo)) {
					dataFieldString += "convert" + fieldInfo.getActualTypeArguments().get(0).getSimpleName() + "List("
						+ createGetStatement(StandardConverterTemplate.CONVERSION_PARAM_NAME, fieldInfo) + ")";
				} else {
					dataFieldString += createGetStatement(StandardConverterTemplate.CONVERSION_PARAM_NAME, fieldInfo);
				}
			}
		}
		return dataFieldString;
	}
}