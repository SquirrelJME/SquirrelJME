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
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
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
	
	/** The character view. */
	protected final CharacterView view;
	
	/** The font to draw with. */
	protected final Font font;
	
	/** The font width and height. */
	protected final int fontw, fonth;
	
	/** Graphics configuration for the best image selection. */
	protected final GraphicsConfiguration config;
	
	/** The precomposed image buffer. */
	private volatile BufferedImage _buffer;
	
	/** Graphics for the image buffer. */
	private volatile Graphics2D _gfx;
	
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
		
		// Get the device graphics configuration
		GraphicsEnvironment ge = GraphicsEnvironment.
			getLocalGraphicsEnvironment();
		GraphicsDevice gs = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gs.getDefaultConfiguration();
		this.config = gc;
		
		// Create an initial 1x1 image to determine the size of the font
		BufferedImage toy = gc.createCompatibleImage(1, 1);
		Graphics2D toygfx = toy.createGraphics();
		
		// Use monospaced font
		Font font = Font.decode(Font.MONOSPACED);
		this.font = font;
		toygfx.setFont(font);
		
		// Get font character size
		FontMetrics fm = toygfx.getFontMetrics();
		int cw = fm.charWidth('S'),
			ch = fm.getHeight();
		this.fontw = cw;
		this.fonth = ch;
		
		// Setup character view
		CharacterView view = new CharacterView();
		this.view = view;
		
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
		// Has the console changed?
		if (hasChanged())
		{
			// Determine the size of the terminal and get the details
			int cw = this.fontw, ch = this.fonth;
			int tc, tr, tw, th, cells;
			char[] chars;
			byte[] attrs;
			synchronized (lock)
			{
				tc = getColumns();
				tr = getRows();
				chars = rawChars();
				attrs = rawAttributes();
			}
			
			// Width and height
			cells = tc * tr;
			tw = tc * cw;
			th = tr * ch;
			
			// Need to (re-)create the buffer?
			BufferedImage buffer = this._buffer;
			Graphics2D gfx = this._gfx;
			if (buffer == null || buffer.getWidth() != tw ||
				buffer.getHeight() != th)
			{
				this._buffer = buffer = this.config.createCompatibleImage(tw,
					th);
				this._gfx = gfx = (Graphics2D)buffer.getGraphics();
			}
			
			// Use monospaced font
			gfx.setFont(this.font);
			
			// Wipe over the entire background
			gfx.setColor(Color.BLACK);
			gfx.fillRect(0, 0, tw, th);
			
			// Draw using a single loop
			int head = tw - cw;
			for (int i = cells - 1, dx = head, dy = th; i >= 0; i--, dx -= cw)
			{
				// Previous row?
				if (dx < 0)
				{
					dx = head;
					dy -= ch;
				}
				
				// Get character data and attribute here
				byte a = attrs[i];
				
				// Draw new background colors over black
				
				// Draw the character
				gfx.setColor(Color.LIGHT_GRAY);
				gfx.drawChars(chars, i, 1, dx, dy);
			}
			
			// Force redraw of the console display
			this.frame.repaint();
		}
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
		/** Has the view size been corrected? */
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
			
			// Fix it?
			if (!this._fixed)
			{
				SwingConsoleView.this.sizeChanged();
				frame.setLocationRelativeTo(null);
			}
			
			// Draw precomposed console image
			BufferedImage bi = SwingConsoleView.this._buffer;
			if (bi != null)
				__g.drawImage(bi, 0, 0, null);
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
			int nw = __c * Math.max(1, fontw);
			int nh = __r * Math.max(1, fonth);
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

