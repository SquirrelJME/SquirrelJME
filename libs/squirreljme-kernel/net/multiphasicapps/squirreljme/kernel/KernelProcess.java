// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

import java.util.ArrayList;
import java.util.List;

/**
 * This represents a single process within the kernel and is used to manage
 * groups of threads within the kernel.
 *
 * @since 2016/11/08
 */
public abstract class KernelProcess
	implements AutoCloseable
{
	/** The owning kernel. */
	protected final Kernel kernel;
	
	/** The class path for this process. */
	private final SuiteDataAccessor[] _classpath;
	
	/** Threads owned by this process. */
	private final List<KernelThread> _threads =
		new ArrayList<>();
	
	/**
	 * Initializes the process.
	 *
	 * @param __k The kernel owning the process.
	 * @param __cp The class path used for user space processes.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/11/08
	 */
	protected KernelProcess(Kernel __k, SuiteDataAccessor[] __cp)
		throws NullPointerException
	{
		// Check
		if (__k == null || __cp == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.kernel = __k;
		
		// Copy the class path
		__cp = __cp.clone();
		for (SuiteDataAccessor sda : __cp)
			if (sda == null)
				throw new NullPointerException("NARG");
		this._classpath = __cp;
	}
	
	/**
	 * Creates a new thread which is owned by this process.
	 *
	 * @param __mc The main class
	 * @param __mm The main method.
	 * @param __args Arguments to the thread, only boxed types and {@code null}
	 * are permitted.
	 * @return The new thread owned by this process.
	 * @throws NullPointerException On null arguments.
	 * @throws ThreadCreationException If the thread could not be created.
	 * @since 2017/01/16
	 */
	public final KernelThread createThread(String __mc, String __mm,
		Object... __args)
		throws NullPointerException, ThreadCreationException
	{
		// Check
		if (__mc == null || __mm == null)
			throw new NullPointerException("NARG");
		
		// Defensive copy
		__args = (__args == null ? new Object[0] : __args.clone());
		
		// {@squirreljme.error BH08 Cannot pass an initial argument to a thread
		// that is not a boxed type. (The name of the class)}
		for (Object	a : __args)
			if (a != null && !(a instanceof Boolean) &&
				!(a instanceof Byte) && !(a instanceof Short) &&
				!(a instanceof Character) && !(a instanceof Integer) &&
				!(a instanceof Long) && !(a instanceof Float) &&
				!(a instanceof Double))
				throw new ThreadCreationException(
					String.format("BH08 %s", a.getClass()));
		
		// Create it
		KernelThread rv = null;
		try
		{
			// Create thread
			Kernel kernel = this.kernel;
			rv = kernel.__createThread(this, __mc, __mm, __args);
			
			// {@squirreljme.error BH05 A null thread was returned, treating
			// as failure.}
			if (rv == null)
				throw new ThreadCreationException("BH05");
			
			// {@squirreljme.error BH06 The created thread was reassigned to
			// another kernel or process during construction.}
			if (rv.kernel() != kernel || rv.process() != this)
				throw new ThreadCreationException("BH06");
			
			// Add to threads managed by this process
			List<KernelThread> threads = this._threads;
			synchronized (threads)
			{
				threads.add(rv);
			}
			
			// Return it
			return rv;
		}
		
		// Failed to create the process
		catch (RuntimeException|Error e)
		{
			// Destroy the thread if it was created
			if (rv != null)
				try
				{
					rv.close();
				}
				catch (Throwable t)
				{
					e.addSuppressed(e);
				}
			
			// Fail
			throw e;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/11/08
	 */
	@Override
	public final void close()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the kernel which owns this process.
	 *
	 * @return The owning kernel.
	 * @since 2017/01/16
	 */
	public final Kernel kernel()
	{
		return this.kernel;
	}
}

