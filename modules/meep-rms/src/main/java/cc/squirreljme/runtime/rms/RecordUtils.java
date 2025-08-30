// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.rms;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import javax.microedition.rms.RecordStoreException;

/**
 * Utilities for record stores.
 *
 * @since 2025/04/20
 */
@SquirrelJMEVendorApi
public class RecordUtils
{
	/**
	 * Wraps the exception and initializes the cause.
	 *
	 * @param <E> The exception to initialize.
	 * @param __e The exception to initialize.
	 * @param __t The cause of the exception.
	 * @return The resultant exception.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/04/18
	 */
	@SquirrelJMEVendorApi
	public static <E extends RecordStoreException> E wrap(E __e, Throwable __t)
		throws NullPointerException
	{
		if (__e == null)
			throw new NullPointerException("NARG");
		
		__e.initCause(__t);
		return __e;
	}
}
