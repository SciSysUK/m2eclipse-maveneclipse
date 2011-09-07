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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.m2e.maveneclipse.MavenEclipseContext;
import org.eclipse.m2e.maveneclipse.configuration.ConfigurationParameter;
import org.eclipse.m2e.maveneclipse.handler.ConfigurationHandler;
import org.eclipse.m2e.maveneclipse.handler.SingleParameterConfigurationHandler;

/**
 * A {@link ConfigurationHandler} that deals with <tt>additionalBuildCommands</tt> from the
 * <tt>maven-eclipse-plugin</tt>.
 * 
 * This supports both ways of adding build commands as specified in the maven eclipse plugin documentation.
 * 
 * @author Alex Clarke
 * @author Phillip Webb
 */
public class AdditionalBuildCommandsConfigurationHandler extends SingleParameterConfigurationHandler {

	private BuildCommandFactory commandFactory = new BuildCommandFactoryImpl();

	@Override
	protected String getParamterName() {
		return "additionalBuildcommands";
	}

	@Override
	protected void handle(MavenEclipseContext context, ConfigurationParameter paramter) throws Exception {
		IProject project = context.getProject();
		IProjectDescription projectDescription = project.getDescription();
		List<ICommand> additionalBuildCommands = getAdditionalBuildCommands(paramter, projectDescription);

		if (additionalBuildCommands.isEmpty()) {
			return;
		}

		Map<String, ICommand> allBuildCommands = new LinkedHashMap<String, ICommand>();

		// Add all the ICommands from the existing buildSpec at the start of the list
		for (ICommand command : projectDescription.getBuildSpec()) {
			allBuildCommands.put(command.getBuilderName(), command);
		}

		// Add all the additional ICommands overwriting where the builder names are identical
		for (ICommand command : additionalBuildCommands) {
			allBuildCommands.put(command.getBuilderName(), command);
		}

		ICommand[] revisedBuildSpec = allBuildCommands.values().toArray(new ICommand[allBuildCommands.size()]);
		projectDescription.setBuildSpec(revisedBuildSpec);
		project.setDescription(projectDescription, context.getMonitor());
	}

	private List<ICommand> getAdditionalBuildCommands(ConfigurationParameter paramter,
			IProjectDescription projectDescription) {
		List<ICommand> buildSpec = new ArrayList<ICommand>();

		// Add a new command for each build command ConfigurationParameter
		for (ConfigurationParameter child : paramter.getChildren()) {
			ICommand command = commandFactory.createCommand(projectDescription, child);
			if (command != null) {
				buildSpec.add(command);
			}
		}
		return buildSpec;
	}

	/**
	 * Enables injection of an {@link BuildCommandFactoryImpl}
	 * @param commandFactory
	 */
	protected void setCommandFactory(BuildCommandFactory commandFactory) {
		this.commandFactory = commandFactory;
	}
}
