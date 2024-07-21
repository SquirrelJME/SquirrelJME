// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.scritchui;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.jetbrains.annotations.Async;

/**
 * Executes text tracker changes in the event loop.
 *
 * @param <T> The object type.
 * @param <L> The listener type to use.
 * @since 2024/07/18
 */
final class __ExecObjectTracker__<T, L>
	implements Runnable
{
	/** The tracker used. */
	protected final ObjectTracker<T, L> tracker;
	
	/**
	 * Initializes the executor.
	 *
	 * @param __tracker The tracker to call.
	 * @throws NullPointerException If no tracker was specified.
	 * @since 2024/07/18
	 */
	__ExecObjectTracker__(ObjectTracker<T, L> __tracker)
		throws NullPointerException
	{
		if (__tracker == null)
			throw new NullPointerException("NARG");
		
		this.tracker = __tracker;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/18
	 */
	@Override
	@Async.Execute
	public void run()
	{
		ObjectTracker<T, L> tracker = this.tracker;
		tracker.exec(tracker._listener, tracker._value);
	}
}
