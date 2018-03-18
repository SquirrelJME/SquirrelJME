// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.server;

/**
 * This class contains the entire state of the LCD subsystem.
 *
 * @since 2018/03/17
 */
public final class LcdState
{
	/** The handler for requests to the LCD server. */
	protected final LcdRequestHandler requesthandler;
	
	/** The display manager. */
	protected final LcdDisplays displays;
	
	/**
	 * Initializes the state storage for the LCDUI interface.
	 *
	 * @param __rh The handler for requests.
	 * @param __dm The display manager.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/15
	 */
	public LcdState(LcdRequestHandler __rh, LcdDisplays __dm)
		throws NullPointerException
	{
		if (__rh == null || __dm == null)
			throw new NullPointerException("NARG");
		
		this.requesthandler = __rh;
		this.displays = __dm;
	}
	
	/**
	 * Returns the display manager.
	 *
	 * @return The display manager.
	 * @since 2018/03/18
	 */
	public final LcdDisplays displays()
	{
		return this.displays;
	}
	
	/**
	 * Returns the request handler.
	 *
	 * @return The request handler.
	 * @since 2018/03/18
	 */
	public final LcdRequestHandler requestHandler()
	{
		return this.requesthandler;
	}
}

