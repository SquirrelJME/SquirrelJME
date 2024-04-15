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
import cc.squirreljme.jvm.mle.constants.UIItemPosition;
import cc.squirreljme.jvm.mle.constants.UIItemType;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.lcdui.mle.UIBackend;
import mleui.forms.BaseUIForm;

/**
 * This is the base class for any tests on lists.
 *
 * @since 2020/10/18
 */
public abstract class BaseList
	extends BaseUIForm
{
	/**
	 * Tests the list.
	 * 
	 * @param __backend The backend used.
	 * @param __display The display used.
	 * @param __form The form used.
	 * @param __list The list used.
	 * @throws Throwable On any exception.
	 * @since 2020/10/18
	 */
	public abstract void test(UIBackend __backend, UIDisplayBracket __display,
		UIFormBracket __form, UIItemBracket __list)
		throws Throwable;
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/18
	 */
	@Override
	protected void test(UIBackend __backend, UIDisplayBracket __display,
		UIFormBracket __form)
		throws Throwable
	{
		UIItemBracket list = __backend.itemNew(UIItemType.LIST);
		try
		{
			// Add to the form
			__backend.formItemPosition(__form, list, UIItemPosition.BODY);
			__backend.flushEvents();
			
			// Forward to the test
			this.test(__backend, __display, __form, list);
		}
		
		// Try deleting the item and freeing it up
		finally
		{
			try
			{
				__backend.formItemRemove(__form, UIItemPosition.BODY);
				__backend.itemDelete(list);
			}
			
			// Ignored otherwise
			catch (MLECallError e)
			{
				e.printStackTrace();
			}
		}
	}
}
