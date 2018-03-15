// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui;

import cc.squirreljme.runtime.cldc.service.ServiceAccessor;

/**
 * This class is the client to the LCDUI server on the kernel end and is
 * used to manage each sub-display accordingly.
 *
 * @since 2018/03/15
 */
public final class LcdClientManager
{
	/** Global lock for the LCD client. */
	private static final Object _LOCK =
		new Object();
	
	/** The local client manager. */
	private static volatile LcdClientManager _MANAGER;
	
	/**
	 * Returns the instance of the client manager.
	 *
	 * @return The client manager instance.
	 * @since 2018/03/15
	 */
	public static final LcdClientManager instance()
	{
		LcdClientManager rv = LcdClientManager._MANAGER;
		if (rv == null)
			synchronized (LcdClientManager._LOCK)
			{
				rv = LcdClientManager._MANAGER;
				if (rv == null)
					_MANAGER = (rv = ServiceAccessor.
						<LcdClientManager>service(LcdClientManager.class));
			}
		return rv;
	}
}

