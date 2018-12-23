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
 * This is used to access the system registry which is compatible in a fashion
 * that is compatible with an extrapolation of Windows registries.
 *
 * @since 2018/10/14
 */
public final class RegistryAccess
{
	/**
	 * Not used.
	 *
	 * @since 2018/10/14
	 */
	private RegistryAccess()
	{
	}
	
	/**
	 * Returns {@code true} if a registry is present.
	 *
	 * @return If a registry is present.
	 * @since 2018/10/14
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static final boolean present()
	{
		return false;
	}
}

