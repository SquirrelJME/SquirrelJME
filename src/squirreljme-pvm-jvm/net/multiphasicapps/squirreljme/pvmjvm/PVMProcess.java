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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.classpath.ClassPath;

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
	
	/** The class path to use. */
	protected final ClassPath classpath;
	
	/** The thread mappings. */
	private final Map<Integer, PVMThread> _threads =
		new TreeMap<>();
	
	/** The next thread ID lock. */
	private final Object _newtidlock =
		new Object();
	
	/** The next thread ID. */
	private volatile int _newtid;
	
	/**
	 * Initializes the para-virtual machine process.
	 *
	 * @param __pvm The owning process.
	 * @param __cp The class path to use for the process.
	 * @param __pid The identifier of this process.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/16
	 */
	PVMProcess(PVM __pvm, ClassPath __cp, int __pid)
		throws NullPointerException
	{
		// Check
		if (__pvm == null || __cp == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.pvm = __pvm;
		this.classpath = __cp;
		this.pid = __pid;
		
		// Setup class loader
		PVMClassLoader pcl = new PVMClassLoader(this);
		this.classloader = pcl;
	}
	
	/**
	 * Returns the class loader that this process uses for virtualization.
	 *
	 * @return The virtualized class loader.
	 * @since 2016/06/20
	 */
	public final PVMClassLoader classLoader()
	{
		return this.classloader;
	}
	
	/**
	 * Returns the class path used by the current process.
	 *
	 * @return The process class path.
	 * @since 2016/06/20
	 */
	public final ClassPath classPath()
	{
		return this.classpath;
	}
	
	/**
	 * Creates a new thread which starts at the given point in the program.
	 *
	 * @param __main The main point for the thread.
	 * @param __args The arguments to pass to the thread main.
	 * @return The newly created thread.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/20
	 */
	public final PVMThread createThread(ClassNameSymbol __main,
		Object... __args)
		throws NullPointerException
	{
		// Check
		if (__main == null)
			throw new NullPointerException("NARG");
		
		// Always must exist
		if (__args == null)
			__args = new Object[0];
		
		// Get next thread ID
		int tid;
		synchronized (this._newtidlock)
		{
			tid = this._newtid++;
		}
		
		// Create thread
		PVMThread rv = new PVMThread(this, tid, __main, (Object[])__args);
		
		// Add to mapping
		Map<Integer, PVMThread> threads = this._threads;
		synchronized (threads)
		{
			threads.put(tid, rv);
		}
		
		// Return it
		return rv;
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
	
	/**
	 * Returns the owning virtual machine.
	 *
	 * @return The owning virtual machine.
	 * @since 2016/06/20
	 */
	public final PVM pvm()
	{
		return this.pvm;
	}
}

