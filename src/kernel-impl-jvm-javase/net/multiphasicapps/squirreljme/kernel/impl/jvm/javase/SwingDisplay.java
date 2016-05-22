// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.impl.jvm.javase;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.ref.Reference;
import javax.swing.JFrame;
import net.multiphasicapps.squirreljme.ui.InternalDisplay;
import net.multiphasicapps.squirreljme.ui.UIDisplay;
import net.multiphasicapps.squirreljme.ui.UIException;

/**
 * This implemens the internal display in Swing.
 *
 * A display in Swing is mapped to a {@link JFrame}.
 *
 * @since 2016/05/21
 */
public class SwingDisplay
	extends InternalDisplay
	implements WindowListener
{
	/** The frame for the display. */
	protected final JFrame frame;
	
	/**
	 * Initializes the swing display.
	 *
	 * @param __ref The external reference.
	 * @since 2016/05/22
	 */
	public SwingDisplay(Reference<UIDisplay> __ref)
	{
		super(__ref);
		
		// Create the frame
		JFrame frame = new JFrame();
		this.frame = frame;
		
		// The frame may have a close callback event attached to it, so the
		// window cannot be normally closed unles disposed.
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		// Handle events
		frame.addWindowListener(this);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/22
	 */
	@Override
	public boolean isVisible()
		throws UIException
	{
		// Lock
		synchronized (this.lock)
		{
			return this.frame.isVisible();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/22
	 */
	@Override
	public void setVisible(boolean __vis)
		throws UIException
	{
		// Lock
		synchronized (this.lock)
		{
			this.frame.setVisible(__vis);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/22
	 */
	@Override
	public void windowActivated(WindowEvent __e)
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/22
	 */
	@Override
	public void windowClosed(WindowEvent __e)
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/22
	 */
	@Override
	public void windowClosing(WindowEvent __e)
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/22
	 */
	@Override
	public void windowDeactivated(WindowEvent __e)
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/22
	 */
	@Override
	public void windowDeiconified(WindowEvent __e)
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/22
	 */
	@Override
	public void windowIconified(WindowEvent __e)
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/22
	 */
	@Override
	public void windowOpened(WindowEvent __e)
	{
	}
}

