// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.javase.lcdui;

import cc.squirreljme.runtime.cldc.system.type.RemoteMethod;
import cc.squirreljme.runtime.cldc.task.SystemTask;
import cc.squirreljme.runtime.lcdui.gfx.PixelFormat;
import cc.squirreljme.runtime.lcdui.server.LcdDisplay;
import cc.squirreljme.runtime.lcdui.server.LcdWidget;
import java.awt.Dimension;
import javax.swing.JFrame;

/**
 * This represents a display which utilizes Java's Swing.
 *
 * @since 2018/03/18
 */
public class SwingDisplay
	extends LcdDisplay
{
	/** The frame which acts as the display. */
	final JFrame _frame;
	
	/** First initialization, used to center initially? */
	private volatile boolean _first;
	
	/**
	 * This makes the JFrames look more matching.
	 *
	 * @since 2018/03/24
	 */
	static
	{
		// This makes everything much faster
		JFrame.setDefaultLookAndFeelDecorated(true);
	}
	
	/**
	 * Initializes the display.
	 *
	 * @param __dx The display index.
	 * @param __cb The callback method for events.
	 * @since 2018/03/17
	 */
	public SwingDisplay(int __dx)
	{
		super(__dx);
		
		// Initialize the frame
		JFrame frame = new JFrame();
		this._frame = frame;
		
		// Use a basic default title
		frame.setTitle("SquirrelJME");
		
		// Exit on close
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Force minimum size to something more friendly
		frame.setMinimumSize(new Dimension(160, 160));
		frame.setPreferredSize(new Dimension(640, 480));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getHeight()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getWidth()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/18
	 */
	@Override
	protected final void internalSetCurrent(LcdWidget __w)
	{
		JFrame frame = this._frame;
		
		// Clearing the frame
		if (__w == null)
		{
			// Remove all widgets
			frame.removeAll();
			
			// Set default title
			frame.setTitle("SquirrelJME");
			
			// Hide it
			frame.setVisible(false);
		}
		
		// Setting it up
		else
		{
			// Add the widget's component to the frame
			frame.add(((SwingWidget)__w)._component);
		
			// Pack it
			frame.pack();
			
			// Setup frame position
			if (!this._first)
			{
				this._first = true;
				
				// Center it on screen
				frame.setLocationRelativeTo(null);
			}
			
			// Set the title of the frame to the displayable's title
			String title = __w.getTitle();
			frame.setTitle((title == null ? "SquirrelJME" : title));
			
			// Make it visible
			frame.setVisible(true);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/24
	 */
	@Override
	public final PixelFormat pixelFormat()
	{
		return ColorInfo.PIXEL_FORMAT;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void repaint(int __x, int __y, int __w, int __h)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/24
	 */
	@Override
	public void setContainedTitle(LcdWidget __w, String __t)
	{
		this._frame.setTitle(__t);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/19
	 */
	@Override
	public final void vibrate(int __ms)
	{
		// Does nothing on Swing
	}
}

