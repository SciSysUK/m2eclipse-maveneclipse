/*
 * Copyright 2000-2011 the original author or authors.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *     http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.eclipse.m2e.maveneclipse.handler;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.m2e.maveneclipse.MavenEclipseContext;
import org.eclipse.m2e.maveneclipse.configuration.ConfigurationParameter;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IFacetedProject.Action;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;

/**
 * A {@link ConfigurationHandler} that deals with <tt>additionalProjectFacets</tt> from the
 * <tt>maven-eclipse-plugin</tt>.
 * 
 * @author Alex Clarke
 * @author Phillip Webb
 */
public class AdditionalProjectFacetsConfigurationHandler extends SingleParamterConfigurationHandler {

	@Override
	protected String getParamterName() {
		return "additionalProjectFacets";
	}

	@Override
	protected void handle(MavenEclipseContext context, ConfigurationParameter parameter) throws Exception {
		IFacetedProject facetedProject = createFacetedProject(context.getProject());
		Set<Action> actions = new LinkedHashSet<IFacetedProject.Action>();
		for (ConfigurationParameter child : parameter.getChildren()) {
			IProjectFacet facet = ProjectFacetsManager.getProjectFacet(child.getName());
			if (facet != null) {
				IProjectFacetVersion projectFacetVersion = facet.getVersion(child.getValue());
				actions.add(new IFacetedProject.Action(IFacetedProject.Action.Type.INSTALL, projectFacetVersion, null));
			}
		}
		if (actions.size() > 0) {
			facetedProject.modify(actions, context.getMonitor());
		}
	}

	protected IFacetedProject createFacetedProject(IProject project) throws CoreException {
		return ProjectFacetsManager.create(project);
	}
}
