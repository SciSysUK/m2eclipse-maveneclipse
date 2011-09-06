package org.eclipse.m2e.maveneclipse.handler;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.maveneclipse.MavenEclipseContext;
import org.eclipse.m2e.maveneclipse.configuration.ConfigurationParameter;

/**
 * A {@link ConfigurationHandler} that handles the configuration of <tt>additionalprojectnatures</tt> from the
 * <tt>maven-eclipse-plugin</tt>.
 * 
 * @author Alex Clarke
 * @author Phillip Webb
 */
public class AdditionalProjectNaturesConfigurationHandler extends SingleParamterConfigurationHandler {

	private static final String PARAMETER_NAME = "additionalProjectnatures";

	static final String PROJECT_NATURE_NAME = "projectnature";

	public void handle(MavenEclipseContext context, ConfigurationParameter configurationParameter) {
		Set<String> newNatureIDs = new HashSet<String>();
		for (ConfigurationParameter child : configurationParameter.getChildren()) {
			if (PROJECT_NATURE_NAME.equals(child.getName())) {
				newNatureIDs.add(child.getValue());
			}
		}
		try {
			addProjectNatures(context.getProject(), newNatureIDs, context.getMonitor());
		} catch (CoreException e) {
			throw new RuntimeException(e);
		}
	}

	private void addProjectNatures(IProject project, Set<String> newNatureIds, IProgressMonitor monitor)
			throws CoreException {
		IProjectDescription projectDescription = project.getDescription();
		newNatureIds.addAll(Arrays.asList(projectDescription.getNatureIds()));
		projectDescription.setNatureIds(newNatureIds.toArray(new String[newNatureIds.size()]));
		project.setDescription(projectDescription, monitor);
	}

	@Override
	protected String getParamterName() {
		return PARAMETER_NAME;
	}

}
