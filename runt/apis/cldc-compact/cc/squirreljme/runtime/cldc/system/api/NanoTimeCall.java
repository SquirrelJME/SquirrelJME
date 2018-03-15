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
 * Interface for {@link SystemFunction#NANOTIME}.
 *
 * @since 2018/03/14
 */
public interface NanoTimeCall
{
	/**
	 * Returns the current monotonic clock time.
	 *
	 * @return The current monotonic clock time.
	 * @since 2018/03/01
	 */
	public abstract long nanoTime();
}

