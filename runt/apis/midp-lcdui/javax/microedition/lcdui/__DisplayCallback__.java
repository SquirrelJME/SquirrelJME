// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.runtime.cldc.system.type.EnumType;
import cc.squirreljme.runtime.cldc.system.type.RemoteMethod;
import cc.squirreljme.runtime.lcdui.LcdCallback;

/**
 * This is the callback used for displays so that the remote server can call
 * local methods accordingly and do important things.
 *
 * @since 2018/03/18
 */
final class __DisplayCallback__
	extends RemoteMethod
{
	/**
	 * Initializes the display callback.
	 *
	 * @since 2018/03/18
	 */
	__DisplayCallback__()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/18
	 */
	@Override
	protected final Object internalInvoke(Object[] __args)
	{
		if (__args == null)
			__args = new Object[0];
		
		LcdCallback func = ((EnumType)__args[0]).<LcdCallback>asEnum(
			LcdCallback.class);
		switch (func)
		{
				// {@squirreljme.error EB23 Unknown LCDUI callback function.
				// (The function)}
			default:
				throw new RuntimeException(String.format("EB23 %s", func));
		}
	}
}

