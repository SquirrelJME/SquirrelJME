// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.lcduidemo;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.lcdui.font.FontUtilities;
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
 * @since 2026/01/15
 */
@SquirrelJMEVendorApi
public class Fonts
	extends MIDlet
{
	/** Font index mapping. */
	public static final int[] INDEX_MAP = new int[] {
			Font.FACE_SYSTEM,
			Font.FACE_MONOSPACE,
			Font.FACE_PROPORTIONAL,
			~Font.FONT_STATIC_TEXT,
			~Font.FONT_INPUT_TEXT,
			~Font.FONT_IDLE_TEXT,
			~Font.FONT_IDLE_HIGHLIGHTED_TEXT,
		};
	
	/** Font index names. */
	public static final String[] INDEX_NAMES = new String[] {
			"System",
			"Monospace",
			"Proportional",
			"Static Text (MIDP 3)",
			"Input Text (MIDP 3)",
			"Idle Text (MIDP 3)",
			"Idle Highlighted Text (MIDP 3)",
		};
	
	/** The style mask. */
	public static final int STYLE_MASK =
		Font.STYLE_BOLD | Font.STYLE_ITALIC | Font.STYLE_UNDERLINED;
	
	/** The number of characters in the width of a page. */
	public static final int PAGE_WIDTH =
		16;
	
	/** The maximum page base permitted. */
	public static final int MAX_PAGE_BASE =
		65536 - Fonts.PAGE_WIDTH;
	
	/**
	 * {@inheritDoc}
	 * @since 2026/01/15
	 */
	@Override
	protected void destroyApp(boolean __uc)
		throws MIDletStateChangeException
	{
		// Do nothing
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/01/15
	 */
	@Override
	protected void startApp()
		throws MIDletStateChangeException
	{
		// Just make a viewport and use it
		Display.getDisplay(this).setCurrent(new Fonts.Viewport());
	}
	
	/**
	 * The viewport canvas.
	 *
	 * @since 2025/12/22
	 */
	public static final class Viewport
		extends Canvas
	{
		/** The current font index. */
		protected volatile int index;
		
		/** Which character page to draw? */
		protected volatile int charPage =
			-1;
		
		/** The current font style. */
		protected volatile int style =
			Font.STYLE_PLAIN;
		
		/** The font size. */
		protected volatile int size =
			Font.getDefaultFont().getPixelSize();
		
		/**
		 * Initializes the viewport.
		 *
		 * @since 2026/01/15
		 */
		public Viewport()
		{
			this.setTitle("Font Demo");
			
			// Exit command
			this.addCommand(Exit.command);
			this.setCommandListener(new Exit());
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2026/01/15
		 */
		@Override
		protected void paint(Graphics __g)
		{
			Font font = Font.getDefaultFont();
			
			// Determine the actual font to use
			int index = Fonts.INDEX_MAP[this.index % Fonts.INDEX_MAP.length];
			try
			{
				// One's complement are the "specification" fonts
				if ((index & 0x8000_0000) != 0)
					font = Font.getFont(~index);
				else
					font = Font.getFont(index,
						this.style & Fonts.STYLE_MASK,
						FontUtilities.pixelSizeToLogicalSize(this.size));
				
				// Derive then set the font
				font = font.deriveFont(this.style & Fonts.STYLE_MASK,
					this.size);
			}
			catch (IllegalArgumentException __e)
			{
				__e.printStackTrace();
			}
			
			// Use the newly derived font
			__g.setFont(font);
			
			// Height of the font, for rows
			int fh = font.getHeight();
			
			// Base draw position
			int ix = 2;
			int iy = 2 + fh;
			
			// Draw the baseline
			int oldColor = __g.getColor();
			__g.setColor(0xFF7900);
			__g.drawLine(0, fh, 128, fh);
			__g.setColor(oldColor);
			
			// Draw basic font information
			int charPage = this.charPage;
			__g.drawString(String.format("%s %d %s%s%s [pg. %04x]",
					Fonts.INDEX_NAMES[this.index], this.size,
					((this.style & Font.STYLE_BOLD) != 0 ? "B" : ""),
					((this.style & Font.STYLE_ITALIC) != 0 ? "I" : ""),
					((this.style & Font.STYLE_UNDERLINED) != 0 ? "U" : ""),
					charPage),
				ix, iy, Graphics.BASELINE);
			
			// Move on
			iy += fh;
			
			// Are we drawing the basic strings, or a character page?
			if (charPage < 0)
			{
				// Yes I do know buried is spelt wrong, but this is staying
				// here because a transphobe troll searched through all of my
				// images on Twitter and chose to insult me here. So this is
				// here to spite them.
				__g.drawString("The quick gray squirrel Xer burried all " +
					"of their acorns! <3 0123456789",
					ix, iy, Graphics.BASELINE);
				iy += fh;
				
				// And to spite them even more!
				__g.drawString("Trans rights! <3",
					ix, iy, Graphics.BASELINE);
				iy += fh;
				
				// Lowercase
				__g.drawString("abcdefghijklmnopqrstuvwxyz",
					ix, iy, Graphics.BASELINE);
				iy += fh;
				
				// Uppercase
				__g.drawString("ABCDEFGHIJKLMNOPQRSTUVWXYZ", 
					ix, iy, Graphics.BASELINE);
				iy += fh;
				
				// Letters and symbols
				__g.drawString("01234567890!@#$%^&*()\",",
					ix, iy, Graphics.BASELINE);
				iy += fh;
				
				// Polish
				__g.drawString("Wiewi\u00F3rki jest uroczy!",
					ix, iy, Graphics.BASELINE);
				iy += fh;
				
				// Japanese
				__g.drawString(
					"\u305D\u308C\u306F\u591A\u5206\u7686\u3082\u3046" +
						"\u5206\u304B\u3063\u3066\u308B\u3068\u601D\u3046" +
						"\u3051\u3069\u3002\u3002\u3002",
					ix, iy, Graphics.BASELINE);
				iy += fh;
				__g.drawString(
					"\u30EA\u30B9\u3068\u3063\u3066\u3082\u304B\u308F" +
						"\u3044\u3044\uFF01",
					ix, iy, Graphics.BASELINE);
				iy += fh;
			}
			
			// Draw character sheet instead
			else
			{
				// The canvas size
				int ph = this.getHeight();
				int max = ph - iy;
				int baseIX = ix;
				int baseIY = iy;
				
				// There is no real way to determine the best width for a
				// given character
				int cw = 1;
				cw = Math.max(cw, font.charWidth('@'));
				cw = Math.max(cw, font.charWidth('W'));
				cw = Math.max(cw, font.charWidth('M'));
				cw = Math.max(cw, font.charWidth('%'));
				cw = Math.max(cw, font.charWidth('~'));
				cw = Math.max(cw, font.charWidth('_'));
				
				// A more sensible width
				int pw = cw * Fonts.PAGE_WIDTH;
				
				// Draw the background grid
				__g.setColor(0xFF7900);
				
				// Vertical
				ix = baseIX;
				for (int l = 0; l < Fonts.PAGE_WIDTH; l++, ix += cw)
					__g.drawLine(ix, baseIY, ix, ph);
				
				// Horizontal
				for (iy = baseIY; iy < max; iy += fh)
					__g.drawLine(baseIX, iy, pw, iy);
				__g.setColor(oldColor);
				
				// Cannot exceed the max height of the canvas
				int atChar = charPage;
				for (iy = baseIY + fh, ix = baseIX; iy <= (max + fh);
					 iy += fh, ix = baseIX)
					for (int l = 0; l < Fonts.PAGE_WIDTH; l++, ix += cw)
					{
						// Which character is being drawn?
						char dc = (char)(atChar++);
						
						// Center the character on the block
						int dw = Math.max(1, font.charWidth(dc));
						__g.drawChar(dc, ix + 
								Math.max(0, ((cw >> 2) - (dw >> 2))), iy,
							Graphics.BASELINE);
					}
			}
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2025/12/22
		 */
		@SuppressWarnings("NonAtomicOperationOnVolatileField")
		@Override
		protected void keyPressed(int __code)
		{
			int charPage = this.charPage;
			switch (this.getGameAction(__code))
			{
				case Canvas.UP:
					this.size = Math.max(1, this.size + 1);
					break;
					
				case Canvas.DOWN:
					this.size = Math.max(1, this.size - 1);
					break;
					
				case Canvas.LEFT:
					this.index = Math.max(0, this.index - 1);
					break;
					
				case Canvas.RIGHT:
					this.index = Math.min(Fonts.INDEX_MAP.length - 1,
						this.index + 1);
					break;
					
				case Canvas.GAME_A:
					this.style = ((this.style + 1) & Fonts.STYLE_MASK);
					break;
					
				case Canvas.GAME_B:
					this.style = ((this.style - 1) & Fonts.STYLE_MASK);
					break;
					
				case Canvas.GAME_C:
					this.charPage = Math.max(-1,
						Math.min(Fonts.MAX_PAGE_BASE,
							charPage - Fonts.PAGE_WIDTH));
					break;
					
				case Canvas.GAME_D:
					if (charPage < 0)
						this.charPage = 0;
					else
						this.charPage = Math.min(Fonts.MAX_PAGE_BASE,
							charPage + Fonts.PAGE_WIDTH);
					break;
			}
			
			// Request redraw
			this.repaint();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2025/12/22
		 */
		@Override
		protected void keyRepeated(int __code)
		{
			// Just treat as a press
			this.keyPressed(__code);
		}
	}
}

