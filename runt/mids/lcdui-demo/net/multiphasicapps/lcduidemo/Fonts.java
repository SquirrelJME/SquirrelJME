// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.lcduidemo;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

/**
 * Font demo, this essentially just draws the fonts which are available.
 *
 * @since 2018/11/23
 */
public class Fonts
	extends MIDlet
{
	/**
	 * {@inheritDoc}
	 * @since 2018/11/23
	 */
	@Override
	protected void destroyApp(boolean __uc)
		throws MIDletStateChangeException
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/23
	 */
	@Override
	protected void startApp()
		throws MIDletStateChangeException
	{
		// Setup canvas
		DemoCanvas cv = new DemoCanvas();
		
		// Set display to the canvas
		Display.getDisplay(this).setCurrent(cv);
	}
	
	/**
	 * The demo canvas which does the animation.
	 *
	 * @since 2018/11/23
	 */
	static public final class DemoCanvas
		extends Canvas
	{
		/** Fonts to use. */
		private final Font[] _fonts =
			Font.getAvailableFonts();
		
		/**
		 * Initializes the canvas.
		 *
		 * @since 2018/11/23
		 */
		{
			this.setTitle("Font Demo");
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/11/23
		 */
		@Override
		public void paint(Graphics __g)
		{
			// Start position
			int x = 10,
				y = 10;
			
			// Draw a sample for each font
			for (Font f : this._fonts)
			{
				// Use this font
				__g.setFont(f);
				
				// Draw sample
				__g.drawString("The quick gray squirrel burried all of " +
					"her acorns.", x, y, Graphics.BASELINE);
				
				// Move position
				y += f.getHeight();
			}
		}
	}
}

