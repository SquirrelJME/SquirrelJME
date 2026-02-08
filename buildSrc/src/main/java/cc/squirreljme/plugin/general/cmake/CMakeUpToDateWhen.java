// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.general.cmake;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.gradle.api.Task;
import org.gradle.api.specs.Spec;

/**
 * Check to determine if the CMake build should be successful or not.
 *
 * @since 2024/03/15
 */
public class CMakeUpToDateWhen
	implements Spec<Task>
{
	/** The rules to check. */
	private final List<String> rules;
	
	/**
	 * Initializes the up-to-date check.
	 *
	 * @param __rules The rules to check
	 * @since 2026/02/01
	 */
	public CMakeUpToDateWhen(List<String> __rules)
	{
		this.rules = (__rules == null ? null :
			new ArrayList<>(__rules));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/15
	 */
	@Override
	public boolean isSatisfiedBy(Task __task)
	{
		CMakeBuildTask cmakeTask = (CMakeBuildTask)__task;
		
		// Configuration is needed?
		if (CMakeUtils.configureNeeded(cmakeTask))
		{
			__task.getLogger().warn(
				"CMake Configuring: Configure is needed!");
			return false;
		}
		
		// Cache directory does not exist?
		if (!Files.isDirectory(cmakeTask.cmakeBuild))
		{
			__task.getLogger().warn("CMake Configuring: Missing BuildDir");
			return false;
		}
		
		// Output is specified but does not exist?
		if (cmakeTask.cmakeOutFile != null &&
			!Files.exists(cmakeTask.cmakeOutFile))
		{
			__task.getLogger().warn("CMake Configuring: Missing Output");
			return false;
		}
		
		// No rules? Success if so!
		if (this.rules == null || this.rules.isEmpty())
		{
			__task.getLogger().warn(
				"CMake Configuring: UP-TO-DATE (No rules)!");
			return true;
		}
		
		// Poke the native build system to see if it is out of date
		// TODO: Checking this way just does not work all that reliably
		// TODO: it would be far better to use the CMake file-api at some
		// TODO: point
		try
		{
			// Load CMake cache
			Map<String, String> cmakeCache = CMakeUtils.loadCache(
				cmakeTask.cmakeBuild);
			
			// Determine arguments
			List<String> args = new ArrayList<>();
			args.addAll(Arrays.asList("--build",
				cmakeTask.cmakeBuild.toString(),
				"--"));
			
			// Which generator is being used?
			String generator = cmakeCache.get("CMAKE_GENERATOR:INTERNAL");
			switch (generator)
			{
				case "MSYS Makefiles":
				case "MinGW Makefiles":
				case "Unix Makefiles":
					args.add("-q");
					args.add("-d");
					args.addAll(this.rules);
					break;
					
				case "NMake Makefiles":
					args.add("/Q");
					args.addAll(this.rules);
					break;
					
					// Force a rebuild since we cannot use these
				default:
					__task.getLogger().warn(String.format(
						"CMake Configuring: Generator %s not known.",
						generator));
					return false;
			}
			
			// Check via the build system?
			if (!args.isEmpty())
				if (CMakeUtils.cmakeExecute(
					cmakeTask.cmakeBuild,
					__task.getLogger(),
					"makefile-check.log",
					__task.getProject().getBuildDir().toPath(),
					args.toArray(new String[args.toArray().length])) != 0)
				{
					__task.getLogger().warn(String.format(
						"CMake Configuring: %s Out-of-Date %s",
						this.rules, generator));
					return false;
				}
		}
		
		// If this occurs then assume out of date
		catch (IOException|RuntimeException __e)
		{
			__task.getLogger().warn(
				"CMake Configuring: Exception", __e);
			return false;
		}
		
		// Otherwise, success!
		__task.getLogger().warn(
			"CMake Configuring: UP-TO-DATE!");
		return true;
	}
}
