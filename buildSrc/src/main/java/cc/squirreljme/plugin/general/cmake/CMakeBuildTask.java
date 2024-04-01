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
import org.gradle.api.DefaultTask;

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
	
	/** The output file we want. */
	public final Path cmakeOutFile;
	
	/** The rule to use. */
	public final String cmakeRule;
	
	/**
	 * Initializes the build task.
	 *
	 * @param __source The project source directory.
	 * @param __outputFile The output file we want, this is optional.
	 * @param __rule The rule to execute.
	 * @since 2024/03/15
	 */
	@Inject
	public CMakeBuildTask(Path __source, String __outputFile,
		String __rule)
		throws NullPointerException
	{
		if (__source == null || __rule == null)
			throw new NullPointerException("NARG");
		
		// Set source for later
		this.cmakeSource = __source;
		this.cmakeRule = __rule;
		
		// The build root is based on the task
		this.cmakeBuild = this.getProject().getBuildDir().toPath()
			.resolve("cmake-" + this.getName());
		
		// The desired output we want
		if (__outputFile == null)
			this.cmakeOutFile = null;
		else
			this.cmakeOutFile = this.cmakeBuild.resolve(
				Paths.get(__outputFile));
		
		// Description
		this.setGroup("squirreljme");
		this.setDescription("Performs a CMake build.");
		
		// Check if out of date
		this.getOutputs().upToDateWhen(new CMakeUpToDateWhen());
		
		// At the minimum we know the base input and outputs
		this.getInputs().dir(this.cmakeSource);
		this.getInputs().files(
			this.cmakeSource.resolve("CMakeLists.txt"));
		this.getOutputs().dirs(this.cmakeBuild);
		if (__outputFile != null)
			this.getOutputs().files(this.cmakeOutFile);
		
		// What to do for this
		this.doFirst(new CMakeBuildTaskAction());
	}
}
