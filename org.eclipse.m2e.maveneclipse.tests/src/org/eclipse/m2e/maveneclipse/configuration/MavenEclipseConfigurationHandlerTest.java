package org.eclipse.m2e.maveneclipse.configuration;

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
import org.eclipse.m2e.maveneclipse.configuration.sectionhandlers.AdditionalConfigHandler;
import org.eclipse.m2e.maveneclipse.configuration.sectionhandlers.ProjectNatureConfigurationSectionHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Tests for {@link MavenEclipseConfigurationHandler}.
 * 
 * @author Alex Clarke
 * @author Phillip Webb
 */
@RunWith(MockitoJUnitRunner.class)
public class MavenEclipseConfigurationHandlerTest
{
    @Mock
    private MavenEclipseContext context;

    @Test
    public void shouldDelegateToAllRelevantSectionHandlers()
    {
        ConfigurationSectionHandler h1 = mock(ConfigurationSectionHandler.class);
        ConfigurationSectionHandler h2 = mock(ConfigurationSectionHandler.class);
        ConfigurationSectionHandler h3 = mock(ConfigurationSectionHandler.class);
        ConfigurationSectionHandler[] sectionHandlers = { h1, h2, h3 };
        MavenEclipseConfigurationHandler configurationHandler = new MavenEclipseConfigurationHandler(sectionHandlers);

        given(h2.canHandle(context)).willReturn(true);
        given(h3.canHandle(context)).willReturn(true);

        configurationHandler.handle(context);

        verify(h1, never()).handle(context);
        verify(h2).handle(context);
        verify(h3).handle(context);
    }

    @Test
    public void shouldHaveDefaultHandlers() throws Exception
    {
        ConfigurationSectionHandler[] defaultHandlers = new MavenEclipseConfigurationHandler().getSectionHandlers();
        Set<Class<?>> defaultHandlerClasses = new HashSet<Class<?>>();
        for (ConfigurationSectionHandler handler : defaultHandlers)
        {
            defaultHandlerClasses.add(handler.getClass());
        }
        Set<Class<?>> expected = new HashSet<Class<?>>();
        expected.addAll(Arrays.<Class<?>> asList(AdditionalConfigHandler.class,
            ProjectNatureConfigurationSectionHandler.class));
        assertThat(defaultHandlerClasses, is(equalTo(expected)));
    }

}
