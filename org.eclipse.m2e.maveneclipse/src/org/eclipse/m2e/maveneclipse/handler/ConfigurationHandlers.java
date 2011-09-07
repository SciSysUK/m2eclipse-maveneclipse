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

import org.eclipse.m2e.maveneclipse.MavenEclipseContext;
import org.eclipse.m2e.maveneclipse.configuration.MavenEclipseConfiguration;

/**
 * Performs the actual work of configuring a m2e eclipse project from {@link MavenEclipseConfiguration} in the specified
 * {@link MavenEclipseContext} as defined by the <tt>maven-eclipse-plugin</tt>.
 * 
 * @author Alex Clarke
 * @author Phillip Webb
 */
public class ConfigurationHandlers {

	private final ConfigurationHandler[] sectionHandlers;

	/**
	 * Create a {@link ConfigurationHandlers} instance with the default set of handlers.
	 */
	public ConfigurationHandlers() {
		this(new ConfigurationHandler[] { new AdditionalConfigConfigurationHandler(),
				new AdditionalProjectNaturesConfigurationHandler(), new AdditionalProjectFacetsConfigurationHandler() });
	}

	/**
	 * Create a {@link ConfigurationHandlers} instance with the specified handlers.
	 * 
	 * @param sectionHandlers
	 */
	protected ConfigurationHandlers(ConfigurationHandler[] sectionHandlers) {
		if (sectionHandlers == null) {
			throw new IllegalArgumentException("SectionHandlers must not be null");
		}
		this.sectionHandlers = sectionHandlers;
	}

	/**
	 * Returns the section handlers.
	 * @return the section handlers
	 */
	protected final ConfigurationHandler[] getSectionHandlers() {
		return sectionHandlers;
	}

	/**
	 * Handle the configuration of the project.
	 * 
	 * @param context the context
	 * @throws Exception
	 */
	public void handle(MavenEclipseContext context) throws Exception {
		for (ConfigurationHandler sectionHandler : getSectionHandlers()) {
			if (sectionHandler.canHandle(context)) {
				sectionHandler.handle(context);
			}
		}
	}
}
