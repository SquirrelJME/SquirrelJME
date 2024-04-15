// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lang;

import net.multiphasicapps.tac.TestSupplier;

/**
 * Tests new class instance.
 *
 * @since 2018/12/04
 */
public class TestNewInstance
	extends TestSupplier<String>
{
	/**
	 * {@inheritDoc}
	 * @since 2018/12/04
	 */
	@Override
	public String test()
		throws Throwable
	{
		return SpawnedClass.class.newInstance().toString();
	}
	
	/**
	 * The class to be spawned.
	 *
	 * @since 2018/12/04
	 */
	public static final class SpawnedClass
	{
		/**
		 * {@inheritDoc}
		 * @since 2018/12/04
		 */
		@Override
		public String toString()
		{
			return "Hello squirrels are cute!";
		}
	}
}

