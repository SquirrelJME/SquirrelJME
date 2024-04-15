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
import cc.squirreljme.jvm.mle.constants.UIItemType;
import cc.squirreljme.runtime.lcdui.mle.UIBackend;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Tests that removing items shifts them accordingly.
 *
 * @since 2020/07/19
 */
public class TestItemRemoveShift
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
		// Setup all the items and add to the form beforehand
		List<__Holder__> items = new ArrayList<>();
		for (int i = 0; i < UIItemType.NUM_TYPES; i++)
		{
			UIItemBracket item = __backend.itemNew(i);
			
			items.add(new __Holder__(item));
			__backend.formItemPosition(__form, item, i);
		}
		
		// We will be removing random elements!
		Random random = new Random(12);
		int removalCount = 0;
		while (!items.isEmpty())
		{
			int count = items.size();
			
			// Determine index to be removed
			int dx = (count == 1 ? 0 : random.nextInt(count));
			
			// Remove that item
			UIItemBracket old = __backend.formItemRemove(__form, dx);
			
			// Should both be the same items from the list
			this.secondary("same-" + removalCount,
				__backend.equals(old, items.remove(dx).item));
			
			// Count should be reduced by one
			int subCount = count - 1;
			this.secondary("subcount-" + removalCount,
				subCount == __backend.formItemCount(__form));
			
			// All of these should be the same item in the list
			boolean[] matches = new boolean[subCount];
			for (int j = 0; j < subCount; j++)
				matches[j] = __backend.equals(items.get(j).item,
					__backend.formItemAtPosition(__form, j));
			this.secondary("sameitems-" + removalCount, matches);
			
			// For the next run (in testing)
			removalCount++;
		}
		
		// Form should be empty
		this.secondary("empty", __backend.formItemCount(__form));
	}
}
