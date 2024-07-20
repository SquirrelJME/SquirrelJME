// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.scritchui;

import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchMenuBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.microedition.lcdui.Menu;
import org.jetbrains.annotations.Async;

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
	 * @param __scritch The ScritchUI interface.
	 * @param __item The item to bind to.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/18
	 */
	protected MenuLayoutMenu(ScritchInterface __scritch, Menu __item)
		throws NullPointerException
	{
		super(__scritch, __item);
	}
}
