// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.scritchui;

import cc.squirreljme.jvm.mle.scritchui.ScritchEventLoopInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchMenuBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchMenuItemBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.microedition.lcdui.Menu;

/**
 * Represents the layout state for a menu.
 *
 * @see ScritchMenuBracket
 * @since 2024/07/18
 */
@SquirrelJMEVendorApi
public class MenuLayoutMenu
	extends MenuLayoutBindableSub<Menu>
{
	/**
	 * Initializes the bindable.
	 *
	 * @param __loop The loop interface.
	 * @param __item The item to bind to.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/18
	 */
	protected MenuLayoutMenu(ScritchEventLoopInterface __loop,
		Menu __item)
		throws NullPointerException
	{
		super(__loop, __item);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/18
	 */
	@Override
	@SquirrelJMEVendorApi
	public void refresh()
		throws IllegalStateException
	{
		throw Debugging.todo();
	}
}
