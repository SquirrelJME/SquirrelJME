// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.unsafe;

import java.io.IOException;

/**
 * This class contains implementations of unsafe SquirrelJME operations which
 * are not part of the bootstrap's external build dependencies. This in the
 * general case will only have socket related code.
 *
 * For documenatation for these methods, see the class using this name in
 * the {@code squirreljme-internal} project.
 *
 * @since 2016/10/11
 */
public final class SquirrelJME
{
	/**
	 * Not used.
	 *
	 * @since 2016/10/11
	 */
	private SquirrelJME()
	{
		throw new RuntimeException("OOPS");
	}
	
	/**
	 * As duplicated.
	 *
	 * @return As duplicated.
	 * @since 2016/10/11
	 */
	public static boolean isKernel()
	{
		// Always runs on the kernel
		return true;
	}
	
	/**
	 * As duplicated.
	 *
	 * @return As duplicated.
	 * @since 2016/10/11
	 */
	public static boolean isSquirrelJME()
	{
		// Never is SquirrelJME
		return false;
	}
}

