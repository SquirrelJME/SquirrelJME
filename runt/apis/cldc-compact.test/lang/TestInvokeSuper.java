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

/**
 * Tests that super calls can be done.
 *
 * @since 2018/10/10
 */
public class TestInvokeSuper
	extends __InvokeSuper__
{
	/**
	 * {@inheritDoc}
	 * @since 2018/10/10
	 */
	@Override
	public void doSuper()
	{
		this.secondary("childbefore", true);
		
		super.doSuper();
		
		this.secondary("childafter", true);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/10
	 */
	@Override
	public void test()
	{
		this.doSuper();
	}
}

