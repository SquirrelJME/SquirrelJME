// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lang;

import net.multiphasicapps.tac.TestSupplier;

/**
 * Tests exceptions being caught in higher stack frames.
 *
 * @since 2018/12/06
 */
public class TestExceptionCatchUp
	extends TestSupplier<Throwable>
{
	/**
	 * {@inheritDoc}
	 * @since 2018/12/06
	 */
	@Override
	public Throwable test()
	{
		try
		{
			this.throwInHere();
			return null;
		}
		catch (Throwable t)
		{
			return t;
		}
	}
	
	/**
	 * Throws an exception.
	 *
	 * @throws Throwable always.
	 * @since 2018/12/06
	 */
	public final void throwInHere()
		throws Throwable
	{
		throw new Throwable("TEST");
	}
}

