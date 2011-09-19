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
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.m2e.core.project.configurator.AbstractProjectConfigurator;
import org.eclipse.m2e.core.project.configurator.ProjectConfigurationRequest;
import org.eclipse.m2e.maveneclipse.handler.ConfigurationHandlers;
import org.eclipse.osgi.util.NLS;
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

	private static Logger log = LoggerFactory.getLogger(MavenEclipseProjectConfigurator.class);

	static final String GROUP_ID = "org.apache.maven.plugins"; //$NON-NLS-1$

	static final String ARTIFACT_ID = "maven-eclipse-plugin"; //$NON-NLS-1$

	private ConfigurationHandlers handlers;

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

	private boolean isMavenEclipsePlugin(Plugin plugin) {
		return GROUP_ID.equals(plugin.getGroupId()) && ARTIFACT_ID.equals(plugin.getArtifactId());
	}

	private void configure(ProjectConfigurationRequest request, IProgressMonitor monitor, Plugin plugin)
			throws CoreException {
		if (handlers == null) {
			handlers = new ConfigurationHandlers();
		}
		MavenEclipseContext context = new DefaultMavenEclipseContext(request, monitor, plugin);
		try {
			handlers.handle(context);
		} catch (Exception e) {
			if (e instanceof CoreException) {
				throw (CoreException) e;
			}
			String msg = NLS.bind(Messages.mavenEclipseProjectConfiguratorError, e.getMessage());
			log.error(msg, e);
			throw new CoreException(new Status(IStatus.ERROR, MavenEclipsePlugin.PLUGIN_ID, -1, msg, e));
		}
	}

	protected void setHandlers(ConfigurationHandlers handlers) {
		this.handlers = handlers;
	}
}