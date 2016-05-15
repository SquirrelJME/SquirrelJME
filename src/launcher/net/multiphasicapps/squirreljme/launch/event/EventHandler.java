// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.launch.event;

/**
 * This is a class which is used as an event handler when one is to be handled.
 *
 * The return values of these event handlers allow other events to be
 * composed and passed through to the next event handler.
 *
 * @since 2016/05/15
 */
public interface EventHandler
{
	/**
	 * If this is returned from an event handler, the event does not get
	 * processed through other handlers (if any).
	 */
	public static final int CONSUME_EVENT =
		0xFFFF_FFF0;
	
	/**
	 * If this is returned from an event handler, the event is passed through
	 * to the next handler without being modified. The next event handler's
	 * input event would be the same as the current event.
	 */
	public static final int PASS_EVENT =
		0xFFFF_FFF1;
	
	/**
	 * Stop handling events with the current event handler and move to the
	 * next one instead.
	 */
	public static final int NEXT_HANDLER =
		0xFFFF_FFF2;
	
	/** Stop event handling and return. */
	public static final int STOP_HANDLING =
		0xFFFF_FFF3;
}

