// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This is thrown when the implementation is based on documentation where an
 * assumption is made on what happens but where the documentation and
 * implementation are both suspect and lacking information.
 * 
 * These errors require more study to be accurate.
 *
 * @since 2024/01/13
 */
@SquirrelJMEVendorApi
public class DubiousImplementationError
	extends Error
{
	/**
	 * Initializes the error with the given message and cause.
	 *
	 * @param __message The message to use.
	 * @param __cause The cause to use.
	 * @since 2024/01/13
	 */
	@SquirrelJMEVendorApi
	public DubiousImplementationError(String __message, Throwable __cause)
	{
		super(__message, __cause);
	}
}
