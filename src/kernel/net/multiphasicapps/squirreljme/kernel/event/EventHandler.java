// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.event;

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
	
	/** Stop event handling and return. */
	public static final int STOP_HANDLING =
		0xFFFF_FFF3;
	
	/**
	 * Interface for handling key events.
	 *
	 * @since 2016/05/18
	 */
	public static interface Key
		extends EventHandler
	{
		/**
		 * Handles a key event.
		 *
		 * @param __k The kind of key event this was.
		 * @param __port The attached controller port.
		 * @param __c The character code of the key.
		 * @return A special code or a raw event.
		 * @since 2016/05/18
		 */
		public abstract int handleKeyEvent(EventKind __k, int __port,
			char __c);
	}
	
	/**
	 * Interface for handling unknown events.
	 *
	 * @since 2016/05/18
	 */
	public static interface Unknown
		extends EventHandler
	{
		/**
		 * Handles an unknown event.
		 *
		 * @param __r The event code.
		 * @return A special code or a raw event.
		 * @since 2016/05/18
		 */
		public abstract int handleUnknownEvent(int __r);
	}
}

