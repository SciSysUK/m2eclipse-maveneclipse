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

import java.util.List;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Tests for {@link Xpp3DomConfigurationParameter}.
 * 
 * @author Alex Clarke
 * @author Phillip Webb
 */
public class Xpp3DomConfigurationParamterTest {

	@Mock
	private Xpp3Dom dom;

	private Xpp3DomConfigurationParameter parameter;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		parameter = new Xpp3DomConfigurationParameter(dom);
	}

	@Test
	public void shouldGetName() throws Exception {
		String expected = "name";
		given(dom.getName()).willReturn(expected);
		String actual = parameter.getName();
		assertThat(actual, is(expected));
	}

	@Test
	public void shouldGetChildren() throws Exception {
		Xpp3Dom c1 = mockDom("c1");
		Xpp3Dom c2 = mockDom("c2");
		given(dom.getChildren()).willReturn(new Xpp3Dom[] { c1, c2 });
		List<ConfigurationParameter> children = parameter.getChildren();
		assertThat(children.size(), is(2));
		assertThat(children.get(0).getName(), is("c1"));
		assertThat(children.get(1).getName(), is("c2"));
	}

	@Test
	public void shouldHaveChildByName() throws Exception {
		Xpp3Dom child = mockDom("found");
		given(dom.getChild("found")).willReturn(child);
		assertThat(parameter.hasChild("missing"), is(false));
		assertThat(parameter.hasChild("found"), is(true));
	}

	@Test
	public void shouldGetChild() throws Exception {
		Xpp3Dom child = mockDom("child");
		given(dom.getChild("child")).willReturn(child);
		ConfigurationParameter actual = parameter.getChild("child");
		assertThat(actual.getName(), is("child"));
	}

	@Test
	public void shouldGetNullChildWhenMissing() throws Exception {
		given(dom.getChild("child")).willReturn(null);
		ConfigurationParameter actual = parameter.getChild("child");
		assertThat(actual, is(nullValue()));
	}

	@Test
	public void shouldGetValue() throws Exception {
		given(dom.getValue()).willReturn("value");
		assertThat(parameter.getValue(), is("value"));
	}

	private Xpp3Dom mockDom(String name) {
		Xpp3Dom dom = mock(Xpp3Dom.class);
		given(dom.getName()).willReturn(name);
		return dom;
	}

}
