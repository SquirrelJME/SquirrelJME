// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.io.DataInputStream;
import java.io.IOException;
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;

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
	
	/** The namespace to write into. */
	protected final JITNamespaceWriter namespace;
	
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
	 * @param __ns The class namespace.
	 * @param __dis The source for data input.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/28
	 */
	__ClassDecoder__(JIT __jit, JITNamespaceWriter __ns, DataInputStream __dis)
		throws NullPointerException
	{
		// Check
		if (__jit == null || __ns == null || __dis == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.jit = __jit;
		this.namespace = __ns;
		this.input = __dis;
	}
	
	/**
	 * This performs the actual decoding of the class file.
	 *
	 * @param __jo The JIT output to use, when the name of the class is known
	 * then will be opened and written to during the decoding process.
	 * @throws IOException On read errors.
	 * @throws JITException If the class file format is not correct.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/28
	 */
	final void __decode(JITOutput __jo)
		throws IOException, JITException, NullPointerException
	{
		// Check
		if (__jo == null)
			throw new NullPointerException("NARG");
		
		DataInputStream input = this.input;
		
		// {@squirreljme.error ED12 The magic number of the input data stream
		// does not match that of the Java class file. (The magic number which
		// was read)}
		int fail;
		if ((fail = input.readInt()) != MAGIC_NUMBER)
			throw new JITException(String.format("ED12 %08x", fail));
		
		// {@squirreljme.error ED13 The version number of the input class file
		// is not valid. (The version number)}
		int cver = input.readShort() | (input.readShort() << 16);
		__ClassVersion__ version = __ClassVersion__.findVersion(cver);
		this._version = version;
		if (version == null)
			throw new JITException(String.format("ED13 %d.%d", cver >>> 16,
				(cver & 0xFFFF)));
		
		// Decode the constant pool
		__ClassPool__ pool = new __ClassPool__(input);
		this._pool = pool;
		
		// Read the flags for this class
		JITClassFlags cf = __FlagDecoder__.__class(input.readUnsignedShort());
		
		// Read class name
		ClassNameSymbol clname = pool.<ClassNameSymbol>get(
			input.readUnsignedShort(), ClassNameSymbol.class);
		
		// There is enough "known" information (just the name) to start
		// outputting a class
		try (JITClassWriter cw = this.namespace.beginClass(clname))
		{
			// Send class flags
			cw.classFlags(cf);
			
			throw new Error("TODO");
		}
	}
}

