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

/**
 * This is used to access databases which are compatible in a fashion that is
 * compatible with an extrapolation of Palm OS databases.
 *
 * @since 2018/10/14
 */
public final class DatabaseAccess
{
	/**
	 * Not used.
	 *
	 * @since 2018/10/14
	 */
	private DatabaseAccess()
	{
	}
	
	/**
	 * Returns {@code true} if a database is present.
	 *
	 * @return If a database is present.
	 * @since 2018/10/14
	 */
	public static final native boolean present();
}

