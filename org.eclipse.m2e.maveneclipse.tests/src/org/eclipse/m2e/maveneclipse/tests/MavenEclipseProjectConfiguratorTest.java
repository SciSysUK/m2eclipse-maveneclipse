package org.eclipse.m2e.maveneclipse.tests;

import static org.mockito.Mockito.verify;

import java.io.OutputStream;

import org.eclipse.m2e.maveneclipse.MavenEclipseProjectConfigurator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MavenEclipseProjectConfiguratorTest {

	@Mock
	private OutputStream outputStream;

	@Test
	public void shouldSupportMockito() throws Exception {
		MavenEclipseProjectConfigurator configurator;
		outputStream.close();
		verify(outputStream).close();
	}

}
