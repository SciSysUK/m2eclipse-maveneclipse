/*
 * Copyright 2000-2011 the original author or authors.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * 
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.m2e.maveneclipse.handler;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.m2e.maveneclipse.MavenEclipseContext;
import org.eclipse.m2e.maveneclipse.configuration.MavenEclipseConfiguration;
import org.eclipse.m2e.maveneclipse.handler.additionalbuildcommands.AdditionalBuildCommandsConfigurationHandler;
import org.eclipse.m2e.maveneclipse.handler.additionalconfig.AdditionalConfigConfigurationHandler;
import org.eclipse.m2e.maveneclipse.handler.additionalprojectfacets.AdditionalProjectFacetsConfigurationHandler;
import org.eclipse.m2e.maveneclipse.handler.additionalprojectnatures.AdditionalProjectNaturesConfigurationHandler;

/**
 * Performs the actual work of configuring an eclipse project from {@link MavenEclipseConfiguration} in the specified
 * {@link MavenEclipseContext} as defined by the <tt>maven-eclipse-plugin</tt>.
 * 
 * @author Alex Clarke
 * @author Phillip Webb
 */
public class ConfigurationHandlers {

	private final ConfigurationHandler[] configurationHandlers;

	/**
	 * Create a {@link ConfigurationHandlers} instance with the specified handlers.
	 * 
	 * @param configurationHandlers
	 */
	protected ConfigurationHandlers(ConfigurationHandler[] configurationHandlers) {
		if (configurationHandlers == null) {
			throw new IllegalArgumentException("configurationHandlers must not be null");
		}
		this.configurationHandlers = configurationHandlers;
	}

	/**
	 * Create a {@link ConfigurationHandlers} instance with handlers loaded from extension points.
	 * @throws CoreException
	 */
	public ConfigurationHandlers() throws CoreException {
		List<ConfigurationHandler> handlers = new ArrayList<ConfigurationHandler>();
		//NOTE: order here is critical, natures must be applied before facets
		handlers.add(new AdditionalConfigConfigurationHandler());
		handlers.add(new AdditionalProjectNaturesConfigurationHandler());
		handlers.add(new AdditionalProjectFacetsConfigurationHandler());
		handlers.add(new AdditionalBuildCommandsConfigurationHandler());
		this.configurationHandlers = handlers.toArray(new ConfigurationHandler[handlers.size()]);
	}

	/**
	 * Returns the configuration handlers.
	 * @return the configuration handlers
	 */
	protected final ConfigurationHandler[] getConfigurationHandlers() {
		return configurationHandlers;
	}

	/**
	 * Handle the configuration of the project.
	 * 
	 * @param context the context
	 * @throws Exception
	 */
	public void handle(MavenEclipseContext context) throws Exception {
		for (ConfigurationHandler configurationHandler : getConfigurationHandlers()) {
			if (configurationHandler.canHandle(context)) {
				configurationHandler.handle(context);
			}
		}
	}
}
