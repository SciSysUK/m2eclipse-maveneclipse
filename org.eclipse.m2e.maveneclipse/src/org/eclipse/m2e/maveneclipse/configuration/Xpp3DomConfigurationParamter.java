package org.eclipse.m2e.maveneclipse.configuration;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.plexus.util.xml.Xpp3Dom;

/**
 * Adapter class that converts {@link Xpp3Dom} into {@link ConfigurationParameter}.
 * 
 * @author Alex Clarke
 * @author Phillip Webb
 */
class Xpp3DomConfigurationParamter implements ConfigurationParameter {

	private Xpp3Dom dom;

	public Xpp3DomConfigurationParamter(Xpp3Dom dom) {
		this.dom = dom;
	}

	public String getName() {
		return dom.getName();
	}

	public List<ConfigurationParameter> getChildren() {
		List<ConfigurationParameter> children = new ArrayList<ConfigurationParameter>();
		for (Xpp3Dom child : dom.getChildren()) {
			children.add(new Xpp3DomConfigurationParamter(child));
		}
		return children;
	}

	public String getValue() {
		return dom.getValue();
	}

	public Xpp3Dom getXpp3Dom() {
		return dom;
	}

}
