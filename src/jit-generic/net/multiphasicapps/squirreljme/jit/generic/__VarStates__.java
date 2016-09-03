// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.generic;

/**
 * This represents the state of variables.
 *
 * @since 2016/09/03
 */
final class __VarStates__
{
	/** The registers being used, will be null if not valid. */
	final GenericRegister[] _regs;
	
	/** Stack variable offsets, will be negative if not valid. */
	final int[] _stackoffs;
	
	/**
	 * Initializes the variable states.
	 *
	 * @param __n The number of states to store.
	 * @since 2016/09/03
	 */
	__VarStates__(int __n)
	{
		this._regs = new GenericRegister[__n];
		
		// Initialize stack to negative values
		int[] stackoffs;
		this._stackoffs = (stackoffs = new int[__n]);
		for (int i = 0; i < __n; i++)
			stackoffs[i] = -1;
	}
	
	/**
	 * Copy constructs the variable states.
	 *
	 * @param __vs The source states to clone from.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/03
	 */
	__VarStates__(__VarStates__ __vs)
		throws NullPointerException
	{
		// Check
		if (__vs == null)
			throw new NullPointerException("NARG");
		
		// Just clone
		this._regs = __vs._regs.clone();
		this._stackoffs = __vs._stackoffs.clone();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public String toString()
	{
		// Setup
		StringBuilder sb = new StringBuilder("[");
		
		// Get
		GenericRegister[] regs = this._regs;
		int[] stackoffs = this._stackoffs;
		
		// Go through int
		int n = regs.length;
		for (int i = 0; i < n; i++)
		{
			// Comma?
			if (i > 0)
				sb.append(", ");	
			
			// Get both
			GenericRegister reg = regs[i];
			int off = stackoffs[i];
			
			// Allocated?
			if (reg != null)
				sb.append(reg);
			
			// On the stack?
			else if (off >= 0)
			{
				sb.append('+');
				sb.append(off);
			}
			
			// Not used at all
			else
				sb.append('-');
		}
		
		// Build
		sb.append(']');
		return sb.toString();
	}
}

