// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.lcdui.event;

/**
 * This is the base interface for all events.
 *
 * @since 2017/10/24
 */
public interface Event
{
	/**
	 * Returns the event type.
	 *
	 * @return The event type.
	 * @since 2017/10/24
	 */
	public abstract EventType type();
}

