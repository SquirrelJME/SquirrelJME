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
import java.util.Arrays;

/**
 * This class represents a lookup switch which is used to lookup indexes as a
 * jump table.
 *
 * @since 2018/09/20
 */
public final class LookupSwitch
{
	/** The default target. */
	protected final InstructionJumpTarget defaultjump;
	
	/** The keys. */
	private final int[] _keys;
	
	/** The jump targets. */
	private final InstructionJumpTarget[] _jumps;
	
	/** String representation. */
	private Reference<String> _string;
	
	/**
	 * Initializes the lookup switch.
	 *
	 * @param __def The default address if no values match at all.
	 * @param __keys The keys to check, must be sorted.
	 * @param __jumps The jump targets.
	 * @throws IllegalArgumentException If the key and jump table length are
	 * difference lengths.
	 * @throws InvalidClassFormatException If the key table is not sorted.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/20
	 */
	public LookupSwitch(InstructionJumpTarget __def,
		int[] __keys, InstructionJumpTarget[] __jumps)
		throws IllegalArgumentException, InvalidClassFormatException,
			NullPointerException
	{
		if (__def == null || __keys == null || __jumps == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error JC2f Key and jump table for lookup switch
		// table is of different lengths.}
		if (__keys.length != __jumps.length)
			throw new IllegalArgumentException("JC2f");
		
		// Defensive copy
		__keys = __keys.clone();
		__jumps = __jumps.clone();
		
		// Check for sorted
		long last = ((long)Integer.MIN_VALUE) - 1;
		for (int i = 0, n = __keys.length; i < n; i++)
		{
			// {@squirreljme.error JC2g Lookup switch key is not in sorted
			// order. (The index; The current key; The last key)}
			int k = __keys[i];
			if (k < last)
				throw new InvalidClassFormatException(
					String.format("JC2g %d %d %d", i, k, last));
			last = k;
			
			if (__jumps[i] == null)
				throw new NullPointerException("NARG");
		}
		
		this.defaultjump = __def;
		this._keys = __keys;
		this._jumps = __jumps;
	}
	
	/**
	 * Matches the input key with the given jump target or returns the default.
	 *
	 * @param __k The key to match.
	 * @return The jump target for the match or the default if it was not
	 * found.
	 * @since 2018/09/20
	 */
	public final InstructionJumpTarget match(int __k)
	{
		// Use binary search since all entries are sorted
		int dx = Arrays.binarySearch(this._keys, __k);
		
		// Match was found, use that result
		if (dx >= 0)
			return this._jumps[dx];
		
		// Not found
		return this.defaultjump;
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
			int[] keys = this._keys;
			InstructionJumpTarget[] jumps = this._jumps;
			for (int i = 0, n = keys.length; i < n; i++)
			{
				sb.append(", ");
				
				sb.append(keys[i]);
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

