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
import net.multiphasicapps.io.region.SizeLimitedInputStream;
import net.multiphasicapps.squirreljme.java.symbols.MethodSymbol;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.base.JITMethodFlags;

/**
 * This class is used to decode the actual code attribute in the method
 * along with any of its attributes.
 *
 * @since 2016/08/19
 */
final class __CodeDecoder__
{
	/** The maximum number of bytes the code attribute can be. */
	private static final int _CODE_SIZE_LIMIT =
		65535;
	
	/** The owning class decoder. */
	final __ClassDecoder__ _decoder;
	
	/** The logic writer to use. */
	final JITMethodWriter _writer;
	
	/** The method flags. */
	final JITMethodFlags _flags;
	
	/** The method type. */
	final MethodSymbol _type;
	
	/** The input code attribute data. */
	private final DataInputStream _input;
	
	/**
	 * Add base code decoder class.
	 *
	 * @param __cd The class decoder being used.
	 * @param __dis The input source.
	 * @param __f The method flags.
	 * @param __t The method type.
	 * @param __mlw The logic writer to write code to.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/18
	 */
	__CodeDecoder__(__ClassDecoder__ __cd, DataInputStream __dis,
		JITMethodFlags __f, MethodSymbol __t, JITMethodWriter __mlw)
		throws NullPointerException
	{
		// Check
		if (__cd == null || __dis == null || __f == null || __t == null ||
			__mlw == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._decoder = __cd;
		this._input = __dis;
		this._flags = __f;
		this._type = __t;
		this._writer = __mlw;
	}
	
	/**
	 * Decodes the code attribute and any of its contained data
	 *
	 * @return The program to be given to the writer.
	 * @throws IOException On read/write errors.
	 * @since 2016/08/23
	 */
	JITMethodProgram __decode()
		throws IOException
	{
		DataInputStream input = this._input;
		
		// Read max stack and locals
		int maxstack = input.readUnsignedShort();
		int maxlocals = input.readUnsignedShort();
		
		// {@squirreljme.error ED06 The code for a given method exceeds the
		// code size limit, or the size is zero. (The current code length;
		// The code size limit)}
		int codelen = input.readInt();
		if (codelen <= 0 || codelen > _CODE_SIZE_LIMIT)
			throw new JITException(String.format("ED06 %d %d",
				codelen & 0xFFFF_FFFFL, _CODE_SIZE_LIMIT));
		
		// Setup read for byte code
		try (DataInputStream cdis = new DataInputStream(
			new SizeLimitedInputStream(input, codelen, true)))
		{
			if (true)
				throw new Error("TODO");
		}
		
		// Read the exception table
		int numex = input.readUnsignedShort();
		for (int i = 0; i < numex; i++)
			throw new Error("TODO");
		
		throw new Error("TODO");
	}
}

