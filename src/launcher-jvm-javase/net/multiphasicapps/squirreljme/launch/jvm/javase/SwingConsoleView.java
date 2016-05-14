// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.launch.jvm.javase;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JFrame;
import javax.swing.JPanel;
import net.multiphasicapps.squirreljme.launch.AbstractConsoleView;

/**
 * This provides a swing console view.
 *
 * @since 2016/05/14
 */
public class SwingConsoleView
	extends AbstractConsoleView
{
	/** The frame which displays the console graphics. */
	protected final JFrame frame;
	
	/** The font to use in the display. */
	protected final Font font;
	
	/** The character view. */
	protected final CharacterView view;
	
	/**
	 * Initializes the swing console view.
	 *
	 * @since 2016/05/14
	 */
	public SwingConsoleView()
	{
		// Setup the console view frame
		JFrame frame = new JFrame("SquirrelJME");
		this.frame = frame;
		
		// Make it exit on close
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Setup character view
		CharacterView view = new CharacterView();
		this.view = view;
		
		// Set font
		font = Font.decode(Font.MONOSPACED);
		
		// Add to the frame
		frame.add(view);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/14
	 */
	@Override
	public int defaultColumns()
	{
		return 80;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/14
	 */
	@Override
	public int defaultRows()
	{
		return 24;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/14
	 */
	@Override
	public void displayConsole()
	{
		// Force redraw of the console display
		this.frame.repaint();
	}
	
	/**
	 * Makes the console view visible.
	 *
	 * @since 2016/05/14
	 */
	public void setVisible()
	{
		// Make it shown
		this.frame.setVisible(true);
		
		// Center on screen
		this.frame.setLocationRelativeTo(null);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/14
	 */
	@Override
	protected void sizeChanged()
	{
		// Get the new size
		int cols = getColumns();
		int rows = getRows();
		
		// Fix the size
		CharacterView view = this.view;
		if (view != null)
			view.__fixSize(cols, rows);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/14
	 */
	@Override
	public boolean supportsSize(int __c, int __r)
	{
		// Supports terminals of any size, provided they are at least one
		// by one.
		return __c >= 1 && __r >= 1;
	}
	
	/**
	 * This is a character view which shows the terminal text.
	 *
	 * @since 2016/05/14
	 */
	@SuppressWarnings({"serial"})
	public class CharacterView
		extends JPanel
	{
		/** The character width and height. */
		volatile int _charw, _charh;
		
		private volatile boolean _fixed;
		
		/**
		 * Initializes the character view.
		 *
		 * @since 2016/05/14
		 */
		private CharacterView()
		{
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/05/14
		 */
		@Override
		public void paintComponent(Graphics __g)
		{
			// Call super paint
			super.paintComponent(__g);
			
			// Determine the size of the characters in pixels
			__g.setFont(font);
			FontMetrics fm = __g.getFontMetrics();
			
			// Set size
			int cw, ch;
			_charw = cw = fm.charWidth('S');
			_charh = ch = fm.getHeight();
			
			// Fix it?
			if (!this._fixed)
			{
				SwingConsoleView.this.sizeChanged();
				frame.setLocationRelativeTo(null);
			}
			
			// Get character and attribute data
			int tc, tr;
			char[] chars;
			byte[] attrs;
			synchronized (lock)
			{
				tc = SwingConsoleView.this.getColumns();
				tr = SwingConsoleView.this.getRows();
				chars = SwingConsoleView.this.rawChars();
				attrs = SwingConsoleView.this.rawAttributes();
			}
			
			// Get clipping area
			Rectangle rect = __g.getClipBounds();
			int sx = rect.x, sy = rect.y;
			int ww = rect.width, hh = rect.height;
			int ex = sx + ww, ey = sy + hh;
			
			// Determine which characters to actually draw
			int zsc = Math.max(0, (sx / cw) - 1),
				zec = Math.min(tc - 1, (ex / cw) + 1),
				zsr = Math.max(0, (sy / ch) - 1),
				zer = Math.min(tr - 1, (ey / ch) + 1);
			
			// Draw every character
			for (int r = zsr; r <= zer; r++)
			{
				// Calculate base draw position
				int gy = (r * ch);
				
				// Draw each character in the row
				for (int c = zsc, gx = (cw * c); c < zec; c++, gx += cw)
				{
					// Get character and attribute data
					int dx = (r * tc) + c;
					byte a = attrs[dx];
					
					// Clear the background
					__g.setColor(Color.BLACK);
					__g.fillRect(gx, gy, cw, ch);
			
					// Draw it
					__g.setColor(Color.LIGHT_GRAY);
					__g.drawChars(chars, dx, 1, gx, gy + ch);
				}
			}
		}
		
		/**
		 * Fixes the panel size.
		 *
		 * @param __c Column count.
		 * @param __r Row count.
		 * @since 2016/05/14
		 */
		void __fixSize(int __c, int __r)
		{
			// Setup dimension
			int nw = __c * Math.max(1, _charw);
			int nh = __r * Math.max(1, _charh);
			Dimension d = new Dimension(nw, nh);
			
			// Set everything
			setPreferredSize(d);
			setMinimumSize(d);
			setMaximumSize(d);
			setSize(d);
			
			// Pack it
			frame.pack();
			frame.validate();
			
			// Set
			this._fixed = true;
		}
	}
}

