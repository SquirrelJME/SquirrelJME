// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreldigger.gui.lui;

import javax.microedition.key.InputDevice;
import javax.microedition.key.KeyListener;
import net.multiphasicapps.squirreldigger.game.player.Controller;

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
		throw new Error("TODO");
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/10/06
	 */
	@Override
	public void keyReleased(InputDevice __dev, int __code, int __mod)
	{
		throw new Error("TODO");
	}

	/**
	 * {@inheritDoc}
	 * @since 2016/10/06
	 */
	@Override
	public void keyRepeated(InputDevice __dev, int __code, int __mod)
	{
		throw new Error("TODO");
	}
}

