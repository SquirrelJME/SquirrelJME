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
			return false;
		
		// Cache directory does not exist?
		if (!Files.isDirectory(cmakeTask.cmakeBuild))
			return false;
		
		// Output is specified but does not exist?
		if (cmakeTask.cmakeOutFile != null &&
			!Files.exists(cmakeTask.cmakeOutFile))
			return false;
		
		// Poke the native build system to see if it is out of date
		try
		{
			// Load CMake cache
			Map<String, String> cmakeCache = CMakeUtils.loadCache(
				cmakeTask.cmakeBuild);
			
			// Which generator is being used?
			String generator = cmakeCache.get("CMAKE_GENERATOR:INTERNAL");
			switch (generator)
			{
				case "MSYS Makefiles":
				case "MinGW Makefiles":
				case "Unix Makefiles":
					if (CMakeUtils.cmakeExecutePipe(false,
						null, null, null,
						"up-to-date", "--", "-q") != 0)
						return false;
					break;
					
				case "NMake Makefiles":
					if (CMakeUtils.cmakeExecutePipe(false,
						null, null, null,
						"up-to-date", "--", "/Q") != 0)
						return false;
					break;
			}
		}
		
		// If this occurs then assume out of date
		catch (IOException __e)
		{
			__task.getLogger().warn("Could not determine if out of date.",
				__e);
			return false;
		}
		
		// Otherwise, success!
		return true;
	}
}
