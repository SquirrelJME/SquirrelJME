// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

import java.io.Closeable;
import java.io.IOException;

/**
 * This interface is used for compressed streams.
 *
 * @since 2017/08/22
 */
public interface CompressionStream
	extends Closeable
{
	/**
	 * Returns the number of compressed bytes which were read.
	 *
	 * @return The number of compressed bytes which were read.
	 * @since 2017/08/22
	 */
	public abstract long compressedBytes();
	
	/**
	 * Returns the number of uncompressed bytes which have been read.
	 *
	 * @return The number of read uncompressed bytes.
	 * @since 2017/08/22
	 */
	public abstract long uncompressedBytes();
}

