package org.eclipse.m2e.maveneclipse.handler.additionalprojectfacets;

import org.apache.maven.model.Plugin;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.eclipse.jst.j2ee.project.facet.IJ2EEModuleFacetInstallDataModelProperties;
import org.eclipse.jst.j2ee.web.project.facet.WebFacetInstallDataModelProvider;
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

	public Object getFacetConfig(MavenEclipseContext context, IProjectFacetVersion projectFacetVersion) {
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
