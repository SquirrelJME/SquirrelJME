// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package squirreljme.mle.forms;

import cc.squirreljme.jvm.mle.UIFormShelf;
import cc.squirreljme.jvm.mle.brackets.UIDisplayBracket;
import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.jvm.mle.brackets.UIItemBracket;
import cc.squirreljme.jvm.mle.constants.UIItemPosition;
import cc.squirreljme.jvm.mle.constants.UIItemType;

/**
 * Drags an item across the form to ensure that it gets reassigned properly
 * and the item only appears at the given index.
 *
 * @since 2020/07/19
 */
public class TestItemDragging
	extends __BaseFormTest__
{
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	protected void uiTest(UIDisplayBracket __display, UIFormBracket __form)
	{
		// This item will be dragged around
		UIItemBracket item = UIFormShelf.itemNew(UIItemType.BUTTON);
		
		// Move around each position!
		int oldPos = UIItemPosition.NOT_ON_FORM;
		for (int pos = UIItemPosition.MIN_VALUE; pos <= 0; pos++)
		{
			// Is the old position null?
			if (oldPos != UIItemPosition.NOT_ON_FORM)
				this.secondary("old-isnull-" + pos, UIFormShelf
					.formItemAtPosition(__form, oldPos) == null);
			
			// The position of the item then
			this.secondary("then-pos-" + pos,
				UIFormShelf.formItemPosition(__form, item));
			
			// Set new item position
			UIFormShelf.formItemPosition(__form, item, pos);
			
			// Is the item at this position, this one?
			this.secondary("now-isthis-" + pos,
				UIFormShelf.equals(UIFormShelf.formItemAtPosition(__form, pos),
				item));
			
			// The position of the item now
			this.secondary("now-pos-" + pos,
				UIFormShelf.formItemPosition(__form, item));
			
			// Old position becomes the current one
			oldPos = pos;
		}
		
		this.secondary("zero-isthis", UIFormShelf.equals(
			UIFormShelf.formItemAtPosition(__form, 0), item));
		this.secondary("zero-pos",
			UIFormShelf.formItemPosition(__form, item));
	}
}
