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
 * This represents something which can have its long label set.
 *
 * @since 2018/04/06
 */
public interface UiHasSettableLongLabel
	extends UiHasViewableLongLabel, UiInterface
{
	/**
	 * Sets or clears the long label for an item.
	 *
	 * @param __s The long label to display, {@code null} clears it.
	 * @since 2018/04/04
	 */
	public abstract void setLongLabel(String __s);
}

