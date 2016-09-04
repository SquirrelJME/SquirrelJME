// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.io.IOException;
import java.util.Arrays;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.io.data.ExtendedDataInputStream;

/**
 * This is used to determine which instruction addresses in the byte code are
 * jumped to, the benefit of this is that the target JIT does not have to
 * remember state for instructions that will never be jumped to.
 *
 * @since 2016/09/03
 */
class __JumpTargetCalc__
{
	/** The returning target array. */
	protected final int[] targets;
	
	/** The current write index. */
	private volatile int _index;
	
	/**
	 * Calculates the jump targets.
	 *
	 * @param __dis The source byte codes.
	 * @param __cl The code length.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/03
	 */
	__JumpTargetCalc__(ExtendedDataInputStream __dis, int __cl)
		throws IOException, NullPointerException
	{
		// Check
		if (__dis == null)
			throw new NullPointerException("NARG");
		
		// Setup return value
		int[] rv = new int[__cl];
		
		// Go through all instructions, since it is marked a read must not
		// cause EOF to occur
		for (int nowpos = (int)__dis.size(), endpos = nowpos + __cl;
			nowpos < endpos; nowpos = (int)__dis.size())
		{
			// Read
			int code = __dis.read();
			
			// Wide? Read another
			if (code == __OpIndex__.WIDE)
				code = (code << 8) | __dis.readUnsignedByte();
			
			// Handle the code
			__handle(__dis, code, nowpos, rv);
		}
		
		// Either use the same array or allocate a new one
		int used = this._index;
		if (used == __cl)
			this.targets = rv;
		
		else
			this.targets = Arrays.copyOf(rv, used);
	}
	
	/**
	 * Returns the calculated jump targets.
	 *
	 * @return The jump targets in the method.
	 * @since 2016/09/03
	 */
	public int[] targets()
	{
		return this.targets;
	}
	
	/**
	 * Adds a single jump target to the list.
	 *
	 * @param __rv The resulting list.
	 * @param __p The position to add.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/04
	 */
	private void __add(int[] __rv, int __p)
		throws NullPointerException
	{
		// Check
		if (__rv == null)
			throw new NullPointerException("NARG");
		
		// Get the end of the list
		int end = this._index;
		
		// Keep the target list sorted, so essentially insertion sort is used
		// here
		for (int i = 0; i < end; i++)
		{
			// Get value
			int val = __rv[i];
			
			// If it is the same, it is already in this list
			if (val == __p)
				return;
			
			// Never insert higher positions before lower ones
			if (__p > val)
				continue;
			
			// Move all values over
			for (int j = end, k = j - 1; j > i; j--, k--)
				__rv[j] = __rv[k];
			
			// Insert
			__rv[i] = __p;
			
			// Would have been inserted
			return;
		}
		
		// Add to the end otherwise
		__rv[end] = __p;
		this._index = end + 1;
	}
	
	/**
	 * Handles a single operation.
	 *
	 * @param __dis The input data source.
	 * @param __code The operation.
	 * @param __pos The current position for relative addressing.
	 * @param __rv The returning resultant array.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/04
	 */
	private void __handle(ExtendedDataInputStream __dis, int __code, int __pos,
		int[] __rv)
		throws IOException, NullPointerException
	{
		// Check
		if (__dis == null || __rv == null)
			throw new NullPointerException("NARG");
		
		// Depends on the code
		switch (__code)
		{
				// Goto
			case __OpIndex__.GOTO:
				__add(__rv, __pos + __dis.readShort());
				break;
				
				// Goto (wide)
			case __OpIndex__.GOTO_W:
				__add(__rv, __pos + __dis.readInt());
				break;
				
				// {@squirreljme.error BA1j Illegal operation in Java byte
				// code. (The operation code; The position of it)}
			default:
				throw new JITException(String.format("BA1j %d %d", __code,
					__pos));
		}
		
		throw new Error("TODO");
	}
}

