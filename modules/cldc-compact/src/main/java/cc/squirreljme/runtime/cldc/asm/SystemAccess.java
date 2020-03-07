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
 * Access to system related details.
 *
 * @since 2018/10/13
 */
@Deprecated
public final class SystemAccess
{
	/**
	 * Not used.
	 *
	 * @since 2018/10/13
	 */
	private SystemAccess()
	{
	}
	
	/**
	 * Returns the specified environment variable, it is unspecified and
	 * system dependent if variables are case sensitive or not. Locale may
	 * be considered by the host system additionally. If environment variables
	 * do not exist in the environment then only {@code null} will be
	 * returned.
	 *
	 * @param __e The environment variable to get.
	 * @return The value of that variable or {@code null} if it is not set.
	 * @since 2018/10/14
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static final native String getEnv(String __e);
}

