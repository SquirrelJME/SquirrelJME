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
import java.io.Closeable;

/**
 * This interface is used for compressed streams.
 *
 * @since 2017/08/22
 */
@SquirrelJMEVendorApi
public interface CompressionStream
	extends Closeable
{
	/**
	 * Returns the number of compressed bytes which were read.
	 *
	 * @return The number of compressed bytes which were read.
	 * @since 2017/08/22
	 */
	@SquirrelJMEVendorApi
	long compressedBytes();
	
	/**
	 * Returns the number of uncompressed bytes which have been read.
	 *
	 * @return The number of read uncompressed bytes.
	 * @since 2017/08/22
	 */
	@SquirrelJMEVendorApi
	long uncompressedBytes();
}

