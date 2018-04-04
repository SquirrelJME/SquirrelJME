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
	/** A collectable was garbage collected, so clean it up. */
	COLLECTABLE_CLEANUP,
	
	/** Create a new collectable. */
	COLLECTABLE_CREATE,
	
	/** Vibrates the display. */
	DISPLAY_VIBRATE,
	
	/** Queries available displays and sets the local callback method. */
	QUERY_DISPLAYS,
	
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
			case COLLECTABLE_CREATE:
			case QUERY_DISPLAYS:
				return true;
			
			default:
				return false;
		}
	}
}

