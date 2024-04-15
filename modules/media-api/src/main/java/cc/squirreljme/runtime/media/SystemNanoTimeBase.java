// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import javax.microedition.media.TimeBase;

/**
 * This is a time base which uses the System nano-time clock.
 *
 * @since 2019/04/15
 */
@SquirrelJMEVendorApi
public final class SystemNanoTimeBase
	implements TimeBase
{
	/**
	 * {@inheritDoc}
	 * @since 2019/04/15
	 */
	@Override
	@SquirrelJMEVendorApi
	public final long getTime()
	{
		// Measured in microseconds
		return System.nanoTime() / 1000L;
	}
}

