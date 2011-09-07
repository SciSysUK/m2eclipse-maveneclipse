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

import java.util.List;

/**
 * Represents a section of configuration from the <tt>maven-eclipse-plugin</tt>.
 * 
 * @author Alex Clarke
 * @author Phillip Webb
 */
public interface ConfigurationParameter {

	/**
	 * Returns the name of the parameter.
	 * @return the parameter name.
	 */
	public String getName();

	/**
	 * Get a {@link List} of children of the this {@link ConfigurationParameter}. Will return an empty {@link List} when
	 * there are no children.
	 * @return a {@link List} of {@link ConfigurationParameter}
	 */
	public List<ConfigurationParameter> getChildren();

	/**
	 * Determine if this parameter has a child parameter with the specified name.
	 * @param name the name of the child
	 * @return true if the a child exists
	 */
	public boolean hasChild(String name);

	/**
	 * Return the child with the specified name.
	 * @param name the name of the child
	 * @return the child or <tt>null</tt>
	 */
	public ConfigurationParameter getChild(String name);

	/**
	 * Get the value of this {@link ConfigurationParameter}.
	 * @return the {@link String} value of this {@link ConfigurationParameter}
	 */
	public String getValue();
}
