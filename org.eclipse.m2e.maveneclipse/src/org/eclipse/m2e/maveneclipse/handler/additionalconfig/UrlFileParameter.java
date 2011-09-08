package org.eclipse.m2e.maveneclipse.handler.additionalconfig;

import java.io.InputStream;
import java.net.URL;

import org.eclipse.m2e.maveneclipse.MavenEclipseContext;
import org.eclipse.m2e.maveneclipse.configuration.ConfigurationParameter;

/**
 * {@link FileParameter} that loads content from a URL.
 */
class UrlFileParameter extends FileParameter {

	public static final String CHILD_NAME = "url";

	public UrlFileParameter(MavenEclipseContext context, ConfigurationParameter fileParameter) {
		super(context, fileParameter);
	}

	@Override
	protected String getChildName() {
		return CHILD_NAME;
	}

	@Override
	protected InputStream getContent() throws Exception {
		String url = getChildValue();
		return new URL(url).openStream();
	}
}