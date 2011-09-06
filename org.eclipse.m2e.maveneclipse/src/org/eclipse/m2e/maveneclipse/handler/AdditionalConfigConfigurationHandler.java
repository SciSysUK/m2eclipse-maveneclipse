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
package org.eclipse.m2e.maveneclipse.handler;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
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

	protected void handle(MavenEclipseContext context, ConfigurationParameter paramter) throws Exception {
		AdditionalConfigFiles additionalConfigFiles = new AdditionalConfigFiles(context, paramter);
		additionalConfigFiles.copyFilesToProject();
	}

	/**
	 * Loads the additional config files and can copy the eclipse project.
	 */
	private static class AdditionalConfigFiles {

		private MavenEclipseContext context;

		private List<AdditionalConfigFile> files;

		private File locationRoot;

		public AdditionalConfigFiles(MavenEclipseContext context, ConfigurationParameter paramter) {
			this.context = context;
			this.locationRoot = context.getMavenProject().getFile().getParentFile();
			this.files = getConfiguredFiles(paramter);
		}

		/**
		 * Returns a list of {@link AdditionalConfigFile}s that have been configured for the plugin.
		 * @param paramter the parameter
		 * @return a list of files
		 */
		private List<AdditionalConfigFile> getConfiguredFiles(ConfigurationParameter paramter) {
			files = new ArrayList<AdditionalConfigFile>();
			for (ConfigurationParameter child : paramter.getChildren()) {
				if ("file".equals(child.getName())) {
					files.add(newFile(child));
				}
			}
			return files;
		}

		/**
		 * Copy all additional files to the project.
		 * @throws Exception
		 */
		public void copyFilesToProject() throws Exception {
			for (AdditionalConfigFile file : files) {
				file.copyToProject();
			}
		}

		/**
		 * Factory method used to create a new file. This method follows the same precedence logic as the
		 * <tt>maven-eclipse-plugin</tt> namely: <ul> <li>content</li> <li>location</li> <li>url</li> </ul>
		 * 
		 * @param parameter the parameter
		 * @return a config file instance
		 */
		private AdditionalConfigFile newFile(ConfigurationParameter parameter) {
			if (parameter.hasChild("content")) {
				return new AdditionalConfigContentFile(parameter);
			}
			if (parameter.hasChild("location")) {
				return new AdditionalConfigLocationFile(parameter);
			}
			if (parameter.hasChild("url")) {
				return new AdditionalConfigUrlFile(parameter);
			}
			throw new IllegalStateException("Malformed additionalConfig file paramter");
		}

		/**
		 * Abstract base class of an additional config file.
		 * @see AdditionalConfigContentFile
		 * @see AdditionalConfigLocationFile
		 * @see AdditionalConfigUrlFile
		 */
		private abstract class AdditionalConfigFile {

			private ConfigurationParameter parameter;

			public AdditionalConfigFile(ConfigurationParameter parameter) {
				this.parameter = parameter;
			}

			protected final ConfigurationParameter getParameter() {
				return parameter;
			}

			/**
			 * Copy this files to the project.
			 * @throws Exception
			 */
			public void copyToProject() throws Exception {
				String name = parameter.getChild("name").getValue();
				IFile destination = context.getProject().getFile(name);
				if (destination.exists()) {
					destination.delete(true, context.getMonitor());
				}
				InputStream content = getContent();
				try {
					destination.create(new BufferedInputStream(content), true, context.getMonitor());
				} finally {
					content.close();
				}
			}

			/**
			 * Returns an input stream containing file content.
			 * @return the files content
			 * @throws Exception
			 */
			protected abstract InputStream getContent() throws Exception;
		}

		/**
		 * {@link AdditionalConfigFile} that has content directly specified.
		 */
		private class AdditionalConfigContentFile extends AdditionalConfigFile {

			public AdditionalConfigContentFile(ConfigurationParameter parameter) {
				super(parameter);
			}

			@Override
			protected InputStream getContent() throws Exception {
				String content = getParameter().getChild("content").getValue();
				return new ByteArrayInputStream(content.getBytes(System.getProperty("file.encoding")));
			}
		}

		/**
		 * {@link AdditionalConfigFile} that loads content from a URL.
		 */
		private class AdditionalConfigUrlFile extends AdditionalConfigFile {

			public AdditionalConfigUrlFile(ConfigurationParameter parameter) {
				super(parameter);
			}

			@Override
			protected InputStream getContent() throws Exception {
				String location = getParameter().getChild("url").getValue();
				URL url = new URL(location);
				return url.openStream();
			}
		}

		/**
		 * {@link AdditionalConfigFile} that loads content from a project location.
		 */
		private class AdditionalConfigLocationFile extends AdditionalConfigFile {

			public AdditionalConfigLocationFile(ConfigurationParameter parameter) {
				super(parameter);
			}

			@Override
			protected InputStream getContent() throws Exception {
				String location = getParameter().getChild("location").getValue();
				File locationFile = new File(locationRoot, location);
				if (!locationFile.exists()) {
					throw new IllegalStateException("Location file " + locationFile.getAbsolutePath()
							+ " does not exist");
				}
				return new FileInputStream(locationFile);
			}
		}
	}
}
