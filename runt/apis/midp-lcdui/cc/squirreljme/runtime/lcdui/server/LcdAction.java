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
 * This represents an action which has a label, image, and may be potentially
 * enabled or disabled.
 *
 * @since 2018/03/31
 */
public abstract class LcdAction
	extends LcdCollectable
{
	/**
	 * Initializes the base action.
	 *
	 * @param __handle The handle of the command.
	 * @param __type The type of collectable this is.
	 * @since 2018/03/31
	 */
	public LcdAction(int __handle, CollectableType __type)
	{
		super(__handle, __type);
	}
}

