// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.cute;

/**
 * This indicates the message to display.
 *
 * @since 2018/03/06
 */
public enum MessageType
{
	/** Information. */
	INFO,
	
	/** Warning. */
	WARNING,
	
	/** Error. */
	ERROR,
	
	/** Lint to detect for possible errors. */
	LINT,
	
	/** End. */
	;
}

