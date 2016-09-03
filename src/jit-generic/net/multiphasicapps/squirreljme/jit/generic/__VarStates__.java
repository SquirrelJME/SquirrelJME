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
		this._stackoffs = new int[__n];
	}
}

