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
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.swing.JFrame;
import net.multiphasicapps.squirreljme.lcdui.DisplayConnector;
import net.multiphasicapps.squirreljme.lcdui.DisplayInstance;

/**
 * This provides access to a single instance of a swing display, since there
 * are many types of displayables that are supported this acts as just a base
 * class.
 *
 * @since 2017/02/08
 */
public abstract class SwingInstance
	implements DisplayInstance
{
	/** The frame used for the display. */
	protected final JFrame frame =
		new JFrame();
	
	/** The displayable to modify. */
	protected final Displayable displayable;
	
	/** The connector. */
	protected final DisplayConnector connector;
	
	/**
	 * Initializes the swing instance.
	 *
	 * @param __d The displayable being shown.
	 * @param __c The connector to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/08
	 */
	public SwingInstance(Displayable __d, DisplayConnector __c)
		throws NullPointerException
	{
		// Check
		if (__d == null || __c == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.displayable = __d;
		this.connector = __c;
		
		// Exit on close
		JFrame frame = this.frame;
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Set a title
		frame.setTitle(__d.getTitle());
		
		// Force minimum size to something more friendly
		frame.setMinimumSize(new Dimension(160, 160));
		frame.setPreferredSize(new Dimension(640, 480));
		
		// Make it visible
		frame.setVisible(true);
		
		// Pack it down
		frame.pack();
		frame.setLocationRelativeTo(null);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/08
	 */
	@Override
	public void destroy()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/26
	 */
	@Override
	public int getState()
	{
		// Applications are always in the foreground
		return Display.STATE_BACKGROUND;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/28
	 */
	@Override
	public void setFullScreen(boolean __f)
	{
		JFrame frame = this.frame;
		
		// Programatically maximize?
		int state = frame.getExtendedState();
		if (__f)
			state |= JFrame.MAXIMIZED_BOTH;
		else
			state &= JFrame.MAXIMIZED_BOTH;
		frame.setExtendedState(state);
		
		// If not maximized then repack and recenter
		if (!__f)
		{
			frame.pack();
			frame.setLocationRelativeTo(null);
		}
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
		this.frame.setTitle((__s != null ? __s : "SquirrelJME"));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/08
	 */
	@Override
	public void update()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/26
	 */
	@Override
	public boolean vibrate(int __d)
	{
		// Not supported in Swing
		return false;
	}
}

