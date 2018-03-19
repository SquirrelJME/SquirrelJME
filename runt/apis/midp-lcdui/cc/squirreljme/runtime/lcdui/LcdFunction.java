// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui;

/**
 * This represents a function for the LCDUI interface for system calls, all
 * service calls to the host interface server.
 *
 * @since 2018/03/16
 */
public enum LcdFunction
{
	/** Create a displayable of a given type and return the handle to it. */
	CREATE_DISPLAYABLE,
	
	/** Returns the displayable height. */
	DISPLAYABLE_GET_HEIGHT,
	
	/** Returns the displayable width. */
	DISPLAYABLE_GET_WIDTH,
	
	/** Repaints a displayable. */
	DISPLAYABLE_REPAINT,
	
	/** Sets the title of a displayable. */
	DISPLAYABLE_SET_TITLE,
	
	/** Sets the current displayable. */
	DISPLAY_SET_CURRENT,
	
	/** Vibrates the display. */
	DISPLAY_VIBRATE,
	
	/** Queries all of the display indexes which are available for usage. */
	QUERY_DISPLAYS,
	
	/** Registers the callback for this task. */
	REGISTER_CALLBACK,
	
	/** End. */
	;
	
	/**
	 * Is this function interruptable?
	 *
	 * @return If this function can be interrupted.
	 * @since 2018/03/18
	 */
	public final boolean isInterruptable()
	{
		switch (this)
		{
			default:
				return false;
		}
	}
	
	/**
	 * Returns {@code true} if this is a function which queries and returns a
	 * value.
	 *
	 * @return If this is to be queried and a value returned.
	 * @since 2018/03/18
	 */
	public final boolean query()
	{
		switch (this)
		{
			case CREATE_DISPLAYABLE:
			case DISPLAYABLE_GET_HEIGHT:
			case DISPLAYABLE_GET_WIDTH:
			case DISPLAY_SET_CURRENT:
			case QUERY_DISPLAYS:
			case REGISTER_CALLBACK:
				return true;
			
			default:
				return false;
		}
	}
}

