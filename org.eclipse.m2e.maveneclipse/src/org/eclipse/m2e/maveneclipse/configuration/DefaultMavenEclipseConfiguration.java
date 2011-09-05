package org.eclipse.m2e.maveneclipse.configuration;

import org.apache.maven.model.Plugin;
import org.codehaus.plexus.util.xml.Xpp3Dom;

/**
 * Default implementation of {@link MavenEclipseConfiguration}
 */
public class DefaultMavenEclipseConfiguration implements MavenEclipseConfiguration {
	private Xpp3Dom dom;

	public DefaultMavenEclipseConfiguration(Plugin plugin) {
		this.dom = (Xpp3Dom) plugin.getConfiguration();
	}

	public ConfigurationParamter getParamter(String name) {
		Xpp3Dom[] children = dom.getChildren(name);
		if (children.length > 1) {
			throw new IllegalStateException("Unexpected number of child parameters defined for '" + name + "'");
		}
		if (children.length == 0) {
			return null;
		}
		return new Xpp3DomConfigurationParamter(children[0]);
	}

	public boolean containsParamter(String name) {
		return getParamter(name) != null;
	}
}