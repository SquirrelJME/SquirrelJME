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
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.mle.UIBackend;

/**
 * Tests dragging items across forms, which should fail.
 *
 * @since 2022/07/26
 */
public class TestCrossFormDragging
	extends BaseUIForm
{
	/**
	 * {@inheritDoc}
	 * @since 2022/07/26
	 */
	@Override
	protected void test(UIBackend __backend, UIDisplayBracket __display,
		UIFormBracket __form)
		throws Throwable
	{
		// The form to attempt dragging to
		UIFormBracket dragTo = __backend.formNew();
		
		// This item will be dragged around
		UIItemBracket item = __backend.itemNew(UIItemType.BUTTON);
		
		// Add to current form
		__backend.formItemPosition(__form, item, 0);
		
		// Set item on the other form
		boolean didFail;
		try
		{
			__backend.formItemPosition(dragTo, item, 0);
			this.secondary("failed", false);
		}
		catch (MLECallError e)
		{
			this.secondary("failed", true);
		}
	}
}
