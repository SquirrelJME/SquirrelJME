// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.lang;

/**
 * This class returns the current guest depth.
 *
 * @since 2018/11/04
 */
public final class GuestDepth
{
	/** The calculated guest depth. */
	private static int _DEPTH =
		-1;
	
	/**
	 * Not used.
	 *
	 * @since 2018/11/04
	 */
	private GuestDepth()
	{
	}
	
	/**
	 * The guest depth for the virtual machine.
	 *
	 * @return The guest depth for the virtual machine.
	 * @since 2018/11/04
	 */
	public static final int guestDepth()
	{
		// Pre-cached depth?
		int rv = GuestDepth._DEPTH;
		if (rv >= 0)
			return rv;
		
		// System property will indicate the number of guests in the VM
		try
		{
			String prop = System.getProperty("cc.squirreljme.guests");
			if (prop == null)
				rv = 0;
			else
				rv = Integer.parseInt(prop);
		}
		
		// Cannot get property so it cannot be known
		catch (SecurityException|NumberFormatException e)
		{
			rv = 0;
		} 
		
		// Cache it for later
		GuestDepth._DEPTH = rv;
		return rv;
	}
}

