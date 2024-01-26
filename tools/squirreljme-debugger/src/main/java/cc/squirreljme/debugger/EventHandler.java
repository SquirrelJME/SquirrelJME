// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

/**
 * Handler for events.
 *
 * @param <E> The event type used.
 * @since 2024/01/20
 */
public interface EventHandler<E extends DebuggerEvent>
{
	/**
	 * Handles the given event.
	 *
	 * @param __state The debugger state.
	 * @param __event The event to be handled.
	 * @since 2024/01/20
	 */
	void handle(DebuggerState __state, E __event);
}
