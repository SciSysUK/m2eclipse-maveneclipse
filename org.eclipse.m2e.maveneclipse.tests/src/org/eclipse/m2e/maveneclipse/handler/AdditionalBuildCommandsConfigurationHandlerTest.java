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

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.maveneclipse.MavenEclipseContext;
import org.eclipse.m2e.maveneclipse.configuration.ConfigurationParameter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

/**
 * Tests for {@link AdditionalBuildCommandsConfigurationHandler}.
 * 
 * @author Alex Clarke
 * @author Phillip Webb
 */
public class AdditionalBuildCommandsConfigurationHandlerTest {

	private AdditionalBuildCommandsConfigurationHandler additionalBuildCommandsConfigurationHandler = new AdditionalBuildCommandsConfigurationHandler();

	private MavenEclipseContext context = mock(MavenEclipseContext.class);

	private IProjectDescription projectDescription = mock(IProjectDescription.class);

	private IProject project = mock(IProject.class);

	private ICommand initialBuildCommand = mock(ICommand.class);

	private IProgressMonitor monitor = mock(IProgressMonitor.class);

	@Before
	public void setupBasicContextWithInitialBuildCommand() throws Exception {
		given(context.getProject()).willReturn(project);
		given(project.getDescription()).willReturn(projectDescription);

		ICommand[] initialBuildSpecs = { initialBuildCommand };
		given(projectDescription.getBuildSpec()).willReturn(initialBuildSpecs);
		given(context.getMonitor()).willReturn(monitor);
	}

	@Test
	public void shouldAddNamedAdditionalBuildCommands() throws Exception {
		// Given
		ConfigurationParameter configurationParameter = mock(ConfigurationParameter.class);
		List<ConfigurationParameter> buildCommandConfigParameters = new ArrayList<ConfigurationParameter>();
		ConfigurationParameter firstBuildCommand = createNamedBuildCommandConfigurationParameter("first");
		ConfigurationParameter secondBuildCommand = createNamedBuildCommandConfigurationParameter("second");
		buildCommandConfigParameters.add(firstBuildCommand);
		buildCommandConfigParameters.add(secondBuildCommand);
		given(configurationParameter.getChildren()).willReturn(buildCommandConfigParameters);

		ICommand firstNewICommand = mock(ICommand.class);
		ICommand secondNewICommand = mock(ICommand.class);
		given(projectDescription.newCommand()).willReturn(firstNewICommand).willReturn(secondNewICommand);

		// When
		additionalBuildCommandsConfigurationHandler.handle(context, configurationParameter);

		// Then
		ArgumentCaptor<ICommand[]> argument = ArgumentCaptor.forClass(ICommand[].class);
		verify(projectDescription).setBuildSpec(argument.capture());
		verify(project).setDescription(projectDescription, monitor);

		boolean hasInitialBuildCommand = false;
		boolean hasFirstBuildCommand = false;
		boolean hasSecondBuildCommand = false;
		ICommand[] newBuildCommands = argument.getValue();
		for (int i = 0; i < newBuildCommands.length; i++) {
			if (initialBuildCommand.equals(newBuildCommands[i])) {
				hasInitialBuildCommand = true;
			} else if (firstNewICommand.equals(newBuildCommands[i])) {
				hasFirstBuildCommand = true;
			} else if (secondNewICommand.equals(newBuildCommands[i])) {
				hasSecondBuildCommand = true;
			} else {
				fail("Unknown build command found: " + newBuildCommands[i]);
			}

		}
		assertTrue(hasInitialBuildCommand);
		assertTrue(hasFirstBuildCommand);
		assertTrue(hasSecondBuildCommand);
	}

	@Test
	public void shouldAddCompleteAdditionalBuildCommands() throws Exception {
		// Given
		ConfigurationParameter configurationParameter = mock(ConfigurationParameter.class);
		List<ConfigurationParameter> buildCommandConfigParameters = new ArrayList<ConfigurationParameter>();
		ConfigurationParameter firstBuildCommand = createCompleteBuildCommandConfigurationParameter("first");
		ConfigurationParameter secondBuildCommand = createCompleteBuildCommandConfigurationParameter("second");
		buildCommandConfigParameters.add(firstBuildCommand);
		buildCommandConfigParameters.add(secondBuildCommand);
		given(configurationParameter.getChildren()).willReturn(buildCommandConfigParameters);

		ICommand firstNewICommand = mock(ICommand.class);
		ICommand secondNewICommand = mock(ICommand.class);
		given(projectDescription.newCommand()).willReturn(firstNewICommand).willReturn(secondNewICommand);

		// When
		additionalBuildCommandsConfigurationHandler.handle(context, configurationParameter);

		// Then
		ArgumentCaptor<ICommand[]> argument = ArgumentCaptor.forClass(ICommand[].class);
		verify(projectDescription).setBuildSpec(argument.capture());
		verify(project).setDescription(projectDescription, monitor);

		boolean hasInitialBuildCommand = false;
		boolean hasFirstBuildCommand = false;
		boolean hasSecondBuildCommand = false;
		ICommand[] newBuildCommands = argument.getValue();
		for (int i = 0; i < newBuildCommands.length; i++) {
			if (initialBuildCommand.equals(newBuildCommands[i])) {
				hasInitialBuildCommand = true;
			} else if (firstNewICommand.equals(newBuildCommands[i])) {
				hasFirstBuildCommand = true;
			} else if (secondNewICommand.equals(newBuildCommands[i])) {
				hasSecondBuildCommand = true;
			} else {
				fail("Unknown build command found: " + newBuildCommands[i]);
			}

		}
		assertTrue(hasInitialBuildCommand);
		assertTrue(hasFirstBuildCommand);
		assertTrue(hasSecondBuildCommand);
	}

	private ConfigurationParameter createNamedBuildCommandConfigurationParameter(String buildCommandName) {
		ConfigurationParameter configParameter = mock(ConfigurationParameter.class);
		given(configParameter.getName()).willReturn(AdditionalBuildCommandsConfigurationHandler.NAMED_BUILD_COMMAND);
		given(configParameter.getValue()).willReturn(buildCommandName);
		return configParameter;
	}

	private ConfigurationParameter createCompleteBuildCommandConfigurationParameter(String buildCommandName) {
		ConfigurationParameter configParameter = mock(ConfigurationParameter.class);
		given(configParameter.getName()).willReturn(AdditionalBuildCommandsConfigurationHandler.COMPLETE_BUILD_COMMAND);
		ConfigurationParameter nameConfigParameter = mock(ConfigurationParameter.class);
		given(nameConfigParameter.getValue()).willReturn(buildCommandName);
		given(configParameter.getChild(AdditionalBuildCommandsConfigurationHandler.NAME)).willReturn(
				nameConfigParameter);
		return configParameter;
	}
}
