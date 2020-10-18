// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package mleui.lists;

import cc.squirreljme.jvm.mle.brackets.UIDisplayBracket;
import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.jvm.mle.brackets.UIItemBracket;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.mle.UIBackend;

/**
 * Tests resetting of list items after a list has been resized and those items
 * were there.
 *
 * @since 2020/10/18
 */
public class TestListItemReset
	extends BaseList
{
	/**
	 * {@inheritDoc}
	 * @since 2020/10/18
	 */
	@Override
	public void test(UIBackend __backend, UIDisplayBracket __display,
		UIFormBracket __form, UIItemBracket __list)
	{
		throw Debugging.todo();
	}
}
