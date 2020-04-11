package de.je.utils.j2a.generation;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

public class BaseGenerationProperties {
	protected static final String PATH_PACKAGE_SEPARATOR = ";";

	private static final String PROPERTY_VALUE_ILLEGAL = "Illegal property value for property '";

	public BaseGenerationProperties(final Properties properties) {
		if (properties == null)
			throw new IllegalArgumentException("Properties must not be null");

		entityPattern = convertEntityPattern(properties);
		entitySuffix = properties.getProperty("entitySuffix");
		entitySourcePath = convertPath(properties, "entitySourcePath");
		targetToPath = convertPath(properties, "targetToPath");
		entitySourcePackages = convertSourcePackages(properties);
		sourceRecursive = convertBoolean(properties, "sourceRecursive");
		defaultNullable = convertBoolean(properties, "defaultNullable");
		targetToBasePackages = convertTargetBasePackages(properties);
		targetChildPackages = convertTargetChildPackages(properties);
		genIdReferences = convertGenIdReference(properties);
		entityDependencyPaths = convertEntityDependencyPaths(properties);
		idType = properties.getProperty("idType");
		versionType = properties.getProperty("versionType");
		targetToFactoryPath = convertPath(properties, "targetToFactoryPath");
		targetToFactoryClassSimpleNames = convertTargetTOFactoryClassSimpleNames(properties);
		targetToFactoryPackages = convertTargetTOFactoryPackages(targetToFactoryClassSimpleNames, properties);

		if (entitySourcePackages.size() != targetToBasePackages.size())
			throw new IllegalArgumentException("Number of source packages (" + entitySourcePackages.size()
				+ ") does not match number of target packages (" + targetToBasePackages.size() + ").");

		if (entitySourcePackages.size() != targetToFactoryPackages.size())
			throw new IllegalArgumentException("Number of source packages (" + entitySourcePackages.size()
				+ ") does not match number of converter packages (" + targetToFactoryPackages.size() + ").");
	}

	/**
	 * Returns the value of the attribute defaultNullable.
	 *
	 * @return defaultNullable The value of the attribute defaultNullable.
	 */
	public boolean isDefaultNullable() {
		return defaultNullable;
	}

	/**
	 * Returns the value of the attribute idType.
	 *
	 * @return idType The value of the attribute idType.
	 */
	public String getIdType() {
		return idType;
	}

	public Pattern getEntityPattern() {
		return entityPattern;
	}

	public String getEntitySuffix() {
		return entitySuffix;
	}

	public File getEntitySourcePath() {
		return entitySourcePath;
	}

	public List<String> getSourcePackages() {
		return entitySourcePackages;
	}

	public boolean isSourceRecursive() {
		return sourceRecursive;
	}

	public File getTargetToPath() {
		return targetToPath;
	}

	public List<String> getTargetToBasePackages() {
		return targetToBasePackages;
	}

	public Map<Pattern, String> getTargetChildPackages() {
		return targetChildPackages;
	}

	/**
	 * Returns the value of the attribute entityDependencyPaths.
	 *
	 * @return entityDependencyPaths The value of the attribute
	 *         entityDependencyPaths.
	 */
	public List<File> getEntityDependencyPaths() {
		return entityDependencyPaths;
	}

	/**
	 * Returns the value of the attribute targetTOFactoryPackages.
	 *
	 * @return targetTOFactoryPackages The value of the attribute
	 *         targetTOFactoryPackages.
	 */
	public List<String> getTargetToFactoryPackages() {
		return targetToFactoryPackages;
	}

	/**
	 * Returns the value of the attribute targetTOFactoryPath.
	 *
	 * @return converterPath The value of the attribute targetTOFactoryPath.
	 */
	public File getTargetToFactoryPath() {
		return targetToFactoryPath;
	}

	/**
	 * Returns the value of the attribute targetTOFactoryClassSimpleNames.
	 *
	 * @return targetTOFactoryClassSimpleNames The value of the attribute
	 *         targetTOFactoryClassSimpleNames.
	 */
	public List<String> getTargetTOFactoryClassClassSimpleNames() {
		return targetToFactoryClassSimpleNames;
	}

	/**
	 * Returns the value of the attribute genPkeyReferences.
	 *
	 * @return genPkeyReferences The value of the attribute genPkeyReferences.
	 */
	public Map<String, Set<String>> getGenIdReferences() {
		return genIdReferences;
	}

	/**
	 * Returns the value of the attribute versionType.
	 *
	 * @return versionType The value of the attribute versionType.
	 */
	public String getVersionType() {
		return versionType;
	}

	@Override
	public String toString() {
		return "BaseGenerationProperties [entityPattern=" + entityPattern + ", entitySuffix=" + entitySuffix
			+ ", entitySourcePath=" + entitySourcePath + ", entityDependencyPaths=" + entityDependencyPaths
			+ ", entitySourcePackages=" + entitySourcePackages + ", sourceRecursive=" + sourceRecursive
			+ ", defaultNullable=" + defaultNullable + ", idType=" + idType + ", versionType=" + versionType
			+ ", targetTOPath=" + targetToPath + ", targetToBasePackages=" + targetToBasePackages
			+ ", targetChildPackages=" + targetChildPackages + ", genIdReferences=" + genIdReferences
			+ ", targetToFactoryPath=" + targetToFactoryPath + ", targetToFactoryPackages=" + targetToFactoryPackages
			+ ", targetToFactoryClassSimpleNames=" + targetToFactoryClassSimpleNames + "]";
	}

	protected static File convertPath(Properties properties, String propertyName) {
		File path = new File(properties.getProperty(propertyName));

		if (!path.isDirectory() || !path.exists())
			throw new IllegalArgumentException(
				PROPERTY_VALUE_ILLEGAL + propertyName + "'." + "Specified path is not a directory or does not exist.");

		return path;
	}

	private static List<String> convertTargetTOFactoryClassSimpleNames(final Properties properties) {
		String converterClasses = properties.getProperty("targetToFactoryNames");

		String[] converterClassesArray = converterClasses.split(PATH_PACKAGE_SEPARATOR);

		List<String> converterClassesList = new ArrayList<>();

		for (int i = 0; i < converterClassesArray.length; i++) {

			String[] segments = converterClassesArray[i].split("\\.");
			converterClassesList.add(segments[segments.length - 1]);
		}

		return converterClassesList;

	}

	private static List<String> convertTargetTOFactoryPackages(List<String> converterClassSimpleNames,
		final Properties properties) {
		String converterClasses = properties.getProperty("targetToFactoryNames");

		String[] converterClassesArray = converterClasses.split(PATH_PACKAGE_SEPARATOR);

		List<String> converterClassesList = new ArrayList<>();

		for (int i = 0; i < converterClassesArray.length; i++) {
			converterClassesList.add(converterClassesArray[i].replace("." + converterClassSimpleNames.get(i), ""));
		}

		return converterClassesList;
	}

	private static List<String> convertSourcePackages(final Properties properties) {
		String sourcePackages = properties.getProperty("entitySourcePackages");

		String[] sourcePackageArray = sourcePackages.split(PATH_PACKAGE_SEPARATOR);

		return Arrays.asList(sourcePackageArray);
	}

	private static List<String> convertTargetBasePackages(final Properties properties) {
		String targetPackages = properties.getProperty("targetToBasePackages");

		String[] targetPackageArray = targetPackages.split(PATH_PACKAGE_SEPARATOR);

		return Arrays.asList(targetPackageArray);
	}

	private static Map<Pattern, String> convertTargetChildPackages(Properties properties) {
		String propertyName = "targetChildPackage";

		int index = 1;

		String currentPropertyName = propertyName + "_" + index++;

		// Linked hash map to conserve order
		Map<Pattern, String> returnedMap = new LinkedHashMap<Pattern, String>();

		while (properties.containsKey(currentPropertyName)) {
			String propertyValue = properties.getProperty(currentPropertyName).trim();

			String[] mapping = propertyValue.split("->");

			if (mapping.length != 2)
				throw new IllegalArgumentException(PROPERTY_VALUE_ILLEGAL + currentPropertyName + "'=" + propertyValue
					+ ". Mapping could not be split");

			returnedMap.put(Pattern.compile(mapping[0]), mapping[1]);

			currentPropertyName = propertyName + "_" + index++;
		}

		return returnedMap;
	}

	private static Map<String, Set<String>> convertGenIdReference(Properties properties) {
		String propertyName = "genIdReference";

		int index = 1;

		String currentPropertyName = propertyName + "_" + index++;

		// Linked hash map to conserve order
		Map<String, Set<String>> returnedMap = new LinkedHashMap<String, Set<String>>();

		while (properties.containsKey(currentPropertyName)) {
			String propertyValue = properties.getProperty(currentPropertyName).trim();

			String[] mapping = propertyValue.split("->");

			if (mapping.length != 2)
				throw new IllegalArgumentException(PROPERTY_VALUE_ILLEGAL + currentPropertyName + "'=" + propertyValue
					+ ". Mapping could not be split");

			if (!returnedMap.containsKey(mapping[0]))
				returnedMap.put(mapping[0], new HashSet<String>());

			returnedMap.get(mapping[0]).add(mapping[1]);

			currentPropertyName = propertyName + "_" + index++;
		}

		return returnedMap;
	}

	private static List<File> convertEntityDependencyPaths(Properties properties) {
		List<File> files = new ArrayList<File>();
		String stringDependency = properties.getProperty("entityDependencyPaths");

		String[] dependencyPaths = stringDependency.split(PATH_PACKAGE_SEPARATOR);

		for (int i = 0; i < dependencyPaths.length; i++) {
			String pathname = dependencyPaths[i].trim();
			File path = new File(pathname);

			if (!path.isDirectory() || !path.exists())
				throw new IllegalArgumentException(
					"Path '" + pathname + "' specified in dependency paths does not exist.");

			files.add(path);
		}

		return files;
	}

	private static boolean convertBoolean(Properties properties, String propertyName) {
		String stringSourceRecursive = properties.getProperty(propertyName);
		return Boolean.parseBoolean(stringSourceRecursive);
	}

	private static Pattern convertEntityPattern(Properties properties) {
		return Pattern.compile(properties.getProperty("entityPattern"));
	}

	/**
	 * Pattern by which persistent object classes in source package are identified
	 * Only classes who's name matches the pattern are recognized.
	 */
	private final Pattern entityPattern;// =*.PO

	/**
	 * A suffix of each PO class that will not make it into the final TO names
	 */
	private final String entitySuffix;

	/**
	 * Absolute or relative path of the source entity classes - must be the root of
	 * their class path
	 */
	private final File entitySourcePath;// =.

	/**
	 * Semicolon separated paths of dependencies Put any paths for here that are
	 * dependencies of the source classes.
	 */
	private final List<File> entityDependencyPaths;// =.
	/**
	 * List of fully qualified package names of the source packages All classes from
	 * this package that match the given entity pattern are taken as source entities
	 * for TO generation. The number of elements in this list equals the number of
	 * targetBasePackageNames.
	 */
	private final List<String> entitySourcePackages;// =de.de.de

	/**
	 * true to recurse into sub-packages of the source package, false otherwise
	 */
	private final boolean sourceRecursive;// =false
	/**
	 * Specifies whether the default for the nullable property of fields is "true"
	 * or "false"
	 */
	private final boolean defaultNullable;// =true
	/**
	 * Type of @Id annotated fields
	 */
	private final String idType;// =Long
	/**
	 * Type of @Version annotated fields
	 */
	private final String versionType;// =Long

	/**
	 * Absolute or relative path of the target classes - i.e. the root of their
	 * class path
	 */
	private final File targetToPath;// =.

	/**
	 * List of fully qualified package names of the target package All classes
	 * generated will be generated into this package The number of elements in this
	 * list equals the number of sourcePackageNames.
	 */
	private final List<String> targetToBasePackages;// =de.de.de

	/**
	 * An optional means to refine target generation, specified like this:
	 * targetChildPackage_X=<target TO name pattern> -> <sub package name> All
	 * generated TO classes matching the give name pattern are written to the given
	 * sub package of the target base package. There can be arbitrary
	 * targetChildPackage_X= properties, but each with different X and X must be a
	 * positive integer.
	 */
	private final Map<Pattern, String> targetChildPackages;

	/**
	 * This option lists all references that shall be generated to an id instead of
	 * to a TO (the default) There can be arbitrary genIdReference_X= properties,
	 * but each with different X and X must be a positive integer.
	 */
	private final Map<String, Set<String>> genIdReferences;

	/**
	 * Absolute or relative path of the implementation project - must be the root of
	 * its class path
	 */
	private final File targetToFactoryPath;

	/**
	 * Package name of the Converter class
	 */
	private final List<String> targetToFactoryPackages;

	/**
	 * Simple name of the Converter class
	 */
	private final List<String> targetToFactoryClassSimpleNames;
}
