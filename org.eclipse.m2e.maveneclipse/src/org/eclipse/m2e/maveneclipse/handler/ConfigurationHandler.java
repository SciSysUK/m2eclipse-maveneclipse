/*
 * Copyright 2000-2011 the original author or authors.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * 
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.m2e.maveneclipse.handler;

import org.eclipse.m2e.maveneclipse.MavenEclipseContext;
import org.eclipse.m2e.maveneclipse.configuration.MavenEclipseConfiguration;

/**
 * Strategy interface called by {@link ConfigurationHandlers} that can handle one particular section of the
 * <tt>maven-eclipse-plugin</tt> {@link MavenEclipseConfiguration}.
 * 
 * @author Alex Clarke
 * @author Phillip Webb
 */
public interface ConfigurationHandler {

	/**
	 * Determines if the handler can deal with the {@link MavenEclipseConfiguration} available in the specified
	 * {@link MavenEclipseContext}.
	 * 
	 * @param context the context
	 * @return <tt>true</tt> if the handler supports the configuration
	 * @see #handle(MavenEclipseContext)
	 */
	boolean canHandle(MavenEclipseContext context);

	/**
	 * Handle the {@link MavenEclipseConfiguration} applying the relevant section to the m2e eclipse project. This
	 * method will only be called when {@link #isSupported} returns <tt>true</tt>.
	 * 
	 * @param context the context
	 * @throws Exception
	 * @see #canHandle(MavenEclipseContext)
	 */
	void handle(MavenEclipseContext context) throws Exception;
}
