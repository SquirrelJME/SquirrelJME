// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.midp.lcdui;

/**
 * This is the base class for displays.
 *
 * @since 2016/10/14
 */
public abstract class VirtualDisplay
{
	/** The display ID. */
	protected final byte id;
	
	/** The owning server. */
	protected final DisplayServer server;
	
	/**
	 * Initilaizes the virtual display.
	 *
	 * @param __ds The owning display server.
	 * @param __id The id of the display.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/14
	 */
	public VirtualDisplay(DisplayServer __ds, byte __id)
		throws NullPointerException
	{
		// Check
		if (__ds == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.id = __id;
		this.server = __ds;
	}
	
	/**
	 * Returns the display capabilities.
	 *
	 * @return The capabilities of the display.
	 * @since 2016/10/14
	 */
	protected abstract int capabilities();
	
	/**
	 * Returns the extended display capabilities.
	 *
	 * @return The extended capabilities.
	 * @since 2016/10/14
	 */
	protected abstract int capabilitiesExtended();
	
	/**
	 * Returns the display ID.
	 *
	 * @return The display ID.
	 * @since 2016/10/14
	 */
	public final byte id()
	{
		return this.id;
	}
}

