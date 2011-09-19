/*
 * Copyright 2000-2011 the original author or authors.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * 
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.m2e.maveneclipse.handler.additionalprojectfacets;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.maveneclipse.MavenEclipseContext;
import org.eclipse.m2e.maveneclipse.configuration.ConfigurationParameter;
import org.eclipse.m2e.maveneclipse.configuration.MavenEclipseConfiguration;
import org.eclipse.m2e.maveneclipse.handler.additionalprojectfacets.AdditionalProjectFacetsConfigurationHandler;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IFacetedProject.Action;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Tests for {@link AdditionalProjectFacetsConfigurationHandler}.
 * 
 * @author Alex Clarke
 * @author Phillip Webb
 */
public class AdditionalProjectFacetsConfigurationHandlerTest {

	@Mock
	private MavenEclipseContext context;

	@Mock
	private ConfigurationParameter paramter;

	@Mock
	private IFacetedProject facetedProject;

	private AdditionalProjectFacetsConfigurationHandler handler = new MockAdditionalProjectFacetsConfigurationHandler();

	@Captor
	private ArgumentCaptor<Set<Action>> actionsCaptor;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		MavenEclipseConfiguration configuration = mock(MavenEclipseConfiguration.class);
		IProject project = mock(IProject.class);
		given(context.getPluginConfiguration()).willReturn(configuration);
		given(configuration.containsParamter("additionalProjectFacets")).willReturn(true);
		given(configuration.getParamter("additionalProjectFacets")).willReturn(paramter);
		given(context.getProject()).willReturn(project);
	}

	@Test
	public void shouldCreateFacets() throws Exception {
		List<ConfigurationParameter> children = new ArrayList<ConfigurationParameter>();
		children.add(mockChild("jst.jsf", "2.0"));
		given(paramter.getChildren()).willReturn(children);
		handler.handle(context);
		verify(facetedProject).modify(actionsCaptor.capture(), any(IProgressMonitor.class));
		List<Action> actions = new ArrayList<IFacetedProject.Action>(actionsCaptor.getValue());

		assertThat(actions.get(0).getType(), is(IFacetedProject.Action.Type.INSTALL));
		assertThat(actions.get(0).getProjectFacetVersion().getProjectFacet().getId(), is("jst.jsf"));
		assertThat(actions.get(0).getProjectFacetVersion().getVersionString(), is("2.0"));
	}

	private ConfigurationParameter mockChild(String name, String value) {
		ConfigurationParameter child = mock(ConfigurationParameter.class);
		given(child.getName()).willReturn(name);
		given(child.getValue()).willReturn(value);
		return child;
	}

	private class MockAdditionalProjectFacetsConfigurationHandler extends AdditionalProjectFacetsConfigurationHandler {
		@Override
		protected IFacetedProject createFacetedProject(MavenEclipseContext context) throws CoreException {
			return facetedProject;
		}
	}

}
