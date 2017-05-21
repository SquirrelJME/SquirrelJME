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
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import net.multiphasicapps.squirreljme.java.symbols.MethodSymbol;
import net.multiphasicapps.squirreljme.linkage.MethodFlags;

/**
 * This handles the code attribute for a method.
 *
 * @since 2017/04/09
 */
class __Code__
{
	/** The number of working variable. */
	static final int _WORK_COUNT =
		4;
	
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
	
	/** The JIT configuration. */
	final JITConfig _config;
	
	/** The byte code for the method. */
	protected final ByteCode code;
	
	/** The program state. */
	protected final ProgramState program;
	
	/**
	 * Initializes the code decoder and perform some initial seeding work that
	 * is needed for the decoder.
	 *
	 * @param __em The output exported method.
	 * @param __is The input stream for the code.
	 * @param __pool The constant pool.
	 * @param __lt The link table.
	 * @param __conf The configuration for the JIT.
	 * @throws IOException On read errors.
	 * @throws JITException On initial parsing errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/04/09
	 */
	__Code__(ExportedMethod __em, DataInputStream __is, __Pool__ __pool,
		LinkTable __lt, JITConfig __conf)
		throws IOException, JITException, NullPointerException
	{
		// Check
		if (__em == null || __is == null || __pool == null || __lt == null ||
			__conf == null)
			throw new NullPointerException("NARG");
		
		// These are needed by the JIT for code generation
		JITConfig config = __conf;
		this._config = config;
		
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
		
		// Read the exception table
		ExceptionHandlerTable exceptions = new ExceptionHandlerTable(__is,
			__pool, len);
		this.exceptions = exceptions;
		
		// Locate the stack map table
		boolean smtmodern = false;
		byte[] smtdata = null;
		int[] count = new int[]{__is.readUnsignedShort()};
		String[] aname = new String[1];
		while ((count[0]--) > 0)
			try (DataInputStream as = JIT.__nextAttribute(__is, __pool, aname))
			{
				// Only handle the stack map, ignore all other information
				// such as debugging
				boolean old = aname[0].equals("StackMap"),
					modern = aname[0].equals("StackMapTable");
				if (!old && !modern)
					continue;
				
				// {@squirreljme.error AQ11 Methods may only have a single
				// stack map table.}
				if (smtdata != null)
					throw new JITException("AQ11");
				
				// Copy to target buffer for later parsing
				try (ByteArrayOutputStream baos = new ByteArrayOutputStream())
				{
					// Write to the buffer
					byte[] buf = new byte[512];
					for (;;)
					{
						int rc = as.read(buf);
						
						// EOF?
						if (rc < 0)
							break;
						
						baos.write(buf, 0, rc);
					}
					
					// Store
					smtmodern = modern;
					smtdata = baos.toByteArray();
				}
			}
		
		// Use a default stack map (one which is completely empty)
		if (smtdata == null)
		{
			smtmodern = true;
			smtdata = new byte[]{0, 0};
		}
		
		// Initialize code
		ByteCode bc = new ByteCode(maxstack, maxlocals, code, exceptions,
			__pool);
		this.code = bc;
		
		// Debug
		System.err.println("DEBUG -- Method Code");
		for (ByteCode.Instruction i : bc)
			System.err.printf("DEBUG -- %s%n", i);
		System.err.println("DEBUG -- -----------");
		
		// Initialize the program
		ProgramState program = new ProgramState(bc, smtdata, smtmodern, __em,
			__conf, linktable);
		this.program = program;
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
		// Run the program parse loop
		ProgramState program = this.program;
		program.__run();
		
		// Export the program to native machine code
		throw new todo.TODO();
	}
}

