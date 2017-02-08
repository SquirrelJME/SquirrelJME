// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.lcdui;

import javax.microedition.lcdui.Display;

/**
 * This is a display engine that is used to determine .
 *
 * @since 2017/02/08
 */
public interface DisplayEngine
{
	/**
	 * Sets the title of the display.
	 *
	 * @param __s The title to use, if {@code null} then a default should be
	 * used instead.
	 * @since 2017/02/08
	 */
	public abstract void setTitle(String __s);
}

