package de.je.utils.j2a.generation;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import de.je.utils.j2a.fillTemplate.AbstractClassTemplate;
import de.je.utils.j2a.reader.EntityReader;

public abstract class AbstractTOGenerator<T extends BaseGenerationProperties, V extends BaseClassGenerationInfo<T>>
	implements TOGenerator {

	private static final File DEFAULT_TEMPLATE_PATH = new File("./src/main/resources/");

	private static ClassLoader createClassLoaderForPaths(List<File> theClassLoaderPaths) {
		URL[] pathUrls = new URL[theClassLoaderPaths.size()];

		for (int i = 0; i < theClassLoaderPaths.size(); ++i) {
			File element = theClassLoaderPaths.get(i);

			try {
				pathUrls[i] = element.toURI().toURL();
			} catch (MalformedURLException e) {
				throw new IllegalArgumentException("One of the paths could not be converted to a URL", e);
			}
		}

		// It is VERY important to set the current class loader as parent here
		return new URLClassLoader(pathUrls, Thread.currentThread().getContextClassLoader());
	}

	private static List<File> findSourceEntityClassFiles(File sourceClassPath, boolean recursive) {
		List<File> sourceClassFiles = new ArrayList<>();

		File[] embeddedFiles = sourceClassPath.listFiles();

		for (int i = 0; i < embeddedFiles.length; i++) {
			File embeddedFile = embeddedFiles[i];

			if (embeddedFile.isFile() && embeddedFile.getName().endsWith(".class")) {
				sourceClassFiles.add(embeddedFile);
			} else if (recursive && embeddedFile.isDirectory()) {
				sourceClassFiles.addAll(AbstractTOGenerator.findSourceEntityClassFiles(embeddedFile, recursive));
			}
		}

		return sourceClassFiles;
	}

	private static String toPath(String packagePath) {
		return packagePath.replace(".", System.getProperty("file.separator"));
	}

	protected static void checkExists(Path joinedPath) {
		if (!Files.exists(joinedPath)) {
			throw new IllegalArgumentException("The path '" + joinedPath + "' does not exist!");
		}
	}

	protected static void fillDefaultTemplateParameters(AbstractClassTemplate template, String targetPackageName) {
		template.setTemplateParam(AbstractClassTemplate.PARAM_NAME_PACKAGE_NAME, targetPackageName);
		template.setTemplateParam(AbstractClassTemplate.PARAM_NAME_AUTHOR, System.getProperty("user.name"));
	}

	protected static void generateOutputFile(String generatedOutput, File targetPath, String targetPackage,
		String name) {
		System.out.println(generatedOutput);
		System.out.println();
		System.out.println();

		Path outputPath = Paths.get(targetPath.getAbsolutePath(), AbstractTOGenerator.toPath(targetPackage),
			name + ".java");

		try {
			Files.writeString(outputPath, generatedOutput, StandardOpenOption.TRUNCATE_EXISTING,
				StandardOpenOption.CREATE);
		} catch (IOException e) {
			throw new IllegalStateException("Could not write to file '" + outputPath + "'.");
		}
	}

	private final EntityReader reader = new EntityReader();

	private final Path templatePath;

	public AbstractTOGenerator(String templatePackage) {
		templatePath = Paths.get(AbstractTOGenerator.DEFAULT_TEMPLATE_PATH.getAbsolutePath(),
			AbstractTOGenerator.toPath(templatePackage));
	}

	@Override
	public void generate(Properties properties) {
		T generationProperties = createGenerationProperties(properties);

		List<File> classLoaderPaths = getClassLoaderPaths(generationProperties);

		ClassLoader classLoader = AbstractTOGenerator.createClassLoaderForPaths(classLoaderPaths);

		List<String> sourcePackages = generationProperties.getSourcePackages();
		List<String> targetPackages = generationProperties.getTargetToBasePackages();

		List<File> sourceClassFiles = new ArrayList<>();
		Map<Class<?>, V> classesWithFields = new HashMap<>();

		for (int i = 0; i < sourcePackages.size(); ++i) {
			String sourcePackage = sourcePackages.get(i);

			Path sourceClassPath = Paths.get(generationProperties.getEntitySourcePath().getAbsolutePath(),
				AbstractTOGenerator.toPath(sourcePackage));

			AbstractTOGenerator.checkExists(sourceClassPath);

			List<File> sourceClassFilesForPackage = AbstractTOGenerator
				.findSourceEntityClassFiles(sourceClassPath.toFile(), generationProperties.isSourceRecursive());

			sourceClassFiles.addAll(sourceClassFilesForPackage);

			classesWithFields.putAll(extractGenerationInfos(sourcePackage, targetPackages.get(i), generationProperties,
				classLoader, sourceClassFilesForPackage));
		}

		doBeforeTOGeneration(generationProperties);

		for (Iterator<Class<?>> sourceClassIterator = classesWithFields.keySet().iterator(); sourceClassIterator
			.hasNext();) {
			Class<?> nextSourceClass = sourceClassIterator.next();
			V generationInfo = classesWithFields.get(nextSourceClass);

			String targetPackage = generationInfo.getTargetPackageName();

			generateTO(generationProperties, classesWithFields, generationInfo, targetPackage);
		}

		doAfterTOGeneration(generationProperties);
	}

	private Map<Class<?>, V> extractGenerationInfos(String sourcePackage, String targetPackage, T properties,
		ClassLoader classLoader, List<File> sourceClassFiles) {
		Map<Class<?>, V> classGenerationInfos = new HashMap<>();

		for (int i = 0; i < sourceClassFiles.size(); ++i) {
			File element = sourceClassFiles.get(i);

			String className = element.getName().replace(".class", "");
			String qualifiedClassName = sourcePackage + "." + className;

			if (className.matches(properties.getEntityPattern().pattern())) {
				try {
					Class<?> theSourceClass = classLoader.loadClass(qualifiedClassName);

					List<FieldInfo> fieldInfos = reader.getFieldInfos(theSourceClass, properties.getIdType(),
						properties.getVersionType(), properties.isDefaultNullable());

					System.out.println(fieldInfos);

					classGenerationInfos.put(theSourceClass,
						createClassGenerationInfo(properties, theSourceClass, fieldInfos, targetPackage));
				} catch (ClassNotFoundException e) {
					throw new IllegalStateException("Could not load class '" + qualifiedClassName + "'.", e);
				}
			}
		}

		return classGenerationInfos;
	}

	protected abstract V createClassGenerationInfo(T properties, Class<?> theSourceClass, List<FieldInfo> fieldInfos,
		String targetPackage);

	protected abstract T createGenerationProperties(Properties properties);

	protected abstract void doAfterTOGeneration(T generationProperties);

	protected abstract void doBeforeTOGeneration(T generationProperties);

	protected abstract void generateTO(T generationProperties, Map<Class<?>, V> classesWithFields, V generationInfo,
		String targetPackage);

	protected List<File> getClassLoaderPaths(T properties) {
		List<File> theClassLoaderPaths = new ArrayList<>();

		theClassLoaderPaths.add(properties.getEntitySourcePath());
		theClassLoaderPaths.add(properties.getTargetToPath());
		theClassLoaderPaths.addAll(properties.getEntityDependencyPaths());
		theClassLoaderPaths.add(properties.getTargetToFactoryPath());
		return theClassLoaderPaths;
	}

	protected Path getTemplatePath() {
		return templatePath;
	}
}
