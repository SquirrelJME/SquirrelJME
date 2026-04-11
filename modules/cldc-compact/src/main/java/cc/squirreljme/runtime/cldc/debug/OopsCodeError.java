package cc.squirreljme.runtime.cldc.debug;

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
 * This is thrown when "Oops" code has been reached.
 *
 * @since 2026/04/11
 */
@SquirrelJMEVendorApi
public class OopsCodeError
	extends Error
{
	/**
	 * Initializes the exception.
	 *
	 * @since 2026/04/11
	 */
	@SquirrelJMEVendorApi
	public OopsCodeError()
	{
	}
}
