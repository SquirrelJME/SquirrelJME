// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.pvmjvm;

/**
 * This represents a process from within the paravirtual machine.
 *
 * @since 2016/06/16
 */
public class PVMProcess
{
	/** The owning paravirtual machine. */
	protected final PVM pvm;
	
	/** The class loader for this process. */
	protected final PVMClassLoader classloader;
	
	/** The process identifier. */
	protected final int pid;
	
	/**
	 * Initializes the para-virtual machine process.
	 *
	 * @param __pvm The owning process.
	 * @param __pid The identifier of this process.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/16
	 */
	public PVMProcess(PVM __pvm, int __pid)
		throws NullPointerException
	{
		// Check
		if (__pvm == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.pvm = __pvm;
		this.pid = __pid;
		
		// Setup class loader
		PVMClassLoader pcl = new PVMClassLoader(this);
		this.classloader = pcl;
	}
	
	/**
	 * Returns the process ID of this process.
	 *
	 * @return The process ID of this process.
	 * @since 2016/06/19
	 */
	public final int pid()
	{
		return this.pid;
	}
}

