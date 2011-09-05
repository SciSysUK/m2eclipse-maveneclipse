package org.eclipse.m2e.maveneclipse;

import org.apache.maven.project.MavenProject;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.maveneclipse.configuration.MavenEclipseConfiguration;

/**
 * Default implementation of {@link MavenEclipseContext}.
 * 
 * @author Alex Clarke
 * @author Phillip Webb
 */
public class DefaultMavenEclipseContext implements MavenEclipseContext {

	private MavenEclipseConfiguration configuration;

	/**
	 * Create a new {@link DefaultMavenEclipseContext} instance.
	 * @param configuration
	 */
	public DefaultMavenEclipseContext(MavenEclipseConfiguration configuration) {
		if (configuration == null) {
			throw new IllegalArgumentException("Configuration must not be null");
		}
		this.configuration = configuration;
	}

	public MavenEclipseConfiguration getConfiguration() {
		return configuration;
	}

	public IProject getProject() {
		//FIXME
		return null;
	}

	public IProgressMonitor getProgressMonitor() {
		//FIXME
		return null;
	}

	public MavenProject getMavenProject() {
		//FIXME
		return null;
	}
}