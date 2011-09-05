package org.eclipse.m2e.maveneclipse.handler;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Properties;

import org.apache.maven.project.MavenProject;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.m2e.maveneclipse.MavenEclipseContext;
import org.eclipse.m2e.maveneclipse.configuration.MavenEclipseConfiguration;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class AdditionalProjectNaturesConfigurationHandlerTest {

	private static final String INITIAL_NATURE = "nature1";
	private static final String NEW_NATURE = "nature2";

	private AdditionalProjectNaturesConfigurationHandler additionalProjectNaturesConfigurationHandler = new AdditionalProjectNaturesConfigurationHandler();

	@Ignore
	@Test
	public void shouldAddProjectNatures() throws Exception {
		// Given
		MavenEclipseContext context = mock(MavenEclipseContext.class);
		IProject project = mock(IProject.class);
		given(context.getProject()).willReturn(project);
		IProjectDescription projectDescription = mock(IProjectDescription.class);
		given(project.getDescription()).willReturn(projectDescription);
		String[] initialNatureIds = { INITIAL_NATURE };
		given(projectDescription.getNatureIds()).willReturn(initialNatureIds);

		MavenProject mavenProject = mock(MavenProject.class);
		given(context.getMavenProject()).willReturn(mavenProject);
		Properties properties = mock(Properties.class);
		given(mavenProject.getProperties()).willReturn(properties);
		//given(properties.get(AdditionalProjectNaturesConfigurationHandler.PROJECT_NATURES_PROPERTY_NAME)).willReturn(NEW_NATURE);

		// When
		additionalProjectNaturesConfigurationHandler.handle(context);

		// Then
		ArgumentCaptor<String[]> argument = ArgumentCaptor.forClass(String[].class);
		verify(projectDescription).setNatureIds(argument.capture());

		boolean hasInitialNature = false;
		boolean hasNewNature = false;
		String[] newNatureIds = argument.getValue();
		for (int i = 0; i < newNatureIds.length; i++) {
			if (INITIAL_NATURE.equals(newNatureIds[i])) {
				hasInitialNature = true;
			}
			if (NEW_NATURE.equals(newNatureIds[i])) {
				hasNewNature = true;
			}
		}
		assertTrue(hasInitialNature);
		assertTrue(hasNewNature);

	}

	@Ignore
	@Test
	public void shouldAddAliasedProjectNature() throws Exception {
		// Given
		MavenEclipseContext context = mock(MavenEclipseContext.class);
		IProject project = mock(IProject.class);
		given(context.getProject()).willReturn(project);
		IProjectDescription projectDescription = mock(IProjectDescription.class);
		given(project.getDescription()).willReturn(projectDescription);
		String[] initialNatureIds = { INITIAL_NATURE };
		given(projectDescription.getNatureIds()).willReturn(initialNatureIds);

		MavenProject mavenProject = mock(MavenProject.class);
		given(context.getMavenProject()).willReturn(mavenProject);
		Properties properties = mock(Properties.class);
		given(mavenProject.getProperties()).willReturn(properties);
		//given(properties.get(AdditionalProjectNaturesConfigurationHandler.PROJECT_NATURES_PROPERTY_NAME)).willReturn("spring");

		// When
		additionalProjectNaturesConfigurationHandler.handle(context);

		// Then
		ArgumentCaptor<String[]> argument = ArgumentCaptor.forClass(String[].class);
		verify(projectDescription).setNatureIds(argument.capture());

		boolean hasInitialNature = false;
		boolean hasNewNature = false;
		String[] newNatureIds = argument.getValue();
		for (int i = 0; i < newNatureIds.length; i++) {
			if (INITIAL_NATURE.equals(newNatureIds[i])) {
				hasInitialNature = true;
			}
			if ("org.springframework.ide.eclipse.core.springnature".equals(newNatureIds[i])) {
				hasNewNature = true;
			}
		}
		assertTrue(hasInitialNature);
		assertTrue(hasNewNature);
	}

	@Test
	public void shouldNotClaimToHandleContextsWithoutAdditionalProjectNaturesParamater() {
		// Given
		MavenEclipseContext context = mock(MavenEclipseContext.class);
		MavenEclipseConfiguration configuration = mock(MavenEclipseConfiguration.class);
		given(context.getPluginConfiguration()).willReturn(configuration);
		given(configuration.getParamter(AdditionalProjectNaturesConfigurationHandler.PARAMETER_NAME)).willReturn(null);

		// When
		boolean canHandle = additionalProjectNaturesConfigurationHandler.canHandle(context);

		assertFalse(canHandle);

	}
}
