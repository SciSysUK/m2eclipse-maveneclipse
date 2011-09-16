/*
 * Copyright 2000-2011 the original author or authors.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * 
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.m2e.maveneclipse.handler.additionalprojectnatures;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.m2e.maveneclipse.MavenEclipseContext;
import org.eclipse.m2e.maveneclipse.configuration.ConfigurationParameter;
import org.eclipse.m2e.maveneclipse.handler.ConfigurationHandler;
import org.eclipse.m2e.maveneclipse.handler.SingleParameterConfigurationHandler;

/**
 * A {@link ConfigurationHandler} that handles <tt>additionalprojectnatures</tt> from the <tt>maven-eclipse-plugin</tt>
 * configuration.
 * 
 * @author Alex Clarke
 * @author Phillip Webb
 */
public class AdditionalProjectNaturesConfigurationHandler
    extends SingleParameterConfigurationHandler
{

    static final String PROJECT_NATURE_NAME = "projectnature";

    protected IExtensionRegistry getExtensionRegistry()
    {
        return Platform.getExtensionRegistry();
    }

    @Override
    protected String getParamterName()
    {
        return "additionalProjectnatures";
    }

    public void handle( MavenEclipseContext context, ConfigurationParameter configurationParameter )
        throws Exception
    {
        Set<String> additionalNatureIDs = new LinkedHashSet<String>();
        for ( ConfigurationParameter child : configurationParameter.getChildren() )
        {
            if ( PROJECT_NATURE_NAME.equals( child.getName() ) )
            {
                additionalNatureIDs.add( child.getValue() );
            }
        }
        if ( !additionalNatureIDs.isEmpty() )
        {
            addAdditionalProjectNatures( context.getProject(), additionalNatureIDs, context.getMonitor() );
        }
    }

    private void addAdditionalProjectNatures( IProject project, Set<String> additionalNatureIDs,
                                              IProgressMonitor monitor )
        throws CoreException
    {
        IExtensionRegistry extensionRegistry = getExtensionRegistry();
        IProjectDescription projectDescription = project.getDescription();
        Set<String> natureIds = new LinkedHashSet<String>();
        natureIds.addAll( Arrays.asList( projectDescription.getNatureIds() ) );
        for ( String additionalNatureID : additionalNatureIDs )
        {
            IExtension extension =
                extensionRegistry.getExtension( ResourcesPlugin.PI_RESOURCES, ResourcesPlugin.PT_NATURES,
                                                additionalNatureID );
            if ( extension != null )
            {
                natureIds.add( additionalNatureID );
            }
        }
        projectDescription.setNatureIds( natureIds.toArray( new String[natureIds.size()] ) );
        project.setDescription( projectDescription, monitor );
    }
}
