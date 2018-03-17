// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.javase.lcdui;

import cc.squirreljme.runtime.cldc.task.SystemTask;
import cc.squirreljme.runtime.lcdui.DisplayableType;
import cc.squirreljme.runtime.lcdui.server.LcdDefinition;
import cc.squirreljme.runtime.lcdui.server.LcdDisplay;
import cc.squirreljme.runtime.lcdui.server.LcdDisplayable;
import cc.squirreljme.runtime.lcdui.server.LcdRequestHandler;
import cc.squirreljme.runtime.lcdui.server.LcdServer;

/**
 * This contains the implementation of the LCDUI server which utilizes Swing
 * to display graphics to the user.
 *
 * @since 2018/03/15
 */
public class SwingDefinition
	extends LcdDefinition
{
	/**
	 * Initializes the Swing LCDUI server.
	 *
	 * @since 2018/03/17
	 */
	public SwingDefinition()
	{
		// Have swing always invoke the request handler
		SwingUtilities.invokeLater(this.requestHandler());
	}
	
	
	
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/17
	 */
	@Override
	protected LcdDisplayable internalCreateDisplayable(Object __lock,
		int __handle, SystemTask __task, DisplayableType __type)
		throws NullPointerException
	{
		if (__lock == null || __task == null || __type == null)
			throw new NullPointerException("NARG");
		
		return new SwingDisplayable(__lock, __handle, __task, __type);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/17
	 */
	@Override
	protected LcdDisplay[] internalQueryDisplays(LcdDisplay[] __k)
		throws NullPointerException
	{
		if (__k == null)
			throw new NullPointerException("NARG");
		
		// Swing only uses a single display which is shared among all
		// programs
		if (__k.length == 0)
			return new LcdDisplay[]{new SwingDisplay(this.lock, 0)};
		return __k;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/16
	 */
	@Override
	protected LcdServer newLcdServer(SystemTask __task)
		throws NullPointerException
	{
		if (__task == null)
			throw new NullPointerException("NARG");
		
		return new SwingServer(__task, this);
	}
}

