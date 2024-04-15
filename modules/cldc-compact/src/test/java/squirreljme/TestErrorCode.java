// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package squirreljme;

import cc.squirreljme.runtime.cldc.debug.ErrorCode;
import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests error code support.
 *
 * @since 2023/07/19
 */
public class TestErrorCode
	extends TestRunnable
{
	/**
	 * {@inheritDoc}
	 * @since 2023/07/19
	 */
	@SuppressWarnings("squirreljme_qualifiedError")
	@Override
	public void test()
	{
		this.secondary("old", ErrorCode.__error__("ZZzz"));
		this.secondary("id", ErrorCode.__error__(1234));
	}
}
