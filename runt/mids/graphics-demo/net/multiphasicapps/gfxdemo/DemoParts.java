// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.gfxdemo;

import javax.microedition.lcdui.Graphics;

/**
 * This draws the individual parts of the demo.
 *
 * @since 2018/03/23
 */
public final class DemoParts
{
	/**
	 * Not used.
	 *
	 * @since 2018/03/23
	 */
	private DemoParts()
	{
	}
	
	/**
	 * Paints the various parts of the demo.
	 *
	 * @param __g The graphics to paint o.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/23
	 */
	public static final void paint(Graphics __g)
		throws NullPointerException
	{
		if (__g == null)
			throw new NullPointerException("NARG");
		
		// Draw lines
		//DemoParts.paintLines(__g);
		
		// Rectangles
		DemoParts.paintRectangles(__g);
	}
	
	/**
	 * Paints a bunch of lines.
	 *
	 * @param __g The graphics to paint o.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/23
	 */
	public static final void paintLines(Graphics __g)
		throws NullPointerException
	{
		if (__g == null)
			throw new NullPointerException("NARG");
		
		for (int i = 0; i < 2; i++)
		{
			__g.translate((20 * i) - __g.getTranslateX(),
				(20 * i) - __g.getTranslateY());
			
			__g.setStrokeStyle((i == 0 ? Graphics.SOLID : Graphics.DOTTED));
			
			__g.setAlpha(255);
			
			__g.setColor(0xFF00FF);
			__g.drawLine(100, 10, 300, 10);
			
			__g.setColor(0xFFFF00);
			__g.drawLine(10, 100, 10, 300);
			
			__g.setAlphaColor(0x7F00FF00);
			__g.drawLine(-10, 150, 150, -10);
			
			__g.setAlphaColor(0x3FFF0000);
			__g.drawLine(20, 30, 2000, 50);
		}
	}
	
	/**
	 * Paints a bunch of rectangles.
	 *
	 * @param __g The graphics to paint o.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/25
	 */
	public static final void paintRectangles(Graphics __g)
		throws NullPointerException
	{
		if (__g == null)
			throw new NullPointerException("NARG");
		
		// Draw solid then alpha bits
		for (int z = 0; z < 2; z++)
		{
			int off = (z == 0 ? 0 : 40),
				mask = (z == 0 ? 0xFF000000 : 0x7F000000);
			
			__g.setAlphaColor(0xFFFFFF | mask);
			__g.fillRect(off + -10, off + -10, off + 20, off + 20);
			
			__g.setAlphaColor(0xFF0000 | mask);
			__g.fillRect(off + 30, off + 0, off + 10, off + 10);
			
			__g.setAlphaColor(0x00FFFF | mask);
			__g.fillRect(off + 45, off + 10, off + 10, off + 20);
			
			__g.setAlphaColor(0x0000FF | mask);
			__g.fillRect(off + 60, off + 20, off + 10, off + 9000);
			
			__g.setAlphaColor(0xFF00FF | mask);
			__g.fillRect(off + -40, off + 50, off + 9000, off + 70);
		}
	}	
}

