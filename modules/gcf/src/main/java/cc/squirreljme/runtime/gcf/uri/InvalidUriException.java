package cc.squirreljme.runtime.gcf.uri;

// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * This is thrown when a URI is not valid.
 *
 * @since 2025/12/28
 */
@SquirrelJMEVendorApi
public class InvalidUriException
	extends RuntimeException
{
	/**
	 * Initializes the exception.
	 *
	 * @param __m The message.
	 * @since 2025/12/28
	 */
	@SquirrelJMEVendorApi
	public InvalidUriException(String __m)
	{
		super(__m);
	}
}
