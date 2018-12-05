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
 * Used to provide access to the time.
 *
 * @since 2018/10/14
 */
public final class TimeAccess
{
	/**
	 * Not used.
	 *
	 * @since 2018/10/14
	 */
	private TimeAccess()
	{
	}
	
	/**
	 * Returns the current time in milliseconds UTC since the epoch.
	 *
	 * @return The since since the epoch in UTC milliseconds.
	 * @since 2018/03/01
	 */
	public static final native long currentTimeMillis();
	
	/**
	 * Returns the current monotonic clock time.
	 *
	 * @return The current monotonic clock time.
	 * @since 2018/03/01
	 */
	public static final native long nanoTime();
}

