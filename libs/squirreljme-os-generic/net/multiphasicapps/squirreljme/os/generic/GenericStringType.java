// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.os.generic;

/**
 * This represents the type of string that is stored.
 *
 * @since 2016/07/30
 */
public enum GenericStringType
{
	/** A single byte wide string (characters 0x00 to 0xFF). */
	BYTE,
	
	/** A two-byte wide UTF-16 string. */
	CHAR,
	
	/** End. */
	;
}

