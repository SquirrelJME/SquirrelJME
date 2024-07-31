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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import org.gradle.api.DefaultTask;
import org.gradle.util.internal.VersionNumber;

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
	public final List<String> cmakeRules;
	
	/**
	 * Initializes the build task.
	 *
	 * @param __source The project source directory.
	 * @param __outputFile The output file we want, this is optional.
	 * @param __rules The rules to execute.
	 * @since 2024/03/15
	 */
	@Inject
	public CMakeBuildTask(Path __source, String __outputFile,
		List<String> __rules)
		throws NullPointerException
	{
		if (__source == null || __rules == null || __rules.isEmpty())
			throw new NullPointerException("NARG");
		
		__rules = new ArrayList<>(__rules);
		for (String rule : __rules)
			if (rule == null)
				throw new NullPointerException("NARG");
		
		// Blank?
		if (__outputFile.isEmpty())
			__outputFile = null;
		
		// Set source for later
		this.cmakeSource = __source;
		this.cmakeRules = Collections.<String>unmodifiableList(__rules);
		
		// Debug
		this.getLogger().lifecycle(
			"CMake Path: " + CMakeUtils.cmakeExePath());
		
		// The build root is based on the task
		Path cmakeBuild = this.getProject().getBuildDir().toPath()
			.resolve("cmake-" + this.getName()).toAbsolutePath();
		this.cmakeBuild = cmakeBuild;
		
		// The desired output we want
		if (__outputFile == null)
			this.cmakeOutFile = null;
		else
			this.cmakeOutFile = this.cmakeBuild.resolve(
				Paths.get(__outputFile)).toAbsolutePath();
		
		// Description
		this.setGroup("squirreljme");
		this.setDescription("Performs a CMake build.");
		
		// Must be run _after_ clean, otherwise bad stuff happens
		this.mustRunAfter(
			this.getProject().getTasks().named("clean"));
		
		// Build action
		CMakeBuildTaskAction action = new CMakeBuildTaskAction();
		
		// Check if out of date
		this.getOutputs().upToDateWhen(new CMakeUpToDateWhen());
		
		// At the minimum we know the base input and outputs
		this.getInputs().dir(this.cmakeSource);
		this.getInputs().files(
			this.cmakeSource.resolve("CMakeLists.txt"));
		this.getOutputs().dirs(this.cmakeBuild);
		this.getOutputs().files(
			this.cmakeBuild.resolve("CMakeCache.txt"));
		if (__outputFile != null)
			this.getOutputs().files(this.cmakeOutFile);
		
		// What to do for this
		this.doFirst(action);
	}
}
