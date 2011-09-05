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
import org.eclipse.m2e.maveneclipse.handler.AdditionalConfigConfigurationHandler;
import org.eclipse.m2e.maveneclipse.handler.ConfigurationHandler;
import org.eclipse.m2e.maveneclipse.handler.ConfigurationHandlers;
import org.eclipse.m2e.maveneclipse.handler.ProjectNatureConfigurationHandler;
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
public class MavenEclipseConfigurationHandlerTest {

	@Mock
	private MavenEclipseContext context;

	@Test
	public void shouldDelegateToAllRelevantSectionHandlers() {
		ConfigurationHandler h1 = mock(ConfigurationHandler.class);
		ConfigurationHandler h2 = mock(ConfigurationHandler.class);
		ConfigurationHandler h3 = mock(ConfigurationHandler.class);
		ConfigurationHandler[] sectionHandlers = { h1, h2, h3 };
		ConfigurationHandlers configurationHandler = new ConfigurationHandlers(sectionHandlers);

		given(h2.canHandle(context)).willReturn(true);
		given(h3.canHandle(context)).willReturn(true);

		configurationHandler.handle(context);

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
