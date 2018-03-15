// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.system.api;

import cc.squirreljme.runtime.cldc.system.SystemFunction;

/**
 * Interface for {@link SystemFunction#CURRENT_TIME_MILLIS}.
 *
 * @since 2018/03/14
 */
public interface CurrentTimeMillisCall
{
	/**
	 * Returns the current time in milliseconds UTC since the epoch.
	 *
	 * @return The since since the epoch in UTC milliseconds.
	 * @since 2018/03/01
	 */
	public abstract long currentTimeMillis();
}

