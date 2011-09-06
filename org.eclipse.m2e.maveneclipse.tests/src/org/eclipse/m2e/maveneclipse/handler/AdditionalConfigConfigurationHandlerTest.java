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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willAnswer;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.IOUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.maveneclipse.MavenEclipseContext;
import org.eclipse.m2e.maveneclipse.configuration.ConfigurationParameter;
import org.eclipse.m2e.maveneclipse.configuration.MavenEclipseConfiguration;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Tests for {@link AdditionalConfigConfigurationHandler}.
 * 
 * @author Alex Clarke
 * @author Phillip Webb
 */
public class AdditionalConfigConfigurationHandlerTest {

	private AdditionalConfigConfigurationHandler handler = new AdditionalConfigConfigurationHandler();

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Mock
	private MavenEclipseContext context;

	@Mock
	private ConfigurationParameter additionalConfigParameter;

	private File location = new File("./src/org/eclipse/m2e/maveneclipse/handler");

	@Mock
	private IFile projectFile;

	@Mock
	private IProgressMonitor monitor;

	private String writtenContent;

	@Before
	public void setup() throws Exception {
		MockitoAnnotations.initMocks(this);
		MavenProject mavenProject = mock(MavenProject.class);
		File file = mock(File.class);
		IProject project = mock(IProject.class);
		MavenEclipseConfiguration configuration = mock(MavenEclipseConfiguration.class);
		given(context.getMavenProject()).willReturn(mavenProject);
		given(mavenProject.getFile()).willReturn(file);
		given(file.getParentFile()).willReturn(location);
		given(context.getPluginConfiguration()).willReturn(configuration);
		given(configuration.containsParamter("additionalConfig")).willReturn(true);
		given(configuration.getParamter("additionalConfig")).willReturn(additionalConfigParameter);
		given(context.getProject()).willReturn(project);
		given(project.getFile(anyString())).willReturn(projectFile);
		given(context.getMonitor()).willReturn(monitor);
		willAnswer(new Answer<Object>() {
			public Object answer(InvocationOnMock invocation) throws Throwable {
				InputStream inputStream = (InputStream) invocation.getArguments()[0];
				writtenContent = IOUtil.toString(inputStream);
				return null;
			}
		}).given(projectFile).create(isA(InputStream.class), eq(true), eq(monitor));
	}

	@Test
	public void shouldNeedContentLocationOrUrl() throws Exception {
		givenSingleChild("name", "unknown", "value");
		thrown.expect(IllegalStateException.class);
		thrown.expectMessage("Malformed additionalConfig file paramter");
		handler.handle(context);
	}

	@Test
	public void shouldCopyContent() throws Exception {
		givenSingleChild("name", "content", "value");
		handler.handle(context);
		assertThatFileContent(is("value"));
	}

	@Test
	public void shouldCopyLocation() throws Exception {
		givenSingleChild("name", "location", "content.txt");
		handler.handle(context);
		assertThatFileContent(is("content"));
	}

	@Test
	public void shouldCopyURL() throws Exception {
		URL url = getClass().getResource("content.txt");
		givenSingleChild("name", "url", url.toString());
		handler.handle(context);
		assertThatFileContent(is("content"));
	}

	private void givenSingleChild(String name, String type, String value) {
		List<ConfigurationParameter> children = Collections.singletonList(childParameter(name, type, value));
		given(additionalConfigParameter.getChildren()).willReturn(children);
	}

	private ConfigurationParameter childParameter(String name, String type, String value) {
		ConfigurationParameter childParameter = mock(ConfigurationParameter.class);
		ConfigurationParameter nameParameter = mock(ConfigurationParameter.class);
		ConfigurationParameter contentParameter = mock(ConfigurationParameter.class);
		given(childParameter.getName()).willReturn("file");
		given(childParameter.getChild("name")).willReturn(nameParameter);
		given(nameParameter.getValue()).willReturn(name);
		given(childParameter.hasChild(type)).willReturn(true);
		given(childParameter.getChild(type)).willReturn(contentParameter);
		given(contentParameter.getValue()).willReturn(value);
		return childParameter;
	}

	private void assertThatFileContent(Matcher<String> matcher) throws Exception {
		verify(projectFile).create(isA(InputStream.class), eq(true), eq(monitor));
		assertThat(writtenContent, matcher);
	}

}
