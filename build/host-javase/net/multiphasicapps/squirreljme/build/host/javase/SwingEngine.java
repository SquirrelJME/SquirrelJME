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
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.DisplayCapabilityException;
import javax.swing.JFrame;
import net.multiphasicapps.squirreljme.lcdui.DisplayCanvasConnector;
import net.multiphasicapps.squirreljme.lcdui.DisplayConnector;
import net.multiphasicapps.squirreljme.lcdui.DisplayInstance;
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

	/**
	 * Initializes the base engine.
	 *
	 * @since 2017/02/08
	 */
	public SwingEngine()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/12
	 */
	@Override
	public boolean hasPointerEvents()
	{
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/12
	 */
	@Override
	public boolean hasPointerMotionEvents()
	{
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/08
	 */
	@Override
	public DisplayInstance setDisplayable(Displayable __d,
		DisplayConnector __c)
		throws DisplayCapabilityException, NullPointerException
	{
		// Check
		if (__d == null || __c == null)
			throw new NullPointerException("NARG");
		
		// Canvas
		if (__d instanceof Canvas)
			return new SwingCanvasInstance((Canvas)__d,
				(DisplayCanvasConnector)__c);
		
		// {@squirreljme.error BM0a The specified class cannot be shown by
		// this engine because it is not supported. (The class used for the
		// displayable))
		throw new DisplayCapabilityException(String.format("BM0a %s",
			__d.getClass()));
	}
}

