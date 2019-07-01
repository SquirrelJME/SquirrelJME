// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.squirrelquarrel;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

/**
 * Main entry point for the game.
 *
 * @since 2019/07/01
 */
public class MainMidlet
	extends MIDlet
{
	/**
	 * {@inheritDoc}
	 * @since 2019/07/01
	 */
	@Override
	protected void destroyApp(boolean __uc)
		throws MIDletStateChangeException
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/07/01
	 */
	@Override
	protected void startApp()
		throws MIDletStateChangeException
	{
		// Setup base game builder
		GameBuilder gb = new GameBuilder();
		
		// Setup and show the game interface
		GameInterface gi = new GameInterface(gb.build());
		
		throw new todo.TODO();
	}
}

