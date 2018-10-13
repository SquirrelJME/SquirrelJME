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
 * Tests a member inner class being able to access a protected member in the
 * parent class.
 *
 * @since 2018/10/13
 */
public class TestInnerClassOnProtected
	implements TestSupplier<Integer>
{
	/** The value to use. */
	protected int value;
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/13
	 */
	@Override
	public Integer test()
	{
		new Inner();
		return this.value;
	}
	
	/**
	 * Inner class.
	 *
	 * @since 2018/10/13
	 */
	public class Inner
	{
		/**
		 * Sets the value.
		 *
		 * @since 2018/10/13
		 */
		public Inner()
		{
			TestInnerClassOnProtected.this.value = 1234;
		}
	}
}

