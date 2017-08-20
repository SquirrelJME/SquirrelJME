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
 * This represents the type of event which has been performed.
 *
 * @since 2017/08/19
 */
public enum EventType
{
	/** Key was pressed. */
	KEY_PRESSED,
	
	/** Key was released. */
	KEY_RELEASED,
	
	/** Key was repeated. */
	KEY_REPEATED,
	
	/** Pointer Dragged. */
	POINTER_DRAGGED,
	
	/** Pointer Pressed. */
	POINTER_PRESSED,
	
	/** Pointer Released. */
	POINTER_RELEASED,
	
	/** End. */
	;
}

