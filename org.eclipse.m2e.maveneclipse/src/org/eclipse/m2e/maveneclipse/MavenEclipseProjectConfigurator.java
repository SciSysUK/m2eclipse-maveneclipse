/*
 * Copyright 2000-2011 the original author or authors.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *     http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.eclipse.m2e.maveneclipse;

import org.apache.maven.model.Plugin;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.core.project.configurator.AbstractCustomizableLifecycleMapping;
import org.eclipse.m2e.core.project.configurator.AbstractProjectConfigurator;
import org.eclipse.m2e.core.project.configurator.ProjectConfigurationRequest;
import org.eclipse.m2e.maveneclipse.configuration.MavenEclipseConfigurationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link AbstractProjectConfigurator Project configurator} that applies configuration defined by the
 * <tt>maven-eclipse-plugin</tt> to m2e eclipse projects.
 * 
 * @see MavenEclipseConfigurationHandler
 * @author Alex Clarke
 * @author Phillip Webb
 */
public class MavenEclipseProjectConfigurator extends AbstractProjectConfigurator
{
    private static Logger log = LoggerFactory.getLogger(AbstractCustomizableLifecycleMapping.class);

    private static final String GROUP_ID = "org.apache.maven.plugins";

    private static final String ARTIFACT_ID = "maven-eclipse-plugin";

    private MavenEclipseConfigurationHandler handler = new MavenEclipseConfigurationHandler();

    @Override
    public void configure(ProjectConfigurationRequest request, IProgressMonitor monitor) throws CoreException
    {
        log.debug("Checking for " + GROUP_ID + ":" + ARTIFACT_ID + " plugin");
        for (Plugin plugin : request.getMavenProject().getBuildPlugins())
        {
            if (isMavenEclipsePlugin(plugin))
            {
                log.debug("Configuring m2e project for " + GROUP_ID + ":" + ARTIFACT_ID + " plugin");
                configure(request, monitor, plugin);
            }
        }
    }

    private void configure(ProjectConfigurationRequest request, IProgressMonitor monitor, Plugin plugin)
    {
        MavenEclipseConfiguration configuration = new ConfigurationImpl(plugin);
        MavenEclipseContext context = new MavenEclipseContextImpl(configuration);
        handler.handle(context);
    }

    private boolean isMavenEclipsePlugin(Plugin plugin)
    {
        return GROUP_ID.equals(plugin.getGroupId()) && ARTIFACT_ID.equals(plugin.getArtifactId());
    }

    /**
     * Default implementation of {@link MavenEclipseContext}
     */
    private static class MavenEclipseContextImpl implements MavenEclipseContext
    {
        private MavenEclipseConfiguration configuration;

        public MavenEclipseContextImpl(MavenEclipseConfiguration configuration)
        {
            this.configuration = configuration;
        }

        public MavenEclipseConfiguration getConfiguration()
        {
            return configuration;
        }

        public IProject getProject()
        {
            return null;
        }

        public IProgressMonitor getProgressMonitor()
        {
            return null;
        }

        public MavenProject getMavenProject()
        {
            return null;
        }
    }

    /**
     * Default implementation of {@link MavenEclipseConfiguration}
     */
    private static class ConfigurationImpl implements MavenEclipseConfiguration
    {
        private Xpp3Dom dom;

        public ConfigurationImpl(Plugin plugin)
        {
            this.dom = (Xpp3Dom)plugin.getConfiguration();
        }

        public ConfigurationParamter getParamter(String name)
        {
            Xpp3Dom[] children = dom.getChildren(name);
            if (children.length > 1)
            {
                throw new IllegalStateException("Unexpected number of child parameters defined for '" + name + "'");
            }
            if (children.length == 0)
            {
                return null;
            }
            return new Xpp3DomConfigurationParamter(children[0]);
        }

        public boolean containsParamter(String name)
        {
            return getParamter(name) != null;
        }
    }
}