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
		ConfigurationHandler[] defaultHandlers = new ConfigurationHandlers().getSectionHandlers();
		Set<Class<?>> defaultHandlerClasses = new HashSet<Class<?>>();
		for (ConfigurationHandler handler : defaultHandlers) {
			defaultHandlerClasses.add(handler.getClass());
		}
		Set<Class<?>> expected = new HashSet<Class<?>>();
		expected.addAll(Arrays.<Class<?>> asList(AdditionalConfigConfigurationHandler.class,
				ProjectNatureConfigurationHandler.class));
		assertThat(defaultHandlerClasses, is(equalTo(expected)));
	}
}
