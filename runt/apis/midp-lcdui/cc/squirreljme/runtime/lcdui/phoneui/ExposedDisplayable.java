// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.phoneui;

import cc.squirreljme.runtime.lcdui.SerializedEvent;
import javax.microedition.lcdui.Graphics;

/**
 * This is a base class for items which need to be exposed action wise and
 * such. All methods here by default do nothing.
 *
 * @since 2019/05/17
 */
public abstract class ExposedDisplayable
{
	/**
	 * Is this widget a full-screen one?
	 *
	 * @return If this is full-screen or not.
	 * @since 2019/05/17
	 */
	protected boolean isFullscreen()
	{
		return false;
	}
	
	/**
	 * Is this display transparent?
	 *
	 * @return If the display is transparent.
	 * @since 2019/05/17
	 */
	protected boolean isTransparent()
	{
		return true;
	}
	
	/**
	 * This is called when the displayable needs to be painted onto the
	 * screen.
	 *
	 * @param __g The graphics to draw into.
	 * @since 2019/05/17
	 */
	@SerializedEvent
	protected void paint(Graphics __g)
	{
	}
}

