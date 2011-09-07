/*
 * Copyright 2000-2011 the original author or authors.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * 
 * http://www.eclipse.org/legal/epl-v10.html
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