package org.eclipse.m2e.maveneclipse.handler;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.eclipse.m2e.maveneclipse.MavenEclipseContext;
import org.eclipse.m2e.maveneclipse.configuration.ConfigurationParamter;
import org.eclipse.m2e.maveneclipse.configuration.Xpp3DomConfigurationParamter;

public class AdditionalConfigConfigurationHandler extends SingleParamterConfigurationHandler {
	@Override
	protected String getParamterName() {
		return "additionalConfig";
	}

	public void handle(MavenEclipseContext context, ConfigurationParamter paramter) {
		Xpp3DomConfigurationParamter xparamter = (Xpp3DomConfigurationParamter) paramter;
		Xpp3Dom dom = xparamter.getDom();
		System.out.println(dom);
	}
}
