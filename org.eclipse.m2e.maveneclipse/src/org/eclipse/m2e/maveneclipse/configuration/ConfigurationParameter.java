/*
 * Copyright 2000-2011 the original author or authors.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *     http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
