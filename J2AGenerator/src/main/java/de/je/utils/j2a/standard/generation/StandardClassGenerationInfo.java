package de.je.utils.j2a.standard.generation;

import java.util.List;

import de.je.utils.j2a.generation.BaseClassGenerationInfo;
import de.je.utils.j2a.generation.FieldInfo;

public class StandardClassGenerationInfo extends BaseClassGenerationInfo<StandardGenerationProperties> {

	public StandardClassGenerationInfo(Class<?> sourceClass, StandardGenerationProperties properties,
		List<FieldInfo> fieldInfos, String targetPackage) {
		super(sourceClass, properties, fieldInfos, targetPackage);
		this.toName = getClearedClassName() + properties.getToSuffix();
		this.daoName = getClearedClassName() + properties.getDaoSuffix();
		this.persistenceUnit = properties.getPersistenceUnit();
	}

	public String getPersistenceUnit() {
		return persistenceUnit;
	}

	public String getDaoName() {
		return daoName;
	}

	/**
	 * Returns the value of the attribute toName.
	 *
	 * @return toName The value of the attribute toName.
	 */
	public String getToName() {
		return toName;
	}

	private final String toName;
	private final String daoName;
	private final String persistenceUnit;
}
