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
import net.multiphasicapps.squirreljme.mmu.MemoryAccessor;
import net.multiphasicapps.squirreljme.sm.StructureManager;
import net.multiphasicapps.squirreljme.sm.PointerType;

/**
 * This is the base class which is used by implementations of the interpreter
 * to handle and managed interpretation.
 *
 * @since 2016/05/27
 */
public abstract class Interpreter
{
	/** The structure manager lock. */
	private final Object _smlock =
		new Object();
	
	/** The structure manager for memory allocation. */
	private final StructureManager _sm;
	
	/** Memory pool access lock. */
	private final Object _mempoollock =
		new Object();
	
	/** The interpreter object manager. */
	private volatile StructureManager _objman;
	
	/** The type of pointers to use in the interpreter. */
	private volatile PointerType _pointertype =
		PointerType.INTEGER;
	
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
	 * Returns the size of the memory pool that all processes within the
	 * interpreter use.
	 *
	 * @return The memory pool size.
	 * @since 2016/06/06
	 */
	public final long getMemorySize()
	{
		// Lock
		synchronized (this._mempoollock)
		{
			// If the pool was already allocated then use the actual size
			if (true)
				throw new Error("TODO");
			
			// Otherwise use the virtual value
			return this._mempoolsize;
		}
	}
	
	/**
	 * Obtains the pointer type which is used by the interpreter's object
	 * manager.
	 *
	 * @return The data type to use for pointer values.
	 * @since 2016/06/08
	 */
	public final PointerType getPointerType()
	{
		// Lock
		synchronized (this._mempoollock)
		{
			// If the object manager was initialized, use that
			StructureManager rv = this._objman;
			if (rv != null)
				return rv.pointerType();
			
			// Otherwise use the future to set value
			return this._pointertype;
		}
	}
	
	/**
	 * Handles the X options which may be passed to the interpreter.
	 *
	 * Sub-classes should call the super-class method so that more common
	 * options are handled.
	 *
	 * @param __xo The X options to handle.
	 * @since 2016/05/29
	 */
	public void handleXOptions(Map<String, String> __xo)
	{
		// Check
		if (__xo == null)
			throw new NullPointerException("NARG");
		
		String v;
		
		// {@squirreljme.cmdline -Xsquirreljme-interpreter-mempool=bytes The
		// size of the memory pool which is shared by all processes running
		// within the interpreter.}
		if ((v = __xo.get("squirreljme-interpreter-mempool")) != null)
			try
			{
				setMemorySize(Long.decode(v));
			}
			
			// {@squirreljme.error AN07 The number of bytes to allocate to the
			// interpreter memory pool could not be decoded. (The non-number)}
			catch (NumberFormatException e)
			{
				throw new IllegalArgumentException(String.format("AN07 %s", v),
					e);
			}
		
		// {@squirreljme.cmdline -Xsquirreljme-interpreter-pointertype=type The
		// Java data type to use for pointer values. Note that the pointer type
		// sets the maximum upper limit on the amount of memory which is
		// available for usage. The current possible values are: short/16,
		// int/32, or long/64. Either a data type or integer value may be
		// specified.}
		if ((v = __xo.get("squirreljme-interpreter-pointertype")) != null)
		{
			switch (v)
			{
				case "short":
				case "16":
					setPointerType(PointerType.SHORT);
					break;
				
				case "int":
				case "32":
					setPointerType(PointerType.INTEGER);
					break;
				
				case "long":
				case "64":
					setPointerType(PointerType.LONG);
					break;
				
					// {@squirreljme.error AN0a Unknown pointer type to use
					// in the interpreter. (The desired type)}
				default:
					throw new IllegalArgumentException(String.format("AN0a %s",
						v));
			}
		}
	}
	
	/**
	 * Obtains the manager which manages allocated memory and provides object
	 * based interaction.
	 *
	 * @return The object manager.
	 * @since 2016/06/08
	 */
	public final StructureManager StructureManager()
	{
		// Lock
		synchronized (this._mempoollock)
		{
			// Get
			StructureManager rv = this._objman;
			
			// Create?
			if (rv == null)
			{
				throw new Error("TODO");
				/*
				this._objman = (rv = new StructureManager(memoryPoolManager(),
					this._pointertype));
				*/
			}
			
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
	public final void setMemorySize(long __sz)
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
			if (true)
				throw new Error("TODO");
			if (true)
				throw new IllegalStateException("AN06");
			
			// Set new size
			this._mempoolsize = __sz;
		}
	}
	
	/**
	 * Sets the data type to use for storing pointer values which refer to
	 * other memory addresses.
	 *
	 * @param __pt The data type to use for pointer based values.
	 * @throws IllegalStateException If the object manager was already
	 * initialized.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/08
	 */
	public final void setPointerType(PointerType __pt)
		throws IllegalStateException, NullPointerException
	{
		// Check
		if (__pt == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this._mempoollock)
		{
			// {@squirreljme.error AN09 Cannot set the specified pointer type
			// because the object manager has already been initialized. (The
			// pointer type)}
			if (null != this._objman)
				throw new IllegalStateException(String.format("AN09 %s",
					__pt));
			
			this._pointertype = __pt;
		}
	}
}

