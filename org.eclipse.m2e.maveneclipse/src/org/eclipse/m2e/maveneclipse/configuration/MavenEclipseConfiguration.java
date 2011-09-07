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

/**
 * Provides access to <tt>pom.xml</tt> configuration defined for the <tt>maven-eclipse-plugin</tt>.
 * 
 * @author Alex Clarke
 * @author Phillip Webb
 */
public interface MavenEclipseConfiguration {

	/**
	 * Return a single configuration parameter from the configuration.
	 * 
	 * @param name the name of the parameter
	 * @return the configuration paramter or <tt>null</tt> if no paramter is defined.
	 */
	ConfigurationParameter getParamter(String name);

	/**
	 * Determines if the configuration contains the specifed paramter.
	 * 
	 * @param name the name of the paramter
	 * @return <tt>true</tt> if {@link #getParamter(String)} returns a <tt>non-null</tt> result.
	 */
	boolean containsParamter(String name);
}
