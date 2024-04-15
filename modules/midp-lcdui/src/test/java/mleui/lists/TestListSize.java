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
import cc.squirreljme.runtime.lcdui.mle.UIBackend;

/**
 * Tests setting of the list size.
 *
 * @since 2020/10/18
 */
public class TestListSize
	extends BaseList
{
	/** The size to use. */
	public static final byte SIZE =
		6;
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/18
	 */
	@Override
	public void test(UIBackend __backend, UIDisplayBracket __display,
		UIFormBracket __form, UIItemBracket __list)
	{
		this.secondary("start-size", __backend.widgetPropertyInt(__list,
			UIWidgetProperty.INT_NUM_ELEMENTS, 0));
		
		// Resize the list
		__backend.widgetProperty(__list, UIWidgetProperty.INT_NUM_ELEMENTS,
			0, TestListSize.SIZE);
		this.secondary("first-size", __backend.widgetPropertyInt(__list,
			UIWidgetProperty.INT_NUM_ELEMENTS, 0));
		
		// Empty the list
		__backend.widgetProperty(__list, UIWidgetProperty.INT_NUM_ELEMENTS,
			0, 0);
		this.secondary("empty-size", __backend.widgetPropertyInt(__list,
			UIWidgetProperty.INT_NUM_ELEMENTS, 0));
		
		// Add elements back
		__backend.widgetProperty(__list, UIWidgetProperty.INT_NUM_ELEMENTS,
			0, TestListSize.SIZE);
		this.secondary("last-size", __backend.widgetPropertyInt(__list,
			UIWidgetProperty.INT_NUM_ELEMENTS, 0));
	}
}
