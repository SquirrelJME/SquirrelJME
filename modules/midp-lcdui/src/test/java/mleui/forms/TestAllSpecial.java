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
 * Tests all the special slots.
 *
 * @since 2020/07/18
 */
public class TestAllSpecial
	extends BaseUIForm
{
	/**
	 * {@inheritDoc}
	 * @since 2020/07/18
	 */
	@Override
	protected void test(UIBackend __backend, UIDisplayBracket __display,
		UIFormBracket __form)
	{
		int n = (-UIItemPosition.MIN_VALUE) + 1;
		UIItemBracket[] special = new UIItemBracket[n];
		UIItemBracket[] normals = new UIItemBracket[n];
		
		for (int i = 0; i < n; i++)
		{
			special[i] = __backend.itemNew(UIItemType.BUTTON);
			normals[i] = __backend.itemNew(UIItemType.SINGLE_LINE_TEXT_BOX);
		}
		
		// Add all the items
		for (int i = 0; i < n; i++)
		{
			// Skip the body so it is not there
			if (i > 0 && i != -UIItemPosition.BODY)
				__backend.formItemPosition(__form, special[i], -i);
			
			// Add normal items otherwise			
			__backend.formItemPosition(__form, normals[i], i);
		}
		
		// Remove all the items
		for (int i = n - 1; i >= 0; i--)
		{
			// Skip the body so it is not there
			if (i > 0 && i != -UIItemPosition.BODY)
				__backend.formItemRemove(__form, -i);
			
			// Add normal items otherwise			
			__backend.formItemRemove(__form, i);
		}
		
		// Cleanup
		for (int i = 1; i < n; i++)
		{
			__backend.itemDelete(special[i]);
			__backend.itemDelete(normals[i]);
		}
	}
}
