// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.java;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Arrays;

/**
 * This class manages all of the basic blocks which are used within a program
 * during processing and compilation. Basic blocks may be processed multiple
 * times during compilation for various input changes.
 *
 * @see BasicBlock
 * @since 2017/08/01
 */
public final class BasicBlocks
{
	/** The owning byte code. */
	protected final ByteCode code;
	
	/** Byte code ranges which appear in integer pairs. */
	private final int[] _ranges;
	
	/** String representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the basic block representation.
	 *
	 * @param __code The byte code for the given method.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/02
	 */
	public BasicBlocks(ByteCode __code)
		throws NullPointerException
	{
		// Check
		if (__code == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.code = __code;
		
		// Determining the ranges is simple. Essentially instructions that are
		// the start of a new basic block follow non-natural flowing
		// instructions or are jump targets themselves
		int count = __code.instructionCount(),
			rangeat = 0,
			fromdx = 0;
		int[] ranges = new int[count * 2],
			jumptargets = __code.jumpTargetAddresses();
		boolean lastnatflow = false;
		for (int i = 0; i <= count; i++)
		{
			// Instructions which precede this instruction may have non-natural
			// flow which means that this instruction is now the start of a new
			// basic block
			boolean end = (i == count);
			boolean hadlast = lastnatflow;
			Instruction u;
			if (end)
				u = null;
			else
			{
				u = __code.getByIndex(i);
				lastnatflow = u.hasNaturalFlow();
			}
			
			// The first instruction could be a jump target but if it is then
			// it would always be in a basic block by itself, the greater than
			// zero check prevents this from happening
			if (i > 0 && (i == count || !hadlast ||
				Arrays.binarySearch(jumptargets, u.address()) >= 0))
			{
				ranges[rangeat++] = __code.indexToAddress(fromdx);
				ranges[rangeat++] = (i == count ? __code.length() :
					__code.indexToAddress(i));
				
				// The from index is now set to the current instruction which
				// starts this basic block
				fromdx = i;
			}
		}
		
		// Store
		this._ranges = Arrays.copyOf(ranges, rangeat);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/02
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
		{
			StringBuilder sb = new StringBuilder("[");
			
			// Record all the ranges
			int[] ranges = this._ranges;
			for (int i = 0, n = ranges.length; i < n; i += 2)
			{
				if (i > 0)
					sb.append(", ");
				
				// Use [, ) for inclusive and exclusive since the second
				// part of the range reaches past the end
				sb.append('[');
				sb.append(ranges[i]);
				sb.append(", ");
				sb.append(ranges[i + 1]);
				sb.append(')');
			}
			
			sb.append(']');
			this._string = new WeakReference<>((rv = sb.toString()));
		}
		
		return rv;
	}
}

