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
package org.eclipse.m2e.maveneclipse;

import org.apache.maven.model.Plugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.core.project.configurator.AbstractCustomizableLifecycleMapping;
import org.eclipse.m2e.core.project.configurator.AbstractProjectConfigurator;
import org.eclipse.m2e.core.project.configurator.ProjectConfigurationRequest;
import org.eclipse.m2e.maveneclipse.handler.ConfigurationHandlers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link AbstractProjectConfigurator Project configurator} that applies configuration defined by the
 * <tt>maven-eclipse-plugin</tt> to m2e eclipse projects.
 * 
 * @see ConfigurationHandlers
 * 
 * @author Alex Clarke
 * @author Phillip Webb
 */
public class MavenEclipseProjectConfigurator extends AbstractProjectConfigurator {

	private static Logger log = LoggerFactory.getLogger(AbstractCustomizableLifecycleMapping.class);

	private static final String GROUP_ID = "org.apache.maven.plugins";

	private static final String ARTIFACT_ID = "maven-eclipse-plugin";

	private ConfigurationHandlers handler = new ConfigurationHandlers();

	@Override
	public void configure(ProjectConfigurationRequest request, IProgressMonitor monitor) throws CoreException {
		log.debug("Checking for " + GROUP_ID + ":" + ARTIFACT_ID + " plugin");
		for (Plugin plugin : request.getMavenProject().getBuildPlugins()) {
			if (isMavenEclipsePlugin(plugin)) {
				log.debug("Configuring m2e project for " + GROUP_ID + ":" + ARTIFACT_ID + " plugin");
				configure(request, monitor, plugin);
			}
		}
	}

	private void configure(ProjectConfigurationRequest request, IProgressMonitor monitor, Plugin plugin)
			throws CoreException {
		MavenEclipseContext context = new DefaultMavenEclipseContext(request, monitor, plugin);
		try {
			handler.handle(context);
		} catch (Exception e) {
			if (e instanceof CoreException) {
				throw (CoreException) e;
			}
			//FIXME
			throw new RuntimeException(e);
		}
	}

	private boolean isMavenEclipsePlugin(Plugin plugin) {
		return GROUP_ID.equals(plugin.getGroupId()) && ARTIFACT_ID.equals(plugin.getArtifactId());
	}
}