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
package org.eclipse.m2.maveneclipse;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2.maveneclipse.configuration.MavenEclipseConfigurationHandler;
import org.eclipse.m2e.core.project.configurator.AbstractProjectConfigurator;
import org.eclipse.m2e.core.project.configurator.ProjectConfigurationRequest;

/**
 * {@link AbstractProjectConfigurator Project configurator} that applies configuration defined by the
 * <tt>maven-eclipse-plugin</tt> to m2e eclipse projects.
 * 
 * @see MavenEclipseConfigurationHandler
 * 
 * @author Alex Clarke
 * @author Phillip Webb
 */
public class MavenEclipseProjectConfigurator extends AbstractProjectConfigurator {

	@Override
	public void configure(ProjectConfigurationRequest request, IProgressMonitor monitor) throws CoreException {
	}
}