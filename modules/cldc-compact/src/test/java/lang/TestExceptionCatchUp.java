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

import java.util.NoSuchElementException;
import net.multiphasicapps.tac.TestSupplier;

/**
 * Tests exceptions being caught in higher stack frames.
 *
 * @since 2018/12/06
 */
public class TestExceptionCatchUp
	extends TestSupplier<Boolean>
{
	/**
	 * Throws an exception.
	 *
	 * @since 2018/12/06
	 */
	public final Boolean levelA()
	{
		return this.levelB();
	}
	
	/**
	 * Throws an exception.
	 *
	 * @since 2018/12/06
	 */
	public final Boolean levelB()
	{
		throw new NoSuchElementException("TEST");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/06
	 */
	@Override
	public Boolean test()
	{
		try
		{
			return this.levelA();
		}
		catch (NoSuchElementException t)
		{
			return true;
		}
	}
}

