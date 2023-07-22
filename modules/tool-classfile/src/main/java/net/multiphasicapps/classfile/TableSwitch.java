// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
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
		
		/* {@squirreljme.error JC42 Table switch high index is less than or
		equal to the low index. (The low index; The high index)} */
		if (__hi <= __lo)
			throw new InvalidClassFormatException(String.format("JC42 %d %d",
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
	 * Converts this to a lookup switch.
	 *
	 * @return This table switch as a lookup switch.
	 * @since 2019/04/16
	 */
	public final LookupSwitch asLookupSwitch()
	{
		// These are needed to build keys
		InstructionJumpTarget[] jumps = this._jumps;
		int low = this.low,
			high = this.high,
			n = jumps.length;
		
		// Setup new key map, the jumps do not need to be adjusted because
		// they will map the same!
		int[] newkeys = new int[n];
		for (int o = 0, k = low; k <= high; k++, o++)
			newkeys[o] = k;
		
		// Build
		return new LookupSwitch(this.defaultjump, newkeys, jumps);
	}
	
	/**
	 * Returns the jumps.
	 *
	 * @return The jumps.
	 * @since 2019/04/16
	 */
	public final InstructionJumpTarget[] jumps()
	{
		return this._jumps.clone();
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
	 * Returns the size of the switch.
	 *
	 * @return The size.
	 * @since 2019/04/16
	 */
	public final int size()
	{
		return this._jumps.length;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/31
	 */
	@Override
	public final InstructionJumpTarget[] targets()
	{
		InstructionJumpTarget[] jumps = this._jumps;
		int n = jumps.length;
		
		// Start off array with the default jump
		InstructionJumpTarget[] rv = new InstructionJumpTarget[n + 1];
		rv[0] = this.defaultjump;
		
		// Add all the others
		for (int i = 0, o = 1; i < n; i++, o++)
			rv[o] = jumps[i];
		
		return rv;
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

