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

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.java.archives.Attributes;
import org.gradle.api.java.archives.Manifest;
import org.gradle.jvm.tasks.Jar;

/**
 * Plugin for all SquirrelJME operations that are needed in Gradle in order
 * to provide the correct functionality.
 *
 * @since 2020/02/15
 */
public class SquirrelJMEPlugin
	implements Plugin<Project>
{
	/**
	 * {@inheritDoc}
	 * @since 2020/02/15
	 */
	@Override
	public void apply(Project __project)
	{
		// Add configuration for SquirrelJME
		__project.getExtensions().<SquirrelJMEPluginConfiguration>create(
			"squirreljme", SquirrelJMEPluginConfiguration.class,
			this, __project);
		
		// Inject manifest properties
		Task injectManTask = __project.getTasks().
			create("squirreljmeInjectManifest");
		injectManTask.doLast((Task __task) ->
			this.__configureManifest(__project));
		
		// The manifest must be done before the JAR is build
		__project.getTasks().getByName("jar").dependsOn(injectManTask);
	}
	
	/**
	 * Performs manifest configuration.
	 *
	 * @param __project The project being adjusted.
	 * @since 2020/02/15
	 */
	private void __configureManifest(Project __project)
	{
		// Find our config
		SquirrelJMEPluginConfiguration config = __project.getExtensions()
			.<SquirrelJMEPluginConfiguration>getByType(
				SquirrelJMEPluginConfiguration.class);
		
		// Obtain the manifest to modify
		Jar jar = (Jar)__project.getTasks().getByName("jar");
		Attributes attributes = jar.getManifest().getAttributes();
		
		// Project name is used internally for dependency lookup
		attributes.put("X-SquirrelJME-InternalProjectName",
			__project.getName());
		
		// Configurations defined?
		if (!config.definedConfigurations.isEmpty())
			attributes.put("X-SquirrelJME-DefinedConfigurations",
				__iterableToString(config.definedConfigurations, ' '));
		
		// Profiles defined?
		if (!config.definedProfiles.isEmpty())
			attributes.put("X-SquirrelJME-DefinedProfiles",
				__iterableToString(config.definedProfiles, ' '));
		
		// Standards defined?
		if (!config.definedStandards.isEmpty())
			attributes.put("X-SquirrelJME-DefinedStandards",
				__iterableToString(config.definedStandards, ' '));
		
		// Ignored in the launcher?
		if (config.ignoreInLauncher)
			attributes.put("X-SquirrelJME-NoLauncher", true);
		
		// Main class of entry?
		if (config.mainClass != null)
			attributes.put("Main-Class", config.mainClass);
		
		// Add any defined MIDlets
		int midletId = 1;
		for (JavaMEMidlet midlet : config.midlets)
			attributes.put("MIDlet-" + (midletId++), midlet.toString());
	}
	
	/**
	 * Turns an iterable into a string with the given delimeter.
	 *
	 * @param __it The iteration to convert.
	 * @param __delim The delimiter.
	 * @return The converted string.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/15
	 */
	private static String __iterableToString(Iterable<?> __it,
		char __delim)
		throws NullPointerException
	{
		if (__it == null)
			throw new NullPointerException("NARG");
		
		// Build output string
		StringBuilder sb = new StringBuilder();
		for (Object o : __it)
		{
			// Add delimeter?
			if (sb.length() > 0)
				sb.append(__delim);
			
			// Add object
			sb.append(o);
		}
		
		return sb.toString();
	}
}
