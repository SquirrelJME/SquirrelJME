// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.ssjit;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This is the base class associated with the JIT code generator which is able
 * to generate native machine code for a given target.
 *
 * The base class handles data output, however it is up to the extending
 * classes to provide the generators for operations.
 *
 * Code producers are not to be reused across multiple methods.
 *
 * @since 2016/06/25
 */
public abstract class CodeProducer
{
	/**
	 * Initializes the code producer.
	 *
	 * @param __os The output stream where data is to be written.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/25
	 */
	CodeProducer(OutputStream __os)
		throws NullPointerException
	{
		// Check
		if (__os == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

