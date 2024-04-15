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
import net.multiphasicapps.tac.TestRunnable;

/**
 * This ensures that throwing excpetions within synchronized methods works
 * properly.
 *
 * @since 2019/12/24
 */
public class TestThrowInSynchronized
	extends TestRunnable
{
	/**
	 * {@inheritDoc}
	 * @since 2019/12/24
	 */
	@Override
	public void test()
	{
		synchronized (new Object())
		{
			TestThrowInSynchronized.levelA(new Object());
		}
	}
	
	/**
	 * First level call.
	 *
	 * @param __v The value.
	 * @since 2019/12/24
	 */
	public static final void levelA(Object __v)
	{
		synchronized (__v)
		{
			TestThrowInSynchronized.levelB(__v);
		}
	}
	
	/**
	 * Second level call.
	 *
	 * @param __v The value.
	 * @since 2019/12/24
	 */
	public static final void levelB(Object __v)
	{
		synchronized (__v)
		{
			throw new NoSuchElementException();
		}
	}
}

