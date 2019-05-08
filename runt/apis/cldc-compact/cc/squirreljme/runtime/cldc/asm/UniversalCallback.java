// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.asm;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.lang.ApiLevel;

/**
 * This is used when a callback is ever needed by any code.
 *
 * @since 2019/05/06
 */
public final class UniversalCallback
{
	/**
	 * Not used.
	 *
	 * @since 2019/05/06
	 */
	private UniversalCallback()
	{
	}
	
	/**
	 * Handles a universal callback.
	 *
	 * @param __func The function being called.
	 * @param __pkg The API package arguments.
	 * @return The result of the universal call, this will be another package.
	 * @since 2019/05/06
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_3_0_DEV)
	public static native Object universalCallback(int __func, Object __pkg);
}

