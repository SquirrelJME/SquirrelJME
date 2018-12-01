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

import java.util.ArrayList;
import java.util.List;
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
		private final Font[] _fonts;
		
		/**
		 * Initializes the canvas.
		 *
		 * @since 2018/11/23
		 */
		{
			this.setTitle("Font Demo");
			
			// Try to get fonts of various pixel sizes
			List<Font> fonts = new ArrayList<>();
			for (Font f : Font.getAvailableFonts())
				for (int i = 4; i <= 36; i += 4)
					try
					{
						fonts.add(f.deriveFont(i));
					}
					catch (IllegalArgumentException e)
					{
					}
			
			// Set font list
			this._fonts = fonts.<Font>toArray(new Font[fonts.size()]);
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
			
			// Draw a sample for each font, for each size!
			for (Font f : this._fonts)
			{
				// Use this font
				__g.setFont(f);
				
				// Draw sample
				__g.drawString("The quick gray squirrel burried all of " +
					"her acorns! 01234567", x, y, Graphics.BASELINE);
				
				// Move position with some extra room
				y += f.getHeight() + 5;
			}
		}
	}
}

