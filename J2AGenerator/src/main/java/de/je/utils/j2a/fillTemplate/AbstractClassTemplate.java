package de.je.utils.j2a.fillTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import de.je.utils.j2a.generation.RepeatBlock;

public abstract class AbstractClassTemplate {
	public final static String PARAM_NAME_AUTHOR = "{$Author}";
	public final static String PARAM_NAME_PACKAGE_NAME = "{$Package-Name}";

	public AbstractClassTemplate(File templateFile, String[] repeatBlockNames, String[] conditionalBlockNames,
		String[] allowedParamNames) {
		for (int i = 0; i < allowedParamNames.length; i++)
			this.allowedParamNames.add(allowedParamNames[i]);

		this.allowedParamNames.add(PARAM_NAME_AUTHOR);
		this.allowedParamNames.add(PARAM_NAME_PACKAGE_NAME);

		try (BufferedReader reader = new BufferedReader(new FileReader(templateFile))) {

			int fileSize = (int) templateFile.length();

			CharBuffer charBuffer = CharBuffer.wrap(new char[fileSize]);

			int readCharacters = 0;

			while (readCharacters < fileSize) {
				int returned = reader.read(charBuffer);

				if (returned == -1)
					break;

				readCharacters += returned;
			}

			charBuffer.rewind();

			templateStaticContents = new String(new StringBuffer(charBuffer));

			for (int i = 0; i < repeatBlockNames.length; i++) {
				String repeatBlockName = repeatBlockNames[i];

				repeatBlocks.addAll(
					RepeatBlock.getAllInstancesForName(repeatBlockName, templateStaticContents, conditionalBlockNames));
			}
		} catch (IOException e) {
			throw new IllegalStateException("Template file " + templateFile.getAbsolutePath()
				+ " could not be found or could not be accessed. Original exception: " + e);
		}
	}

	public String finishTemplate() {
		checkState();

		for (int i = 0; i < repeatBlocks.size(); i++) {
			RepeatBlock repeatBlock = repeatBlocks.get(i);

			currentTemplateContents = repeatBlock.removeBlock(currentTemplateContents);
		}

		String returnValue = currentTemplateContents;

		currentTemplateContents = null;

		return returnValue;
	}

	public void setTemplateParam(String paramName, String paramValue) {
		checkState();

		if (paramName == null || paramValue == null)
			throw new IllegalArgumentException("Parameters may not be null");

		if (!allowedParamNames.contains(paramName))
			throw new IllegalArgumentException("Parameter name '" + paramName + "' is not supported.");

		currentTemplateContents = currentTemplateContents.replaceAll(Pattern.quote(paramName), paramValue);
	}

	public void startNewTemplate() {
		currentTemplateContents = new String(templateStaticContents);
	}

	/**
	 * Returns the value of the attribute repeatBlocks.
	 *
	 * @return repeatBlocks The value of the attribute repeatBlocks.
	 */
	public List<RepeatBlock> getRepeatBlocks() {
		return repeatBlocks;
	}

	public void replaceContent(String toReplace, String replacement) {
		checkState();

		// replaceFirst can't handle it any other way, Pattern.quote() is not sufficient
		replacement = replacement.replace("$", "\\$");

		currentTemplateContents = currentTemplateContents.replaceFirst(Pattern.quote(toReplace), replacement);
	}

	protected void checkState() {
		if (currentTemplateContents == null)
			throw new IllegalStateException("startNewTemplate() has not been called");
	}

	private final Set<String> allowedParamNames = new HashSet<String>();
	private final List<RepeatBlock> repeatBlocks = new ArrayList<RepeatBlock>();
	private final String templateStaticContents;
	private String currentTemplateContents = null;
}
