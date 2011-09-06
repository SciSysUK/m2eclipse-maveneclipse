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
package org.eclipse.m2e.maveneclipse;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.core.project.configurator.ProjectConfigurationRequest;
import org.eclipse.m2e.maveneclipse.handler.ConfigurationHandlers;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Tests for {@link MavenEclipseProjectConfigurator}.
 * 
 * @author Alex Clarke
 * @author Phillip Webb
 */
@RunWith(MockitoJUnitRunner.class)
@Ignore
public class MavenEclipseProjectConfiguratorTest {

	@InjectMocks
	private MavenEclipseProjectConfigurator mavenEclipseProjectConfigurator = new MavenEclipseProjectConfigurator();

	@Mock
	private ConfigurationHandlers mavenEclipseConfigurationHandler;

	@Test
	public void shouldConfigureProject() throws Exception {
		// Given
		ProjectConfigurationRequest request = mock(ProjectConfigurationRequest.class);
		IProgressMonitor monitor = mock(IProgressMonitor.class);
		// Need to mock statics to test this
		//PowerMockito.mockStatic(MavenPlugin.class);

		// When
		mavenEclipseProjectConfigurator.configure(request, monitor);

		// Then
		verify(mavenEclipseConfigurationHandler).handle(any(MavenEclipseContext.class));
	}

}
