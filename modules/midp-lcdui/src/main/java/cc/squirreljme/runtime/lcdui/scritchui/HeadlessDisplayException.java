// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.scritchui;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * This is thrown when ScritchUI is not supported.
 *
 * @since 2024/08/07
 */
@SquirrelJMEVendorApi
public class HeadlessDisplayException
	extends RuntimeException
{
	/**
	 * Initializes the exception.
	 *
	 * @param __c The cause of it.
	 * @since 2024/08/07
	 */
	@SquirrelJMEVendorApi
	public HeadlessDisplayException(Throwable __c)
	{
		super(__c);
	}
}
