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
import java.io.IOException;

/**
 * This performs the decoding of the class file format.
 *
 * @since 2016/06/28
 */
final class __ClassDecoder__
{
	/** The owning JIT. */
	protected final SSJIT jit;
	
	/** The input data source. */
	protected final DataInputStream input;
	
	/**
	 * This initializes the decoder for classes.
	 *
	 * @param __jit The owning JIT.
	 * @param __dis The source for data input.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/28
	 */
	__ClassDecoder__(SSJIT __jit, DataInputStream __dis)
		throws NullPointerException
	{
		// Check
		if (__jit == null || __dis == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.jit = __jit;
		this.input = __dis;
	}
	
	/**
	 * This performs the actual decoding of the class file.
	 *
	 * @throws IOException On read errors.
	 * @throws SSJITException If the class file format is not correct.
	 * @since 2016/06/28
	 */
	final void __decode()
		throws SSJITException
	{
		throw new Error("TODO");
	}
}

