// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.event;

/**
 * This represents the type of event which has been performed.
 *
 * @since 2017/08/19
 */
public enum EventType
{
	/** Command activated. */
	COMMAND,
	
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
	
	/** Repaint the display. */
	DISPLAY_REPAINT,
	
	/** Display size has changed. */
	DISPLAY_SIZE_CHANGED,
	
	/** Exit program requested. */
	EXIT_REQUEST,
	
	/** End. */
	;
	
	/** The ordinal of this event. */
	public final int ordinal =
		this.ordinal();
	
	/**
	 * Returns the event type for the given ID>
	 *
	 * @param __i The ID to get the event for.
	 * @return The event type.
	 * @since 2018/11/17
	 */
	public static final EventType of(int __i)
	{
		switch (__i)
		{
			case 0:		return COMMAND;
			case 1:		return KEY_PRESSED;
			case 2:		return KEY_RELEASED;
			case 3:		return KEY_REPEATED;
			case 4:		return POINTER_DRAGGED;
			case 5:		return POINTER_PRESSED;
			case 6:		return POINTER_RELEASED;
			case 7:		return DISPLAY_REPAINT;
			case 8:		return DISPLAY_SIZE_CHANGED;
			case 9:		return EXIT_REQUEST;
			
				// {@squirreljme.error EB2a Unknown event type. (The event
				// type)}
			default:
				throw new IllegalArgumentException("EB2a " + __i);
		}
	}
}

