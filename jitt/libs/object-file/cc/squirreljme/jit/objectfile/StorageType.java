// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jit.objectfile;

/**
 * Represents the type of value which can be stored in a variable.
 *
 * @since 2018/02/26
 */
public enum StorageType
{
	/** Byte. */
	BYTE,
	
	/** Short. */
	SHORT,
	
	/** Integer. */
	INTEGER,
	
	/** Long. */
	LONG,
	
	/** Pointer. */
	POINTER,
	
	/** Offset. */
	OFFSET,
	
	/** Float. */
	FLOAT,
	
	/** Double. */
	DOUBLE,
	
	/** End. */
	;
}

