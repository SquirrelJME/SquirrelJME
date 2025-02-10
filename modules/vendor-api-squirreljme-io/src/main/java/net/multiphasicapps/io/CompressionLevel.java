// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This represents the compression level, which is based on how fast things
 * are.
 *
 * @since 2018/11/10
 */
public enum CompressionLevel
{
	/** Fast compression level. */
	FASTEST,
	
	/** Faster compression. */
	FASTER,
	
	/** Fast compression. */
	FAST,
	
	/** Slow compression. */
	SLOW,
	
	/** Slower compression. */
	SLOWER,
	
	/** Slowest compression. */
	SLOWEST,
	
	/** End. */
	;
	
	/** The default compression level. */
	public static final CompressionLevel DEFAULT =
		CompressionLevel.SLOW;
	
	/** The best compression level. */
	public static final CompressionLevel BEST =
		CompressionLevel.SLOWEST;
	
	/**
	 * The number of symbols to look at as a single unit with a given
	 * dictionary before attempting with another dictionary.
	 *
	 * @return The block size to use for compression.
	 * @since 2018/11/10
	 */
	public final int blockSize()
	{
		switch (this)
		{
				// Fast has no sliding window
			case FASTEST:	return 64;
			case FASTER:	return 128;
			case FAST:		return 256;
			
				// Slow algorithms compress in more chunks
			case SLOW:		return 256;
			case SLOWER:	return 512;
			case SLOWEST:	return 1024;
			
			default:
				throw Debugging.oops();
		}
	}
	
	/**
	 * Converts a ZIP compression 1-10 scale index to compression level.
	 *
	 * @param __i The input scale.
	 * @return The compression level for the scale.
	 * @since 2018/11/10
	 */
	public static final CompressionLevel ofLevel(int __i)
	{
		switch (__i)
		{
			case 1:		return CompressionLevel.FASTEST;
			
			case 2:
			case 3:		return CompressionLevel.FASTER;
			
			case 4:
			case 5:		return CompressionLevel.FAST;
			
			case 6:
			case 7:		return CompressionLevel.SLOW;
			
			case 8:
			case 9:		return CompressionLevel.SLOWER;
			
			case 10:	return CompressionLevel.SLOWEST;
			
				// Out of range, so just treat as capped
			default:
				return (__i <= 0 ? CompressionLevel.FASTEST :
					CompressionLevel.SLOWEST);
		}
	}
}

