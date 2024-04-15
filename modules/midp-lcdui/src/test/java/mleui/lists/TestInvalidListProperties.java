// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package mleui.lists;

import cc.squirreljme.jvm.mle.brackets.UIDisplayBracket;
import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.jvm.mle.brackets.UIItemBracket;
import cc.squirreljme.jvm.mle.constants.UIWidgetProperty;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.lcdui.mle.UIBackend;

/**
 * Tests for invalid list properties.
 *
 * @since 2020/10/31
 */
public class TestInvalidListProperties
	extends BaseList
{
	/**
	 * {@inheritDoc}
	 * @since 2020/10/31
	 */
	@Override
	public void test(UIBackend __backend, UIDisplayBracket __display,
		UIFormBracket __form, UIItemBracket __list)
	{
		// Negative list size
		try
		{
			__backend.widgetProperty(__list, UIWidgetProperty.INT_NUM_ELEMENTS,
				0, -1);
			this.secondary("invalid-neg-size", 0);
		}
		catch (MLECallError e)
		{
			this.secondary("neg-size", true);
		}
	}
}
