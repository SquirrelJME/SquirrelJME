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
		SLOW;
	
	/** The best compression level. */
	public static final CompressionLevel BEST =
		SLOWEST;
	
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
				throw new RuntimeException("OOPS");
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
			case 1:		return FASTEST;
			
			case 2:
			case 3:		return FASTER;
			
			case 4:
			case 5:		return FAST;
			
			case 6:
			case 7:		return SLOW;
			
			case 8:
			case 9:		return SLOWER;
			
			case 10:	return SLOWEST;
			
				// Out of range, so just treat as capped
			default:
				return (__i <= 0 ? FASTEST : SLOWEST);
		}
	}
}

