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
 * Tests that item counts are correct in forms.
 *
 * @since 2020/07/19
 */
public class TestFormItemCounts
	extends BaseUIForm
{
	/** The number of items to add. */
	public static final int COUNT =
		16;
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	protected void test(UIBackend __backend, UIDisplayBracket __display,
		UIFormBracket __form)
	{
		// Before adding everything
		this.secondary("beforeall", __backend.formItemCount(__form));
		
		// Adding to the special items should not have any effect on size
		UIItemBracket left = __backend.itemNew(UIItemType.BUTTON);
		__backend.formItemPosition(__form, left,
			UIItemPosition.LEFT_COMMAND);
		this.secondary("leftcmd", __backend.formItemCount(__form));
		
		// Adding items should increase the item count
		UIItemBracket[] items = new UIItemBracket[TestFormItemCounts.COUNT];
		for (int i = 0; i < TestFormItemCounts.COUNT; i++)
		{
			items[i] = __backend.itemNew(UIItemType.BUTTON);
			
			__backend.formItemPosition(__form, items[i], i);
			
			this.secondary("afteradd-" + i,
				__backend.formItemCount(__form));
		}
		
		// Should be COUNT
		this.secondary("afteradds", __backend.formItemCount(__form));
		
		// Remove everything
		for (int i = TestFormItemCounts.COUNT - 1; i >= 0; i--)
		{
			this.secondary("removed-same-" + i, __backend.equals(
				__backend.formItemRemove(__form, i), items[i]));
			
			this.secondary("remove-" + i,
				__backend.formItemCount(__form));
		}
		
		// There should be nothing left
		this.secondary("afterall", __backend.formItemCount(__form));
	}
}
