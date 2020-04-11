package de.je.utils.j2a.generation;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class BaseClassGenerationInfo<T extends BaseGenerationProperties> {
	public BaseClassGenerationInfo(Class<?> sourceClass, T properties, List<FieldInfo> fieldInfos,
		String targetPackage) {
		this.sourceClass = sourceClass;
		this.fieldInfos = fieldInfos;

		// Remove PO suffix
		String classSimpleName = sourceClass.getSimpleName();
		this.clearedClassName = classSimpleName.endsWith(properties.getEntitySuffix())
			? classSimpleName.replace(properties.getEntitySuffix(), "")
			: classSimpleName;

		this.targetPackageName = determineTargetPackage(properties, classSimpleName, targetPackage);

		determineReferencedPkeys(properties, fieldInfos, classSimpleName);
	}

	private void determineReferencedPkeys(T properties, List<FieldInfo> fieldInfos, String classSimpleName) {
		Map<String, Set<String>> genPkeyReferences = properties.getGenIdReferences();

		if (genPkeyReferences.containsKey(classSimpleName)) {
			Set<String> referencedTypeNames = genPkeyReferences.get(classSimpleName);

			for (int i = 0; i < fieldInfos.size(); ++i) {
				FieldInfo element = fieldInfos.get(i);

				if (referencedTypeNames.contains(element.getFieldType().getSimpleName()))
					fieldsAsIds.add(element);
			}
		}
	}

	/**
	 * Returns the value of the attribute clearedClassName.
	 *
	 * @return clearedClassName The value of the attribute clearedClassName.
	 */
	public String getClearedClassName() {
		return clearedClassName;
	}

	/**
	 * Returns the value of the attribute targetPackageName.
	 *
	 * @return targetPackageName The value of the attribute targetPackageName.
	 */
	public String getTargetPackageName() {
		return targetPackageName;
	}

	/**
	 * Returns the value of the attribute sourceClass.
	 *
	 * @return sourceClass The value of the attribute sourceClass.
	 */
	public Class<?> getSourceClass() {
		return sourceClass;
	}

	/**
	 * Returns the value of the attribute fieldInfos.
	 *
	 * @return fieldInfos The value of the attribute fieldInfos.
	 */
	public List<FieldInfo> getFieldInfos() {
		return fieldInfos;
	}

	private static String determineTargetPackage(BaseGenerationProperties properties, String classSimpleName,
		String targetPackage) {
		// Determine child package of target package, if any
		Map<Pattern, String> targetChildPackages = properties.getTargetChildPackages();

		for (Iterator<Pattern> iterator = targetChildPackages.keySet().iterator(); iterator.hasNext();) {
			Pattern nextPattern = iterator.next();
			String nextChildPackage = targetChildPackages.get(nextPattern);

			if (classSimpleName.matches(nextPattern.pattern())) {
				targetPackage += "." + nextChildPackage;
				break;
			}
		}
		return targetPackage;
	}

	/**
	 * Returns the value of the attribute fieldsAsIds.
	 *
	 * @return fieldsAsIds The value of the attribute fieldsAsIds.
	 */
	public Set<FieldInfo> getFieldsAsIds() {
		return fieldsAsIds;
	}

	private final String clearedClassName;
	private final String targetPackageName;
	private final Class<?> sourceClass;
	private final Set<FieldInfo> fieldsAsIds = new HashSet<FieldInfo>();
	private final List<FieldInfo> fieldInfos;
}