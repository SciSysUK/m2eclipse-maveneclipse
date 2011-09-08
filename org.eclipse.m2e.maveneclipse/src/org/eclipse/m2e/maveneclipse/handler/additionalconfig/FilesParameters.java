package org.eclipse.m2e.maveneclipse.handler.additionalconfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.m2e.maveneclipse.MavenEclipseContext;
import org.eclipse.m2e.maveneclipse.configuration.ConfigurationParameter;

/**
 * Additional config files that can be copied to the eclipse project.
 */
class FilesParameters {

	private MavenEclipseContext context;

	private File locationRoot;

	private List<FileParameter> files;

	public FilesParameters(MavenEclipseContext context, ConfigurationParameter paramter) {
		this.context = context;
		File pomFile = context.getMavenProject().getFile();
		this.locationRoot = pomFile.getParentFile();
		this.files = getConfiguredFiles(paramter);
	}

	/**
	 * Returns a list of {@link FileParameter}s that have been configured for the plugin.
	 * @param paramter the parameter
	 * @return a list of files
	 */
	private List<FileParameter> getConfiguredFiles(ConfigurationParameter paramter) {
		files = new ArrayList<FileParameter>();
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
		for (FileParameter file : files) {
			file.copyToProject();
		}
	}

	/**
	 * Factory method used to create a new file. This method follows the same precedence logic as the
	 * <tt>maven-eclipse-plugin</tt> namely: <ul> <li>content</li> <li>location</li> <li>url</li> </ul>
	 * 
	 * @param fileParameter the parameter
	 * @return a config file instance
	 */
	private FileParameter newFile(ConfigurationParameter fileParameter) {
		if (fileParameter.hasChild(ContentFileParameter.CHILD_NAME)) {
			return new ContentFileParameter(context, fileParameter);
		}
		if (fileParameter.hasChild(LocationFileParameter.CHILD_NAME)) {
			return new LocationFileParameter(context, fileParameter, locationRoot);
		}
		if (fileParameter.hasChild(UrlFileParameter.CHILD_NAME)) {
			return new UrlFileParameter(context, fileParameter);
		}
		throw new IllegalStateException("Malformed additionalConfig file paramter");
	}
}