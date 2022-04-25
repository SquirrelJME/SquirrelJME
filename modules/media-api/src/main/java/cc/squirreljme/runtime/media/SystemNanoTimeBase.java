// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media;

import javax.microedition.media.TimeBase;

/**
 * This is a time base which uses the System nano-time clock, the time is
 * relative to the initialization of the class.
 *
 * @since 2019/04/15
 */
public final class SystemNanoTimeBase
	implements TimeBase
{
	/** Base nano time. */
	private final long _baseTime =
		System.nanoTime();
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/15
	 */
	@Override
	public final long getTime()
	{
		// Measured in microseconds
		return (System.nanoTime() - this._baseTime) / 1000L;
	}
}

