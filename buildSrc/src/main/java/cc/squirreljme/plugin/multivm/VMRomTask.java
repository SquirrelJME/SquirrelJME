// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import java.nio.file.Path;
import javax.inject.Inject;
import org.gradle.api.DefaultTask;
import org.gradle.api.provider.Provider;

/**
 * This task is responsible for compiling a combined ROM, if a VM uses one.
 *
 * @since 2020/08/23
 */
public class VMRomTask
	extends DefaultTask
	implements VMExecutableTask
{
	/** The source set used. */
	public final String sourceSet;
	
	/** The virtual machine type. */
	public final VMSpecifier vmType;
	
	/**
	 * Initializes the library creation task.
	 * 
	 * @param __sourceSet The source set used.
	 * @param __vmType The virtual machine type.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/07
	 */
	@Inject
	public VMRomTask(String __sourceSet,
		VMSpecifier __vmType)
		throws NullPointerException
	{
		if (__sourceSet == null || __vmType == null)
			throw new NullPointerException("NARG");
		
		this.sourceSet = __sourceSet;
		this.vmType = __vmType;
		
		// Set details of this task
		this.setGroup("squirreljme");
		this.setDescription("Builds the combined ROM.");
		
		// The JAR we are compiling has to be built first
		this.dependsOn(new VMRomDependencies(
			this, __sourceSet, __vmType));
		
		// Only execute this task in certain cases
		this.onlyIf(new CheckRomShouldBuild(__vmType));
		
		// The inputs of this tasks are all the ROM files to merge
		this.getInputs().files(new VMRomInputs(
			this, __sourceSet, __vmType));
		
		// And the output is a primary single file for the ROM
		this.getOutputs().file(this.outputPath());
		
		// Action for performing the actual linkage of the ROM
		this.doLast(new VMRomTaskAction(__sourceSet, __vmType));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/17
	 */
	@Override
	public String getSourceSet()
	{
		return this.sourceSet;
	}
	
	/**
	 * Returns the output path of the archive. 
	 * 
	 * @return The output path.
	 * @since 2020/08/07
	 */
	public final Provider<Path> outputPath()
	{
		return this.getProject().provider(() -> VMHelpers.cacheDir(
			this.getProject(), this.vmType, this.sourceSet).get()
			.resolve(this.vmType.outputRomName(this.sourceSet)));
	}
}
