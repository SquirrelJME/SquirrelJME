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
 * This class represents universal system calls which are designed to reduce
 * the complexity of the system call interface and reduce the amount of
 * extra handlers that are needed.
 *
 * @since 2019/05/06
 */
public final class UniversalAPI
{
	/**
	 * Not used.
	 *
	 * @since 2019/05/06
	 */
	private UniversalAPI()
	{
	}
	
	/**
	 * Returns the type of packages the VM uses.
	 *
	 * @return The package type.
	 * @since 2019/05/06
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_3_0_DEV)
	public static native int packageType();
	
	/**
	 * Performs a universal API call.
	 *
	 * @param __func The function to call.
	 * @param __pkg The API package arguments.
	 * @return The result of the universal call, this will be another package.
	 * @since 2019/05/06
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_3_0_DEV)
	public static native Object universalCall(int __func, Object __pkg);
}

