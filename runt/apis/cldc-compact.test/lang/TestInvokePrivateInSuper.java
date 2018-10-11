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
 * Tests calling a private method where the extending class also has a method
 * with the same name and descriptor, but is public.
 *
 * @since 2018/10/10
 */
public class TestInvokePrivateInSuper
	extends __InvokePrivateInSuper__
{
	/**
	 * {@inheritDoc}
	 * @since 2018/10/10
	 */
	@Override
	public void test()
	{
		this.doSuper();
	}
	
	/**
	 * Private method in current class, named the same but is public since it
	 * was hidden in a super class.
	 *
	 * @since 2018/10/10
	 */
	public void doPrivate()
	{
		this.secondary("instanceprivatewascalled", false);
	}
}

