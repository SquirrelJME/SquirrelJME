// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.bui.framebuffer.javase;

import java.awt.Dimension;
import net.multiphasicapps.squirreljme.bui.framebuffer.Framebuffer;
import javax.swing.JFrame;

/**
 * This class implements the framebuffer interface and provides the view and
 * the handling of input controls via the standard Swing interface.
 *
 * @since 2016/10/08
 */
public class JavaSEFramebuffer
	implements Framebuffer
{
	/** The frame used for the framebuffer. */
	private volatile JFrame _frame;
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/08
	 */
	@Override
	public boolean isInActiveMode()
	{
		// Not supported
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/08
	 */
	@Override
	public void setActiveMode(boolean __active)
	{
		// Display sleeping and/or screensaver inhibition is not supported
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/08
	 */
	@Override
	public boolean supportsInputEvents()
	{
		// Always supports input
		return true;
	}
	
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/08
	 */
	@Override
	public void setTitle(String __s)
	{
		// Pass it to the frame
		__frame().setTitle(__s);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/08
	 */
	@Override
	public void useDefaultMode()
	{
		// Just make it visible
		JFrame frame = __frame();
		boolean wasvis = frame.isVisible();
		frame.setVisible(true);
		
		// Center if it was not visible
		if (!wasvis)
			frame.setLocationRelativeTo(null);
	}
	
	/**
	 * Returns and potentially creates the JFrame used for the framebuffer.
	 *
	 * @return The frame to use.
	 * @since 2016/10/08
	 */
	private JFrame __frame()
	{
		JFrame rv = this._frame;
		if (rv != null)
			return rv;
		
		// Create it
		this._frame = (rv = new JFrame());
		
		// Force minimum size
		rv.setMinimumSize(new Dimension(320, 320));
		
		// Do nothing on close
		rv.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		// Return it
		return rv;
	}
}

