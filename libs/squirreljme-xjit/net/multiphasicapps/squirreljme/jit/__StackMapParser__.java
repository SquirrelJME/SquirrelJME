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
import java.util.ArrayList;
import java.util.List;
import net.multiphasicapps.squirreljme.java.symbols.FieldSymbol;
import net.multiphasicapps.squirreljme.java.symbols.MethodSymbol;
import net.multiphasicapps.squirreljme.linkage.MethodFlags;

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
	
	/** The resulting state. */
	private final SnapshotCacheStates _result;
	
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
	 * @param __m The current method being exported.
	 * @param __ms The maximum number of stack entries.
	 * @param __ml The maximum number of local entries.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/04/16
	 */
	__StackMapParser__(__Code__ __c, boolean __modern, DataInputStream __in,
		ExportedMethod __m, int __ms, int __ml)
		throws NullPointerException
	{
		// Check
		if (__in == null || __m == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.in = __in;
		this.maxstack = __ms;
		this.maxlocals = __ml;
		
		// This is used to set which variables appear next before a state is
		// constructed with them
		JavaType[] nextstack, nextlocals;
		this._nextstack = (nextstack = new JavaType[__ms]);
		this._nextlocals = (nextlocals = new JavaType[__ml]);
		
		// And this is used to store the registers for the currently being
		// parsed state for instructions
		JITConfig config = __c._config;
		ActiveCacheState nextstate;
		this._nextstate = (nextstate = new ActiveCacheState(__c, __ms, __ml,
			config));
		
		// Initialize the starting state with one that matches the input for
		// a method call
		SnapshotCacheStates result = new SnapshotCacheStates(__c);
		this._result = result;
		NativeType[] argmap = new NativeType[__ml];
		int at = 0;
		
		// Non-static methods always have an implicit instance argument
		if (!__m.methodFlags().isStatic())
		{
			nextlocals[at] = JavaType.OBJECT;
			argmap[at++] = config.toNativeType(JavaType.OBJECT);
		}
		
		// Handle each argument
		for (FieldSymbol f : __m.methodType().arguments())
		{
			// Map type
			JavaType j;
			nextlocals[at] = (j = JavaType.bySymbol(f));
			argmap[at++] = config.toNativeType(j);
			
			// Skip space for wide
			if (j.isWide())
				nextlocals[at++] = JavaType.TOP;
		}
		
		// Get the allocations to initialize with, these are always fixed
		TypedAllocation[] allocs = config.entryAllocations(argmap);
		
		// Fill in allocations to the initial state
		for (int i = 0; i < __ml; i++)
		{
			// Ignore missing variables
			JavaType j;
			if (null == (j = nextlocals[i]))
				continue;
			
			// Force allocation for argument entry.
			nextstate.getSlot(AreaType.STACK, i).forceAllocation(
				allocs[i], nextlocals[i]);
		}
		
		// Set the initial calculated state
		result.set(0, nextstate);
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
		// Parse the input
		DataInputStream in = this.in;
		int n = in.readUnsignedShort();
		for (int i = 0; i < n; i++)
		{
			throw new todo.TODO();
		}
		
		// Use it
		return this._result;
	}
}

