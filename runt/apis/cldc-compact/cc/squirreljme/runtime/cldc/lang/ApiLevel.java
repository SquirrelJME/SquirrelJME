// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.lang;

import cc.squirreljme.runtime.cldc.asm.SystemProperties;

/**
 * Provides access to the current API level.
 *
 * This is used for backwards compatibility.
 *
 * Levels are in the form of {@code 0M_mm_R_yyDDD} (Major version, minor
 * version, release version, year (20yy), day of year). This means they are
 * limited to the form of {@code [0-9].[0-99].[0-9] (Day [0-999] of 20[0-99])}.
 *
 * @since 2018/12/05
 */
public final class ApiLevel
{
	/** The current API level. */
	public static final int CURRENT_LEVEL =
		SystemProperties.apiLevel();
	
	/** Undefined. */
	public static final int UNDEFINED =
		0x7FFFFFFF;
	
	/** SquirrelJME 0.2.0 (December 25, 2018). */
	public static final int LEVEL_SQUIRRELJME_0_2_0_20181225 =
		2_0_18359;
	
	/** SquirrelJME 0.3.0 (Development). */
	public static final int LEVEL_SQUIRRELJME_0_3_0_DEV =
		3_0_19001;
	
	/** SquirrelJME 0.4.0 (May 5, 2019). */
	public static final int LEVEL_SQUIRRELJME_0_4_0_20190505 =
		4_0_19111;
	
	/**
	 * Not used.
	 *
	 * @since 2018/12/05
	 */
	private ApiLevel()
	{
	}
	
	/**
	 * Converts an API level to a string.
	 *
	 * @param __l The input level.
	 * @return The string.
	 * @since 2018/12/05
	 */
	public static String levelToString(int __l)
	{
		return String.format("%d.%d.%d (Day %d of %d)",
			(__l / 100000000) % 10,
			(__l / 1000000) % 100,
			(__l / 100000) % 10,
			__l % 1000,
			2000 + ((__l / 1000) % 100));
	}
	
	/**
	 * Checks if the runtime API level is at a minimum this given level.
	 *
	 * @param __l The level to check.
	 * @return If the minimum level is met.
	 * @since 2019/02/02
	 */
	public static boolean minimumLevel(int __l)
	{
		return (SystemProperties.apiLevel() >= __l);
	}
}

