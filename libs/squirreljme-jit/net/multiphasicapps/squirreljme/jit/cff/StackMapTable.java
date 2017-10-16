// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.cff;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;
import net.multiphasicapps.util.sorted.SortedTreeMap;

/**
 * This is the stack map table which is used for verification purposes.
 *
 * @since 2017/10/09
 */
public final class StackMapTable
{
	/** Stack map states. */
	private final Map<Integer, StackMapTableState> _states;
	
	/**
	 * Initializes the stack map table.
	 *
	 * @param __s The input states, this is used directly.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/16
	 */
	private StackMapTable(Map<Integer, StackMapTableState> __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		this._states = __s;
	}
	
	/**
	 * Decodes the stack map table.
	 *
	 * @param __p The constant pool.
	 * @param __m The method this code exists within.
	 * @param __new Should the new stack map table format be used?
	 * @param __in The data for the stack map table.
	 * @param __bc The owning byte code.
	 * @throws InvalidClassFormatException If the stack map table is not
	 * valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/09
	 */
	public static StackMapTable decode(Pool __p, Method __m, boolean __new,
		byte[] __in, ByteCode __bc)
		throws InvalidClassFormatException, NullPointerException
	{
		if (__p == null || __m == null || __in == null || __bc == null)
			throw new NullPointerException("NARG");
		
		// Each initial state
		Map<Integer, StackMapTableState> states = new SortedTreeMap<>();
		
		// Setup initial base from the method arguments
		int maxstack = __bc.maxStack(),
			maxlocals = __bc.maxLocals();
		__WorkingState__ work = new __WorkingState__(maxstack, maxlocals);
		
		// Setup initial state
		// {@squirreljme.error JI2l The arguments that are required for the
		// given method exceeds the maximum number of permitted local
		// variables. (The method in question; The required number of local
		// variables; The maximum number of local variables)}
		MethodHandle handle = __m.handle();
		boolean isinstance = !__m.flags().isStatic();
		JavaType[] jis = handle.javaStack(isinstance);
		int jn = jis.length;
		if (jn > maxlocals)
			throw new InvalidClassFormatException(
				String.format("JI2l %s %d %d", handle, jn, maxlocals));
		
		// Setup entries
		// If this is an instance initializer method then only the first
		// argument is not initialized
		boolean isiinit = isinstance && __m.name().isInstanceInitializer();
		for (int i = 0; i < jn; i++)
			work.setLocal(i, new StackMapTableEntry(jis[i],
				(isiinit ? (i != 0) : true)));
		
		// Store initial state
		states.put(0, work.build());
		
		// Wrap it
		try (DataInputStream in = new DataInputStream(
			new ByteArrayInputStream(__in)))
		{
			// Read of modern StackMapTable
			int count = in.readUnsignedShort();
			if (__new)
				for (int tdx = 0; tdx < count; tdx++)
				{
					throw new todo.TODO();
				}
			
			// The old-style StackMap is much simpler
			else
				for (int tdx = 0; tdx < count; tdx++)
				{
					// Read in address to use
					int addr = in.readUnsignedShort();
					
					// Read in local variables
					int nl = in.readUnsignedShort(),
						j;
					for (j = 0; j < nl; j++)
						work.setLocal(j, __read(in, __bc));
					for (; j < maxlocals; j++)
						work.setLocal(j, null);
					
					// Read in stack variables
					int ns = in.readUnsignedShort();
					work.setStackTop(ns);
					for (j = 0; j < ns; j++)
						work.setStack(j, __read(in, __bc));
					
					// Generate
					states.put(addr, work.build());
				}
		}
		
		// {@squirreljme.error JI3k Failed to read the stack map table data.}
		catch (IOException e)
		{
			throw new InvalidClassFormatException("JI3k");
		}
		
		// Build table
		return new StackMapTable(states);
	}
	
	/**
	 * Reads a single entry.
	 *
	 * @param __in The input data stream.
	 * @param __bc The owning byte code, needed for new.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/16
	 */
	private static StackMapTableEntry __read(DataInputStream __in,
		ByteCode __bc)
		throws IOException, NullPointerException
	{
		if (__in == null || __bc == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

