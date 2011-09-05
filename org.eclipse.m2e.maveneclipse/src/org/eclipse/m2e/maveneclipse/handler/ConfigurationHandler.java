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
 * Strategy interface called by {@link ConfigurationHandlers} that can handle one particular section of the
 * <tt>maven-eclipse-plugin</tt> {@link MavenEclipseConfiguration}.
 * 
 * @author Alex Clarke
 * @author Phillip Webb
 */
public interface ConfigurationHandler {

	/**
	 * Determines if the handler can deal with the {@link MavenEclipseConfiguration} available in the specified
	 * {@link MavenEclipseContext}.
	 * 
	 * @param context the context
	 * @return <tt>true</tt> if the handler supports the configuration
	 * @see #handle(MavenEclipseContext)
	 */
	boolean canHandle(MavenEclipseContext context);

	/**
	 * Handle the {@link MavenEclipseConfiguration} applying the relevant section to the m2e eclipse project. This
	 * method will only be called when {@link #isSupported} returns <tt>true</tt>.
	 * 
	 * @param context the context
	 * @throws Exception
	 * @see #canHandle(MavenEclipseContext)
	 */
	void handle(MavenEclipseContext context) throws Exception;
}
