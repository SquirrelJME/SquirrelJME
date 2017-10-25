// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.lcdui.widget;

import javax.microedition.lcdui.Graphics;

/**
 * This represents a canvas which can be embedded into a displayable.
 *
 * @since 2017/10/25
 */
public abstract class EmbeddedCanvas
	extends Embedded
{
	/**
	 * Returns the graphics to draw on this canvas.
	 *
	 * @return The graphics to draw on this canvas.
	 * @since 2017/10/25
	 */
	public abstract Graphics getGraphics();
}

