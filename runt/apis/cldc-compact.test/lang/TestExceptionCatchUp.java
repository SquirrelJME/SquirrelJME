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
import java.util.NoSuchElementException;

/**
 * Tests exceptions being caught in higher stack frames.
 *
 * @since 2018/12/06
 */
public class TestExceptionCatchUp
	extends TestSupplier<Throwable>
{
	/**
	 * Throws an exception.
	 *
	 * @since 2018/12/06
	 */
	public final void levelA()
	{
		todo.DEBUG.note("A");
		this.levelB();
	}
	
	/**
	 * Throws an exception.
	 *
	 * @since 2018/12/06
	 */
	public final void levelB()
	{
		todo.DEBUG.note("B");
		throw new NoSuchElementException("TEST");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/06
	 */
	@Override
	public Throwable test()
	{
		try
		{
			todo.DEBUG.note("0");
			
			this.levelA();
			return null;
		}
		catch (NoSuchElementException t)
		{
			return t;
		}
	}
}

