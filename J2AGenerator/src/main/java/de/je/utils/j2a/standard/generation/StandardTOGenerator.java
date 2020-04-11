package de.je.utils.j2a.standard.generation;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import de.je.utils.j2a.generation.AbstractTOGenerator;
import de.je.utils.j2a.generation.FieldInfo;
import de.je.utils.j2a.generation.TOGenerator;
import de.je.utils.j2a.standard.fillTemplate.StandardConverterTemplate;
import de.je.utils.j2a.standard.fillTemplate.StandardDAOTemplate;
import de.je.utils.j2a.standard.fillTemplate.StandardTOTemplate;

public class StandardTOGenerator extends AbstractTOGenerator<StandardGenerationProperties, StandardClassGenerationInfo>
	implements TOGenerator {
	private final StandardDAOTemplate daoTemplate;

	private final StandardTOTemplate toTemplate;

	private Map<String, StandardConverterTemplate> converterTemplates = new HashMap<>();

	public StandardTOGenerator() {
		super("de.je.utils.j2a.templates.examples.entity2to");

		File toPath = new File(getTemplatePath().toFile(), "TO.java.template");
		File daoPath = new File(getTemplatePath().toFile(), "DAO.java.template");

		AbstractTOGenerator.checkExists(toPath.toPath());
		AbstractTOGenerator.checkExists(daoPath.toPath());

		toTemplate = new StandardTOTemplate(toPath);
		daoTemplate = new StandardDAOTemplate(daoPath);
	}

	private String fillDAOTemplate(StandardClassGenerationInfo generationInfo,
		Map<Class<?>, StandardClassGenerationInfo> generationInfos) {
		daoTemplate.startNewTemplate();

		AbstractTOGenerator.fillDefaultTemplateParameters(daoTemplate, generationInfo.getTargetPackageName());

		daoTemplate.setTemplateParam(StandardDAOTemplate.PARAM_NAME_DAO_NAME, generationInfo.getDaoName());
		daoTemplate.setTemplateParam(StandardDAOTemplate.PARAM_NAME_TO_IMPORT,
			generationInfo.getSourceClass().getPackage().getName());
		daoTemplate.setTemplateParam(StandardDAOTemplate.PARAM_NAME_TO_NAME, generationInfo.getToName());
		daoTemplate.setTemplateParam(StandardDAOTemplate.PARAM_NAME_ENTITY_IMPORT,
			generationInfo.getTargetPackageName());
		daoTemplate.setTemplateParam(StandardDAOTemplate.PARAM_NAME_ENTITY_NAME,
			generationInfo.getSourceClass().getSimpleName());
		daoTemplate.setTemplateParam(StandardDAOTemplate.PARAM_PERSISTENCE_UNIT, generationInfo.getPersistenceUnit());

		for (int j = 0; j < generationInfo.getFieldInfos().size(); ++j) {
			FieldInfo fieldInfo = generationInfo.getFieldInfos().get(j);

			if (fieldInfo.isPkey()) {
				daoTemplate.setTemplateParam(StandardTOTemplate.PARAM_NAME_IDFIELD_TYPE,
					fieldInfo.getFieldType().getSimpleName());
			}

			if (fieldInfo.isVersion()) {
				daoTemplate.setTemplateParam(StandardTOTemplate.PARAM_NAME_VESIONFIELD_TYPE,
					fieldInfo.getFieldType().getSimpleName());
			}

			// Version fields are ignored
			if (!fieldInfo.isVersion() && !fieldInfo.isPkey() && !fieldInfo.isUnique()) {
//				// Foreign key whose type is one of the other entities is replaced with a surrogate type
//				if (fieldInfo.isFkey())
//				{
//					if (generationInfos.containsKey(fieldInfo.getFieldType()))
//					{
//						StandardClassGenerationInfo referencedGenerationInfo = generationInfos.get(fieldInfo.getFieldType());
//
//						// Generate as reference to PkeyTO
//						if (generationInfo.getFieldsAsIds().contains(fieldInfo))
//							fieldInfo.setTypeSurrogate(referencedGenerationInfo.getToName());
//					}
//
//					else
//					{
//						Map<Class<?>, String> typeSurrogatesTypeArguments = new HashMap<>();
//
//						for (int i = 0; i < fieldInfo.getActualTypeArguments().size(); ++i) {
//							Class<?> typeArgumentType = fieldInfo.getActualTypeArguments().get(i);
//
//							if (generationInfos.containsKey(typeArgumentType))
//							{
//								StandardClassGenerationInfo typeGenerationInfo = generationInfos.get(typeArgumentType);
//
//								typeSurrogatesTypeArguments.put(typeArgumentType, typeGenerationInfo.getToName());
//							}
//						}
//
//						fieldInfo.setActualTypeArgumentSurrogates(typeSurrogatesTypeArguments);
//					}
//				}

				daoTemplate.addField(fieldInfo);
			}
		}

		return daoTemplate.finishTemplate();
	}

	private String fillTOTemplate(StandardClassGenerationInfo generationInfo,
		Map<Class<?>, StandardClassGenerationInfo> generationInfos) {
		toTemplate.startNewTemplate();

		AbstractTOGenerator.fillDefaultTemplateParameters(toTemplate, generationInfo.getTargetPackageName());
		toTemplate.setTemplateParam(StandardTOTemplate.PARAM_NAME_TO_NAME, generationInfo.getToName());

		for (int j = 0; j < generationInfo.getFieldInfos().size(); ++j) {
			FieldInfo fieldInfo = generationInfo.getFieldInfos().get(j);

			if (fieldInfo.isPkey()) {
				toTemplate.setTemplateParam(StandardTOTemplate.PARAM_NAME_IDFIELD_TYPE,
					fieldInfo.getFieldType().getSimpleName());
			}

			if (fieldInfo.isVersion()) {
				toTemplate.setTemplateParam(StandardTOTemplate.PARAM_NAME_VESIONFIELD_TYPE,
					fieldInfo.getFieldType().getSimpleName());
			}

			// Version fields are ignored
			if (!fieldInfo.isVersion() && !fieldInfo.isPkey() && !fieldInfo.isUnique()) {
				// Foreign key whose type is one of the other entities is replaced with a
				// surrogate type
				if (fieldInfo.isFkey()) {
					if (generationInfos.containsKey(fieldInfo.getFieldType())) {
						StandardClassGenerationInfo referencedGenerationInfo = generationInfos
							.get(fieldInfo.getFieldType());

						// Generate as reference to PkeyTO
						if (generationInfo.getFieldsAsIds().contains(fieldInfo)) {
							fieldInfo.setTypeSurrogate(referencedGenerationInfo.getToName());
						}
					}

					else {
						Map<Class<?>, String> typeSurrogatesTypeArguments = new HashMap<>();

						for (int i = 0; i < fieldInfo.getActualTypeArguments().size(); ++i) {
							Class<?> typeArgumentType = fieldInfo.getActualTypeArguments().get(i);

							if (generationInfos.containsKey(typeArgumentType)) {
								StandardClassGenerationInfo typeGenerationInfo = generationInfos.get(typeArgumentType);

								typeSurrogatesTypeArguments.put(typeArgumentType, typeGenerationInfo.getToName());
							}
						}

						fieldInfo.setActualTypeArgumentSurrogates(typeSurrogatesTypeArguments);
					}
				}

				toTemplate.addField(fieldInfo);
			}
		}

		return toTemplate.finishTemplate();
	}
//
//	private String fillPkeyTOTemplate(List<FieldInfo> fieldInfos, String pkeyTOName, String targetPackageName, Map<Class<?>, StandardClassGenerationInfo> generationInfos)
//	{
//		pkeyTOTemplate.startNewTemplate();
//
//		fillDefaultTemplateParameters(pkeyTOTemplate, targetPackageName);
//		pkeyTOTemplate.setTemplateParam(FhuniiPkeyTOTemplate.PARAM_NAME_PKEY_TO_NAME, pkeyTOName);
//
//		for (int j = 0; j < fieldInfos.size(); ++j)
//		{
//			FieldInfo fieldInfo = fieldInfos.get(j);
//
//			// Generate the unique id
//			if (fieldInfo.isPkey())
//			{
//				pkeyTOTemplate.setTemplateParam(FhuniiPkeyTOTemplate.PARAM_NAME_IDFIELD_NAME, fieldInfo.getFieldName());
//				pkeyTOTemplate.setTemplateParam(FhuniiPkeyTOTemplate.PARAM_NAME_IDFIELD_TYPE, fieldInfo.getFieldType().getSimpleName());
//			}
//
//			// Furthermore, only unique fields additionally contribute to the pkey
//			if (fieldInfo.isUnique())
//			{
//				// Foreign key whose type is one of the other POs is replaced with a surrogate type
//				if (fieldInfo.isFkey() && generationInfos.containsKey(fieldInfo.getFieldType()))
//				{
//					StandardClassGenerationInfo generationInfo = generationInfos.get(fieldInfo.getFieldType());
//
//					setParameterizedTypeSurrogate(fieldInfo, true, generationInfos);
//
//					fieldInfo.setTypeSurrogate(generationInfo.getPkeyTOName());
//				}
//
//				pkeyTOTemplate.addField(fieldInfo);
//			}
//		}
//
//		return pkeyTOTemplate.finishTemplate();
//	}
//
//	private String fillCTOTemplate(String ctoName, String dataTOName, String pkeyTOName, String targetPackageName)
//	{
//		ctoTemplate.startNewTemplate();
//
//		fillDefaultTemplateParameters(ctoTemplate, targetPackageName);
//		ctoTemplate.setTemplateParam(FhuniiCTOTemplate.PARAM_NAME_DATA_TO_NAME, dataTOName);
//		ctoTemplate.setTemplateParam(FhuniiCTOTemplate.PARAM_NAME_PKEY_TO_NAME, pkeyTOName);
//		ctoTemplate.setTemplateParam(FhuniiCTOTemplate.PARAM_NAME_CTO_NAME, ctoName);
//
//		return ctoTemplate.finishTemplate();
//	}

	private void finishConverterTemplates(StandardGenerationProperties properties) {
		List<String> converterClassNames = properties.getTargetTOFactoryClassClassSimpleNames();
		List<String> converterPackages = properties.getTargetToFactoryPackages();
		List<String> targetPackages = properties.getTargetToBasePackages();

		for (int i = 0; i < targetPackages.size(); ++i) {
			String targetPackage = targetPackages.get(i);

			StandardConverterTemplate template = converterTemplates.get(targetPackage);
			String generatedContent = template.finishTemplate();

			System.out.println(generatedContent);
			AbstractTOGenerator.generateOutputFile(generatedContent, properties.getTargetToFactoryPath(),
				converterPackages.get(i), converterClassNames.get(i));
		}
	}

	private void prepareConverterTemplates(StandardGenerationProperties properties) {
		List<String> converterClassNames = properties.getTargetTOFactoryClassClassSimpleNames();
		List<String> converterPackages = properties.getTargetToFactoryPackages();
		List<String> targetPackages = properties.getTargetToBasePackages();

		for (int i = 0; i < targetPackages.size(); ++i) {
			String targetPackage = targetPackages.get(i);

			File converterPath = new File(getTemplatePath().toFile(), "Converter.java.template");
			AbstractTOGenerator.checkExists(converterPath.toPath());
			StandardConverterTemplate template = new StandardConverterTemplate(converterPath);
			template.startNewTemplate();
			template.setTemplateParam(StandardConverterTemplate.PARAM_NAME_CONVERTER_NAME, converterClassNames.get(i));

			AbstractTOGenerator.fillDefaultTemplateParameters(template, converterPackages.get(i));

			converterTemplates.put(targetPackage, template);
		}
	}

	@Override
	protected StandardClassGenerationInfo createClassGenerationInfo(StandardGenerationProperties properties,
		Class<?> theSourceClass, List<FieldInfo> fieldInfos, String targetPackage) {
		return new StandardClassGenerationInfo(theSourceClass, properties, fieldInfos, targetPackage);
	}

	@Override
	protected StandardGenerationProperties createGenerationProperties(Properties properties) {
		return new StandardGenerationProperties(properties);
	}

	@Override
	protected void doAfterTOGeneration(StandardGenerationProperties generationProperties) {
		finishConverterTemplates(generationProperties);
	}

	@Override
	protected void doBeforeTOGeneration(StandardGenerationProperties generationProperties) {
		prepareConverterTemplates(generationProperties);
	}

	@Override
	protected void generateTO(StandardGenerationProperties generationProperties,
		Map<Class<?>, StandardClassGenerationInfo> classesWithFields, StandardClassGenerationInfo generationInfo,
		String targetPackage) {

		converterTemplates.get(targetPackage).addEntity(generationInfo, classesWithFields);

		String generatedTOString = fillTOTemplate(generationInfo, classesWithFields);

		AbstractTOGenerator.generateOutputFile(generatedTOString, generationProperties.getTargetToPath(), targetPackage,
			generationInfo.getToName());

		String generatedDAOString = fillDAOTemplate(generationInfo, classesWithFields);

		AbstractTOGenerator.generateOutputFile(generatedDAOString, generationProperties.getTargetToFactoryPath(),
			targetPackage, generationInfo.getDaoName());
	}
}
