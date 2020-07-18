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
import cc.squirreljme.jvm.mle.constants.UIItemType;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Tests the creation of items.
 *
 * @since 2020/07/18
 */
public class TestCreateItems
	extends __BaseFormTest__
{
	/**
	 * {@inheritDoc}
	 * @since 2020/07/18
	 */
	@Override
	protected void uiTest(UIDisplayBracket __display, UIFormBracket __form)
	{
		for (int i = 0; i < UIItemType.NUM_TYPES; i++)
		{
			// Create the item
			UIItemBracket item = UIFormShelf.itemNew(i);
			
			// Then quickly delete it
			UIFormShelf.itemDelete(item);
		}
	}
}
