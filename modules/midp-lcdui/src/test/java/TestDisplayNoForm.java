// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

import cc.squirreljme.jvm.mle.brackets.UIDisplayBracket;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.mle.UIBackend;
import mleui.BaseBackend;

/**
 * Tests that a display is shown without a form.
 *
 * @since 2023/01/14
 */
public class TestDisplayNoForm
	extends BaseBackend
{
	/**
	 * {@inheritDoc}
	 * @since 2023/01/14
	 */
	@Override
	public void test(UIBackend __backend, UIDisplayBracket __display)
		throws Throwable
	{
		// Show it
		__backend.displayShow(__display, true);
		
		// Hide it
		__backend.displayShow(__display, false);
	}
}
