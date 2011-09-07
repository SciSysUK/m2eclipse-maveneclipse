package org.eclipse.m2e.maveneclipse.handler.additionalbuildcommands;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.m2e.maveneclipse.configuration.ConfigurationParameter;

/**
 * A {@link BuildCommandFactory} is responsible for creation and setup of {@link ICommand} for a
 * {@link ConfigurationParameter} with the assistance of an {@link IProjectDescription}.
 * 
 * @author Alex Clarke
 * @author Phillip Webb
 */
public interface BuildCommandFactory {

	/**
	 * Create a {@link ICommand} for the provided {@link ConfigurationParameter}. Created {@link ICommand}s are not
	 * added to the <tt>projectDescription</tt>.
	 * 
	 * @param projectDescription the {@link IProjectDescription} used when creating the {@link ICommand}
	 * @param parameter the {@link ConfigurationParameter} containing the details of the command.
	 * @return a new {@link ICommand} or <tt>null</tt> if no new {@link ICommand} could be created.
	 */
	public ICommand createCommand(IProjectDescription projectDescription, ConfigurationParameter parameter);

}