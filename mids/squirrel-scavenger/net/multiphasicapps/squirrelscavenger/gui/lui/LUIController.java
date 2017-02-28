// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelscavenger.gui.lui;

import javax.microedition.key.InputDevice;
import javax.microedition.key.KeyListener;
import net.multiphasicapps.squirrelscavenger.game.player.Controller;

/**
 * This provides a controller which handles key inputs and provides actions
 * that are used by the controller.
 *
 * @since 2016/10/06
 */
public class LUIController
	implements Controller, KeyListener
{
	/**
	 * Initializes the controller interface.
	 *
	 * @param __id The input device to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/06
	 */
	public LUIController(InputDevice __id)
		throws NullPointerException
	{
		// Check
		if (__id == null)
			throw new NullPointerException("NARG");
		
		// Set
		__id.setKeyListener(this);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/06
	 */
	@Override
	public void keyPressed(InputDevice __dev, int __code, int __mod)
	{
		throw new todo.TODO();
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/10/06
	 */
	@Override
	public void keyReleased(InputDevice __dev, int __code, int __mod)
	{
		throw new todo.TODO();
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/10/06
	 */
	@Override
	public void keyRepeated(InputDevice __dev, int __code, int __mod)
	{
		throw new todo.TODO();
	}
}

