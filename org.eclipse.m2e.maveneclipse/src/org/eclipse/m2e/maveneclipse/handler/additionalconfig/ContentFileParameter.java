package org.eclipse.m2e.maveneclipse.handler.additionalconfig;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.m2e.maveneclipse.MavenEclipseContext;
import org.eclipse.m2e.maveneclipse.configuration.ConfigurationParameter;

/**
 * {@link FileParameter} that has content directly specified.
 */
class ContentFileParameter extends FileParameter {

	public static final String CHILD_NAME = "content";

	public ContentFileParameter(MavenEclipseContext context, ConfigurationParameter fileParameter) {
		super(context, fileParameter);
	}

	@Override
	protected String getChildName() {
		return CHILD_NAME;
	}

	@Override
	protected InputStream getContent() throws Exception {
		String content = getChildValue();
		return new ByteArrayInputStream(content.getBytes(System.getProperty("file.encoding")));
	}
}