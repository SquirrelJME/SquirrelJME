// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.debug;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * Indicates that code is incomplete.
 *
 * @since 2025/03/27
 */
@SquirrelJMEVendorApi
public class IncompleteCodeError
	extends Error
{
	/**
	 * Initializes the exception.
	 *
	 * @since 2025/03/27
	 */
	@SquirrelJMEVendorApi
	public IncompleteCodeError()
	{
		super("TODO");
	}
}
