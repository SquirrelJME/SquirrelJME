// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This represents a table switch.
 *
 * @since 2018/09/20
 */
public final class TableSwitch
	implements IntMatchingJumpTable
{
	/** The default jump address. */
	protected final InstructionJumpTarget defaultjump;
	
	/** The low index. */
	protected final int low;
	
	/** The high index. */
	protected final int high;
	
	/** The jump targets. */
	private final InstructionJumpTarget[] _jumps;
	
	/** String representation. */
	private Reference<String> _string;
	
	/**
	 * Initializes the table switch.
	 *
	 * @param __def The default address.
	 * @param __lo The low address.
	 * @param __hi The high address.
	 * @param __jumps The jump offsets.
	 * @throws InvalidClassFormatException If the high is less than or equal
	 * to the low.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/20
	 */
	public TableSwitch(InstructionJumpTarget __def, int __lo, int __hi,
		InstructionJumpTarget[] __jumps)
		throws InvalidClassFormatException, NullPointerException
	{
		if (__def == null || __jumps == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error JC2i Table switch high index is less than or
		// equal to the low index. (The low index; The high index)}
		if (__hi <= __lo)
			throw new InvalidClassFormatException(String.format("JC2i %d %d",
				__lo, __hi));
		
		// Check for null
		__jumps = __jumps.clone();
		for (InstructionJumpTarget j : __jumps)
			if (j == null)
				throw new NullPointerException("NARG");
		
		// Set
		this.low = __lo;
		this.high = __hi;
		this.defaultjump = __def;
		this._jumps = __jumps;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/20
	 */
	@Override
	public final InstructionJumpTarget match(int __k)
	{
		// Use default if out of bounds
		int low = this.low,
			high = this.high;
		if (__k < low || __k > high)
			return this.defaultjump;
		
		return this._jumps[__k - low];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/20
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
		{
			// Set with default first
			StringBuilder sb = new StringBuilder("{default=");
			sb.append(this.defaultjump);
			
			// Add all matches and their targets
			InstructionJumpTarget[] jumps = this._jumps;
			for (int i = 0, n = jumps.length, v = this.low; i < n; i++, v++)
			{
				sb.append(", ");
				
				sb.append(v);
				sb.append('=');
				sb.append(jumps[i]);
			}
			
			// Cleans
			sb.append('}');
			
			this._string = new WeakReference<>((rv = sb.toString()));
		}
		
		return rv;
	}
}

