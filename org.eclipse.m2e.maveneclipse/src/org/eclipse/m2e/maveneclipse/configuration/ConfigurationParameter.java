package org.eclipse.m2e.maveneclipse.configuration;

import java.util.List;

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

	/**
	 * Get a {@link List} of children of the this {@link ConfigurationParameter}. Will return an empty {@link List} when
	 * there are no children.
	 * @return a {@link List} of {@link ConfigurationParameter}
	 */
	public List<ConfigurationParameter> getChildren();

	/**
	 * Get the value of this {@link ConfigurationParameter}.
	 * @return the {@link String} value of this {@link ConfigurationParameter}
	 */
	public String getValue();
}
