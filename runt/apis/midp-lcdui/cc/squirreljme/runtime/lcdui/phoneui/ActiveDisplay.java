// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.phoneui;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import java.util.Arrays;

/**
 * This contains the display display along with the internal image buffer.
 *
 * @since 2019/05/16
 */
public final class ActiveDisplay
{
	/** The width of the display. */
	protected final int width;
	
	/** The height of the display. */
	protected final int height;
	
	/** The backing buffer image. */
	protected final Image image;
	
	/**
	 * Initializes the active display.
	 *
	 * @param __w The width.
	 * @param __h The height.
	 * @since 2019/05/16
	 */
	public ActiveDisplay(int __w, int __h)
	{
		// Set sizes
		this.width = __w;
		this.height = __h;
		
		// Setup buffer
		this.image = Image.createImage(__w, __h);
	}
	
	/**
	 * Paints whatever is in the active display.
	 *
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @param __w The width.
	 * @param __h The height.
	 * @since 2019/05/16
	 */
	public final void paint(int __x, int __y, int __w, int __h)
	{
		// Get display details
		Graphics dg = this.image.getGraphics();
		int dw = this.width,
			dh = this.height;
		
		// Draw box
		dg.setColor(0x0000FF);
		dg.fillRect(0, 0, dw, dh);
		
		// Draw an X
		dg.setColor(0xFFFF00);
		dg.drawLine(0, 0, dw, dh);
		dg.drawLine(0, dh, dw, 0);
	}
}

