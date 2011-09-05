package org.eclipse.m2e.maveneclipse.configuration;

/**
 * A single configuration parameter.
 * 
 * @author Alex Clarke
 * @author Phillip Webb
 */
public interface ConfigurationParameter {

	/**
	 * Returns the name of the parameter.
	 * @return the parameter name.
	 */
	public String getName();
}
