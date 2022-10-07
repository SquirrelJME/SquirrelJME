// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import java.util.concurrent.Callable;
import org.gradle.api.Task;

/**
 * Dependencies for the VM library building task.
 *
 * @since 2020/11/21
 */
public class VMLibraryTaskDependencies
	implements Callable<Iterable<Task>>
{
	/** The task this is for. */
	protected final VMLibraryTask task;
	
	/** The virtual machine to target. */
	protected final VMSpecifier vmType;
	
	/**
	 * Initializes the task dependencies.
	 * 
	 * @param __task The task owning this.
	 * @param __vmType The virtual machine to target.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/11/21
	 */
	public VMLibraryTaskDependencies(VMLibraryTask __task,
		VMSpecifier __vmType)
		throws NullPointerException
	{
		if (__task == null || __vmType == null)
			throw new NullPointerException("NARG");
		
		this.task = __task;
		this.vmType = __vmType;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/11/21
	 */
	@Override
	public Iterable<Task> call()
	{
		return this.vmType.processLibraryDependencies(this.task);
	}
}
