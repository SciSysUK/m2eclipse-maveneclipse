/*
 * Copyright 2000-2011 the original author or authors.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * 
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.m2e.maveneclipse.handler.additionalprojectfacets;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.m2e.maveneclipse.MavenEclipseContext;
import org.eclipse.m2e.maveneclipse.configuration.ConfigurationParameter;
import org.eclipse.m2e.maveneclipse.handler.ConfigurationHandler;
import org.eclipse.m2e.maveneclipse.handler.SingleParameterConfigurationHandler;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IFacetedProject.Action;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;

/**
 * A {@link ConfigurationHandler} that deals with <tt>additionalProjectFacets</tt> from the
 * <tt>maven-eclipse-plugin</tt> configuration.
 * 
 * @author Alex Clarke
 * @author Phillip Webb
 */
public class AdditionalProjectFacetsConfigurationHandler extends SingleParameterConfigurationHandler {

	private static final String FACET_CONFIG_EXTENSION = "org.eclipse.m2e.maveneclipse.facetconfigprovider";

	@Override
	protected String getParamterName() {
		return "additionalProjectFacets";
	}

	@Override
	protected void handle(MavenEclipseContext context, ConfigurationParameter parameter) throws Exception {
		IFacetedProject facetedProject = createFacetedProject(context);
		Set<Action> actions = new LinkedHashSet<IFacetedProject.Action>();
		for (ConfigurationParameter child : parameter.getChildren()) {
			IProjectFacet projectFacet = getFacetIfAvailable(child.getName());
			if (projectFacet != null) {
				IProjectFacetVersion projectFacetVersion = projectFacet.getVersion(child.getValue());
				FacetConfigProvider configProvider = getFacetConfigProvider(projectFacet);
				Object config = configProvider == null ? null : configProvider.getFacetConfig(context,
						projectFacetVersion);
				if (!facetedProject.hasProjectFacet(projectFacetVersion)) {
					if (facetedProject.hasProjectFacet(projectFacet)) {
						actions.add(new IFacetedProject.Action(IFacetedProject.Action.Type.VERSION_CHANGE,
								projectFacetVersion, config));
					} else {
						actions.add(new IFacetedProject.Action(IFacetedProject.Action.Type.INSTALL,
								projectFacetVersion, config));
					}
				}
			}
		}
		if (actions.size() > 0) {
			facetedProject.modify(actions, context.getMonitor());
		}
	}

	private FacetConfigProvider getFacetConfigProvider(IProjectFacet facet) throws CoreException {
		IConfigurationElement[] configurationElements = Platform.getExtensionRegistry().getConfigurationElementsFor(
				FACET_CONFIG_EXTENSION);
		for (IConfigurationElement configurationElement : configurationElements) {
			String facetId = configurationElement.getAttribute("facetid");
			if (facet.getId().equals(facetId)) {
				return (FacetConfigProvider) configurationElement.createExecutableExtension("class");
			}
		}
		return null;
	}

	private IProjectFacet getFacetIfAvailable(String id) {
		try {
			return ProjectFacetsManager.getProjectFacet(id);
		} catch (Exception e) {
			return null;
		}
	}

	protected IFacetedProject createFacetedProject(MavenEclipseContext context) throws CoreException {
		return ProjectFacetsManager.create(context.getProject(), true, context.getMonitor());
	}
}
