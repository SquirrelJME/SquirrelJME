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

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
	
	/** Verification targets. */
	private final Map<Integer, BasicVerificationTarget> _targets;
	
	/**
	 * Initializes the stack map parser.
	 *
	 * @param __c The owning code parser.
	 * @param __modern Does this represent the modern stack map?
	 * @param __in The stack map table information,
	 * @param __m The current method being exported.
	 * @param __ms The maximum number of stack entries.
	 * @param __ml The maximum number of local entries.
	 * @throws JITException If the table is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/04/16
	 */
	__StackMapParser__(ByteCode __code, byte[] __smtdata, boolean __smtmodern,
		ExportedMethod __em)
		throws JITException, NullPointerException
	{
		// Check
		if (__code == null || __em == null || __smtdata == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.in = new DataInputStream(new ByteArrayInputStream(__smtdata));
		int maxstack = __code.maxStack(),
			maxlocals = __code.maxLocals();
		this.maxstack = maxstack;
		this.maxlocals = maxlocals;
		
		// This is used to set which variables appear next before a state is
		// constructed with them
		JavaType[] nextstack, nextlocals;
		nextstack = new JavaType[maxstack];
		nextlocals = new JavaType[maxlocals];
		
		// Setup initial entry state
		if (true)
			throw new todo.TODO();
		
		// Parse the stack map table
		Map<Integer, BasicVerificationTarget> targets = new LinkedHashMap<>();
		try
		{
			throw new todo.TODO();
		}
		
		// {@squirreljme.error AQ1b Failed to parse the stack map table.}
		catch (IOException e)
		{
			throw new JITException("AQ1b", e);
		}
		
		// Store targets
		this._targets = targets;
		
		/*
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
			nextstate.getSlot(AreaType.LOCAL, i).forceAllocation(
				allocs[i], nextlocals[i]);
		}
		
		// Set the initial calculated state
		result.set(0, nextstate);
		*/
	}
	
	/**
	 * Returns the basic verification state for the specified address.
	 *
	 * @param __a The address to get.
	 * @return The verification target for the specified address or
	 * {@code null} if none is available.
	 * @since 2017/05/20
	 */
	public BasicVerificationTarget get(int __a)
	{
		return this._targets.get(__a);
	}
}

