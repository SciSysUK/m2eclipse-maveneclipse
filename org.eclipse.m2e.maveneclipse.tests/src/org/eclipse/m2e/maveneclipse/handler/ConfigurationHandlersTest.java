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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.m2e.maveneclipse.MavenEclipseContext;
import org.eclipse.m2e.maveneclipse.handler.additionalbuildcommands.AdditionalBuildCommandsConfigurationHandler;
import org.eclipse.m2e.maveneclipse.handler.additionalconfig.AdditionalConfigConfigurationHandler;
import org.eclipse.m2e.maveneclipse.handler.additionalprojectfacets.AdditionalProjectFacetsConfigurationHandler;
import org.eclipse.m2e.maveneclipse.handler.additionalprojectnatures.AdditionalProjectNaturesConfigurationHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Tests for {@link ConfigurationHandlers}.
 * 
 * @author Alex Clarke
 * @author Phillip Webb
 */
@RunWith(MockitoJUnitRunner.class)
public class ConfigurationHandlersTest {

	@Mock
	private MavenEclipseContext context;

	@Test
	public void shouldDelegateToAllRelevantSectionHandlers() throws Exception {
		ConfigurationHandler h1 = mock(ConfigurationHandler.class);
		ConfigurationHandler h2 = mock(ConfigurationHandler.class);
		ConfigurationHandler h3 = mock(ConfigurationHandler.class);
		ConfigurationHandler[] handlers = { h1, h2, h3 };
		ConfigurationHandlers configurationHandlers = new ConfigurationHandlers(handlers);

		given(h2.canHandle(context)).willReturn(true);
		given(h3.canHandle(context)).willReturn(true);

		configurationHandlers.handle(context);

		verify(h1, never()).handle(context);
		verify(h2).handle(context);
		verify(h3).handle(context);
	}

	@Test
	public void shouldHaveDefaultHandlers() throws Exception {
		ConfigurationHandler[] defaultHandlers = new ConfigurationHandlers().getConfigurationHandlers();
		Set<Class<?>> defaultHandlerClasses = new HashSet<Class<?>>();
		for (ConfigurationHandler handler : defaultHandlers) {
			defaultHandlerClasses.add(handler.getClass());
		}
		Set<Class<?>> expected = new HashSet<Class<?>>();
		expected.addAll(Arrays.<Class<?>> asList(AdditionalBuildCommandsConfigurationHandler.class,
				AdditionalConfigConfigurationHandler.class, AdditionalProjectFacetsConfigurationHandler.class,
				AdditionalProjectNaturesConfigurationHandler.class));
		assertThat(defaultHandlerClasses, is(equalTo(expected)));
	}
}
