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
