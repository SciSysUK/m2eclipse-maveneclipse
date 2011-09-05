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
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
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

	public void handle(MavenEclipseContext context, ConfigurationParameter paramter) throws Exception {
		AdditionalConfigFiles additionalConfigFiles = new AdditionalConfigFiles(context, paramter);
		additionalConfigFiles.copyFilesToProject();
	}

	private static class AdditionalConfigFiles {

		private MavenEclipseContext context;

		private List<AdditionalConfigFile> files;

		private File locationRoot;

		public AdditionalConfigFiles(MavenEclipseContext context, ConfigurationParameter paramter) {
			this.context = context;
			this.locationRoot = context.getMavenProject().getFile().getParentFile();
			this.files = new ArrayList<AdditionalConfigFile>();
			for (ConfigurationParameter child : paramter.getChildren()) {
				if ("file".equals(child.getName())) {
					files.add(newFile(child));
				}
			}
		}

		public void copyFilesToProject() throws Exception {
			for (AdditionalConfigFile file : files) {
				file.copyToProject();
			}
		}

		private AdditionalConfigFile newFile(ConfigurationParameter parameter) {
			//Follow the same logic as the EclipsePlugin class
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

		private abstract class AdditionalConfigFile {

			private ConfigurationParameter parameter;

			public AdditionalConfigFile(ConfigurationParameter parameter) {
				this.parameter = parameter;
			}

			protected final ConfigurationParameter getParameter() {
				return parameter;
			}

			public void copyToProject() throws Exception {
				String name = parameter.getChild("name").getValue();
				IFile destination = context.getProject().getFile(name);
				if (destination.exists()) {
					destination.delete(true, context.getMonitor());
				}
				InputStream source = getSource();
				try {
					destination.create(new BufferedInputStream(source), true, context.getMonitor());
				} finally {
					source.close();
				}
			}

			protected InputStream getSource() throws Exception {
				return null; //FIXME make abst
			}
		}

		private class AdditionalConfigContentFile extends AdditionalConfigFile {

			public AdditionalConfigContentFile(ConfigurationParameter parameter) {
				super(parameter);
			}

		}

		private class AdditionalConfigUrlFile extends AdditionalConfigFile {

			public AdditionalConfigUrlFile(ConfigurationParameter parameter) {
				super(parameter);
			}
		}

		private class AdditionalConfigLocationFile extends AdditionalConfigFile {

			public AdditionalConfigLocationFile(ConfigurationParameter parameter) {
				super(parameter);
			}

			@Override
			protected InputStream getSource() throws Exception {
				String location = getParameter().getChild("location").getValue();
				File locationFile = new File(locationRoot, location);
				return new FileInputStream(locationFile);
			}
		}

	}

}
