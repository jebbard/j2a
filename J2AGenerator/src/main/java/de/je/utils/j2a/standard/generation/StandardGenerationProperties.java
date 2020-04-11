package de.je.utils.j2a.standard.generation;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import de.je.utils.j2a.generation.BaseGenerationProperties;

public class StandardGenerationProperties extends BaseGenerationProperties {

	public StandardGenerationProperties(Properties properties) {
		super(properties);
		toSuffix = properties.getProperty("toSuffix", "TO");
		daoSuffix = properties.getProperty("daoSuffix", "DAO");
		targetDaoPackages = convertTargetDaoPackages(properties);
		persistenceUnit = properties.getProperty("persistenceUnit", "DEFAULT");
	}

	private static List<String> convertTargetDaoPackages(Properties properties) {
		String daoTargetPackages = properties.getProperty("targetDaoPackages");

		String[] daoTargetPackagesArray = daoTargetPackages.split(PATH_PACKAGE_SEPARATOR);

		return Arrays.asList(daoTargetPackagesArray);
	}

	public String getToSuffix() {
		return toSuffix;
	}

	/**
	 * A suffix for each generated TO. If nothing is given, defaults to "TO"
	 */
	private final String toSuffix;

	public List<String> getTargetDaoPackages() {
		return targetDaoPackages;
	}

	public String getDaoSuffix() {
		return daoSuffix;
	}

	/**
	 * Semicolon-separated list of fully qualified package names of the target
	 * package to store entity DAOs The number of package names given here must
	 * exactly match the number of packages specified in sourcePackages and
	 * targetBasePackages. Each given package is mapped to the target base package
	 * with same position.
	 */
	private final List<String> targetDaoPackages;// =de.je.wm.user.impl.dao;de.je.wm.nutrition.impl.dao;de.je.wm.diary.impl.dao

	/**
	 * The suffix to use for DAOs
	 */
	private final String daoSuffix;// =DAO

	/**
	 * The persistence unit used for any generated entity DAO
	 */
	private final String persistenceUnit;// =DEFAULT

	public String getPersistenceUnit() {
		return persistenceUnit;
	}
}
