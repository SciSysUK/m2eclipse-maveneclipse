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
import org.apache.maven.project.MavenProject;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.core.project.configurator.ProjectConfigurationRequest;
import org.eclipse.m2e.maveneclipse.configuration.DefaultMavenEclipseConfiguration;
import org.eclipse.m2e.maveneclipse.configuration.MavenEclipseConfiguration;

/**
 * Default implementation of {@link MavenEclipseContext}.
 * 
 * @author Alex Clarke
 * @author Phillip Webb
 */
public class DefaultMavenEclipseContext implements MavenEclipseContext {

	private ProjectConfigurationRequest request;
	private IProgressMonitor monitor;
	private MavenEclipseConfiguration pluginConfiguration;

	/**
	 * Create a new {@link DefaultMavenEclipseContext} instance.
	 * @param request the request
	 * @param monitor the monitor
	 * @param plugin the plugin
	 */
	public DefaultMavenEclipseContext(ProjectConfigurationRequest request, IProgressMonitor monitor, Plugin plugin) {
		this.request = request;
		this.monitor = monitor;
		this.pluginConfiguration = new DefaultMavenEclipseConfiguration(plugin);
	}

	public IProject getProject() {
		return request.getProject();
	}

	public IProgressMonitor getMonitor() {
		return monitor;
	}

	public MavenProject getMavenProject() {
		return request.getMavenProject();
	}

	public MavenEclipseConfiguration getPluginConfiguration() {
		return pluginConfiguration;
	}
}