// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package mleui.forms;

import cc.squirreljme.jvm.mle.brackets.UIDisplayBracket;
import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.jvm.mle.brackets.UIItemBracket;
import cc.squirreljme.jvm.mle.constants.UIItemType;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.lcdui.mle.UIBackend;

/**
 * Checks that deleting visible items fails.
 *
 * @since 2020/07/19
 */
public class TestDeleteVisibleItem
	extends __BaseFormTest__
{
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	protected void test(UIBackend __backend, UIDisplayBracket __display,
		UIFormBracket __form)
		throws Throwable
	{
		// Create the item and make it visible on the form
		UIItemBracket item = __backend.itemNew(UIItemType.BUTTON);
		__backend.formItemPosition(__form, item, 0);
		
		// Attempt to delete the item
		try
		{
			__backend.itemDelete(item);
		}
		catch (MLECallError e)
		{
			throw new FormTestException(e);
		}
	}
}
