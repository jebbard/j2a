/**
 *
 * {@link GeneratorResult}.java
 *
 * @author Jens Ebert
 *
 * @date 04.04.2020
 *
 */
package de.je.utils.j2a.generation;

import java.nio.file.Path;

/**
 * {@link GeneratorResult}
 *
 */
public class GeneratorResult {
	private final String output;
	private final Path outputRelativeTargetPath;

	/**
	 * Creates a new {@link GeneratorResult}.
	 * 
	 * @param output
	 * @param outputTargetPath
	 */
	public GeneratorResult(String output, Path outputTargetPath) {
		this.output = output;
		this.outputRelativeTargetPath = outputTargetPath;
	}

	public String getOutput() {
		return output;
	}

	public Path getOutputRelativeTargetPath() {
		return outputRelativeTargetPath;
	}
}
