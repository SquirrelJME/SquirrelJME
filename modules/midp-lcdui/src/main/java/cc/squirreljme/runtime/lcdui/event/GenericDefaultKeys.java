// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.event;

import cc.squirreljme.jvm.mle.constants.NonStandardKey;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * Generic default key assignments.
 *
 * @since 2026/05/12
 */
public interface GenericDefaultKeys
{
	/** Down arrow. */
	int ARROW_DOWN =
		NonStandardKey.KEY_DOWN;

	/** Left arrow. */
	int ARROW_LEFT =
		NonStandardKey.KEY_LEFT;

	/** Right arrow. */
	int ARROW_RIGHT =
		NonStandardKey.KEY_RIGHT;

	/** Up arrow. */
	int ARROW_UP =
		NonStandardKey.KEY_UP;

	/** Back/CLR key. */
	int MENU_BACK =
		NonStandardKey.SHIFT;

	/** First menu item. */
	int MENU_ITEM_1 =
		NonStandardKey.F1;
	
	/** Second menu item. */
	int MENU_ITEM_2 =
		NonStandardKey.F2;
	
	/** Third menu item. */
	int MENU_ITEM_3 =
		NonStandardKey.F3;
}
