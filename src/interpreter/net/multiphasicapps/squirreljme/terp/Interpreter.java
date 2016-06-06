// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.terp;

import java.util.Map;
import net.multiphasicapps.descriptors.ClassNameSymbol;
import net.multiphasicapps.squirreljme.ci.CIMethod;
import net.multiphasicapps.squirreljme.ci.CIMethodID;
import net.multiphasicapps.squirreljme.classpath.ClassPath;
import net.multiphasicapps.squirreljme.classpath.ClassUnit;
import net.multiphasicapps.squirreljme.classpath.ClassUnitProvider;

/**
 * This is the base class which is used by implementations of the interpreter
 * to handle and managed interpretation.
 *
 * @since 2016/05/27
 */
public abstract class Interpreter
{
	/** Memory pool defaults to 24MiB. */
	public static final long DEFAULT_MEMORY_POOL_SIZE =
		25_165_824;
	
	/** Memory pool access lock. */
	private final Object _mempoollock =
		new Object();
	
	/** The memory pool of the interpreter. */
	private volatile InterpreterMemoryPool _mempool;
	
	/** The current size of the memory pool. */
	private volatile long _mempoolsize =
		DEFAULT_MEMORY_POOL_SIZE;
	
	/**
	 * Creates a new process in the interpreter for storing object states
	 * for a group of threads.
	 *
	 * @param __cp The classpath that the process uses.
	 * @return The new interpreter process.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/03
	 */
	public abstract InterpreterProcess createProcess(ClassPath __cp)
		throws NullPointerException;
	
	/**
	 * Creates a new thread within the interpreter which starts execution at
	 * the specified method and uses the given arguments.
	 *
	 * @param __ip The process which owns the thread.
	 * @param __mc The main class of the thread.
	 * @param __mm The main method of the thread.
	 * @param __args The initial arguments which are passed to the starting
	 * method.
	 * @throws InterpreterException If the method is not static or an input
	 * argument is not of the expected type.
	 * @throws NullPointerException If the process or method were not
	 * specified.
	 * @since 2016/06/03
	 */
	public abstract InterpreterThread createThread(InterpreterProcess __ip,
		ClassNameSymbol __mc, CIMethodID __mm, Object... __args)
		throws InterpreterException, NullPointerException;
	
	/**
	 * Handles the X options which may be passed to the interpreter.
	 *
	 * @param __xo The X options to handle.
	 * @since 2016/05/29
	 */
	public abstract void handleXOptions(Map<String, String> __xo);
	
	/**
	 * This runs a single cycle in the interpreter.
	 *
	 * @since 2016/05/30
	 */
	public abstract void runCycle();
	
	/**
	 * Adjusts the class path to be used by a process.
	 *
	 * @param __cups Class unit providers.
	 * @param __cp The class path to use.
	 * @return The adjusted class path.
	 * @since 2016/06/03
	 */
	public ClassPath adjustClassPath(ClassUnitProvider[] __cups,
		ClassPath __cp)
	{
		return __cp;
	}
	
	/**
	 * Potentially adjusts the arguments to be used by a thread.
	 *
	 * @param __args The original arguments.
	 * @return The adjusted arguments.
	 * @since 2016/06/03
	 */
	public Object[] adjustMainArguments(Object... __args)
	{
		return __args;
	}
	
	/**
	 * Potentially adjusts the main entry class to be used by a thread.
	 *
	 * @param __mc The original class.
	 * @return The adjusted main class.
	 * @since 2016/06/03
	 */
	public ClassNameSymbol adjustMainClass(ClassNameSymbol __mc)
	{
		return __mc;
	}
	
	/**
	 * Potentially adjusts the main entry method to be used by a thread.
	 *
	 * @param __mm The original method.
	 * @return The adjusted main method.
	 * @since 2016/06/03
	 */
	public CIMethodID adjustMainMethod(CIMethodID __mm)
	{
		return __mm;
	}
	
	/**
	 * Obtains the memory pool or creates it if it does not exist.
	 *
	 * @return The memory pool that all processes use to store data.
	 * @since 2016/06/06
	 */
	public final InterpreterMemoryPool getMemoryPool()
	{
		// Lock
		synchronized (this._mempoollock)
		{
			// Get
			InterpreterMemoryPool rv = this._mempool;
			
			// Create?
			if (rv == null)
				this._mempool = (rv = new InterpreterMemoryPool(
					(int)this._mempoolsize));
			
			// Return
			return rv;
		}
	}
	
	/**
	 * Sets the size of the memory pool.
	 *
	 * @param __sz The memory pool size to use.
	 * @throws IllegalArgumentException If the size is zero, negative, not a
	 * power of two, or is greater than 2GiB.
	 * @throws IllegalStateException If a memory pool was already created and
	 * its size cannot be changed.
	 * @since 2016/06/06
	 */
	public final void setMemoryPoolSize(long __sz)
		throws IllegalArgumentException, IllegalStateException
	{
		// {@squirreljme.error AN05 The requested size of the memory pool is
		// zero, negative, not a power of two, or exceeds 2GiB. (The requested
		// size)}
		if (__sz <= 0 || __sz > Integer.MAX_VALUE || Long.bitCount(__sz) != 1)
			throw new IllegalArgumentException(String.format("AN05 %d", __sz));
		
		// Lock
		synchronized (this._mempoollock)
		{
			// {@squirreljme.error AN06 The memory pool was already created
			// which means the size cannot be set.}
			if (this._mempool != null)
				throw new IllegalStateException("AN06");
			
			// Set new size
			this._mempoolsize = __sz;
		}
	}
}

