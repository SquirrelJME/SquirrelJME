// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package mleui.forms;

import cc.squirreljme.jvm.mle.brackets.UIDisplayBracket;
import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.runtime.lcdui.mle.UIBackend;

/**
 * Tests that showing a blank form works properly.
 *
 * @since 2020/07/01
 */
public class TestShowBlankDisplay
	extends BaseUIForm
{
	/**
	 * {@inheritDoc}
	 * @since 2020/07/01
	 */
	@Override
	protected void test(UIBackend __backend, UIDisplayBracket __display,
		UIFormBracket __form)
	{
		// Does nothing because the form test shows this automatically
	}
}
