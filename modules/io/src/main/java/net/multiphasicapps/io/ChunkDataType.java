// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

import cc.squirreljme.runtime.cldc.annotation.Exported;

/**
 * The type of data to write in.
 *
 * @since 2019/08/17
 */
@Exported
public enum ChunkDataType
{
	/** Byte. */
	@Exported
	BYTE(1),
	
	/** Short. */
	@Exported
	SHORT(2),
	
	/** Integer. */
	@Exported
	INTEGER(4),
	
	/** Long. */
	@Exported
	LONG(8),
	
	/* End. */
	;
	
	/** The number of bytes used for the data type. */
	public final int numBytes;
	
	/**
	 * Initializes the data type information.
	 * 
	 * @param __numBytes The number of bytes to use.
	 * @since 2020/12/04
	 */
	ChunkDataType(int __numBytes)
	{
		this.numBytes = __numBytes;
	}
}

