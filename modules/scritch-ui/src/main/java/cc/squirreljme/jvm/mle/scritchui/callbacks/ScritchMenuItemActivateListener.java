// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.scritchui.callbacks;

import cc.squirreljme.jvm.mle.scritchui.annotation.ScritchEventLoop;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchMenuKindBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchWindowBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * Listener for when a menu item is activated.
 *
 * @since 2024/07/30
 */
@SquirrelJMEVendorApi
public interface ScritchMenuItemActivateListener
	extends ScritchListener
{
	/**
	 * Called when a menu item has been activated.
	 *
	 * @param __window The window the activation occurred in.
	 * @param __menuItem The menu item that was activated.
	 * @since 2024/07/30
	 */
	@SquirrelJMEVendorApi
	@ScritchEventLoop
	void menuItemActivate(ScritchWindowBracket __window,
		ScritchMenuKindBracket __menuItem);
}
