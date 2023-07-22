// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
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
	extends TestSupplier<String>
{
	/**
	 * Throws an exception.
	 *
	 * @since 2018/12/06
	 */
	public final String levelA()
	{
		return this.levelB();
	}
	
	/**
	 * Throws an exception.
	 *
	 * @since 2018/12/06
	 */
	public final String levelB()
	{
		throw new NoSuchElementException("TEST");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/06
	 */
	@Override
	public String test()
	{
		try
		{
			return this.levelA();
		}
		catch (NoSuchElementException t)
		{
			throw new IllegalArgumentException(t);
		}
	}
}

