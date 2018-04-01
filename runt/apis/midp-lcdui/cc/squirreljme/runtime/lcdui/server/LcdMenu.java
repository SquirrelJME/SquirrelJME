// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.server;

import cc.squirreljme.runtime.lcdui.CollectableType;

/**
 * This represents a menu which may contain multiple sub-menus and commands.
 *
 * @since 2018/03/31
 */
public final class LcdMenu
	extends LcdAction
{
	/**
	 * Initializes the base menu with the given handle.
	 *
	 * @param __handle The handle of the command.
	 * @since 2018/03/31
	 */
	public LcdMenu(int __handle)
	{
		super(__handle, CollectableType.MENU);
	}
}

