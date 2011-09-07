/*
 * Copyright 2000-2011 the original author or authors.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * 
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.m2e.maveneclipse.configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.codehaus.plexus.util.xml.Xpp3Dom;

/**
 * Adapter class that converts {@link Xpp3Dom} into {@link ConfigurationParameter}.
 * 
 * @author Alex Clarke
 * @author Phillip Webb
 */
class Xpp3DomConfigurationParameter implements ConfigurationParameter {

	private Xpp3Dom dom;

	public Xpp3DomConfigurationParameter(Xpp3Dom dom) {
		this.dom = dom;
	}

	public String getName() {
		return dom.getName();
	}

	public List<ConfigurationParameter> getChildren() {
		List<ConfigurationParameter> children = new ArrayList<ConfigurationParameter>();
		for (Xpp3Dom child : dom.getChildren()) {
			children.add(new Xpp3DomConfigurationParameter(child));
		}
		return Collections.unmodifiableList(children);
	}

	public boolean hasChild(String name) {
		return dom.getChild(name) != null;
	}

	public ConfigurationParameter getChild(String name) {
		Xpp3Dom child = dom.getChild(name);
		return (child == null ? null : new Xpp3DomConfigurationParameter(child));
	}

	public String getValue() {
		return dom.getValue();
	}
}
