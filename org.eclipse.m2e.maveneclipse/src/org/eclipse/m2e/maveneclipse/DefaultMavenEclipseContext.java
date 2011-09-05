package org.eclipse.m2e.maveneclipse;

import org.apache.maven.project.MavenProject;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.maveneclipse.configuration.MavenEclipseConfiguration;

/**
 * Default implementation of {@link MavenEclipseContext}
 */
public class DefaultMavenEclipseContext implements MavenEclipseContext {
	private MavenEclipseConfiguration configuration;

	public DefaultMavenEclipseContext(MavenEclipseConfiguration configuration) {
		this.configuration = configuration;
	}

	public MavenEclipseConfiguration getConfiguration() {
		return configuration;
	}

	public IProject getProject() {
		return null;
	}

	public IProgressMonitor getProgressMonitor() {
		return null;
	}

	public MavenProject getMavenProject() {
		return null;
	}
}