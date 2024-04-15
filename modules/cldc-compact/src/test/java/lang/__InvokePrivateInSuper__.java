// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lang;

import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests calling a private method where the extending class also has a method
 * with the same name and descriptor, but is public.
 *
 * @since 2018/10/10
 */
abstract class __InvokePrivateInSuper__
	extends TestRunnable
{
	/**
	 * Super method call.
	 *
	 * @since 2018/10/10
	 */
	public final void doSuper()
	{
		this.doPrivate();
	}
	
	/**
	 * Private method in superclass.
	 *
	 * @since 2018/10/10
	 */
	private void doPrivate()
	{
		this.secondary("superprivate", true);
	}
}

