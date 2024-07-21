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
import cc.squirreljme.runtime.cldc.debug.Debugging;
import org.jetbrains.annotations.Async;

/**
 * This keeps track of a text string, if it changes then an update to
 * a signal end is emitted.
 * 
 * The listener that is called is always in the event loop.
 *
 * @since 2024/07/18
 */
@SquirrelJMEVendorApi
public final class StringTracker
	extends ObjectTracker<String, StringTrackerListener>
{
	/**
	 * Initializes the text tracker with the given initial text.
	 *
	 * @param __loop The event loop interface.
	 * @param __init The initial text to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/18
	 */
	@SquirrelJMEVendorApi
	public StringTracker(ScritchEventLoopInterface __loop, String __init)
		throws NullPointerException
	{
		super(__loop, __init);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/20
	 */
	@SquirrelJMEVendorApi
	@Async.Execute
	@Override
	protected void exec(StringTrackerListener __listener, String __value)
		throws NullPointerException
	{
		if (__listener == null)
			throw new NullPointerException("NARG");
		
		// Forward call
		__listener.stringUpdated(__value);
	}
}
