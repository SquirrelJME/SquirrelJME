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
import cc.squirreljme.jvm.mle.brackets.UIItemBracket;
import cc.squirreljme.jvm.mle.constants.UIItemPosition;
import cc.squirreljme.jvm.mle.constants.UIItemType;
import cc.squirreljme.runtime.lcdui.mle.UIBackend;

/**
 * Drags an item across the form to ensure that it gets reassigned properly
 * and the item only appears at the given index.
 *
 * @since 2020/07/19
 */
public class TestItemDragging
	extends BaseUIForm
{
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	protected void test(UIBackend __backend, UIDisplayBracket __display,
		UIFormBracket __form)
	{
		// This item will be dragged around
		UIItemBracket item = __backend.itemNew(UIItemType.BUTTON);
		
		// Move around each position!
		int oldPos = UIItemPosition.NOT_ON_FORM;
		for (int pos = UIItemPosition.MIN_VALUE; pos <= 0; pos++)
		{
			// Is the old position null?
			if (oldPos != UIItemPosition.NOT_ON_FORM)
				this.secondary("old-isnull-" + pos, __backend
					.formItemAtPosition(__form, oldPos) == null);
			
			// The position of the item then
			this.secondary("then-pos-" + pos,
				__backend.formItemPosition(__form, item));
			
			// Set new item position
			__backend.formItemPosition(__form, item, pos);
			
			// Is the item at this position, this one?
			this.secondary("now-isthis-" + pos,
				__backend.equals(__backend.formItemAtPosition(__form, pos),
				item));
			
			// The position of the item now
			this.secondary("now-pos-" + pos,
				__backend.formItemPosition(__form, item));
			
			// Old position becomes the current one
			oldPos = pos;
		}
		
		this.secondary("zero-isthis", __backend.equals(
			__backend.formItemAtPosition(__form, 0), item));
		this.secondary("zero-pos",
			__backend.formItemPosition(__form, item));
	}
}
