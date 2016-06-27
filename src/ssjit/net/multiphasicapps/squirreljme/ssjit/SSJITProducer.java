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
 * This is the base class for code producers, this class is given a number of
 * interfaces which implement the required functionality that is needed to
 * generate actual output code.
 *
 * Code producers are not to be reused across multiple methods.
 *
 * @since 2016/06/25
 */
public final class SSJITProducer
{
	/** The target system variant. */
	protected final SSJITVariant variant;
	
	/**
	 * Initializes the code producer.
	 *
	 * @param __os The output stream where data is to be written.
	 * @param __var The variant of the target system.
	 * @param __fps Function providers that provide the needed functionality
	 * for generating specific fragments of native code.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/25
	 */
	SSJITProducer(OutputStream __os, SSJITVariant __var,
		SSJITFunctionProvider... __fps)
		throws NullPointerException
	{
		// Check
		if (__os == null || __var == null || __fps == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.variant = __var;
		
		throw new Error("TODO");
	}
	
	/**
	 * Returns the variant that is used for this producer.
	 *
	 * @return The producer variant.
	 * @since 2016/06/25
	 */
	public final SSJITVariant variant()
	{
		return this.variant;
	}
}

