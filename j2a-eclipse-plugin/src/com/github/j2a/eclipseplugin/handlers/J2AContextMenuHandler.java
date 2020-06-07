package com.github.j2a.eclipseplugin.handlers;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.github.j2a.core.definition.JavaClassDefinition;
import com.github.j2a.core.generation.GenerationContext;
import com.github.j2a.core.generation.Generator;
import com.github.j2a.core.generation.GeneratorResult;
import com.github.j2a.core.generation.J2A;
import com.github.j2a.core.parser.JavaParserBasedClassParser;

public class J2AContextMenuHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		List<Generator> generators = new J2A().getAllRegisteredGenerators();

		String genNames = "";

		for (Generator generator : generators) {
			genNames += generator.getName();
		}

		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);

		MessageDialog.openInformation(window.getShell(), "J2a-eclipse-plugin", genNames);

		ISelectionService service = window.getSelectionService();

		IStructuredSelection structured = (IStructuredSelection) service
			.getSelection("org.eclipse.jdt.ui.PackageExplorer");

		ICompilationUnit compilationUnit = (ICompilationUnit) structured.getFirstElement();

		IFile file = (IFile) compilationUnit.getResource();

		String fileContent;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(file.getContents(), file.getCharset()));

			fileContent = "";
			String nextLine = "";

			do {
				nextLine = reader.readLine();

				if (nextLine != null) {

					fileContent += nextLine + "\n";
				}
			} while (nextLine != null);
		} catch (CoreException | IOException e) {
			throw new RuntimeException("Unexpected exception accessing file " + file.getLocation(), e);
		}

		MessageDialog.openInformation(window.getShell(), "J2a-eclipse-plugin", fileContent);

		JavaClassDefinition classDefinition = new JavaParserBasedClassParser().parse(fileContent);

		for (Generator generator : generators) {
			GeneratorResult result = generator.generateResult(classDefinition, new GenerationContext("a.b.c"));
			MessageDialog.openInformation(window.getShell(), "J2a-eclipse-plugin", result.getOutput());
		}

		IPath makeAbsolute = file.getLocation().removeLastSegments(1).makeRelative();
		MessageDialog.openInformation(window.getShell(), "J2a-eclipse-plugin", makeAbsolute.toString());

		IFile outputFile = file.getProject().getFolder(makeAbsolute).getFile("TEST.txt");

		try {
			outputFile.create(new ByteArrayInputStream(new byte[] { 'A', 'B', 'B', 'A' }), IResource.NONE, null);
		} catch (CoreException e) {
			throw new RuntimeException("fff", e);
		}

		return null;
	}
}
