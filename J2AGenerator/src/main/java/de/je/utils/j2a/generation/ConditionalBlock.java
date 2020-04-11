package de.je.utils.j2a.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.je.utils.j2a.fillTemplate.AbstractTOWithFieldsTemplate;

public class ConditionalBlock {

	@Override
	public String toString() {
		return "ConditionalBlock [name=" + name + ", instanceId=" + instanceId + ", contents=" + contents
			+ ", completeBlock=" + completeBlock + "]";
	}

	// Repeat Block pattern before name content
	private static final String CONDITIONAL_BLOCK_PATTERN_START_PREFIX = "[$COND-BLOCK Name=\"";

	// Repeat Block pattern after name content
	private static final String CONDITIONAL_BLOCK_PATTERN_START_SUFFIX = "\", InstanceID=\"" + "([A-Za-z0-9]*?)"
		+ "\"]";

	private static final String CONDITIONAL_BLOCK_PATTERN_MIDDLE = "([.\\s\\S]*?)";
	private static final String CONDITIONAL_BLOCK_PATTERN_END = "[$COND-END]";

	private static final int CB_INSTANCE_ID_GROUP_INDEX = 1;
	private static final int CB_CONTENT_GROUP_INDEX = 2;

	public ConditionalBlock(String name, String instanceId, String contents, String completeBlockRaw,
		String enclosingRepeatBlockId) {
		this.name = name;
		this.instanceId = instanceId;
		this.contents = contents;
		this.completeBlock = completeBlockRaw;
		this.enclosingRepeatBlockId = enclosingRepeatBlockId;
	}

	public String expand(AbstractTOWithFieldsTemplate template, FieldInfo fi, RepeatBlock enclosingBlock,
		String enclosingBlockCurrentContent) {
		String replaceString = "";

		if (template.mayExpandConditionalBlock(fi, this, enclosingBlock))
			replaceString = getContents();

		String replacedBlockContent = enclosingBlockCurrentContent.replaceAll(getPattern(name).pattern(),
			Matcher.quoteReplacement(replaceString));

		return replacedBlockContent;
	}

	public static List<ConditionalBlock> getAllInstancesForName(String name, String document) {
		Pattern conditionalBlockPattern = getPattern(name);

		Matcher matcher1 = conditionalBlockPattern.matcher(document);
		Matcher matcher = matcher1;

		List<ConditionalBlock> returnedBlocks = new ArrayList<ConditionalBlock>();

		while (matcher.find()) {
			String wholeConditionalBlock = matcher.group();

			String enclosingRepeatBlockId = null;

			returnedBlocks.add(new ConditionalBlock(name, matcher.group(CB_INSTANCE_ID_GROUP_INDEX),
				matcher.group(CB_CONTENT_GROUP_INDEX), wholeConditionalBlock, enclosingRepeatBlockId));
		}

		return returnedBlocks;
	}

	private static Pattern getPattern(String name) {
		Pattern conditionalBlockPattern = Pattern
			.compile(
				Pattern.quote(CONDITIONAL_BLOCK_PATTERN_START_PREFIX) + name + CONDITIONAL_BLOCK_PATTERN_START_SUFFIX
					+ CONDITIONAL_BLOCK_PATTERN_MIDDLE + Pattern.quote(CONDITIONAL_BLOCK_PATTERN_END),
				Pattern.MULTILINE);
		return conditionalBlockPattern;
	}

	public String getName() {
		return name;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public String getContents() {
		return contents;
	}

	public String getCompleteBlock() {
		return completeBlock;
	}

	public String getEnclosingRepeatBlockId() {
		return enclosingRepeatBlockId;
	}

	private final String name;
	private final String instanceId;
	private final String contents;
	private final String completeBlock;
	private final String enclosingRepeatBlockId;
}
