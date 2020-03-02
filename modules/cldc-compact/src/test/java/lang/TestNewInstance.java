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
	 * The class to be spawed.
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

