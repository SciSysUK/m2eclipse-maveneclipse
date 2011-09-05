package org.eclipse.m2e.maveneclipse.configuration.sectionhandlers;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.eclipse.m2e.maveneclipse.ConfigurationParamter;
import org.eclipse.m2e.maveneclipse.MavenEclipseContext;
import org.eclipse.m2e.maveneclipse.Xpp3DomConfigurationParamter;

public class AdditionalConfigHandler extends SingleParamterConfigurationSectionHandler
{
    @Override
    protected String getParamterName()
    {
        return "additionalConfig";
    }

    public void handle(MavenEclipseContext context, ConfigurationParamter paramter)
    {
        Xpp3DomConfigurationParamter xparamter = (Xpp3DomConfigurationParamter)paramter;
        Xpp3Dom dom = xparamter.getDom();
        System.out.println(dom);
    }
}
