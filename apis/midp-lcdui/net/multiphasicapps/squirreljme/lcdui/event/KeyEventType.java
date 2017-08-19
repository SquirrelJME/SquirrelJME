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
 * This represents the kind of key event which was generated.
 *
 * @since 2017/02/12
 */
public enum KeyEventType
{
	/** Key was pressed. */
	PRESSED,
	
	/** Key was released. */
	RELEASED,
	
	/** Key was repeated. */
	REPEATED,
	
	/** End. */
	;
}

