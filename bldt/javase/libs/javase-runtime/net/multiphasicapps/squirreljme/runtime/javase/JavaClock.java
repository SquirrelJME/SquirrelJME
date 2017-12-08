// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.javase;

import net.multiphasicapps.squirreljme.runtime.cldc.core.Clock;

/**
 * This contains the implementation of the clock methods.
 *
 * @since 2017/12/07
 */
public class JavaClock
	extends Clock
{
	/**
	 * {@inheritDoc}
	 * @since 2017/12/07
	 */
	@Override
	public long currentTimeMillis()
	{
		return System.currentTimeMillis();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/07
	 */
	@Override
	public long nanoTime()
	{
		return System.nanoTime();
	}
}

