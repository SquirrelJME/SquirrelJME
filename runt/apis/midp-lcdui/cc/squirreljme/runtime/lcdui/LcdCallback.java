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
 * This represents the type of callback function is being requested by the
 * server side.
 *
 * @since 2018/03/18
 */
public enum LcdCallback
{
	/** Paint graphics in the widget. */
	WIDGET_PAINT,
	
	/** The widget has been shown or hidden. */
	WIDGET_SHOWN,
	
	/** The size of the widget changed. */
	WIDGET_SIZE_CHANGED,
	
	/** End. */
	;
}

