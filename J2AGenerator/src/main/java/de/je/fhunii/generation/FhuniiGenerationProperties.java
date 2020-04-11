package de.je.fhunii.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import de.je.utils.j2a.generation.BaseGenerationProperties;

public class FhuniiGenerationProperties extends BaseGenerationProperties {

	public FhuniiGenerationProperties(Properties properties) {
		super(properties);
		dataTOSuffix = properties.getProperty("dataTOSuffix", "DataTO");
		pkeyTOSuffix = properties.getProperty("pkeyTOSuffix", "PkeyTO");
		compTOSuffix = properties.getProperty("compTOSuffix", "CTO");
		infoTOSuffix = properties.getProperty("infoTOSuffix", "InfoTO");
		genInfoTO = convertGenInfoTO(properties);
	}

	/**
	 * Returns the value of the attribute genSingleTO.
	 *
	 * @return genSingleTO The value of the attribute genSingleTO.
	 */
	public List<String> getGenSingleTO() {
		return genInfoTO;
	}

	public String getDataTOSuffix() {
		return dataTOSuffix;
	}

	public String getPkeyTOSuffix() {
		return pkeyTOSuffix;
	}

	public String getCompTOSuffix() {
		return compTOSuffix;
	}

	/**
	 * Returns the value of the attribute infoTOSuffix.
	 *
	 * @return infoTOSuffix The value of the attribute infoTOSuffix.
	 */
	public String getInfoTOSuffix() {
		return infoTOSuffix;
	}

	@Override
	public String toString() {
		return "FhuniiGenerationProperties [[" + super.toString() + "], dataTOSuffix=" + dataTOSuffix
			+ ", pkeyTOSuffix=" + pkeyTOSuffix + ", compTOSuffix=" + compTOSuffix + ", infoTOSuffix=" + infoTOSuffix
			+ ", genInfoTO=" + genInfoTO + "]";
	}

	private static List<String> convertGenInfoTO(Properties properties) {
		String propertyName = "genInfoTO";

		String[] singleTOClasses = properties.getProperty(propertyName).split(";");

		List<String> asList = new ArrayList<String>(singleTOClasses.length);

		for (int i = 0; i < singleTOClasses.length; i++)
			asList.add(singleTOClasses[i].trim());

		return asList;
	}

	/**
	 * A suffix for each generated data TO - Must be different from the other
	 * suffixes. If nothing is given, defaults to "DataTO"
	 */
	private final String dataTOSuffix;// =DataTO
	/**
	 * A suffix for each generated primary key TO - Must be different from the other
	 * suffixes. If nothing is given, defaults to "PkeyTO"
	 */
	private final String pkeyTOSuffix;// =PkeyTO
	/**
	 * A suffix for each generated compound TO - Must be different from the other
	 * suffixes. If nothing is given, defaults to "CTO"
	 */
	private final String compTOSuffix;// =CTO
	/**
	 * A suffix for each generated info TO If nothing is given, defaults to "InfoTO"
	 */
	private final String infoTOSuffix;// =InfoTO
	/**
	 * This option allows to specify PO class names for which only a single InfoTO
	 * shall be generated (semicolon-separated) instead of pkeyTO, dataTO and CTO
	 */
	private final List<String> genInfoTO;

}
