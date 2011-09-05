package org.eclipse.m2e.maveneclipse.handler;

import org.eclipse.m2e.maveneclipse.MavenEclipseContext;
import org.eclipse.m2e.maveneclipse.configuration.ConfigurationParamter;

public class AdditionalConfigConfigurationHandler extends SingleParamterConfigurationHandler {
	@Override
	protected String getParamterName() {
		return "additionalConfig";
	}

	public void handle(MavenEclipseContext context, ConfigurationParamter paramter) {
	}
}
