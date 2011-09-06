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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.plexus.util.StringUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.maveneclipse.MavenEclipseContext;
import org.eclipse.m2e.maveneclipse.configuration.ConfigurationParameter;

public class AdditionalProjectNaturesConfigurationHandler extends SingleParamterConfigurationHandler {

	static final String PARAMETER_NAME = "additionalProjectnatures";

	private static final Map<String, String> ALIASES;
	static {
		ALIASES = new HashMap<String, String>();
		ALIASES.put("spring", "org.springframework.ide.eclipse.core.springnature");
	}

	public void handle(MavenEclipseContext context, ConfigurationParameter configurationParameter) {
		if (!canHandle(context)) {
			throw new IllegalArgumentException("Unable to handle context");
		}

		for (ConfigurationParameter child : configurationParameter.getChildren()) {
			if (child.getName().equals("projectnature")) {
				try {
					addProjectNature(context.getProject(), child.getValue(), context.getMonitor());
				} catch (CoreException e) {
					throw new RuntimeException(e);
				}
			}

		}
	}

	private void addProjectNature(IProject project, String natureId, IProgressMonitor monitor) throws CoreException {
		if (StringUtils.isEmpty(natureId)) {
			return;
		}
		if (!project.hasNature(natureId)) {
			IProjectDescription projectDescription = project.getDescription();
			List<String> natureIds = new ArrayList<String>();
			natureIds.addAll(Arrays.asList(projectDescription.getNatureIds()));
			natureIds.add(natureId);
			projectDescription.setNatureIds(natureIds.toArray(new String[natureIds.size()]));
			project.setDescription(projectDescription, monitor);
		}
	}

	@Override
	protected String getParamterName() {
		return PARAMETER_NAME;
	}

}
