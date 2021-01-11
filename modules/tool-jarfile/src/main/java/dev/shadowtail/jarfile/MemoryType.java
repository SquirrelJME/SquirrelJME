// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.jarfile;

/**
 * The memory type used.
 *
 * @since 2021/01/10
 */
public enum MemoryType
{
	/** Byte. */
	BYTE(1),
	
	/** Short. */
	SHORT(2),
	
	/** Integer. */
	INTEGER(4),
	
	/* End. */
	;
	
	/** The number of bytes that make up the type. */
	public final int byteCount;
	
	/** The last address to read off. */
	public final int lastOffset;
	
	/**
	 * Initializes the constant.
	 * 
	 * @param __size The size of the type.
	 * @since 2021/01/10
	 */
	MemoryType(int __size)
	{
		this.byteCount = __size;
		this.lastOffset = __size - 1;
	}
}
