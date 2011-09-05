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
import org.eclipse.m2e.maveneclipse.configuration.ConfigurationParameter;

/**
 * Convenient base class for {@link ConfigurationHandler}s that are based around a single configuration parameter.
 * 
 * @author Alex Clarke
 * @author Phillip Webb
 */
public abstract class SingleParamterConfigurationHandler implements ConfigurationHandler {

	/**
	 * Returns the parameter name that is used to determine if the handler should run.
	 * 
	 * @return the parameter name
	 */
	protected abstract String getParamterName();

	public boolean canHandle(MavenEclipseContext context) {
		return context.getPluginConfiguration().containsParamter(getParamterName());
	}

	public final void handle(MavenEclipseContext context) throws Exception {
		ConfigurationParameter paramter = context.getPluginConfiguration().getParamter(getParamterName());
		if (!canHandle(context) || paramter == null) {
			throw new IllegalStateException("Unable to handle context");
		}
		handle(context, paramter);
	}

	protected abstract void handle(MavenEclipseContext context, ConfigurationParameter paramter) throws Exception;
}
