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

import javax.swing.JFrame;
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
		throw new Error("TODO");
	}
	
	/**
	 * Makes the console view visible.
	 *
	 * @since 2016/05/14
	 */
	public void setVisible()
	{
		this.frame.setVisible(true);
	}
}

