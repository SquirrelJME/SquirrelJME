// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.midlet;

import cc.squirreljme.runtime.midlet.ApplicationHandler;

/**
 * This class is the main entry point for anything that implements
 * {@link MIDlet}.
 *
 * @since 2020/02/29
 */
final class __MainHandler__
{
	/** One second in milliseconds. */
	private static final int _TERM_WAIT_TIME =
		30_000;
	
	/** Maximum settle time after starting. */
	private static final long _SETTLE_NS =
		2_000_000_000;
	
	/**
	 * Main entry point.
	 *
	 * @param __args Program arguments.
	 * @throws Throwable On any exception.
	 * @since 2020/02/29
	 */
	public static void main(String... __args)
		throws Throwable
	{
		/* {@squirreljme.error AD02 No main MIDlet class specified.} */
		if (__args == null || __args.length < 1 || __args[0] == null)
			throw new IllegalArgumentException("AD02");
		
		// Call the common application handler
		ApplicationHandler.main(new __MIDletInterface__(__args[0]));
	}
}
