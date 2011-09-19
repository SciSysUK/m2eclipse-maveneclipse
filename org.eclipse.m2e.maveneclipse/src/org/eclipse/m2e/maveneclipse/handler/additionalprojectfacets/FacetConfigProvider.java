package org.eclipse.m2e.maveneclipse.handler.additionalprojectfacets;

import org.eclipse.m2e.maveneclipse.MavenEclipseContext;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;

/**
 * Strategy interface used to provide additional config for project facets.
 * 
 * @author Phillip Webb
 */
public interface FacetConfigProvider {

	/**
	 * Returns the config for the project facet version.
	 * @param context the context
	 * @param projectFacetVersion the project facet version
	 * @return the config
	 */
	Object getFacetConfig(MavenEclipseContext context, IProjectFacetVersion projectFacetVersion);
}
