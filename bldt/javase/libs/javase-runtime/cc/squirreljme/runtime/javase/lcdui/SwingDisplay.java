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

import cc.squirreljme.runtime.cldc.task.SystemTask;
import cc.squirreljme.runtime.lcdui.server.LcdDisplay;
import cc.squirreljme.runtime.lcdui.server.LcdDisplayable;
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
	
	/** First initialization? */
	private volatile boolean _first;
	
	/**
	 * Initializes the display.
	 *
	 * @param __dx The display index.
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
	 * @since 2018/03/18
	 */
	@Override
	protected final void internalSetCurrent(LcdDisplayable __d)
	{
		JFrame frame = this._frame;
		
		// If clearing displayable remove it from the frame and make it
		// hidden
		if (__d == null)
		{
			// Use default title
			frame.setTitle("SquirrelJME");
			
			// Remove all components from the frame
			frame.removeAll();
			
			// Hide it
			frame.setVisible(false);
			return;
		}
		
		// Set this display to use the displayable's panel
		SwingDisplayable sd = (SwingDisplayable)__d;
		frame.add(sd._panel);
		
		// Setup frame position
		if (!this._first)
		{
			this._first = true;
			
			// Center it on screen
			frame.setLocationRelativeTo(null);
		}
		
		// Set the title of the frame to the displayable's title
		String title = sd.getTitle();
		frame.setTitle((title == null ? "SquirrelJME" : title));
		
		// Make it visible
		frame.setVisible(true);
	}
}

