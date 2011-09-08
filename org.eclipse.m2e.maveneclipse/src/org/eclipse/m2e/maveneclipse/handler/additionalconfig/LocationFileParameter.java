package org.eclipse.m2e.maveneclipse.handler.additionalconfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.eclipse.m2e.maveneclipse.MavenEclipseContext;
import org.eclipse.m2e.maveneclipse.configuration.ConfigurationParameter;

/**
 * {@link FileParameter} that loads content from a project location.
 */
class LocationFileParameter extends FileParameter {

	public static final String CHILD_NAME = "location";

	private File locationRoot;

	public LocationFileParameter(MavenEclipseContext context, ConfigurationParameter fileParameter, File locationRoot) {
		super(context, fileParameter);
		this.locationRoot = locationRoot;
	}

	@Override
	protected String getChildName() {
		return CHILD_NAME;
	}

	@Override
	protected InputStream getContent() throws Exception {
		String location = getChildValue();
		File locationFile = new File(locationRoot, location);
		if (!locationFile.exists()) {
			throw new IllegalStateException("Location file " + locationFile.getAbsolutePath() + " does not exist");
		}
		return new FileInputStream(locationFile);
	}
}