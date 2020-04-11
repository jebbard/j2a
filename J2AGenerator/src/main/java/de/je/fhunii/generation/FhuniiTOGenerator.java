package de.je.fhunii.generation;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import de.je.fhunii.fillTemplate.FhuniiCTOTemplate;
import de.je.fhunii.fillTemplate.FhuniiConverterTemplate;
import de.je.fhunii.fillTemplate.FhuniiDataTOTemplate;
import de.je.fhunii.fillTemplate.FhuniiPkeyTOTemplate;
import de.je.utils.j2a.generation.AbstractTOGenerator;
import de.je.utils.j2a.generation.FieldInfo;
import de.je.utils.j2a.generation.TOGenerator;
import de.je.utils.j2a.standard.fillTemplate.StandardConverterTemplate;

public class FhuniiTOGenerator extends AbstractTOGenerator<FhuniiGenerationProperties, FhuniiClassGenerationInfo>
	implements TOGenerator {
	private static void setParameterizedTypeSurrogate(FieldInfo fi, boolean generateAsPkeyRef,
		Map<Class<?>, FhuniiClassGenerationInfo> generationInfos) {
		if (fi.getTypeSurrogate() == null && !fi.getActualTypeArguments().isEmpty()) {
			String fieldTypeSurrogate = fi.getFieldType().getSimpleName() + "<";

			for (int i = 0; i < fi.getActualTypeArguments().size(); ++i) {
				Class<?> actualArgumentType = fi.getActualTypeArguments().get(i);

				if (generationInfos.containsKey(actualArgumentType)) {
					FhuniiClassGenerationInfo classGenerationInfo = generationInfos.get(actualArgumentType);

					classGenerationInfo.getFieldsAsIds();

					if (classGenerationInfo.isGenerateAsInfoTO()) {
						fieldTypeSurrogate += classGenerationInfo.getInfoToName();
					} else if (generateAsPkeyRef) {
						fieldTypeSurrogate += classGenerationInfo.getPkeyTOName();
					} else {
						fieldTypeSurrogate += classGenerationInfo.getCtoName();
					}
				} else {
					fieldTypeSurrogate += actualArgumentType.getSimpleName();
				}

				if (i != fi.getActualTypeArguments().size() - 1) {
					fieldTypeSurrogate += ",";
				}

				fieldTypeSurrogate += ">";
			}

			fi.setTypeSurrogate(fieldTypeSurrogate);
		}

	}

	private final FhuniiDataTOTemplate dataTOTemplate;
	private final FhuniiPkeyTOTemplate pkeyTOTemplate;

	private final FhuniiCTOTemplate ctoTemplate;

	private Map<String, FhuniiConverterTemplate> converterTemplates = new HashMap<>();

	public FhuniiTOGenerator() {
		super("de.je.utils.toGenerator.fhunii.templates");

		File dataToPath = new File(getTemplatePath().toFile(), "DataTO.java.template");
		File ctoPath = new File(getTemplatePath().toFile(), "CTO.java.template");
		File pkeyTOPath = new File(getTemplatePath().toFile(), "PkeyTO.java.template");
		File toConverterPath = new File(getTemplatePath().toFile(), "Converter.java.template");

		AbstractTOGenerator.checkExists(dataToPath.toPath());
		AbstractTOGenerator.checkExists(ctoPath.toPath());
		AbstractTOGenerator.checkExists(pkeyTOPath.toPath());
		AbstractTOGenerator.checkExists(toConverterPath.toPath());

		dataTOTemplate = new FhuniiDataTOTemplate(dataToPath);
		ctoTemplate = new FhuniiCTOTemplate(ctoPath);
		pkeyTOTemplate = new FhuniiPkeyTOTemplate(pkeyTOPath);
	}

	private String fillCTOTemplate(String ctoName, String dataTOName, String pkeyTOName, String targetPackageName) {
		ctoTemplate.startNewTemplate();

		AbstractTOGenerator.fillDefaultTemplateParameters(ctoTemplate, targetPackageName);
		ctoTemplate.setTemplateParam(FhuniiCTOTemplate.PARAM_NAME_DATA_TO_NAME, dataTOName);
		ctoTemplate.setTemplateParam(FhuniiCTOTemplate.PARAM_NAME_PKEY_TO_NAME, pkeyTOName);
		ctoTemplate.setTemplateParam(FhuniiCTOTemplate.PARAM_NAME_CTO_NAME, ctoName);

		return ctoTemplate.finishTemplate();
	}

	private String fillDataTOTemplate(FhuniiClassGenerationInfo generationInfo, String toName,
		Map<Class<?>, FhuniiClassGenerationInfo> generationInfos) {
		dataTOTemplate.startNewTemplate();

		AbstractTOGenerator.fillDefaultTemplateParameters(dataTOTemplate, generationInfo.getTargetPackageName());
		dataTOTemplate.setTemplateParam(FhuniiDataTOTemplate.PARAM_NAME_DATA_TO_NAME, toName);

		for (int j = 0; j < generationInfo.getFieldInfos().size(); ++j) {
			FieldInfo fieldInfo = generationInfo.getFieldInfos().get(j);

			// Version fields are ignored
			if (!fieldInfo.isVersion() && !fieldInfo.isPkey() && !fieldInfo.isUnique()) {
				// Foreign key whose type is one of the other POs is replaced with a surrogate
				// type
				if (fieldInfo.isFkey() && generationInfos.containsKey(fieldInfo.getFieldType())) {
					FhuniiClassGenerationInfo referencedGenerationInfo = generationInfos.get(fieldInfo.getFieldType());

					// Generate as reference to PkeyTO
					if (generationInfo.getFieldsAsIds().contains(fieldInfo)) {
						fieldInfo.setTypeSurrogate(referencedGenerationInfo.getPkeyTOName());
					} else {
						if (referencedGenerationInfo.isGenerateAsInfoTO()) {
							fieldInfo.setTypeSurrogate(referencedGenerationInfo.getInfoTOName());
						} else {
							fieldInfo.setTypeSurrogate(referencedGenerationInfo.getDataTOName());
						}
					}
				}

				FhuniiTOGenerator.setParameterizedTypeSurrogate(fieldInfo,
					generationInfo.getFieldsAsIds().contains(fieldInfo), generationInfos);

				dataTOTemplate.addField(fieldInfo);
			}
		}

		return dataTOTemplate.finishTemplate();
	}

	private String fillPkeyTOTemplate(List<FieldInfo> fieldInfos, String pkeyTOName, String targetPackageName,
		Map<Class<?>, FhuniiClassGenerationInfo> generationInfos) {
		pkeyTOTemplate.startNewTemplate();

		AbstractTOGenerator.fillDefaultTemplateParameters(pkeyTOTemplate, targetPackageName);
		pkeyTOTemplate.setTemplateParam(FhuniiPkeyTOTemplate.PARAM_NAME_PKEY_TO_NAME, pkeyTOName);

		for (int j = 0; j < fieldInfos.size(); ++j) {
			FieldInfo fieldInfo = fieldInfos.get(j);

			// Generate the unique id
			if (fieldInfo.isPkey()) {
				pkeyTOTemplate.setTemplateParam(FhuniiPkeyTOTemplate.PARAM_NAME_IDFIELD_NAME, fieldInfo.getFieldName());
				pkeyTOTemplate.setTemplateParam(FhuniiPkeyTOTemplate.PARAM_NAME_IDFIELD_TYPE,
					fieldInfo.getFieldType().getSimpleName());
			}

			// Furthermore, only unique fields additionally contribute to the pkey
			if (fieldInfo.isUnique()) {
				// Foreign key whose type is one of the other POs is replaced with a surrogate
				// type
				if (fieldInfo.isFkey() && generationInfos.containsKey(fieldInfo.getFieldType())) {
					FhuniiClassGenerationInfo generationInfo = generationInfos.get(fieldInfo.getFieldType());

					FhuniiTOGenerator.setParameterizedTypeSurrogate(fieldInfo, true, generationInfos);

					fieldInfo.setTypeSurrogate(generationInfo.getPkeyTOName());
				}

				pkeyTOTemplate.addField(fieldInfo);
			}
		}

		return pkeyTOTemplate.finishTemplate();
	}

	private void finishConverterTemplates(FhuniiGenerationProperties properties) {
		List<String> converterClassNames = properties.getTargetTOFactoryClassClassSimpleNames();
		List<String> converterPackages = properties.getTargetToFactoryPackages();
		List<String> targetPackages = properties.getTargetToBasePackages();

		for (int i = 0; i < targetPackages.size(); ++i) {
			String targetPackage = targetPackages.get(i);

			FhuniiConverterTemplate template = converterTemplates.get(targetPackage);
			String generatedContent = template.finishTemplate();

			System.out.println(generatedContent);
			AbstractTOGenerator.generateOutputFile(generatedContent, properties.getTargetToFactoryPath(),
				converterPackages.get(i), converterClassNames.get(i));
		}
	}

	private void prepareConverterTemplates(FhuniiGenerationProperties properties) {
		List<String> converterClassNames = properties.getTargetTOFactoryClassClassSimpleNames();
		List<String> converterPackages = properties.getTargetToFactoryPackages();
		List<String> targetPackages = properties.getTargetToBasePackages();

		for (int i = 0; i < targetPackages.size(); ++i) {
			String targetPackage = targetPackages.get(i);

			File converterPath = new File(getTemplatePath().toFile(), "Converter.java.template");
			AbstractTOGenerator.checkExists(converterPath.toPath());
			FhuniiConverterTemplate template = new FhuniiConverterTemplate(converterPath);
			template.startNewTemplate();
			template.setTemplateParam(StandardConverterTemplate.PARAM_NAME_CONVERTER_NAME, converterClassNames.get(i));

			AbstractTOGenerator.fillDefaultTemplateParameters(template, converterPackages.get(i));

			converterTemplates.put(targetPackage, template);
		}
	}

	@Override
	protected FhuniiClassGenerationInfo createClassGenerationInfo(FhuniiGenerationProperties properties,
		Class<?> theSourceClass, List<FieldInfo> fieldInfos, String targetPackage) {
		return new FhuniiClassGenerationInfo(theSourceClass, properties, fieldInfos, targetPackage);
	}

	@Override
	protected FhuniiGenerationProperties createGenerationProperties(Properties properties) {
		return new FhuniiGenerationProperties(properties);
	}

	@Override
	protected void doAfterTOGeneration(FhuniiGenerationProperties generationProperties) {
		finishConverterTemplates(generationProperties);
	}

	@Override
	protected void doBeforeTOGeneration(FhuniiGenerationProperties generationProperties) {
		prepareConverterTemplates(generationProperties);
	}

	@Override
	protected void generateTO(FhuniiGenerationProperties generationProperties,
		Map<Class<?>, FhuniiClassGenerationInfo> classesWithFields, FhuniiClassGenerationInfo generationInfo,
		String targetPackage) {
		// Generate Info TO only
		if (generationInfo.isGenerateAsInfoTO()) {
			converterTemplates.get(targetPackage).addEntity(generationInfo, classesWithFields);

			String generatedInfoTOString = fillDataTOTemplate(generationInfo, generationInfo.getInfoTOName(),
				classesWithFields);

			AbstractTOGenerator.generateOutputFile(generatedInfoTOString, generationProperties.getTargetToPath(),
				targetPackage, generationInfo.getInfoTOName());
		}

		// Otherwise generate pkey TO, data TO and CTO
		else {
			converterTemplates.get(targetPackage).addEntity(generationInfo, classesWithFields);

			String generatedDataTOString = fillDataTOTemplate(generationInfo, generationInfo.getDataTOName(),
				classesWithFields);
			String generatedPkeyTOString = fillPkeyTOTemplate(generationInfo.getFieldInfos(),
				generationInfo.getPkeyTOName(), targetPackage, classesWithFields);
			String generatedCTOString = fillCTOTemplate(generationInfo.getCtoName(), generationInfo.getDataTOName(),
				generationInfo.getPkeyTOName(), targetPackage);

			AbstractTOGenerator.generateOutputFile(generatedDataTOString, generationProperties.getTargetToPath(),
				targetPackage, generationInfo.getDataTOName());
			AbstractTOGenerator.generateOutputFile(generatedPkeyTOString, generationProperties.getTargetToPath(),
				targetPackage, generationInfo.getPkeyTOName());
			AbstractTOGenerator.generateOutputFile(generatedCTOString, generationProperties.getTargetToPath(),
				targetPackage, generationInfo.getCtoName());
		}
	}
}
