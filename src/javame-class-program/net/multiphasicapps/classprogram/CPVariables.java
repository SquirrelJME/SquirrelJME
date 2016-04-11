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

