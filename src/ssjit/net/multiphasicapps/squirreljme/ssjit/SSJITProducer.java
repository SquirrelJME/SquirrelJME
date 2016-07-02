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
	protected final String variant;
	
	/** Where the output machine code is placed. */
	protected final SSJITOutput output;
	
	/**
	 * Initializes the code producer.
	 *
	 * @param __os The output where produced code is to be placed.
	 * @param __var The variant of the target system, if a variant is not
	 * supported then the default variant will be used instead.
	 * @param __fps Function providers that provide the needed functionality
	 * for generating specific fragments of native code.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/25
	 */
	SSJITProducer(SSJITOutput __os, String __var,
		SSJITFunctionProvider... __fps)
		throws NullPointerException
	{
		// Check
		if (__os == null || __var == null || __fps == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.variant = __var;
		this.output = __os;
		
		// Go through all function providers to create and bind sub-functions
		for (SSJITFunctionProvider fp : __fps.clone())
		{
			// Locate variant
			SSJITVariant var = fp.getVariant(__var);
			
			// Fallback to default?
			if (var == null)
				var = fp.genericVariant();
			
			// Go through functions and bind them
			for (SSJITFunction func : fp.functions(var))
			{
				// Bind to the producer
				func.bind(this);
			}
		}
	}
	
	/**
	 * Returns the variant that is used for this producer.
	 *
	 * @return The producer variant.
	 * @since 2016/06/25
	 */
	public final String variant()
	{
		return this.variant;
	}
}

