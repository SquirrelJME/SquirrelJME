// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.asm;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.lang.ApiLevel;

/**
 * Used for printing to the console.
 *
 * @since 2018/09/21
 */
@Deprecated
public final class ConsoleOutput
{
	/** Standard output. */
	public static final int OUTPUT =
		1;
	
	/** Standard error. */
	public static final int ERROR =
		2;
	
	/** End of file. */
	public static final int ERROR_EOF =
		-1;
	
	/** Invalid file descriptor. */
	public static final int ERROR_INVALIDFD =
		-2;
	
	/**
	 * Not used.
	 *
	 * @since 2018/09/21
	 */
	private ConsoleOutput()
	{
	}
	
	/**
	 * Reads the display console, that is anything which was output to the
	 * console itself.
	 *
	 * @param __dim The output dimensions of the console, columns and rows.
	 * This array must always have a length of at least two.
	 * @param __b The output byte array.
	 * @param __o The offset.
	 * @param __l The length.
	 * @return The number of bytes which were read, this will be the minimum
	 * of either {@code __dim[0] * __dim[1]} or {@code __l}. Zero may be
	 * returned if this is not supported.
	 * @since 2018/12/16
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static final native int displayRead(int[] __dim,
		byte[] __b, int __o, int __l);
	
	/**
	 * Flushes the stream.
	 *
	 * @param __fd The file descriptor to flush.
	 * @return Zero on success, negative values for failure.
	 * @since 2018/12/08
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static final native int flush(int __fd);
	
	/**
	 * Writes the character to the console output.
	 *
	 * @param __fd The file descriptor to write to.
	 * @param __c The byte to write, only the lowest 8-bits are used.
	 * @return Zero on success, negative values for failure.
	 * @since 2018/09/21
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static final native int write(int __fd, int __c);
	
	/**
	 * Writes the given bytes to the console output.
	 *
	 * @param __fd The file descriptor to write to.
	 * @param __b The bytes to write.
	 * @param __o The offset.
	 * @param __l The length.
	 * @return Zero on success, negative values for failure.
	 * @since 2018/12/05
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static final native int write(int __fd,
		byte[] __b, int __o, int __l);
}

