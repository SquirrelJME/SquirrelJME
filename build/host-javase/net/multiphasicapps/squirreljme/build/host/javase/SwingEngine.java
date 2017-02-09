// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.host.javase;

import java.awt.Dimension;
import javax.microedition.lcdui.Displayable;
import javax.swing.JFrame;
import net.multiphasicapps.squirreljme.lcdui.DisplayEngine;

/**
 * This is the display engine for Swing based systems.
 *
 * @since 2017/02/08
 */
public class SwingEngine
	implements DisplayEngine
{
	/** The display frame used. */
	protected final JFrame frame =
		new JFrame();
	
	/** The current thing to display. */
	private volatile Displayable _show;
	
	/** The title to use. */
	private volatile String _title;
	
	/** Needs repacking? */
	private volatile boolean _dopack;

	/**
	 * Initializes the base engine.
	 *
	 * @since 2017/02/08
	 */
	public SwingEngine()
	{
		// Exit on close
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Force a minimum size
		frame.setMinimumSize(new Dimension(160, 160));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/08
	 */
	@Override
	public void setDisplayable(Displayable __d)
	{
		// Set and update
		this._show = __d;
		this._dopack = true;
		update();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/08
	 */
	@Override
	public void setState(int __s)
	{
		// States have no effect on Swing
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/08
	 */
	@Override
	public void setTitle(String __s)
	{
		String use;
		this._title = (use = (__s != null ? __s : "SquirrelJME"));
		this.frame.setTitle(use);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/08
	 */
	@Override
	public void update()
	{
		// Showing something?
		Displayable show = this._show;
		if (show == null)
			return;
		
		// Make sure the frame is visible
		JFrame frame = this.frame;
		frame.setVisible(true);
		
		// Update the title to match what is displayed
		setTitle(show.getTitle());
		
		// Repack?
		if (this._dopack)
		{
			// Pack it
			frame.pack();
			this._dopack = false;
			
			// Center on screen
			frame.setLocationRelativeTo(null);
		}
	}
}

