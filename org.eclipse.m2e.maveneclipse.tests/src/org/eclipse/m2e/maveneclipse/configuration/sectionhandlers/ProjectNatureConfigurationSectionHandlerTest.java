package org.eclipse.m2e.maveneclipse.configuration.sectionhandlers;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Properties;

import org.apache.maven.project.MavenProject;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.m2e.maveneclipse.MavenEclipseContext;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class ProjectNatureConfigurationSectionHandlerTest
{

    private static final String NATURE1 = "nature1";

    private static final String NATURE2 = "nature2";

    private static final String NATURE3 = "nature3";

    private ProjectNatureConfigurationSectionHandler projectNatureConfigurationSectionHandler =
        new ProjectNatureConfigurationSectionHandler();

    @Test
    public void shouldAddProjectNature() throws Exception
    {
        // Given
        MavenEclipseContext context = mock(MavenEclipseContext.class);
        IProject project = mock(IProject.class);
        given(context.getProject()).willReturn(project);
        IProjectDescription projectDescription = mock(IProjectDescription.class);
        given(project.getDescription()).willReturn(projectDescription);
        String[] initialNatureIds = { NATURE1, NATURE2 };
        given(projectDescription.getNatureIds()).willReturn(initialNatureIds);

        MavenProject mavenProject = mock(MavenProject.class);
        given(context.getMavenProject()).willReturn(mavenProject);
        Properties properties = mock(Properties.class);
        given(mavenProject.getProperties()).willReturn(properties);
        given(properties.get(ProjectNatureConfigurationSectionHandler.PROJECT_NATURES_PROPERTY_NAME)).willReturn(
            NATURE3);

        // When
        projectNatureConfigurationSectionHandler.handle(context);

        // Then
        String[] expectedNatureIds = { NATURE1, NATURE2, NATURE3 };
        ArgumentCaptor<String[]> argument = ArgumentCaptor.forClass(String[].class);
        verify(projectDescription).setNatureIds(expectedNatureIds);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToAddProjectNatureWithIrrelevantConfiguration()
    {

    }

    @Test
    public void shouldClaimToHandleIrrelevantConfiguration()
    {

    }

    @Test
    public void shouldNotClaimToHandleIrrelevantConfiguration()
    {

    }

}
