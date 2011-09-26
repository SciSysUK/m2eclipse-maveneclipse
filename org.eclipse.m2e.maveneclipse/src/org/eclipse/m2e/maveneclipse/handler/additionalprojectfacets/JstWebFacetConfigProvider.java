package org.eclipse.m2e.maveneclipse.handler.additionalprojectfacets;

import org.apache.maven.model.Plugin;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.j2ee.project.facet.IJ2EEModuleFacetInstallDataModelProperties;
import org.eclipse.jst.j2ee.web.project.facet.WebFacetInstallDataModelProvider;
import org.eclipse.jst.j2ee.web.project.facet.WebFacetInstallDelegate;
import org.eclipse.m2e.core.project.MavenProjectUtils;
import org.eclipse.m2e.maveneclipse.MavenEclipseContext;
import org.eclipse.wst.common.frameworks.datamodel.DataModelFactory;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;

/**
 * {@link FacetConfigProvider} for <tt>jst.web</tt> facets. Sets the web source directory to prevent superfluous folders
 * being created.
 * 
 * @author Phillip Webb
 */
public class JstWebFacetConfigProvider implements FacetConfigProvider {

	private static final String DEFAULT_WAR_SOURCE = "/src/main/webapp";

	public void prepare(MavenEclipseContext context) throws CoreException {
		setJavaOutputLocation(context);
	}

	/**
	 * Sets the java output directory. This is required here to ensure that the
	 * correct <tt>java-output-path</tt> is set by the {@link WebFacetInstallDelegate}. 
	 * @param context the context
	 * @throws JavaModelException
	 */
	private void setJavaOutputLocation(MavenEclipseContext context)
			throws JavaModelException {
		IProject project = context.getProject();
		IJavaProject javaProject = JavaCore.create(project);
		if (javaProject != null) {
			String outputDirectory = context.getMavenProject().getBuild().getOutputDirectory();
			IPath outputPath = MavenProjectUtils.getProjectRelativePath(project, outputDirectory);
			IFolder outputFolder = project.getFolder(outputPath);
			javaProject.setOutputLocation(outputFolder.getFullPath(), context.getMonitor());
		}
	}

	public Object getFacetConfig(MavenEclipseContext context, IProjectFacetVersion projectFacetVersion) throws CoreException {
		String configFolder = DEFAULT_WAR_SOURCE;
		Plugin plugin = context.getMavenProject().getPlugin("org.apache.maven.plugins:maven-war-plugin");
		if (plugin != null) {
			Xpp3Dom configuration = (Xpp3Dom) plugin.getConfiguration();
			if (configuration != null) {
				Xpp3Dom[] warSourceConfiguration = configuration.getChildren("warSourceDirectory");
				if (warSourceConfiguration != null && warSourceConfiguration.length > 0) {
					configFolder = warSourceConfiguration[0].getValue();
					configFolder = MavenProjectUtils.getProjectRelativePath(context.getProject(), configFolder)
							.toOSString();
				}
			}
		}
		IDataModel webFacetConfig = DataModelFactory.createDataModel(new WebFacetInstallDataModelProvider());
		webFacetConfig.setProperty(IJ2EEModuleFacetInstallDataModelProperties.CONFIG_FOLDER, configFolder);
		return webFacetConfig;
	}
}
