// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.general.cmake;

import java.nio.file.Path;
import java.nio.file.Paths;
import javax.inject.Inject;
import lombok.Getter;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputFile;

/**
 * Task for building CMake projects.
 *
 * @since 2024/03/15
 */
public class CMakeBuildTask
	extends DefaultTask
{
	/** The CMake project root. */
	public final Path cmakeSource;
	
	/** CMake build path, where output goes. */
	public final Path cmakeBuild;
	
	/** The output library we want. */
	public final Path cmakeOutLibrary;
	
	/** The rule to use. */
	public final String cmakeRule;
	
	/**
	 * Initializes the build task.
	 *
	 * @param __source The project source directory.
	 * @param __outputLib The output library we want.
	 * @param __rule The rule to execute.
	 * @since 2024/03/15
	 */
	@Inject
	public CMakeBuildTask(Path __source, String __outputLib,
		String __rule)
		throws NullPointerException
	{
		if (__source == null || __outputLib == null || __rule == null)
			throw new NullPointerException("NARG");
		
		// Set source for later
		this.cmakeSource = __source;
		this.cmakeRule = __rule;
		
		// The build root is based on the task
		this.cmakeBuild = this.getProject().getBuildDir().toPath()
			.resolve("cmake");
		
		// The desired library we want
		this.cmakeOutLibrary = this.cmakeBuild.resolve(
			Paths.get(System.mapLibraryName(__outputLib)));
		
		// Description
		this.setGroup("squirreljme");
		this.setDescription("Performs a CMake build.");
		
		// Only build if CMake is available
		/*this.onlyIf(new CMakeOnlyIf());*/
		
		// Check if out of date
		this.getOutputs().upToDateWhen(new CMakeUpToDateWhen());
		
		// At the minimum we know the base input and outputs
		this.getInputs().dir(this.cmakeSource);
		this.getInputs().files(
			this.cmakeSource.resolve("CMakeLists.txt"));
		this.getOutputs().dirs(this.cmakeBuild);
		this.getOutputs().files(this.cmakeOutLibrary);
		
		// What to do for this
		this.doFirst(new CMakeBuildTaskAction());
	}
}
