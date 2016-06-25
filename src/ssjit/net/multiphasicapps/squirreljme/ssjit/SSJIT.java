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

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This class is the interface which reads an input class file and calls code
 * generaton methods so that native code may be generated based on the input
 * byte code.
 *
 * @since 2016/06/24
 */
public class SSJIT
{
	/** The input class file. */
	protected final DataInputStream input;
	
	/** The code producer. */
	protected final SSJITProducer producer;
	
	/**
	 * This intializes the single stage JIT compiler.
	 *
	 * @param __ic The input class stream.
	 * @param __ob The output binary which contains native code.
	 * @param __pf The code producer factory.
	 * @param __pfv The variant to use for the code generator.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/24
	 */
	public SSJIT(InputStream __ic, OutputStream __ob,
		SSJITProducerFactory __pf, SSJITProducerFactory.Variant __pfv)
		throws NullPointerException
	{
		// Check
		if (__ic == null || __ob == null || __pf == null || __pfv == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.input = new DataInputStream(__ic);
		
		// Create producer
		SSJITProducer producer = __pf.createProducer(__ob, __pfv);
		this.producer = producer;
		
		throw new Error("TODO");
	}
}

