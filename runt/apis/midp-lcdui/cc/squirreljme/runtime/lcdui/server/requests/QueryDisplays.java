// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.server.requests;

import cc.squirreljme.runtime.cldc.system.type.LocalIntegerArray;
import cc.squirreljme.runtime.cldc.system.type.RemoteMethod;
import cc.squirreljme.runtime.lcdui.LcdFunction;
import cc.squirreljme.runtime.lcdui.server.LcdDisplay;
import cc.squirreljme.runtime.lcdui.server.LcdDisplays;
import cc.squirreljme.runtime.lcdui.server.LcdRequest;
import cc.squirreljme.runtime.lcdui.server.LcdServer;
import cc.squirreljme.runtime.lcdui.server.LcdWidget;

/**
 * This queries the displays which are currently available along with setting
 * the callback method if it has not been set.
 *
 * @since 2018/03/23
 */
public class QueryDisplays
	extends LcdRequest
{
	/** The remote handler method. */
	protected final RemoteMethod callback;
	
	/**
	 * Initializes the request.
	 *
	 * @param __sv The calling server.
	 * @param __cb The callback method.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/23
	 */
	public QueryDisplays(LcdServer __sv, RemoteMethod __cb)
		throws NullPointerException
	{
		super(__sv, LcdFunction.QUERY_DISPLAYS);
		
		if (__cb == null)
			throw new NullPointerException("NARG");
		
		this.callback = __cb;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	protected final Object invoke()
	{
		// Query the available displays, however for each server there is
		// always a widget which acts as a virtual display on a real display
		LcdWidget[] displays = this.server.queryDisplays(this.callback);
		
		// Fill result with handles
		int n = displays.length;
		int[] rv = new int[n];
		for (int i = 0; i < n; i++)
			rv[i] = displays[i].handle();
		return new LocalIntegerArray(rv);
	}
}

