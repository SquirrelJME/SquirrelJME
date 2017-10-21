// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.host.javase;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import javax.microedition.lcdui.Font;
import net.multiphasicapps.squirreljme.lcdui.DisplayHardwareState;
import net.multiphasicapps.squirreljme.lcdui.DisplayHead;
import net.multiphasicapps.squirreljme.lcdui.DisplayState;

/**
 * This is a display head which outputs to Swing.
 *
 * @since 2017/08/19
 */
public class SwingDisplayHead
	extends DisplayHead
{
	/**
	 * Initializes the display head.
	 *
	 * @since 2017/10/01
	 */
	public SwingDisplayHead()
	{
		// Make the display hardware enabled always
		setHardwareState(DisplayHardwareState.ENABLED);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/20
	 */
	@Override
	public int displayHeightMillimeters()
	{
		return (int)((displayHeightPixels() * 25.4) /
			(double)Toolkit.getDefaultToolkit().getScreenResolution());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/20
	 */
	@Override
	public int displayHeightPixels()
	{
		return GraphicsEnvironment.getLocalGraphicsEnvironment().
			getDefaultScreenDevice().getDisplayMode().getHeight();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/01
	 */
	@Override
	protected void displayStateChanged(DisplayState __old,
		DisplayState __new)
		throws NullPointerException
	{
		if (__old == null || __new == null)
			throw new NullPointerException("NARG");
		
		// The swing interface has no notion of foreground and background
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/20
	 */
	@Override
	public int displayWidthMillimeters()
	{
		return (int)((displayWidthPixels() * 25.4) /
			(double)Toolkit.getDefaultToolkit().getScreenResolution());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/20
	 */
	@Override
	public int displayWidthPixels()
	{
		return GraphicsEnvironment.getLocalGraphicsEnvironment().
			getDefaultScreenDevice().getDisplayMode().getWidth();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/20
	 */
	@Override
	public int fontSizeToPixelSize(int __s)
	{
		// There is really no real known way to get the pixel font size in
		// Swing and it is really really generic.
		// So the pixel sizes are calibrated for my display with the DPI
		// scaled accordingly
		// The pixel sizes of the font are determined to be the distance
		// between the lowest part of the o character in my word processor
		// in pixels, but regardless it should still work for the most
		// part.
		double dpimul = ((double)displayDpi() / 120.0);
		switch (__s)
		{
				// Small, 8 pt
			case Font.SIZE_SMALL:
				return (int)(15.0 * dpimul);
			
				// Large, 16 pt
			case Font.SIZE_LARGE:
				return (int)(23.0 * dpimul);
			
				// Default, 12 pt
			case Font.SIZE_MEDIUM:
			default:
				return (int)(30.0 * dpimul);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/01
	 */
	@Override
	protected void hardwareStateChanged(DisplayHardwareState __old,
		DisplayHardwareState __new)
		throws NullPointerException
	{
		if (__new == null)
			throw new NullPointerException("NARG");
		
		// The swing interface does not really care about this, so do
		// nothing at all
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/20
	 */
	@Override
	public boolean isColor()
	{
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/20
	 */
	@Override
	public int numColors()
	{
		return 256 * 256 * 256;
	}
}

