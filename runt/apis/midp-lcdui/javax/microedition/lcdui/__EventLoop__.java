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

/**
 * This is the single event loop which handles all events, since they must all
 * be serialized within each other the bulk of the code is called from this
 * event loop as such.
 *
 * @since 2018/11/17
 */
final class __EventLoop__
	implements Runnable
{
	/**
	 * {@inheritDoc}
	 * @since 2018/11/17
	 */
	@Override
	public void run()
	{
		// This is run constantly in a loop waiting for events to happen
		try
		{
			// Event handling loop
			for (;;)
			{
				throw new todo.TODO();
			}
		}
		
		// In the event this loop dies or terminates, if this is the active
		// event loop always make sure it no longer is the active loop.
		// Otherwise events will just suddenly stop working.
		finally
		{
			if (this == Display._EVENT_LOOP)
				Display._EVENT_LOOP = null;
		}
	}
}

