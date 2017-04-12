// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;

/**
 * This handles the code attribute for a method.
 *
 * @since 2017/04/09
 */
class __Code__
{
	/** The maximum number of bytes the code attribute can be. */
	private static final int _CODE_SIZE_LIMIT =
		65535;
	
	/** The exported method. */
	protected final ExportedMethod method;
	
	/** The constant pool. */
	protected final __Pool__ pool;
	
	/** The link table for imports. */
	protected final LinkTable linktable;
	
	/** The exception handler table for the current method. */
	protected final ExceptionHandlerTable exceptions;
	
	/** The byte code for the method. */
	private final byte[] _code;
	
	/**
	 * Initializes the code decoder and perform some initial seeding work that
	 * is needed for the decoder.
	 *
	 * @param __em The output exported method.
	 * @param __is The input stream for the code.
	 * @param __pool The constant pool.
	 * @param __lt The link table.
	 * @throws IOException On read errors.
	 * @throws JITException On initial parsing errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/04/09
	 */
	__Code__(ExportedMethod __em, DataInputStream __is, __Pool__ __pool,
		LinkTable __lt)
		throws IOException, JITException, NullPointerException
	{
		// Check
		if (__em == null || __is == null || __pool == null || __lt == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.method = __em;
		this.pool = __pool;
		this.linktable = __lt;
		
		// Read local variable count
		int maxstack = __is.readUnsignedShort(),
			maxlocals = __is.readUnsignedShort();
		
		// {@squirreljme.error AQ0x Method has an invalid size for the length
		// of its byte code. (The length of the code)}
		int len = __is.readInt();
		if (len <= 0 || len > _CODE_SIZE_LIMIT)
			throw new JITException(String.format("AQ0x %d", len));
		
		// Read all of it
		byte[] code = new byte[len];
		__is.readFully(code);
		this._code = code;
		
		// Read the exception table
		ExceptionHandlerTable exceptions = new ExceptionHandlerTable(__is,
			__pool, len);
		this.exceptions = exceptions;
		
		// Only handle the stack map information
		int[] count = new int[]{__is.readUnsignedShort()};
		String[] aname = new String[1];
		boolean didsmt = false;
		while ((count[0]--) > 0)
			try (DataInputStream as = JIT.__nextAttribute(__is, __pool, aname))
			{
				// Only handle the stack map, ignore all other information
				// such as debugging
				boolean old = aname[0].equals("StackMap"),
					modern = aname[0].equals("StackMapTable");
				if (!old && !modern)
					continue;
				
				// {@squirreljme.error AQ11 Already parsed the stack map
				// table.}
				if (didsmt)
					throw new JITException("AQ11");
				
				throw new todo.TODO();
			}
		
		// Process the byte code
		throw new todo.TODO();
	}
	
	/**
	 * Runs the code decoder.
	 *
	 * @throws IOException On read errors.
	 * @throws JITException If the code decodes improperly.
	 * @since 2017/04/09
	 */
	void __run()
		throws IOException, JITException
	{
		throw new todo.TODO();
	}
}

