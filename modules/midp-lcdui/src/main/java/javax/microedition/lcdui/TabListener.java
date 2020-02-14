// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.runtime.lcdui.SerializedEvent;

/**
 * This interface is used as a callback when there has been a change to the
 * tabbed pane such as which screen has been made visible or ones which were
 * added or removed.
 *
 * @since 2018/03/29
 */
public interface TabListener
{
	/**
	 * This is called to specify that the given tab along with the screen
	 * has been added to the tabbed pane.
	 *
	 * @param __i The index where insertion is to occur.
	 * @param __tab The screen that was added to the tabbed pane.
	 * @since 2018/03/29
	 */
	@SerializedEvent
	public abstract void tabAddedEvent(int __i, Screen __tab);
	
	/**
	 * This indicates when a new tab has been focused and will indicate the
	 * screen which now has focus.
	 *
	 * @param __tab The tab which now has focus.
	 * @since 2018/03/29
	 */
	@SerializedEvent
	public abstract void tabChangeEvent(Screen __tab);
	
	/**
	 * This is called after a tab has been removed and only specifies the
	 * index of that tab.
	 *
	 * @param __i The tab that has been removed.
	 * @since 2018/03/29
	 */
	@SerializedEvent
	public abstract void tabRemoveEvent(int __i);
}

