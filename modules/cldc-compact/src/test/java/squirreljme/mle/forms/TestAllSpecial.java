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
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Tests all the special slots.
 *
 * @since 2020/07/18
 */
public class TestAllSpecial
	extends __BaseFormTest__
{
	/**
	 * {@inheritDoc}
	 * @since 2020/07/18
	 */
	@Override
	protected void uiTest(UIDisplayBracket __display, UIFormBracket __form)
		throws Throwable
	{
		int n = (-UIItemPosition.MIN_VALUE) + 1;
		UIItemBracket[] special = new UIItemBracket[n];
		UIItemBracket[] normals = new UIItemBracket[n];
		
		for (int i = 0; i < n; i++)
		{
			special[i] = UIFormShelf.itemNew(UIItemType.BUTTON);
			normals[i] = UIFormShelf.itemNew(UIItemType.SINGLE_LINE_TEXT_BOX);
		}
		
		// Add all the items
		for (int i = 0; i < n; i++)
		{
			// Skip the body so it is not there
			if (i > 0 && i != -UIItemPosition.BODY)
				UIFormShelf.formItemPosition(__form, special[i], -i);
			
			// Add normal items otherwise			
			UIFormShelf.formItemPosition(__form, normals[i], i);
		}
		
		// Wait for a bit
		try
		{
			Thread.sleep(3000);
		}
		catch (InterruptedException ignored)
		{
		}
		
		// Delete all the items
		for (int i = n - 1; i >= 0; i--)
		{
			// Skip the body so it is not there
			if (i > 0 && i != -UIItemPosition.BODY)
				UIFormShelf.formItemRemove(__form, -i);
			
			// Add normal items otherwise			
			UIFormShelf.formItemRemove(__form, i);
		}
		
		// Wait for a bit
		try
		{
			Thread.sleep(3000);
		}
		catch (InterruptedException ignored)
		{
		}
		
		// Cleanup
		for (int i = 1; i < n; i++)
		{
			UIFormShelf.itemDelete(special[i]);
			UIFormShelf.itemDelete(normals[i]);
		}
	}
}
