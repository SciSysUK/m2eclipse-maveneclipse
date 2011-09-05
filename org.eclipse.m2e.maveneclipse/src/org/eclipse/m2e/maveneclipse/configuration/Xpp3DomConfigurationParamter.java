package org.eclipse.m2e.maveneclipse.configuration;

import org.codehaus.plexus.util.xml.Xpp3Dom;

public class Xpp3DomConfigurationParamter implements ConfigurationParamter {
	private Xpp3Dom dom;

	public Xpp3DomConfigurationParamter(Xpp3Dom dom) {
		this.dom = dom;
	}

	//FIXME delete
	public Xpp3Dom getDom() {
		return dom;
	}
}
