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
package org.eclipse.m2e.maveneclipse.handler.additionalbuildcommands;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.maveneclipse.MavenEclipseContext;
import org.eclipse.m2e.maveneclipse.configuration.ConfigurationParameter;
import org.eclipse.m2e.maveneclipse.configuration.MavenEclipseConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Tests for {@link AdditionalBuildCommandsConfigurationHandler}.
 * 
 * @author Alex Clarke
 * @author Phillip Webb
 */
@RunWith(MockitoJUnitRunner.class)
public class AdditionalBuildCommandsConfigurationHandlerTest {

	@InjectMocks
	private AdditionalBuildCommandsConfigurationHandler additionalBuildCommandsConfigurationHandler = new AdditionalBuildCommandsConfigurationHandler();

	@Mock
	private BuildCommandFactory commandFactory;

	@Mock
	private MavenEclipseContext context = mock(MavenEclipseContext.class);

	@Mock
	private IProjectDescription projectDescription = mock(IProjectDescription.class);

	@Mock
	private IProject project = mock(IProject.class);

	@Mock
	private ICommand initialBuildCommand = mock(ICommand.class);

	@Mock
	private IProgressMonitor monitor = mock(IProgressMonitor.class);

	@Captor
	ArgumentCaptor<ICommand[]> argument;

	@Before
	public void setupBasicContextWithInitialBuildCommand() throws Exception {
		given(context.getProject()).willReturn(project);
		given(project.getDescription()).willReturn(projectDescription);

		given(initialBuildCommand.getBuilderName()).willReturn("initial");
		ICommand[] initialBuildSpecs = { initialBuildCommand };
		given(projectDescription.getBuildSpec()).willReturn(initialBuildSpecs);
		given(context.getMonitor()).willReturn(monitor);
	}

	@Test
	public void shouldAddAdditionalBuildCommands() throws Exception {
		// Given
		ConfigurationParameter additionalBuildCommandParameter = mock(ConfigurationParameter.class);
		List<ConfigurationParameter> buildCommandParameters = new ArrayList<ConfigurationParameter>();
		given(additionalBuildCommandParameter.getChildren()).willReturn(buildCommandParameters);

		ConfigurationParameter firstBuildCommandParameter = mock(ConfigurationParameter.class);
		buildCommandParameters.add(firstBuildCommandParameter);
		ICommand iCommandForFirst = mock(ICommand.class);
		given(iCommandForFirst.getBuilderName()).willReturn("first");
		given(commandFactory.createCommand(projectDescription, firstBuildCommandParameter))
				.willReturn(iCommandForFirst);

		ConfigurationParameter secondBuildCommandParameter = mock(ConfigurationParameter.class);
		buildCommandParameters.add(secondBuildCommandParameter);
		ICommand iCommandForSecond = mock(ICommand.class);
		given(iCommandForSecond.getBuilderName()).willReturn("second");
		given(commandFactory.createCommand(projectDescription, secondBuildCommandParameter)).willReturn(
				iCommandForSecond);

		// When
		additionalBuildCommandsConfigurationHandler.handle(context, additionalBuildCommandParameter);

		// Then
		verify(projectDescription).setBuildSpec(argument.capture());

		List<ICommand> actualBuildSpec = Arrays.asList(argument.getValue());
		assertThat(actualBuildSpec.size(), is(3));
		assertTrue(actualBuildSpec.contains(initialBuildCommand));
		assertTrue(actualBuildSpec.contains(iCommandForFirst));
		assertTrue(actualBuildSpec.contains(iCommandForSecond));

		verify(project).setDescription(projectDescription, monitor);
	}

	@Test
	public void shouldClaimToHandleContextsWithAdditionalProjectNaturesParamater() {
		// Given
		MavenEclipseContext context = mock(MavenEclipseContext.class);
		MavenEclipseConfiguration configuration = mock(MavenEclipseConfiguration.class);
		given(context.getPluginConfiguration()).willReturn(configuration);
		given(configuration.containsParamter(additionalBuildCommandsConfigurationHandler.getParamterName()))
				.willReturn(true);

		// When
		boolean canHandle = additionalBuildCommandsConfigurationHandler.canHandle(context);

		assertTrue(canHandle);
	}

	@Test
	public void shouldNotClaimToHandleContextsWithoutAdditionalProjectNaturesParamater() {
		// Given
		MavenEclipseContext context = mock(MavenEclipseContext.class);
		MavenEclipseConfiguration configuration = mock(MavenEclipseConfiguration.class);
		given(context.getPluginConfiguration()).willReturn(configuration);
		given(configuration.getParamter(additionalBuildCommandsConfigurationHandler.getParamterName()))
				.willReturn(null);

		// When
		boolean canHandle = additionalBuildCommandsConfigurationHandler.canHandle(context);

		assertFalse(canHandle);
	}

}
