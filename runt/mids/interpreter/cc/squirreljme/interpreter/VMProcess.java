// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.interpreter;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import cc.squirreljme.jit.cff.ClassName;
import cc.squirreljme.jit.VerifiedJITInput;
import net.multiphasicapps.collections.SortedTreeMap;

/**
 * This represents a single process within the virtual machine.
 *
 * @since 2017/10/08
 */
public final class VMProcess
{
	/** The reference to the owning interpreter. */
	protected final Reference<Interpreter> _interpreterref;
	
	/** Threads owned by this process. */
	private final List<VMThread> _threads =
		new ArrayList<>();
	
	/** Classes which have been loaded by the virtual machine. */
	final Map<ClassName, ClassInstance> _classes =
		new SortedTreeMap<>();
	
	/**
	 * Initializes the process.
	 *
	 * @param __i The owning interpreter.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/08
	 */
	VMProcess(Reference<Interpreter> __i)
		throws NullPointerException
	{
		if (__i == null)
			throw new NullPointerException("NARG");
		
		this._interpreterref = __i;
	}
	
	/**
	 * Creates a new thread within the process.
	 *
	 * @return The newly created thread.
	 * @since 2017/10/08
	 */
	public final VMThread createThread()
	{
		Interpreter terp = __interpreter();
		synchronized (terp._lock)
		{
			VMThread rv = new VMThread(new WeakReference<>(terp),
				new WeakReference<>(this), terp.__nextThreadId());
			
			this._threads.add(rv);
			
			return rv;
		}
	}
	
	/**
	 * Returns the interpreter which owns this process.
	 *
	 * @return The interpreter.
	 * @throws IllegalStateException If the interpreter was garbage collected.
	 * @since 2017/10/06
	 */
	final Interpreter __interpreter()
		throws IllegalStateException
	{
		// {@squirreljme.error AH02 The interpreter has been garbage
		// collected.}
		Interpreter rv = this._interpreterref.get();
		if (rv == null)
			throw new IllegalStateException("AH02");
		return rv;
	}
}

