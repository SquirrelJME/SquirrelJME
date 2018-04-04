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
 * This represents a widget which may be displayed on a display for a given
 * screen.
 *
 * @since 2018/04/04
 */
public interface UiDisplayable
	extends UiWidget
{
	/**
	 * Returns the title of the displayable.
	 *
	 * @return The displayable title.
	 * @since 2018/04/04
	 */
	public abstract String getTitle();
	
	/**
	 * Sets the title of the displayable.
	 *
	 * @param __t The title to set, {@code null} will set a default title if
	 * there is one.
	 * @since 2018/04/04
	 */
	public abstract void setTitle(String __t);
}

