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
package org.eclipse.m2e.maveneclipse.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.m2e.maveneclipse.MavenEclipseContext;
import org.eclipse.m2e.maveneclipse.configuration.ConfigurationParameter;

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
	static final String COMPLETE_BUILD_COMMAND = "buildCommand";
	static final String NAMED_BUILD_COMMAND = "buildcommand";
	static final String NAME = "name";

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
			if (COMPLETE_BUILD_COMMAND.equals(child.getName())) {
				ICommand newCommand = projectDescription.newCommand();
				newCommand.setBuilderName(child.getChild(NAME).getValue());
				buildSpec.add(newCommand);
			} else if (NAMED_BUILD_COMMAND.equals(child.getName())) {
				ICommand newCommand = projectDescription.newCommand();
				newCommand.setBuilderName(child.getValue());
				buildSpec.add(newCommand);
			}
		}

		projectDescription.setBuildSpec(buildSpec.toArray(projectDescription.getBuildSpec()));
		project.setDescription(projectDescription, context.getMonitor());
	}
}
