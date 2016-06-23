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

import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;

/**
 * This represents a single thread which is owned by a process in the virtual
 * machine.
 *
 * @since 2016/06/20
 */
public class PVMThread
{
	/** The owning virtual machine. */
	protected final PVM pvm;
	
	/** The owning process. */
	protected final PVMProcess process;
	
	/** The ID of this thread. */
	protected final int tid;
	
	/** The real backing host JVM thread. */
	protected final Thread realthread;
	
	/**
	 * Initializes the thread.
	 *
	 * @param __owner The process which owns this thread.
	 * @param __tid The ID of this thread.
	 * @param __main The main entry point of the thread.
	 * @param __args The arguments to pass to the thread.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/20
	 */
	PVMThread(PVMProcess __owner, int __tid, ClassNameSymbol __main,
		Object... __args)
		throws NullPointerException
	{
		// Check
		if (__owner == null || __main == null)
			throw new NullPointerException("NARG");
		
		// Must always exist
		if (__args == null)
			__args = new Object[0];
		
		// Set
		this.process = __owner;
		this.pvm = __owner.pvm();
		this.tid = __tid;
		
		// Locate the main class in the class loader
		PVMClassLoader pcl = __owner.classLoader();
		Class<?> maincl;
		try
		{
			maincl = pcl.virtualFindClass(__main);
		}
		
		// {@squirreljme.error CL03 Could not locate the main class. (The name
		// of the main class)}
		catch (ClassNotFoundException e)
		{
			throw new IllegalArgumentException(String.format("CL03 %s",
				__main), e);
		}
		
		throw new Error("TODO");
	}
	
	/**
	 * Starts this thread.
	 *
	 * @throws IllegalStateException If the thread has already started or is
	 * terminated.
	 * @since 2016/06/20
	 */
	public final void start()
		throws IllegalStateException
	{
		throw new Error("TODO");
	}
}

