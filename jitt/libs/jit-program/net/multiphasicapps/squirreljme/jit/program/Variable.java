// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.program;

import java.lang.ref.Reference;

/**
 * This represents a single variable.
 *
 * The value in a variable (if it has one) will always point to a
 * {@link DataValues} which represent unique values within variables. This
 * makes it similar to SSA and may make certain optimizations available.
 *
 * @since 2017/10/19
 */
public final class Variable
{
	/** The index of this variable. */
	protected final int index;
	
	/** The owning tread. */
	private final Reference<VariableTread> _treadref;
	
	/**
	 * Initializes the single variable.
	 *
	 * @param __tr The owning variable tread.
	 * @param __i The index of this variables.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/19
	 */
	Variable(Reference<VariableTread> __tr, int __i)
		throws NullPointerException
	{
		if (__tr == null)
			throw new NullPointerException("NARG");
		
		this.index = __i;
		this._treadref = __tr;
	}
}

