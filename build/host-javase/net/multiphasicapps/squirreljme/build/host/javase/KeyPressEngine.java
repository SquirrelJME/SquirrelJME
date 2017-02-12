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

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import net.multiphasicapps.squirreljme.lcdui.KeyEventCapableConnector;
import net.multiphasicapps.squirreljme.lcdui.KeyEventType;

/**
 * This is an engine which is used to convert AWT's rather nasty key event
 * handling across platforms into one which is more sane and matches LCDUI.
 *
 * @since 2017/02/12
 */
public class KeyPressEngine
	implements KeyListener
{
	/** The connector to send events to. */
	protected final KeyEventCapableConnector connector;
	
	/**
	 * Initializes the engine.
	 *
	 * @param __c The connector to send events to.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/12
	 */
	public KeyPressEngine(KeyEventCapableConnector __c)
		throws NullPointerException
	{
		// Check
		if (__c == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.connector = __c;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/12
	 */
	@Override
	public void keyPressed(KeyEvent __e)
	{
		// Debug
		System.err.printf("DEBUG -- AWT Pressed	: %s", __e);
	}

	/**
	 * {@inheritDoc}
	 * @since 2017/02/12
	 */
	@Override
	public void keyReleased(KeyEvent __e)
	{
		// Debug
		System.err.printf("DEBUG -- AWT Released: %s", __e);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/12
	 */
	@Override
	public void keyTyped(KeyEvent __e)
	{
		// Debug
		System.err.printf("DEBUG -- AWT Typed: %s", __e);
	}
}

