// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.javase.lcdui;

import cc.squirreljme.runtime.lcdui.server.LcdTicker;

/**
 * This is a ticker which shows scrolling text.
 *
 * @since 2018/03/26
 */
public class SwingTicker
	extends LcdTicker
{
	/**
	 * Initializes the ticker.
	 *
	 * @param __handle The ticker handle.
	 * @since 2018/03/26
	 */
	public SwingTicker(int __handle)
	{
		super(__handle);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/26
	 */
	@Override
	protected void internalSetText(String __t)
		throws NullPointerException
	{
		throw new todo.TODO();
	}
}

