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
	/** Vibrates the display. */
	DISPLAY_VIBRATE,
	
	/** Queries available displays and sets the local callback method. */
	QUERY_DISPLAYS,
	
	/** Adds a widget to this widget. */
	WIDGET_ADD,
	
	/** Shows a widget which is an alert, the alert is considered modal. */
	WIDGET_ALERT_SHOW,
	
	/** A widget was garbage collected, so clean it up. */
	WIDGET_CLEANUP,
	
	/** Clears all widgets that are to be displayed then sets it. */
	WIDGET_CLEAR_AND_SET,
	
	/** Create a new widget. */
	WIDGET_CREATE,
	
	/** Returns the widget height. */
	WIDGET_GET_HEIGHT,
	
	/** Returns the widget width. */
	WIDGET_GET_WIDTH,
	
	/** Repaints a widget. */
	WIDGET_REPAINT,
	
	/** Sets the title of a widget. */
	WIDGET_SET_TITLE,
	
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
			case QUERY_DISPLAYS:
			case WIDGET_ADD:
			case WIDGET_CREATE:
			case WIDGET_GET_HEIGHT:
			case WIDGET_GET_WIDTH:
				return true;
			
			default:
				return false;
		}
	}
}

