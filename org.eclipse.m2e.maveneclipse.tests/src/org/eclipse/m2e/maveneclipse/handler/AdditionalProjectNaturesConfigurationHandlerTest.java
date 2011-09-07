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

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.maveneclipse.MavenEclipseContext;
import org.eclipse.m2e.maveneclipse.configuration.ConfigurationParameter;
import org.eclipse.m2e.maveneclipse.configuration.MavenEclipseConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

/**
 * Tests for {@link AdditionalProjectNaturesConfigurationHandler}.
 * 
 * @author Alex Clarke
 * @author Phillip Webb
 */
public class AdditionalProjectNaturesConfigurationHandlerTest {

	private static final String FIRST_PROJECT_NATURE = "first";
	private static final String SECOND_PROJECT_NATURE = "second";

	private static final String INITIAL_NATURE = "initial";

	private AdditionalProjectNaturesConfigurationHandler additionalProjectNaturesConfigurationHandler = new AdditionalProjectNaturesConfigurationHandler();

	private MavenEclipseContext context = mock(MavenEclipseContext.class);

	private IProjectDescription projectDescription = mock(IProjectDescription.class);

	private IProject project = mock(IProject.class);

	private IProgressMonitor monitor = mock(IProgressMonitor.class);

	@Before
	public void setupBasicContextWithInitialNature() throws Exception {
		given(context.getProject()).willReturn(project);

		given(project.getDescription()).willReturn(projectDescription);
		String[] initialNatureIds = { INITIAL_NATURE };
		given(projectDescription.getNatureIds()).willReturn(initialNatureIds);

		given(context.getMonitor()).willReturn(monitor);
	}

	@Test
	public void shouldAddAdditionalProjectNatures() throws Exception {
		// Given
		MavenEclipseConfiguration mavenEclipseConfiguration = mock(MavenEclipseConfiguration.class);
		given(context.getPluginConfiguration()).willReturn(mavenEclipseConfiguration);
		ConfigurationParameter configurationParameter = mock(ConfigurationParameter.class);
		given(mavenEclipseConfiguration.getParamter(additionalProjectNaturesConfigurationHandler.getParamterName()))
				.willReturn(configurationParameter);
		given(
				mavenEclipseConfiguration.containsParamter(additionalProjectNaturesConfigurationHandler
						.getParamterName())).willReturn(true);

		List<ConfigurationParameter> projectNatureConfigurationParameters = new ArrayList<ConfigurationParameter>();
		ConfigurationParameter firstProjectNature = createProjectNatureConfigParameter(FIRST_PROJECT_NATURE);
		ConfigurationParameter secondProjectNature = createProjectNatureConfigParameter(SECOND_PROJECT_NATURE);
		projectNatureConfigurationParameters.add(firstProjectNature);
		projectNatureConfigurationParameters.add(secondProjectNature);
		given(configurationParameter.getChildren()).willReturn(projectNatureConfigurationParameters);

		// When
		additionalProjectNaturesConfigurationHandler.handle(context);

		// Then
		ArgumentCaptor<String[]> argument = ArgumentCaptor.forClass(String[].class);
		verify(projectDescription).setNatureIds(argument.capture());
		verify(project).setDescription(projectDescription, monitor);

		boolean hasInitialNature = false;
		boolean hasFirstNature = false;
		boolean hasSecondNature = false;
		String[] newNatureIds = argument.getValue();
		for (int i = 0; i < newNatureIds.length; i++) {
			if (INITIAL_NATURE.equals(newNatureIds[i])) {
				hasInitialNature = true;
			} else if (FIRST_PROJECT_NATURE.equals(newNatureIds[i])) {
				hasFirstNature = true;
			} else if (SECOND_PROJECT_NATURE.equals(newNatureIds[i])) {
				hasSecondNature = true;
			} else {
				fail("Unknown project nature found: " + newNatureIds[i]);
			}

		}
		assertTrue(hasInitialNature);
		assertTrue(hasFirstNature);
		assertTrue(hasSecondNature);
	}

	@Test
	public void shouldClaimToHandleContextsWithAdditionalProjectNaturesParamater() {
		// Given
		MavenEclipseContext context = mock(MavenEclipseContext.class);
		MavenEclipseConfiguration configuration = mock(MavenEclipseConfiguration.class);
		given(context.getPluginConfiguration()).willReturn(configuration);
		given(configuration.containsParamter(additionalProjectNaturesConfigurationHandler.getParamterName()))
				.willReturn(true);

		// When
		boolean canHandle = additionalProjectNaturesConfigurationHandler.canHandle(context);

		assertTrue(canHandle);
	}

	@Test
	public void shouldNotClaimToHandleContextsWithoutAdditionalProjectNaturesParamater() {
		// Given
		MavenEclipseContext context = mock(MavenEclipseContext.class);
		MavenEclipseConfiguration configuration = mock(MavenEclipseConfiguration.class);
		given(context.getPluginConfiguration()).willReturn(configuration);
		given(configuration.getParamter(additionalProjectNaturesConfigurationHandler.getParamterName())).willReturn(
				null);

		// When
		boolean canHandle = additionalProjectNaturesConfigurationHandler.canHandle(context);

		assertFalse(canHandle);
	}

	private ConfigurationParameter createProjectNatureConfigParameter(String natureId) {
		ConfigurationParameter firstProjectNature = mock(ConfigurationParameter.class);
		given(firstProjectNature.getName())
				.willReturn(AdditionalProjectNaturesConfigurationHandler.PROJECT_NATURE_NAME);
		given(firstProjectNature.getValue()).willReturn(natureId);
		return firstProjectNature;
	}
}
