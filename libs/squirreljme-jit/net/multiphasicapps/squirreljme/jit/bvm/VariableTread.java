// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.bvm;

import java.lang.ref.Reference;

/**
 * This contains a single tread of variables and may optionally be used as
 * a stack.
 *
 * If a tread is usable as a stack, it also additionally supports the push and
 * pop operations.
 *
 * @since 2017/10/17
 */
public final class VariableTread
{
	/** The number of variables in the tread. */
	protected final int size;
	
	/** Is this a stack? */
	protected final boolean isstack;
	
	/** Owning variables. */
	private Reference<Variables> _varsref;
	
	/**
	 * Initializes the variable tread.
	 *
	 * @param __vr Reference to the owning variables.
	 * @param __n The number of variables to store in this tread.
	 * @param __stack Is this tread to be treated as a stack?
	 * @throws IllegalArgumentException If the number of variables to store
	 * is negative.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/17
	 */
	VariableTread(Reference<Variables> __vr, int __n, boolean __stack)
		throws IllegalArgumentException, NullPointerException
	{
		if (__vr == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error JI3y Negative number of variable slots in the
		// tread specified.}
		if (__n < 0)
			throw new IllegalArgumentException("JI3y");
		
		// Set
		this._varsref = __vr;
		this.size = __n;
		this.isstack = __stack;
	}
}

