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
 * Used to test the virtual machine, generates test results from the run.
 *
 * @since 2020/08/07
 */
public class MultiVMTestTask
	extends DefaultTask
	implements MultiVMExecutableTask
{
	/** The source set used. */
	protected final String sourceSet;
	
	/** The virtual machine type. */
	protected final VirtualMachineSpecifier vmType;
	
	/**
	 * Initializes the task.
	 * 
	 * @param __sourceSet The source set to use.
	 * @param __vmType The virtual machine type.
	 * @param __libTask The task used to create libraries, this may be directly
	 * depended upon.
	 * @since 2020/08/07
	 */
	@Inject
	public MultiVMTestTask(String __sourceSet,
		VirtualMachineSpecifier __vmType, MultiVMLibraryTask __libTask)
		throws NullPointerException
	{
		if (__sourceSet == null || __vmType == null || __libTask == null)
			throw new NullPointerException("NARG");
			
		// These are used at the test stage
		this.sourceSet = __sourceSet;
		this.vmType = __vmType;
		
		// Set details of this task
		this.setGroup("squirreljme");
		this.setDescription("Runs the various automated tests.");
		
		// Depends on the library to exist first
		this.dependsOn(this.getProject().provider(
			new VMRunDependencies(this, __sourceSet, __vmType)));
		
		// Additionally this depends on the emulator backend to be available
		this.dependsOn(new VMEmulatorDependencies(this, __vmType));
		
		// Only run if there are actual tests to run
		this.onlyIf(new CheckForTests(__sourceSet));
		
		// Performs the action of the task
		this.doLast(new MultiVMTestTaskAction(__sourceSet, __vmType));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/08/21
	 */
	@Override
	public String getSourceSet()
	{
		return this.sourceSet;
	}
}
