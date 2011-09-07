package org.eclipse.m2e.maveneclipse.handler.additionalbuildcommands;

import java.util.HashMap;
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

	static final String COMPLETE_BUILD_COMMAND_ELEMENT_NAME = "buildCommand";
	static final String NAMED_BUILD_COMMAND_ELEMENT_NAME = "buildcommand";
	static final String NAME_ELEMENT_NAME = "name";
	static final String ARGUMENTS = "arguments";

	/* (non-Javadoc)
	 * @see org.eclipse.m2e.maveneclipse.handler.CommandFactory#createICommand(org.eclipse.core.resources.IProjectDescription, org.eclipse.m2e.maveneclipse.configuration.ConfigurationParameter)
	 */
	public ICommand createICommand(IProjectDescription projectDescription, ConfigurationParameter parameter) {
		ICommand newCommand = null;
		if (COMPLETE_BUILD_COMMAND_ELEMENT_NAME.equals(parameter.getName())) {
			newCommand = projectDescription.newCommand();
			newCommand.setBuilderName(parameter.getChild(NAME_ELEMENT_NAME).getValue());
			Map<String, String> arguments = new HashMap<String, String>();
			List<ConfigurationParameter> configurationParameterArguments = parameter.getChild(ARGUMENTS).getChildren();
			for (ConfigurationParameter argumentParameter : configurationParameterArguments) {
				arguments.put(argumentParameter.getName(), argumentParameter.getValue());
			}
			newCommand.setArguments(arguments);

		} else if (NAMED_BUILD_COMMAND_ELEMENT_NAME.equals(parameter.getName())) {
			newCommand = projectDescription.newCommand();
			newCommand.setBuilderName(parameter.getValue());

		}
		return newCommand;
	}
}
