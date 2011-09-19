/*
 * Copyright 2000-2011 the original author or authors.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * 
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.m2e.maveneclipse.handler.additionalconfig;

import org.eclipse.m2e.maveneclipse.MavenEclipseContext;
import org.eclipse.m2e.maveneclipse.configuration.ConfigurationParameter;
import org.eclipse.m2e.maveneclipse.handler.ConfigurationHandler;
import org.eclipse.m2e.maveneclipse.handler.SingleParameterConfigurationHandler;

/**
 * A {@link ConfigurationHandler} that deals with <tt>additionalConfig</tt> from the <tt>maven-eclipse-plugin</tt>.
 * 
 * @author Alex Clarke
 * @author Phillip Webb
 */
public class AdditionalConfigConfigurationHandler extends SingleParameterConfigurationHandler {

	@Override
	protected String getParamterName() {
		return "additionalConfig";
	}

	protected void handle(MavenEclipseContext context, ConfigurationParameter paramter) throws Exception {
		FilesParameters fileParameters = new FilesParameters(context, paramter);
		fileParameters.copyFilesToProject();
	}
}
