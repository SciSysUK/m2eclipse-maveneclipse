/*
 * Copyright 2000-2011 the original author or authors.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * 
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.m2e.maveneclipse.handler.additionalbuildcommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.m2e.maveneclipse.MavenEclipseContext;
import org.eclipse.m2e.maveneclipse.configuration.ConfigurationParameter;
import org.eclipse.m2e.maveneclipse.handler.ConfigurationHandler;
import org.eclipse.m2e.maveneclipse.handler.SingleParamterConfigurationHandler;

/**
 * A {@link ConfigurationHandler} that deals with <tt>additionalBuildCommands</tt> from the
 * <tt>maven-eclipse-plugin</tt>.
 * 
 * This supports both ways of adding build commands as specified in the maven eclipse plugin documentation.
 * 
 * @author Alex Clarke
 * @author Phillip Webb
 */
public class AdditionalBuildCommandsConfigurationHandler extends SingleParamterConfigurationHandler {

	private static final String PARAMETER_NAME = "additionalBuildcommands";

	private BuildCommandFactory commandFactory = new BuildCommandFactoryImpl();

	@Override
	protected String getParamterName() {
		return PARAMETER_NAME;
	}

	@Override
	protected void handle(MavenEclipseContext context, ConfigurationParameter paramter) throws Exception {
		IProject project = context.getProject();
		IProjectDescription projectDescription = project.getDescription();
		List<ICommand> buildSpec = new ArrayList<ICommand>();

		// Add all the ICommands from the existing buildSpec 
		buildSpec.addAll(Arrays.asList(projectDescription.getBuildSpec()));

		// Add a new command for each build command ConfigurationParameter
		for (ConfigurationParameter child : paramter.getChildren()) {
			ICommand newICommand = commandFactory.createICommand(projectDescription, child);
			if (newICommand != null) {
				buildSpec.add(newICommand);
			}
		}

		projectDescription.setBuildSpec(buildSpec.toArray(projectDescription.getBuildSpec()));
		project.setDescription(projectDescription, context.getMonitor());
	}

	/**
	 * Enables injection of an {@link BuildCommandFactoryImpl}
	 * @param commandFactory
	 */
	public void setCommandFactory(BuildCommandFactory commandFactory) {
		this.commandFactory = commandFactory;
	}
}
