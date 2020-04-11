package de.je.utils.j2a.generation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.je.utils.j2a.fillTemplate.AbstractTOWithFieldsTemplate;

public class RepeatBlock {

	@Override
	public String toString() {
		return "RepeatBlock [parameterNames=" + parameterNames + ", name=" + name + ", instanceId=" + instanceId
			+ ", delimiter=" + delimiter + ", contents=" + contents + ", completeBlock=" + completeBlock + "]";
	}

	// Repeat Block pattern before name content
	private static final String REPEAT_BLOCK_PATTERN_START_PREFIX = "[$REPEAT-BLOCK Name=\"";

	// Repeat Block pattern after name content
	private static final String REPEAT_BLOCK_PATTERN_START_SUFFIX = "\", InstanceID=\"" + "([A-Za-z0-9]*?)" + "\""
		+ "(?:, Delim=\"(.*?)\")??]";

	private static final String REPEAT_BLOCK_PATTERN_MIDDLE = "([.\\s\\S]*?)";
	private static final String REPEAT_BLOCK_PATTERN_END = "[$REPEAT-END]";

	private static final int RB_INSTANCE_ID_GROUP_INDEX = 1;
	private static final int RB_DELIM_GROUP_INDEX = 2;
	private static final int RB_CONTENT_GROUP_INDEX = 3;

	private static final Pattern PARAMETER_PATTERN = Pattern.compile("\\{\\$.*?\\}");

	public RepeatBlock(String name, String instanceId, String delimiter, String contents, String completeBlockRaw,
		String[] conditionalBlockNames) {
		this.name = name;
		this.instanceId = instanceId;
		this.delimiter = delimiter;
		this.contents = contents;
		this.completeBlock = completeBlockRaw;

		this.parameterNames = new HashSet<String>();

		for (int i = 0; i < conditionalBlockNames.length; i++) {
			String conditionalBlockName = conditionalBlockNames[i];

			conditionalBlocks.addAll(ConditionalBlock.getAllInstancesForName(conditionalBlockName, contents));
		}

		extractParameterNames();
	}

	private void extractParameterNames() {
		Matcher parameterMatcher = PARAMETER_PATTERN.matcher(this.contents);

		while (parameterMatcher.find())
			parameterNames.add(parameterMatcher.group());
	}

	public String expand(Map<String, String> parameterValues, AbstractTOWithFieldsTemplate template, FieldInfo fi) {
		String replicatedBlock = this.contents;

		for (int i = 0; i < conditionalBlocks.size(); i++)
			replicatedBlock = conditionalBlocks.get(i).expand(template, fi, this, replicatedBlock);

		for (Iterator<String> iterator = parameterValues.keySet().iterator(); iterator.hasNext();) {
			String parameterName = iterator.next();

			replicatedBlock = replicatedBlock.replace(parameterName, parameterValues.get(parameterName));
		}

		if (this.delimiter != null)
			replicatedBlock += this.delimiter;

		// Add repeat block again for next replacement
		replicatedBlock += this.completeBlock;
		return replicatedBlock;
	}

	public static List<RepeatBlock> getAllInstancesForName(String name, String document,
		String[] conditionalBlockNames) {
		Pattern repeatBlockPattern = Pattern.compile(Pattern.quote(REPEAT_BLOCK_PATTERN_START_PREFIX) + name
			+ REPEAT_BLOCK_PATTERN_START_SUFFIX + REPEAT_BLOCK_PATTERN_MIDDLE + Pattern.quote(REPEAT_BLOCK_PATTERN_END),
			Pattern.MULTILINE);

		Matcher matcher = repeatBlockPattern.matcher(document);

		List<RepeatBlock> returnedBlocks = new ArrayList<RepeatBlock>();

		while (matcher.find()) {
			returnedBlocks.add(
				new RepeatBlock(name, matcher.group(RB_INSTANCE_ID_GROUP_INDEX), matcher.group(RB_DELIM_GROUP_INDEX),
					matcher.group(RB_CONTENT_GROUP_INDEX), matcher.group(), conditionalBlockNames));
		}

		return returnedBlocks;
	}

	public String removeBlock(String currentTemplateContents) {
		String newTemplateContent = currentTemplateContents;

		// Replace last delimiter, because its unnecessary and may lead to compile
		// errors.
		String delimiterReplacement = (getDelimiter() != null ? getDelimiter() : "");

		newTemplateContent = currentTemplateContents
			.replaceAll(Pattern.quote(delimiterReplacement + getCompleteBlock()), "");

		return newTemplateContent;
	}

	public Set<String> getParameters() {
		return parameterNames;
	}

	public String getName() {
		return name;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public String getContents() {
		return contents;
	}

	public String getCompleteBlock() {
		return completeBlock;
	}

	public String getBlockUniqueId() {
		return name + "_" + instanceId;
	}

	public List<ConditionalBlock> getConditionalBlocks() {
		return conditionalBlocks;
	}

	private final Set<String> parameterNames;
	private final String name;
	private final String instanceId;
	private final String delimiter;
	private final String contents;
	private final String completeBlock;

	private final List<ConditionalBlock> conditionalBlocks = new ArrayList<ConditionalBlock>();
}
