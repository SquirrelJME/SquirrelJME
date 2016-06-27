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
	
	/** Internal once lock. */
	private final Object _once =
		new Object();
	
	/** Internal did? */
	private volatile boolean _did;
	
	/**
	 * This intializes the single stage JIT compiler.
	 *
	 * @param __ic The input class stream.
	 * @param __ob The output binary which contains native code.
	 * @param __pfv The variant to use for the code generator.
	 * @param __fps The factories to provide functions to this JIT compiler.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/24
	 */
	public SSJIT(InputStream __ic, OutputStream __ob,
		SSJITVariant __pfv, SSJITFunctionProvider... __fps)
		throws NullPointerException
	{
		// Check
		if (__ic == null || __ob == null || __fps == null || __pfv == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.input = new DataInputStream(__ic);
		
		// Create producer
		SSJITProducer producer = new SSJITProducer(__ob, __pfv, __fps);
		this.producer = producer;
	}
	
	/**
	 * Performs actual JIT compilation.
	 *
	 * @throws IllegalStateException If JIT compilation is currently being
	 * performed or is being performed.
	 * @throws IOException On read/write errors.
	 * @throws SSJITException If JIT compilation fails.
	 * @since 2016/06/25
	 */
	public final void performJit()
		throws IllegalStateException, IOException, SSJITException
	{
		// {@squirreljme.error DV01 JIT Compilation has already been
		// performed.}
		if (this._did)
			throw new IllegalStateException("DV01");
		
		// Only once can things be JITted
		synchronized (this._once)
		{
			if (this._did)
				throw new IllegalStateException("DV01");
			this._did = true;
		}
		
		throw new Error("TODO");
	}
}

