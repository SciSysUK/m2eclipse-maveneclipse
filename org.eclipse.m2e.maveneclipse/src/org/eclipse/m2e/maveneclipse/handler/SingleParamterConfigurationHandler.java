package org.eclipse.m2e.maveneclipse.handler;

import org.eclipse.m2e.maveneclipse.MavenEclipseContext;
import org.eclipse.m2e.maveneclipse.configuration.ConfigurationParamter;

/**
 * Convenient base class for {@link ConfigurationHandler}s that are based around a single configuration
 * paramters.
 * 
 * @author Alex Clarke
 * @author Phillip Webb
 */
public abstract class SingleParamterConfigurationHandler implements ConfigurationHandler {
	/**
	 * Returns the parameter name that is used to determine if the handler should run.
	 * 
	 * @return the parameter name
	 */
	protected abstract String getParamterName();

	public boolean canHandle(MavenEclipseContext context) {
		return context.getConfiguration().containsParamter(getParamterName());
	}

	public final void handle(MavenEclipseContext context) {
		ConfigurationParamter paramter = context.getConfiguration().getParamter(getParamterName());
		if (!canHandle(context) || paramter == null) {
			throw new IllegalStateException("Unable to handle context");
		}
		handle(context, paramter);
	}

	protected abstract void handle(MavenEclipseContext context, ConfigurationParamter paramter);
}
