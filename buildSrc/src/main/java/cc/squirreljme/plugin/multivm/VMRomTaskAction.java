// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import org.gradle.api.Action;
import org.gradle.api.Task;

/**
 * This performs the actual work that is needed to build the ROM.
 *
 * @since 2020/08/23
 */
public class VMRomTaskAction
	implements Action<Task>
{
	/** The source set used. */
	protected final String sourceSet;
	
	/** The virtual machine to generate for. */
	protected final VMSpecifier vmType;
	
	/**
	 * Initializes the task.
	 * 
	 * @param __sourceSet The source set used.
	 * @param __vmType The VM to make a ROM for.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/23
	 */
	public VMRomTaskAction(String __sourceSet,
		VMSpecifier __vmType)
		throws NullPointerException
	{
		if (__sourceSet == null || __vmType == null)
			throw new NullPointerException("NARG");
		
		this.sourceSet = __sourceSet;
		this.vmType = __vmType;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/08/23
	 */
	@Override
	public void execute(Task __task)
		throws NullPointerException
	{
		throw new Error("TODO -- MultiVMRomTaskAction");
	}
}
