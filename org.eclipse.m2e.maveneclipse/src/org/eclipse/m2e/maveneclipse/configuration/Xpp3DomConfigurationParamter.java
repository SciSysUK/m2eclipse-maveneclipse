package org.eclipse.m2e.maveneclipse.configuration;

import org.codehaus.plexus.util.xml.Xpp3Dom;

/**
 * Adapter class that converts {@link Xpp3Dom} into {@link ConfigurationParamter}.
 * 
 * @author Alex Clarke
 * @author Phillip Webb
 */
class Xpp3DomConfigurationParamter implements ConfigurationParamter {

	private Xpp3Dom dom;

	public Xpp3DomConfigurationParamter(Xpp3Dom dom) {
		this.dom = dom;
	}

	public String getName() {
		return dom.getName();
	}
}
