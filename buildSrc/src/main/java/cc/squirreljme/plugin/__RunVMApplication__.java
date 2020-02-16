// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin;

import org.gradle.api.Project;

/**
 * Helper class for running an application within a VM.
 *
 * @since 2020/02/16
 */
abstract class __RunVMApplication__
	implements Runnable
{
	/** The project to run. */
	protected final Project project;
	
	/** The virtual machine to run. */
	protected final String vmName;
	
	/**
	 * Initializes the runner.
	 *
	 * @param __project The project to be ran.
	 * @param __vm The VM to run.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/16
	 */
	__RunVMApplication__(Project __project, String __vm)
		throws NullPointerException
	{
		if (__project == null || __vm == null)
			throw new NullPointerException("No project passed.");
		
		this.project = __project;
		this.vmName = __vm;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/02/16
	 */
	@Override
	public final void run()
	{
		throw new Error("TODO");
	}
}

