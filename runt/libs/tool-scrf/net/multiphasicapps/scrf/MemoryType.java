// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.scrf;

/**
 * This represents a type which is used in memory which will change how
 * something be read or stored.
 *
 * @since 2019/02/03
 */
public enum MemoryType
{
	/** Byte (8-bit). */
	BYTE,
	
	/** Short (16-bit). */
	SHORT,
	
	/** Integer (32-bit). */
	INTEGER,
	
	/** Long (64-bit). */
	LONG,
	
	/** Pointer (varies in size). */
	POINTER,
	
	/** End. */
	;
}
