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
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.lcdui.mle.UIBackend;

/**
 * Tests that items cannot be used after deletion.
 *
 * @since 2020/07/19
 */
public class TestUseItemAfterDelete
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
		// Create the item
		UIItemBracket item = __backend.itemNew(UIItemType.BUTTON);
		
		// Quickly delete it so it is not valid
		__backend.itemDelete(item);
		
		// Attempt to place it on the form
		try
		{
			__backend.formItemPosition(__form, item, 0);
		}
		catch (MLECallError e)
		{
			throw new FormTestException(e);
		}
	}
}
