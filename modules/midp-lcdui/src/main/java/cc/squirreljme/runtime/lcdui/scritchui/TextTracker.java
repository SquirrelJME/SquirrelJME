// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.scritchui;

import cc.squirreljme.jvm.mle.scritchui.ScritchEventLoopInterface;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * This keeps track of a text string, if it changes then an update to
 * a signal end is emitted.
 * 
 * The listener that is called is always in the event loop.
 *
 * @since 2024/07/18
 */
@SquirrelJMEVendorApi
public final class TextTracker
{
	/** The event loop used. */
	protected final ScritchEventLoopInterface loop;
	
	/** The current text value. */
	private volatile String _text;
	
	/** The currently attached listener. */
	private volatile TextTrackerListener _listener;
	
	/**
	 * Initializes the text tracker with the given initial text.
	 *
	 * @param __loop The event loop interface.
	 * @param __init The initial text to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/18
	 */
	@SquirrelJMEVendorApi
	public TextTracker(ScritchEventLoopInterface __loop, String __init)
		throws NullPointerException
	{
		if (__loop == null)
			throw new NullPointerException("NARG");
		
		this.loop = __loop;
		this._text = __init;
	}
	
	/**
	 * Connects to the given listener.
	 *
	 * @param __listener The listener to connect to.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/18
	 */
	public void connect(TextTrackerListener __listener)
		throws NullPointerException
	{
		if (__listener == null)
			throw new NullPointerException("NARG");
		
		synchronized (this)
		{
			this._listener = __listener;
		}
	}
	
	/**
	 * Gets the current text.
	 *
	 * @return The current text.
	 * @since 2024/07/18
	 */
	@SquirrelJMEVendorApi
	public String get()
	{
		synchronized (this)
		{
			return this._text;
		}
	}
	
	/**
	 * Sets the given text.
	 *
	 * @param __t The text to set.
	 * @since 2024/07/18
	 */
	@SquirrelJMEVendorApi
	public void set(String __t)
	{
		TextTrackerListener listener;
		synchronized (this)
		{
			this._text = __t;
			listener = this._listener;
		}
		
		// Inform listener of the change?
		if (listener != null)
			this.loop.execute(new __ExecTextTracker__(listener, __t));
	}
}
