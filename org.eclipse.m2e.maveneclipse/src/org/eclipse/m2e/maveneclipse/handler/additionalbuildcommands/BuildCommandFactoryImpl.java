package org.eclipse.m2e.maveneclipse.handler.additionalbuildcommands;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.m2e.maveneclipse.configuration.ConfigurationParameter;

/**
 * {@link BuildCommandFactory} class for the creation and population of {@link ICommand}s.
 * 
 * @author Alex Clarke
 * @author Phillip Webb
 */
public class BuildCommandFactoryImpl implements BuildCommandFactory {

	static final String BUILD_COMMAND_ELEMENT_NAME = "buildCommand";
	static final String LEGACY_BUILD_COMMAND_ELEMENT_NAME = "buildcommand";
	static final String NAME_ELEMENT_NAME = "name";
	static final String ARGUMENTS_ELEMENT_NAME = "arguments";

	public ICommand createCommand(IProjectDescription projectDescription, ConfigurationParameter parameter) {
		if (BUILD_COMMAND_ELEMENT_NAME.equals(parameter.getName())) {
			return createBuildCommand(projectDescription, parameter);
		}
		if (LEGACY_BUILD_COMMAND_ELEMENT_NAME.equals(parameter.getName())) {
			return createLegacyBuildCommand(projectDescription, parameter);
		}
		return null;
	}

	private ICommand createBuildCommand(IProjectDescription projectDescription, ConfigurationParameter parameter) {
		ICommand command = projectDescription.newCommand();

		command.setBuilderName(parameter.getChild(NAME_ELEMENT_NAME).getValue());

		Map<String, String> arguments = new LinkedHashMap<String, String>();
		List<ConfigurationParameter> configurationParameterArguments = parameter.getChild(ARGUMENTS_ELEMENT_NAME)
				.getChildren();
		for (ConfigurationParameter argumentParameter : configurationParameterArguments) {
			arguments.put(argumentParameter.getName(), argumentParameter.getValue());
		}
		command.setArguments(arguments);

		return command;
	}

	private ICommand createLegacyBuildCommand(IProjectDescription projectDescription, ConfigurationParameter parameter) {
		ICommand command = projectDescription.newCommand();
		command.setBuilderName(parameter.getValue());
		return command;
	}

}
