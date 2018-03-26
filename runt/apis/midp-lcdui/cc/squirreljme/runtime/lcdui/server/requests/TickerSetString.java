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
public class TickerSetString
	extends LcdRequest
{
	/** The ticker to set the text for. */
	protected final LcdTicker ticker;
	
	/** The text to set. */
	protected final String text;
	
	/**
	 * Initializes the request.
	 *
	 * @param __sv The calling server.
	 * @param __tick The ticker to set.
	 * @param __text The text to set.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/26
	 */
	public TickerSetString(LcdServer __sv, LcdTicker __tick, String __text)
		throws NullPointerException
	{
		super(__sv, LcdFunction.TICKER_SET_STRING);
		
		if (__tick == null || __text == null)
			throw new NullPointerException("NARG");
		
		this.ticker = __tick;
		this.text = __text;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/26
	 */
	@Override
	protected final Object invoke()
	{
		this.ticker.setText(this.text);
		return VoidType.INSTANCE;
	}
}

