package org.eclipse.m2e.maveneclipse.handler.additionalconfig;

import java.io.BufferedInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.m2e.maveneclipse.MavenEclipseContext;
import org.eclipse.m2e.maveneclipse.configuration.ConfigurationParameter;

/**
 * Abstract base class of an additional config file.
 * @see ContentFileParameter
 * @see LocationFileParameter
 * @see UrlFileParameter
 */
abstract class FileParameter {

	private MavenEclipseContext context;

	private ConfigurationParameter fileParameter;

	public FileParameter(MavenEclipseContext context, ConfigurationParameter fileParameter) {
		this.context = context;
		this.fileParameter = fileParameter;
	}

	/**
	 * @return the name of the child element that this paramter supports.
	 */
	protected abstract String getChildName();

	/**
	 * @return the child value
	 */
	protected final String getChildValue() {
		return fileParameter.getChild(getChildName()).getValue();
	}

	/**
	 * Copy this files to the project.
	 * @throws Exception
	 */
	public void copyToProject() throws Exception {
		String name = fileParameter.getChild("name").getValue();
		IFile projectFile = context.getProject().getFile(name);
		if (projectFile.exists()) {
			try {
				projectFile.delete(true, context.getMonitor());
			} catch (CoreException e) {
			}
		}
		InputStream content = getContent();
		try {
			projectFile.create(new BufferedInputStream(content), true, context.getMonitor());
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