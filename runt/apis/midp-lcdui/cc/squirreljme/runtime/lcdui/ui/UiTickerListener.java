// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.ui;

/**
 * This represents something that is currently listening for changes to text
 * from a ticker.
 *
 * @since 2018/04/04
 */
public interface UiTickerListener
	extends UiInterface
{
	/**
	 * This is called when the text has been changed.
	 *
	 * @param __t The ticker which was changed.
	 * @param __s The new string to be displayed.
	 * @since 2018/04/04
	 */
	public abstract void changedTickerString(UiTicker __t, String __s);
}

