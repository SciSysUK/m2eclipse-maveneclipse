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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.maveneclipse.MavenEclipseContext;
import org.eclipse.m2e.maveneclipse.configuration.ConfigurationParameter;

/**
 * A {@link ConfigurationHandler} that handles the configuration of <tt>additionalprojectnatures</tt> from the
 * <tt>maven-eclipse-plugin</tt>.
 * 
 * @author Alex Clarke
 * @author Phillip Webb
 */
public class AdditionalProjectNaturesConfigurationHandler extends SingleParamterConfigurationHandler {

	private static final String PARAMETER_NAME = "additionalProjectnatures";

	static final String PROJECT_NATURE_NAME = "projectnature";

	public void handle(MavenEclipseContext context, ConfigurationParameter configurationParameter) {
		Set<String> newNatureIDs = new HashSet<String>();
		for (ConfigurationParameter child : configurationParameter.getChildren()) {
			if (PROJECT_NATURE_NAME.equals(child.getName())) {
				newNatureIDs.add(child.getValue());
			}
		}
		try {
			addProjectNatures(context.getProject(), newNatureIDs, context.getMonitor());
		} catch (CoreException e) {
			throw new RuntimeException(e);
		}
	}

	private void addProjectNatures(IProject project, Set<String> newNatureIds, IProgressMonitor monitor)
			throws CoreException {
		IProjectDescription projectDescription = project.getDescription();
		newNatureIds.addAll(Arrays.asList(projectDescription.getNatureIds()));
		projectDescription.setNatureIds(newNatureIds.toArray(new String[newNatureIds.size()]));
		project.setDescription(projectDescription, monitor);
	}

	@Override
	protected String getParamterName() {
		return PARAMETER_NAME;
	}

}
