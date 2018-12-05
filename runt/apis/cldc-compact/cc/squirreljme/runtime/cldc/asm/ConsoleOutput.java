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
	 * Writes the character to the console output.
	 *
	 * @param __fd The file descriptor to write to.
	 * @param __c The byte to write, only the lowest 8-bits are used.
	 * @return Zero on success, negative values for EOF.
	 * @since 2018/09/21
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static final native int write(int __fd, int __c);
}

