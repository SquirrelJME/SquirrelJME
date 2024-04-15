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
 * Tests that icon images are requested and drawn for list items.
 *
 * @since 2020/10/29
 */
public class TestListIconImage
	extends BaseList
{
	/**
	 * {@inheritDoc}
	 * @since 2020/10/29
	 */
	@Override
	public void test(UIBackend __backend, UIDisplayBracket __display,
		UIFormBracket __form, UIItemBracket __list)
	{
		// We need to register a callback and know when events occur
		ListCallback lc = new ListCallback();
		__backend.callback(__form, lc);
		
		// Make the list able to store an element
		__backend.widgetProperty(__list, UIWidgetProperty.INT_NUM_ELEMENTS,
			0, 1);
		__backend.widgetProperty(__list,
			UIWidgetProperty.STRING_LIST_ITEM_LABEL,
			0, "A list item!");
		__backend.flushEvents();
		
		// Draw various numbers of items
		ListItem item = new ListItem();
		for (int dim : new int[]{1, 2, 4, 8, 16, 32, 64})
		{
			// Store item information
			item._iconDimension = dim;
			item.into(__backend, __list, 0);
			
			// Hope that it gets a draw event
			__backend.flushEvents();
		}
		
		// Wait until drawing potentially happens
		__backend.flushEvents();
		
		// Icons which got painted?
		synchronized (lc)
		{
			this.secondary("painted", lc._painted);
		}
	}
}
