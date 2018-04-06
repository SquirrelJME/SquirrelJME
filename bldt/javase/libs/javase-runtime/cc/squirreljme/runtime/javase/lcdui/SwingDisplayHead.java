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
import cc.squirreljme.runtime.lcdui.CollectableType;
import cc.squirreljme.runtime.lcdui.gfx.PixelFormat;
import cc.squirreljme.runtime.lcdui.ui.UiDisplay;
import cc.squirreljme.runtime.lcdui.ui.UiDisplayHead;
import cc.squirreljme.runtime.lcdui.ui.UiTicker;
import cc.squirreljme.runtime.lcdui.ui.UiWidget;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 * This represents a display which utilizes Java's Swing.
 *
 * @since 2018/03/18
 */
public class SwingDisplayHead
	implements UiDisplayHead
{
	/** The display handle. */
	protected final int handle;
	
	/** The frame which acts as the display. */
	final JFrame _frame;
	
	/** The ticker which was shown. */
	private JComponent _usedticker;
	
	/** First initialization, used to center initially? */
	private boolean _first;
	
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
	 * @param __handle The display handle.
	 * @since 2018/03/17
	 */
	public SwingDisplayHead(int __handle)
	{
		this.handle = __handle;
		
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
	 * @since 2018/04/06
	 */
	@Override
	public void cleanup()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/06
	 */
	@Override
	public CollectableType collectableType()
	{
		return CollectableType.DISPLAY_HEAD;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/06
	 */
	@Override
	public int displayMaximumHeightPixels()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/06
	 */
	@Override
	public int displayMaximumWidthPixels()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/06
	 */
	@Override
	public int displayPhysicalHeightMillimeters()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/06
	 */
	@Override
	public int displayPhysicalWidthMillimeters()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/06
	 */
	@Override
	public UiDisplay getCurrentDisplay()
	{
		throw new todo.TODO();
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
	 * @since 2018/04/06
	 */
	@Override
	public final int handle()
	{
		return this.handle;
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
	 * @since 2018/03/18
	 */
	@Override
	public void setCurrentDisplay(UiDisplay __d)
	{
		throw new todo.TODO();
		/*
		JFrame frame = this._frame;
		JComponent usedticker = this._usedticker;
		
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
			// Add the widget's component to the centerframe
			frame.add(((SwingWidget)__w)._component,
				BorderLayout.CENTER);
			
			// Add the ticker, if there is any
			if (usedticker != null)
				frame.add(usedticker, BorderLayout.PAGE_START);
		
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
		*/
		/*// From ticker update code
		
		JFrame frame = this._frame;
		JComponent usedticker = this._usedticker;
		
		// Clear the old ticker first if there is one
		if (usedticker != null)
		{
			frame.remove(usedticker);
			this._usedticker = null;
		}
		
		// Setting a new one
		if (__t != null)
		{
			usedticker = ((SwingTicker)__t).createComponent();
			frame.add(usedticker, BorderLayout.PAGE_START);
			this._usedticker = usedticker;
		}
		*/
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

