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
				new ProjectNatureConfigurationHandler() });
	}

	/**
	 * Create a {@link ConfigurationHandlers} instance with the sepecified handlers.
	 * 
	 * @param sectionHandlers
	 */
	protected ConfigurationHandlers(ConfigurationHandler[] sectionHandlers) {
		if (sectionHandlers == null) {
			throw new IllegalArgumentException("SectionHandlers must not be null");
		}
		this.sectionHandlers = sectionHandlers;
	}

	protected final ConfigurationHandler[] getSectionHandlers() {
		return sectionHandlers;
	}

	/**
	 * Handle the configuration of the project.
	 * 
	 * @param context the context
	 */
	public void handle(MavenEclipseContext context) {
		for (ConfigurationHandler sectionHandler : getSectionHandlers()) {
			if (sectionHandler.canHandle(context)) {
				sectionHandler.handle(context);
			}
		}
	}
}
