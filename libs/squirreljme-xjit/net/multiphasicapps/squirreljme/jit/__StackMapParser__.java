// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * This class is used to parse the stack map and initialize the initial
 * snapshot states for jump targets within the method.
 *
 * @since 2017/04/16
 */
class __StackMapParser__
{
	/** The stream to decode from. */
	protected final DataInputStream in;
	
	/** The number of stack entries. */
	protected final int maxstack;
	
	/** The number of local entries. */
	protected final int maxlocals;
	
	/** The current cache state for the method. */
	private final ActiveCacheState _nextstate;
	
	/** The next stack state. */
	private final JavaType[] _nextstack;
	
	/** The next local variable state. */
	private final JavaType[] _nextlocals;
	
	/**
	 * Initializes the stack map parser.
	 *
	 * @param __c The owning code parser.
	 * @param __modern Does this represent the modern stack map?
	 * @param __in The stack map table information,
	 * @param __method The current method being exported.
	 * @param __ms The maximum number of stack entries.
	 * @param __ml The maximum number of local entries.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/04/16
	 */
	__StackMapParser__(__Code__ __c, boolean __modern, DataInputStream __in,
		ExportedMethod __method, int __ms, int __ml)
		throws NullPointerException
	{
		// Check
		if (__in == null || __method == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.in = __in;
		this.maxstack = __ms;
		this.maxlocals = __ml;
		
		// This is used to set which variables appear next before a state is
		// constructed with them
		this._nextstack = new JavaType[__ms];
		this._nextlocals = new JavaType[__ml];
		
		// And this is used to store the registers for the currently being
		// parsed state for instructions
		this._nextstate = new ActiveCacheState(__c, __ms, __ml, __c._config);
		
		throw new todo.TODO();
	}
		
	/**
	 * Obtains the resulting cache states.
	 *
	 * @throws IOException On read errors.
	 * @throws JITException If the stack map is not parsed correctly.
	 * @return The result of the parsed stack map table.
	 * @since 2017/04/16
	 */
	SnapshotCacheStates __get()
		throws IOException, JITException
	{
		throw new todo.TODO();
	}
}

