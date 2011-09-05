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
package org.eclipse.m2e.maveneclipse.configuration;

import org.apache.maven.model.Plugin;
import org.codehaus.plexus.util.xml.Xpp3Dom;

/**
 * Default implementation of {@link MavenEclipseConfiguration}.
 * 
 * @author Alex Clarke
 * @author Phillip Webb
 */
public class DefaultMavenEclipseConfiguration implements MavenEclipseConfiguration {

	private Xpp3Dom dom;

	/**
	 * Create a new {@link DefaultMavenEclipseConfiguration} instance.
	 * @param plugin the source plugin
	 */
	public DefaultMavenEclipseConfiguration(Plugin plugin) {
		if (plugin == null) {
			throw new IllegalArgumentException("Plugin must not be null");
		}
		this.dom = (Xpp3Dom) plugin.getConfiguration();
	}

	public ConfigurationParameter getParamter(String name) {
		Xpp3Dom[] children = dom.getChildren(name);
		if (children.length > 1) {
			throw new IllegalStateException("Unexpected number of child parameters defined for '" + name + "'");
		}
		if (children.length == 0) {
			return null;
		}
		return new Xpp3DomConfigurationParameter(children[0]);
	}

	public boolean containsParamter(String name) {
		return getParamter(name) != null;
	}
}