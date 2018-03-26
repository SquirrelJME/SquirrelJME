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

import cc.squirreljme.runtime.lcdui.CollectableType;

/**
 * This represents a ticker which represents a sequence of scrolling text for
 * information purposes.
 *
 * @since 2018/03/26
 */
public abstract class LcdTicker
	extends LcdCollectable
{
	/** The ticker text. */
	private volatile String _text;
	
	/**
	 * Initializes the ticker with the given handle.
	 *
	 * @param __handle The handle used.
	 * @since 2018/03/26
	 */
	public LcdTicker(int __handle)
	{
		super(__handle, CollectableType.TICKER);
	}
	
	/**
	 * Internally sets the ticker's text.
	 *
	 * @param __t The text to set.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/26
	 */
	protected abstract void internalSetText(String __t)
		throws NullPointerException;
	
	/**
	 * Sets the text for the ticker.
	 *
	 * @param __t The text for the ticker.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/26
	 */
	public final void setText(String __t)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		this._text = __t;
		this.internalSetText(__t);
	}
	
	/**
	 * Returns the ticker's text.
	 *
	 * @return The ticker text.
	 * @since 2018/03/26
	 */
	public final String text()
	{
		return this._text;
	}
}

