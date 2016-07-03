// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * This performs the decoding of the class file format.
 *
 * @since 2016/06/28
 */
final class __ClassDecoder__
{
	/** The magic number of the class file. */
	public static final int MAGIC_NUMBER =
		0xCAFE_BABE;
	
	/** The owning JIT. */
	protected final JIT jit;
	
	/** The input data source. */
	protected final DataInputStream input;
	
	/** The version of this class file. */
	private volatile __ClassVersion__ _version;
	
	/** The constant pool of the class. */
	private volatile __ClassPool__ _pool;
	
	/**
	 * This initializes the decoder for classes.
	 *
	 * @param __jit The owning JIT.
	 * @param __dis The source for data input.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/28
	 */
	__ClassDecoder__(JIT __jit, DataInputStream __dis)
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
	 * @throws JITException If the class file format is not correct.
	 * @since 2016/06/28
	 */
	final void __decode()
		throws IOException, JITException
	{
		DataInputStream input = this.input;
		
		// {@squirreljme.error DV02 The magic number of the input data stream
		// does not match that of the Java class file. (The magic number which
		// was read)}
		int fail;
		if ((fail = input.readInt()) != MAGIC_NUMBER)
			throw new JITException(String.format("DV02 %08x", fail));
		
		// {@squirreljme.error DV03 The version number of the input class file
		// is not valid. (The version number)}
		int cver = input.readShort() | (input.readShort() << 16);
		__ClassVersion__ version = __ClassVersion__.findVersion(cver);
		this._version = version;
		if (version == null)
			throw new JITException(String.format("DV03 %d.%d", cver >>> 16,
				(cver & 0xFFFF)));
		
		// Decode the constant pool
		__ClassPool__ pool = new __ClassPool__(input);
		this._pool = pool;
		
		// Read the flags for this class
		
		throw new Error("TODO");
	}
}

