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

import cc.squirreljme.runtime.cldc.system.type.VoidType;
import cc.squirreljme.runtime.lcdui.LcdFunction;
import cc.squirreljme.runtime.lcdui.server.LcdRequest;
import cc.squirreljme.runtime.lcdui.server.LcdServer;
import cc.squirreljme.runtime.lcdui.server.LcdTicker;

/**
 * This sets the text for a ticker.
 *
 * @since 2018/03/26
 */
public class TickerGetString
	extends LcdRequest
{
	/** The ticker to get the text from. */
	protected final LcdTicker ticker;
	
	/**
	 * Initializes the request.
	 *
	 * @param __sv The calling server.
	 * @param __tick The ticker to get from.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/26
	 */
	public TickerGetString(LcdServer __sv, LcdTicker __tick)
		throws NullPointerException
	{
		super(__sv, LcdFunction.TICKER_GET_STRING);
		
		if (__tick == null)
			throw new NullPointerException("NARG");
		
		this.ticker = __tick;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/26
	 */
	@Override
	protected final Object invoke()
	{
		return this.ticker.text();
	}
}

