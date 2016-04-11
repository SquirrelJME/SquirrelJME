// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classprogram;

import java.util.AbstractList;

/**
 * This represents the state of variables for a given operation in the program.
 *
 * @since 2016/04/10
 */
public class CPVariables
	extends AbstractList<CPVariables.Slot>
{
	/**
	 * Initializes the variable state.
	 *
	 * @param __op The operation to hold the state for.
	 * @param __Vs The optional verification state, if one is set then it is
	 * used to fill the types of variables for each state. Otherwise, all
	 * types are implicit based on the source operation (and the source
	 * operations of those operations).
	 * @throws NullPointerException If no operation was specified.
	 * @since 2016/04/10
	 */
	CPVariables(CPOp __op, CPVerifyState __vs)
		throws NullPointerException
	{
		// Check
		if (__op == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/10
	 */
	@Override
	public Slot get(int __i)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/10
	 */
	@Override
	public int size()
	{
		throw new Error("TODO");
	}
	
	/**
	 * This is a variable slot,
	 *
	 * @since 2016/04/10
	 */
	public class Slot
	{
		private Slot()
		{
		}
	}
}

