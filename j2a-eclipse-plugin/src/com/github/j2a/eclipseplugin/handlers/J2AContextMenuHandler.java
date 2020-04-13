package com.github.j2a.eclipseplugin.handlers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public class J2AContextMenuHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);

		/*
		 * TODO
		 *
		 * 1. Suche alle Klassen, die GeneratorOutput oder GeneratorOutputGroup
		 * implementieren 2. Ermittele das Projekt, in dem sich diese befinden und
		 * dessen Classpath 3. Rufe J2A mit Klassenname und Classpath auf, und erhalte
		 * Instanz von GneratorOutput oder GeneratorOutputGroup zurück 4. Puffere
		 * Instanz und erhalte Name der Gruppe /Klasse
		 */

		SearchPattern pattern = SearchPattern.createPattern("com.github.j2a.core.generation.Generator",
			IJavaSearchConstants.TYPE, IJavaSearchConstants.IMPLEMENTORS, SearchPattern.R_CASE_SENSITIVE);

		SearchEngine engine = new SearchEngine();

		List<String> matches = new ArrayList<>();

		try {
			engine.search(pattern, new SearchParticipant[] { SearchEngine.getDefaultSearchParticipant() },
				SearchEngine.createWorkspaceScope(), new SearchRequestor() {

					@Override
					public void acceptSearchMatch(SearchMatch arg0) throws CoreException {
						System.out.println(arg0);
						matches.add(arg0.getResource().getLocation().toString());
						matches.add(arg0.getResource().toString());

						IJavaProject javaProject = JavaCore.create(arg0.getResource().getProject());

						IClasspathEntry[] classPathEntries = javaProject.getResolvedClasspath(false);

						for (IClasspathEntry classpathEntry : classPathEntries) {
							matches.add(classpathEntry.getPath().toString());
						}

//						arg0.getResource().getProject().getWorkspace().getRoot().get
					}
				}, null);
		} catch (CoreException e) {
			throw new RuntimeException("", e);
		}

		MessageDialog.openInformation(window.getShell(), "J2a-eclipse-plugin", matches.toString());

		return null;
	}
}
