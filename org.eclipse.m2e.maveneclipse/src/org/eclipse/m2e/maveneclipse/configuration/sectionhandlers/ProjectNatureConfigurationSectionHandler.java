package org.eclipse.m2e.maveneclipse.configuration.sectionhandlers;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.StringUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.maveneclipse.MavenEclipseContext;
import org.eclipse.m2e.maveneclipse.configuration.ConfigurationSectionHandler;

public class ProjectNatureConfigurationSectionHandler implements ConfigurationSectionHandler {

	protected static final String PROJECT_NATURES_PROPERTY_NAME = "eclipse.project.natures";

	private static final Map<String, String> ALIASES;
	static {
		ALIASES = new HashMap<String, String>();
		ALIASES.put("spring", "org.springframework.ide.eclipse.core.springnature");
	}

	public boolean canHandle(MavenEclipseContext context) {
		return getProjectNaturesProperty(context.getMavenProject()) != null;
	}

	public void handle(MavenEclipseContext context) {
		if (!canHandle(context)) {
			throw new IllegalArgumentException("Unable to handle context");
		}

		MavenProject mavenProject = context.getMavenProject();
		NatureProperty natureProperty = new NatureProperty(mavenProject.getBasedir(),
				getProjectNaturesProperty(mavenProject));

		for (String natureId : natureProperty.getIds()) {
			try {
				addProjectNature(context.getProject(), natureId, context.getProgressMonitor());
			} catch (CoreException e) {
				throw new RuntimeException(e);
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

	private String getProjectNaturesProperty(MavenProject mavenProject) {
		return (String) mavenProject.getProperties().get(PROJECT_NATURES_PROPERTY_NAME);
	}

	static class NatureProperty {

		private static final Pattern CONDITIONAL_ITEM_PATTERN = Pattern.compile("(.*)\\[(.*)\\]");

		private List<String> ids = new ArrayList<String>();

		private File baseDir;

		public NatureProperty(File baseDir, String property) {
			this.baseDir = baseDir;
			parse(property);
		}

		private void parse(String property) {
			if (StringUtils.isEmpty(property)) {
				return;
			}
			String[] items = property.split(",");
			for (String item : items) {
				parseItem(item);
			}
		}

		private void parseItem(String item) {
			item = item.trim();

			// Get condition if there is one
			String condition = "";
			Matcher matcher = CONDITIONAL_ITEM_PATTERN.matcher(item);
			if (matcher.matches()) {
				item = matcher.group(1);
				condition = matcher.group(2);
			}

			// Deal with alias
			if (ALIASES.containsKey(item)) {
				item = ALIASES.get(item);
			}

			// Add only if condition is met
			if (isConditionMet(condition)) {
				ids.add(item);
			}
		}

		private boolean isConditionMet(String condition) {
			if (StringUtils.isEmpty(condition)) {
				return true;
			}
			return new File(baseDir, condition).isFile();
		}

		public List<String> getIds() {
			return ids;
		}
	}
}
