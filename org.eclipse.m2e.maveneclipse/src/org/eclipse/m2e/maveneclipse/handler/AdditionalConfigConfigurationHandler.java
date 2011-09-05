package org.eclipse.m2e.maveneclipse.handler;

import org.eclipse.m2e.maveneclipse.MavenEclipseContext;
import org.eclipse.m2e.maveneclipse.configuration.ConfigurationParameter;

/**
 * A {@link ConfigurationHandler} that deals with <tt>additionalConfig</tt> from the <tt>maven-eclipse-plugin</tt>.
 * 
 * @author Alex Clarke
 * @author Phillip Webb
 */
public class AdditionalConfigConfigurationHandler extends SingleParamterConfigurationHandler {

	@Override
	protected String getParamterName() {
		return "additionalConfig";
	}

	public void handle(MavenEclipseContext context, ConfigurationParameter paramter) {
		//FIXME
	}
}
