package com.github.j2a.eclipseplugin.handlers;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.github.j2a.core.generation.Generator;
import com.github.j2a.core.generation.J2A;

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

		List<IResource> resources = getResourcesInWorkspaceImplementing(Generator.class.getName());

		Map<IResource, IJavaProject> projectsOfResources = resources.stream()
			.collect(Collectors.toMap(res -> res, res -> JavaCore.create(res.getProject())));

		Map<IJavaProject, IClasspathEntry[]> classPathEntryMap = getProjectClasspaths(resources);

		for (IResource resource : resources) {

			IJavaElement javaElement = JavaCore.create(resource);

			IClasspathEntry[] resClassPath = classPathEntryMap.get(projectsOfResources.get(resource));

			Path[] paths = Arrays.stream(resClassPath).map(ce -> ce.getPath().toFile().toPath())
				.toArray(size -> new Path[size]);

			paths[0] = Paths.get("C:\\Programmieren\\08_GitProjekte\\runtime-EclipseApplication\\d\\target\\classes\\");
			MessageDialog.openInformation(window.getShell(), "J2a-eclipse-plugin", Arrays.toString(paths));

			new J2A().getAllRegisteredGenerators();

//			Generator gen = new J2A().getGenerator("d.FDefaef", paths);

//			System.out.println(gen.getName());
		}

		return null;
	}

	/**
	 * @param resources
	 * @return
	 */
	private Map<IJavaProject, IClasspathEntry[]> getProjectClasspaths(List<IResource> resources) {
		Map<IJavaProject, IClasspathEntry[]> classPathEntryMap = new HashMap<>();

		for (IResource resource : resources) {
			IJavaProject javaProject = JavaCore.create(resource.getProject());

			if (!classPathEntryMap.containsKey(javaProject)) {
				IClasspathEntry[] classPathEntries;
				try {
					classPathEntries = javaProject.getResolvedClasspath(false);
				} catch (JavaModelException e) {
					throw new RuntimeException("Unexpected java model exception", e);
				}

				classPathEntryMap.put(javaProject, classPathEntries);
			}
		}
		return classPathEntryMap;
	}

	/**
	 * @return
	 */
	private List<IResource> getResourcesInWorkspaceImplementing(String fullyQualifiedInterfaceName) {
		SearchPattern pattern = SearchPattern.createPattern(fullyQualifiedInterfaceName, IJavaSearchConstants.TYPE,
			IJavaSearchConstants.IMPLEMENTORS, SearchPattern.R_EQUIVALENT_MATCH);

		SearchEngine engine = new SearchEngine();

		List<IResource> resources = new ArrayList<>();

		try {
			engine.search(pattern, new SearchParticipant[] { SearchEngine.getDefaultSearchParticipant() },
				SearchEngine.createWorkspaceScope(), new SearchRequestor() {

					@Override
					public void acceptSearchMatch(SearchMatch match) throws CoreException {
						resources.add(match.getResource());
					}
				}, null);
		} catch (CoreException e) {
			throw new RuntimeException("Could not complete search", e);
		}
		return resources;
	}
}
