package org.eclipse.m2e.maveneclipse;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.core.project.configurator.ProjectConfigurationRequest;
import org.eclipse.m2e.maveneclipse.configuration.MavenEclipseConfigurationHandler;
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
	private MavenEclipseConfigurationHandler mavenEclipseConfigurationHandler;

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
