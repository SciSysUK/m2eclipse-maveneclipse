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
package org.eclipse.m2e.maveneclipse.configuration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import org.apache.maven.model.Plugin;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Tests for {@link DefaultMavenEclipseConfiguration}.
 * 
 * @author Alex Clarke
 * @author Phillip Webb
 */
public class DefaultMavenEclipseConfigurationTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Mock
	private Xpp3Dom dom;

	@Mock
	private Plugin plugin;

	private DefaultMavenEclipseConfiguration configuration;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		given(plugin.getConfiguration()).willReturn(dom);
		configuration = new DefaultMavenEclipseConfiguration(plugin);
	}

	@Test
	public void shouldGetParamter() throws Exception {
		Xpp3Dom child = mock(Xpp3Dom.class);
		given(child.getName()).willReturn("name");
		given(dom.getChildren("p")).willReturn(new Xpp3Dom[] { child });
		ConfigurationParameter paramter = configuration.getParamter("p");
		assertThat(paramter.getName(), is("name"));
	}

	@Test
	public void shouldReturnNullParamter() throws Exception {
		given(dom.getChildren("p")).willReturn(new Xpp3Dom[] {});
		ConfigurationParameter paramter = configuration.getParamter("p");
		assertThat(paramter, is(nullValue()));
	}

	@Test
	public void shouldNotSupportMultipleParamtersOfSameName() throws Exception {
		Xpp3Dom c1 = mock(Xpp3Dom.class);
		Xpp3Dom c2 = mock(Xpp3Dom.class);
		given(dom.getChildren("p")).willReturn(new Xpp3Dom[] { c1, c2 });
		thrown.expect(IllegalStateException.class);
		thrown.expectMessage("Unexpected number of child parameters defined for 'p'");
		configuration.getParamter("p");
	}
}
