// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.general.cmake;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.gradle.api.Action;
import org.gradle.api.Task;

/**
 * Actual build task for CMake projects.
 *
 * @since 2024/03/15
 */
public class CMakeBuildTaskAction
	implements Action<Task>
{
	public void configure(Task __task)
		throws IOException
	{
		CMakeBuildTask from = (CMakeBuildTask)__task;
		
		Path cmakeBuild = from.cmakeBuild;
		Path cmakeSource = from.cmakeSource;
		
		// Make sure the output build directory exists
		Files.createDirectories(cmakeBuild);
		
		// Configure CMake first before we continue with anything
		CMakeUtils.cmakeExecute(__task.getLogger(),
			"configure", __task.getProject().getBuildDir().toPath(),
			"-S", cmakeSource.toAbsolutePath().toString(),
			"-B", cmakeBuild.toAbsolutePath().toString());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/15
	 */
	@Override
	public void execute(Task __task)
	{
		CMakeBuildTask from = (CMakeBuildTask)__task;
		
		Path cmakeBuild = from.cmakeBuild;
		
		try
		{
			// Make sure the output build directory exists
			Files.createDirectories(cmakeBuild);
			
			// Then perform the actual build, for each rule
			for (String cmakeRule : from.cmakeRules)
			{
				// Does the cache seem invalid? Then configure first
				if (!Files.isDirectory(cmakeBuild) ||
					!Files.exists(cmakeBuild.resolve("CMakeCache.txt")))
					this.configure(__task);
				
				// Run normal CMake build
				CMakeUtils.cmakeExecute(__task.getLogger(),
					"build-" + cmakeRule,
					__task.getProject().getBuildDir().toPath(),
					"--build",
					cmakeBuild.toAbsolutePath().toString(),
					"-t", cmakeRule);
			}
			
			// Was the output file even created?
			if (from.cmakeOutFile != null && !Files.exists(from.cmakeOutFile))
				throw new FileNotFoundException(
					"Could not find output file: " +
						from.cmakeOutFile);
		}
		catch (IOException __e)
		{
			throw new RuntimeException("CMake Build Failed.", __e);
		}
	}
}
