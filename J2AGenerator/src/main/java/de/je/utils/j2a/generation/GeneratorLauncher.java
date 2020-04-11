package de.je.utils.j2a.generation;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

public class GeneratorLauncher {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Properties properties = new Properties();

		if (args.length != 2)
			throw new IllegalArgumentException(
				"Illegal number of arguments for starting this program. You must specify a fully qualified generator class name and a path to a properties file.");

		String generatorClassName = args[0];

		File propertiesFile = new File(args[1]);

		if (!propertiesFile.exists())
			throw new IllegalArgumentException(
				"The specified properties file does not exist: '" + propertiesFile.getAbsolutePath() + "'.");

		try (Reader myReader = new FileReader(propertiesFile)) {
			properties.load(myReader);
		} catch (IOException e) {
			throw new IllegalStateException(
				"Properties file '" + propertiesFile.getAbsolutePath() + "' could not be loaded.", e);
		}

		try {
			@SuppressWarnings("unchecked")
			Class<TOGenerator> generatorClass = (Class<TOGenerator>) Class.forName(generatorClassName);

			TOGenerator generator = generatorClass.newInstance();

			generator.generate(properties);
		} catch (Exception e1) {
			throw new IllegalArgumentException(
				"The specified class wasn't found or couldn't be instantiated: '" + generatorClassName + "'.", e1);
		}
	}

}
