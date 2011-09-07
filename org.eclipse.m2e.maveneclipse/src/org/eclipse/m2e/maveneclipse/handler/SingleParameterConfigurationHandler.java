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
import org.eclipse.m2e.maveneclipse.configuration.ConfigurationParameter;
import org.eclipse.m2e.maveneclipse.configuration.MavenEclipseConfiguration;

/**
 * Convenient base class for {@link ConfigurationHandler}s that are based around a single configuration parameter.
 * 
 * @author Alex Clarke
 * @author Phillip Webb
 */
public abstract class SingleParameterConfigurationHandler implements ConfigurationHandler {

	/**
	 * Returns the parameter name that is used to determine if the handler should run.
	 * 
	 * @return the parameter name
	 */
	protected abstract String getParamterName();

	public boolean canHandle(MavenEclipseContext context) {
		return context.getPluginConfiguration().containsParamter(getParamterName());
	}

	public final void handle(MavenEclipseContext context) throws Exception {
		ConfigurationParameter parameter = context.getPluginConfiguration().getParamter(getParamterName());
		if (!canHandle(context) || parameter == null) {
			throw new IllegalStateException("Unable to handle context");
		}
		handle(context, parameter);
	}

	/**
	 * Handle the {@link MavenEclipseConfiguration} applying the specified {@link ConfigurationParameter configuration}
	 * to the eclipse project.
	 * 
	 * @param context the {@link MavenEclipseContext}
	 * @param parameter the {@link ConfigurationParameter}
	 * @throws Exception
	 */
	protected abstract void handle(MavenEclipseContext context, ConfigurationParameter parameter) throws Exception;
}
