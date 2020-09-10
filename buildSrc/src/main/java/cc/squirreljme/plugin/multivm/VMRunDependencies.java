// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import java.util.concurrent.Callable;

/**
 * This is the set of dependencies for {@link VMRunTask} which takes all
 * of the dependencies directly needed in order to run the program.
 *
 * @since 2020/08/15
 */
public final class VMRunDependencies
	implements Callable<Iterable<VMLibraryTask>>
{
	/** The task executing under. */
	protected final VMExecutableTask task;
	
	/** The source set working under. */
	protected final String sourceSet;
	
	/** The virtual machine type. */
	protected final VMSpecifier vmType;
	
	/**
	 * Initializes the provider.
	 * 
	 * @param __task The task working under.
	 * @param __sourceSet The current source set.
	 * @param __vmType The virtual machine type.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/15
	 */
	public VMRunDependencies(VMExecutableTask __task, String __sourceSet,
		VMSpecifier __vmType)
		throws NullPointerException
	{
		if (__task == null || __sourceSet == null || __vmType == null)
			throw new NullPointerException("NARG");
		
		this.task = __task;
		this.sourceSet = __sourceSet;
		this.vmType = __vmType;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/08/15
	 */
	@Override
	public final Iterable<VMLibraryTask> call()
	{
		VMExecutableTask task = this.task;
		return VMHelpers.<VMLibraryTask>resolveProjectTasks(
			VMLibraryTask.class, task.getProject(),
			VMHelpers.runClassTasks(this.task.getProject(),
				this.sourceSet, this.vmType));
	}
}
