package de.je.fhunii.generation;

import java.util.List;

import de.je.utils.j2a.generation.BaseClassGenerationInfo;
import de.je.utils.j2a.generation.FieldInfo;

public class FhuniiClassGenerationInfo extends BaseClassGenerationInfo<FhuniiGenerationProperties> {

	public FhuniiClassGenerationInfo(Class<?> sourceClass, FhuniiGenerationProperties properties,
		List<FieldInfo> fieldInfos, String targetPackage) {
		super(sourceClass, properties, fieldInfos, targetPackage);
		// Get TO suffices
		this.dataTOName = getClearedClassName() + properties.getDataTOSuffix();
		this.pkeyTOName = getClearedClassName() + properties.getPkeyTOSuffix();
		this.ctoName = getClearedClassName() + properties.getCompTOSuffix();
		this.infoToName = getClearedClassName() + properties.getInfoTOSuffix();

		String classSimpleName = sourceClass.getSimpleName();
		this.generateAsInfoTO = properties.getGenSingleTO().contains(classSimpleName);
	}

	/**
	 * Returns the value of the attribute ctoName.
	 *
	 * @return ctoName The value of the attribute ctoName.
	 */
	public String getCtoName() {
		return ctoName;
	}

	/**
	 * Returns the value of the attribute dataTOName.
	 *
	 * @return dataTOName The value of the attribute dataTOName.
	 */
	public String getDataTOName() {
		return dataTOName;
	}

	/**
	 * Returns the value of the attribute infoToName.
	 *
	 * @return infoToName The value of the attribute infoToName.
	 */
	public String getInfoToName() {
		return infoToName;
	}

	/**
	 * Returns the value of the attribute infoToName.
	 *
	 * @return infoToName The value of the attribute infoToName.
	 */
	public String getInfoTOName() {
		return infoToName;
	}

	/**
	 * Returns the value of the attribute pkeyTOName.
	 *
	 * @return pkeyTOName The value of the attribute pkeyTOName.
	 */
	public String getPkeyTOName() {
		return pkeyTOName;
	}

	/**
	 * Returns the value of the attribute generateAsInfoTO.
	 *
	 * @return generateAsInfoTO The value of the attribute generateAsInfoTO.
	 */
	public boolean isGenerateAsInfoTO() {
		return generateAsInfoTO;
	}

	private final String ctoName;
	private final String dataTOName;
	private final boolean generateAsInfoTO;
	private final String infoToName;
	private final String pkeyTOName;

}
