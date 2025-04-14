// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * The type of data to write in.
 *
 * @since 2019/08/17
 */
@SquirrelJMEVendorApi
public enum ChunkDataType
{
	/** Byte. */
	@SquirrelJMEVendorApi
	BYTE(1),
	
	/** Short. */
	@SquirrelJMEVendorApi
	SHORT(2),
	
	/** Integer. */
	@SquirrelJMEVendorApi
	INTEGER(4),
	
	/** Long. */
	@SquirrelJMEVendorApi
	LONG(8),
	
	/* End. */
	;
	
	/** The number of bytes used for the data type. */
	@SquirrelJMEVendorApi
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

