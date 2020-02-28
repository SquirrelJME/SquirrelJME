// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin;

import org.gradle.api.Project;
import org.gradle.api.artifacts.ResolveException;

import java.util.ArrayList;
import java.util.List;

/**
 * Allows SquirrelJME specific parts of modules to be configured.
 *
 * @since 2020/02/15
 */
public class SquirrelJMEPluginConfiguration
{
	/** The current plugin. */
	public SquirrelJMEPlugin currentPlugin;
	
	/** The current project. */
	public Project currentProject;
	
	/** The JavaDoc Error Code. */
	public String javaDocErrorCode;
	
	/** The type of program this is. */
	public JavaMEMidletType swmType =
		JavaMEMidletType.APPLICATION;
	
	/** The name of the MIDlet or LIBlet. */
	public String swmName;
	
	/** The vendor of the MIDlet or LIBlet. */
	public String swmVendor;
	
	/** Ignore in the launcher? */
	public boolean ignoreInLauncher;
	
	/** The configurations this defines. */
	public List<JavaMEConfiguration> definedConfigurations =
		new ArrayList<>();
	
	/** The profiles this defines. */
	public List<JavaMEProfile> definedProfiles =
		new ArrayList<>();
	
	/** The standards defined. */
	public List<JavaMEStandard> definedStandards =
		new ArrayList<>();
	
	/** The main class entry point (optional). */
	public String mainClass;
	
	/** MIDlets that are available for entry. */
	public List<JavaMEMidlet> midlets =
		new ArrayList<>();
	
	/**
	 * Initializes the configuration with the contained project.
	 *
	 * @param __plugin The current plugin.
	 * @param __project The project to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/15
	 */
	public SquirrelJMEPluginConfiguration(SquirrelJMEPlugin __plugin,
		Project __project)
		throws NullPointerException
	{
		if (__plugin == null || __project == null)
			throw new NullPointerException("NARG");
		
		this.currentPlugin = __plugin;
		this.currentProject = __project;
	}
	
	/**
	 * Gets the configuration from the given project.
	 *
	 * @param __proj The project to get the config from.
	 * @return The resulting configuration.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/28
	 */
	public static SquirrelJMEPluginConfiguration configuration(Project __proj)
		throws NullPointerException
	{
		if (__proj == null)
			throw new NullPointerException("No project specified.");
			
		// Find our config
		return __proj.getExtensions()
			.<SquirrelJMEPluginConfiguration>getByType(
				SquirrelJMEPluginConfiguration.class);
	}
	
	/**
	 * Is this a SquirrelJME application?
	 *
	 * @param __project The project to check.
	 * @return If it is an application.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/16
	 */
	public static boolean isApplication(Project __project)
		throws NullPointerException
	{
		return configuration(__project).swmType ==
			JavaMEMidletType.APPLICATION;
	}
}
