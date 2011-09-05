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

import org.apache.maven.project.MavenProject;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.maveneclipse.configuration.MavenEclipseConfiguration;
import org.eclipse.m2e.maveneclipse.handler.ConfigurationHandlers;

/**
 * Context for made available from the {@link MavenEclipseProjectConfigurator} to expose relevant objects for
 * {@link ConfigurationHandlers handlers} to use.
 * 
 * @author Alex Clarke
 * @author Phillip Webb
 */
public interface MavenEclipseContext {

	/**
	 * Access to the <tt>maven-eclipse-plugin</tt> configuration.
	 * 
	 * @return the configuration
	 */
	MavenEclipseConfiguration getConfiguration();

	/**
	 * Provides access to the {@link IProject}.
	 * 
	 * @return the {@link IProject}
	 */
	IProject getProject();

	/**
	 * Provides access to the {@link IProgressMonitor}.
	 * 
	 * @return the {@link IProgressMonitor}
	 */
	IProgressMonitor getProgressMonitor();

	/**
	 * Provides access to the {@link MavenProject}.
	 * 
	 * @return the {@link MavenProject}
	 */
	MavenProject getMavenProject();
}
