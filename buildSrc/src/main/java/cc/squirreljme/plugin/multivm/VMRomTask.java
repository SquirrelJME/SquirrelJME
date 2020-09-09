// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import javax.inject.Inject;
import org.gradle.api.DefaultTask;

/**
 * This task is responsible for compiling a ROM, if a VM uses one.
 *
 * @since 2020/08/23
 */
public class VMRomTask
	extends DefaultTask
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
		this.getInputs().file(new VMRomInputs(
			this, __sourceSet, __vmType));
		
		// And the output is a primary single file for the ROM
		this.getOutputs().file(new VMRomOutputs(
			this, __sourceSet, __vmType));
		
		// Action for performing the actual linkage of the ROM
		this.doLast(new VMRomTaskAction(__sourceSet, __vmType));
	}
}
