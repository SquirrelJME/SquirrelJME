// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.requests;

import cc.squirreljme.runtime.lcdui.LcdFunction;
import cc.squirreljme.runtime.lcdui.server.LcdRequest;
import cc.squirreljme.runtime.lcdui.server.LcdServer;
import cc.squirreljme.runtime.lcdui.ui.UiDisplay;

/**
 * This queries the displays which are currently available along with setting
 * the callback method if it has not been set.
 *
 * @since 2018/03/23
 */
public final class QueryDisplays
	extends LcdRequest
{
	/**
	 * Initializes the request.
	 *
	 * @param __sv The calling server.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/23
	 */
	public QueryDisplays(LcdServer __sv)
		throws NullPointerException
	{
		super(__sv, LcdFunction.QUERY_DISPLAYS);
		
		throw new todo.TODO();
		/*
		if (__cb == null)
			throw new NullPointerException("NARG");
		
		this.callback = __cb;
		*/
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	protected final Object invoke()
	{
		throw new todo.TODO();
		/*
		// Query the available displays, however for each server there is
		// always a widget which acts as a virtual display on a real display
		UiDisplay[] displays = this.server.queryDisplays(this.callback);
		
		// Fill result with handles
		int n = displays.length;
		int[] rv = new int[n];
		for (int i = 0; i < n; i++)
			rv[i] = displays[i].handle();
		return new LocalIntegerArray(rv);
		*/
	}
}

