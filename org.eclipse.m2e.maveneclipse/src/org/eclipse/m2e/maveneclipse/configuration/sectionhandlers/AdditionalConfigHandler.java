package org.eclipse.m2e.maveneclipse.configuration.sectionhandlers;

import org.eclipse.m2e.maveneclipse.MavenEclipseContext;
import org.eclipse.m2e.maveneclipse.configuration.ConfigurationSectionHandler;

public class AdditionalConfigHandler implements ConfigurationSectionHandler
{
    public boolean canHandle(MavenEclipseContext context)
    {
        return false;
    }

    public void handle(MavenEclipseContext context)
    {
    }
}
