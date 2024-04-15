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
 * Super class to test execution with.
 *
 * @since 2018/10/10
 */
abstract class __InvokeSuper__
	extends TestRunnable
{
	/**
	 * Tests the super call.
	 *
	 * @since 2018/10/10
	 */
	public void doSuper()
	{
		this.secondary("super", true);
	}
}

