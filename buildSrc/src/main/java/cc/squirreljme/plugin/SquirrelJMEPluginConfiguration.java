// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin;

import cc.squirreljme.plugin.multivm.VMHelpers;
import cc.squirreljme.plugin.swm.JavaMEConfiguration;
import cc.squirreljme.plugin.swm.JavaMEMidlet;
import cc.squirreljme.plugin.swm.JavaMEMidletType;
import cc.squirreljme.plugin.swm.JavaMEProfile;
import cc.squirreljme.plugin.swm.JavaMEStandard;
import cc.squirreljme.plugin.util.UnassistedLaunchEntry;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.gradle.api.Project;
import org.gradle.api.UnknownDomainObjectException;
import org.iban4j.CountryCode;

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
	
	/** The original vendor of the API. */
	public String originalApiVendor;
	
	/** The URL to the original vendor's homepage. */
	public String originalApiVendorUrl;
	
	/** The country of origin for the original vendor. */
	public CountryCode originalApiVendorCountry;
	
	/** Ignore in the launcher? */
	public boolean ignoreInLauncher;
	
	/** The configurations this defines. */
	public Set<JavaMEConfiguration> definedConfigurations =
		new LinkedHashSet<>();
	
	/** The profiles this defines. */
	public Set<JavaMEProfile> definedProfiles =
		new LinkedHashSet<>();
	
	/** The standards defined. */
	public Set<JavaMEStandard> definedStandards =
		new LinkedHashSet<>();
	
	/** The main class entry point (optional). */
	public String mainClass;
	
	/** MIDlets that are available for entry. */
	public List<JavaMEMidlet> midlets =
		new ArrayList<>();
	
	/** Tags for the module, used for ROM selection. */
	public List<String> tags =
		new ArrayList<>();
	
	/** Is this a bootloader? */
	public boolean isBootLoader;
	
	/** System properties to be used by tests only. */
	public Map<String, String> testSystemProperties =
		new LinkedHashMap<>();
	
	/** Do not run these tests in parallel. */
	public boolean noParallelTests;
	
	/** Projects to have optional dependencies on. */
	public List<Project> optionalDependencies =
		new ArrayList<>();
	
	/** For tests, projects to have optional dependencies on. */
	public List<Project> optionalDependenciesTest =
		new ArrayList<>();
	
	/** For test fixtures, projects to have optional dependencies on. */
	public List<Project> optionalDependenciesTestFixtures =
		new ArrayList<>();
	
	/** Do not emit {@code Microedition-Configuration}? */
	public boolean noEmitConfiguration;
	
	/** Is this the main launcher? */
	public boolean isMainLauncher;
	
	/** The main class for the boot loader. */
	public String bootLoaderMainClass;
	
	/** Extra proguard options. */
	public List<String> proGuardOptions =
		new ArrayList<>();
	
	/** Extra proguard options, for testing. */
	public List<String> proGuardOptionsTest =
		new ArrayList<>();
	
	/** Extra proguard options, for test fixtures. */
	public List<String> proGuardOptionsTestFixtures =
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
	 * Returns the primary launch entry.
	 * 
	 * @return The primary launch entry.
	 * @since 2021/08/22
	 */
	public final UnassistedLaunchEntry primaryEntry()
	{
		JavaMEMidlet firstMidlet = null;
		List<JavaMEMidlet> midlets = this.midlets;
		if (midlets != null && !midlets.isEmpty())
			firstMidlet = midlets.get(0);
		
		return VMHelpers.unassistedLaunch(this, firstMidlet);
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
	 * Gets the configuration from the given project.
	 *
	 * @param __proj The project to get the config from.
	 * @return The resulting configuration.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/03/07
	 */
	public static SquirrelJMEPluginConfiguration configurationOrNull(
		Project __proj)
		throws NullPointerException
	{
		try
		{
			return SquirrelJMEPluginConfiguration.configuration(__proj);
		}
		catch (UnknownDomainObjectException e)
		{
			return null;
		}
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
		return SquirrelJMEPluginConfiguration
			.configuration(__project).swmType == JavaMEMidletType.APPLICATION;
	}
}
