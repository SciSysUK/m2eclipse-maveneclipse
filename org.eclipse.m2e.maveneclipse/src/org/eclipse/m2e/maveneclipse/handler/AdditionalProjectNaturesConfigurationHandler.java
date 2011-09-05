package org.eclipse.m2e.maveneclipse.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.plexus.util.StringUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.maveneclipse.MavenEclipseContext;
import org.eclipse.m2e.maveneclipse.configuration.ConfigurationParameter;

public class AdditionalProjectNaturesConfigurationHandler extends SingleParamterConfigurationHandler {

	static final String PARAMETER_NAME = "additionalProjectnatures";

	private static final Map<String, String> ALIASES;
	static {
		ALIASES = new HashMap<String, String>();
		ALIASES.put("spring", "org.springframework.ide.eclipse.core.springnature");
	}

	public void handle(MavenEclipseContext context, ConfigurationParameter configurationParameter) {
		if (!canHandle(context)) {
			throw new IllegalArgumentException("Unable to handle context");
		}

		for (ConfigurationParameter child : configurationParameter.getChildren()) {
			if (child.getName().equals("projectnature")) {
				try {
					addProjectNature(context.getProject(), child.getValue(), context.getProgressMonitor());
				} catch (CoreException e) {
					throw new RuntimeException(e);
				}
			}

		}
	}

	private void addProjectNature(IProject project, String natureId, IProgressMonitor monitor) throws CoreException {
		if (StringUtils.isEmpty(natureId)) {
			return;
		}
		if (!project.hasNature(natureId)) {
			IProjectDescription projectDescription = project.getDescription();
			List<String> natureIds = new ArrayList<String>();
			natureIds.addAll(Arrays.asList(projectDescription.getNatureIds()));
			natureIds.add(natureId);
			projectDescription.setNatureIds(natureIds.toArray(new String[natureIds.size()]));
			project.setDescription(projectDescription, monitor);
		}
	}

	@Override
	protected String getParamterName() {
		return PARAMETER_NAME;
	}

}
