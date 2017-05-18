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
		
		// Initialize base input and output states
		if (true)
			throw new todo.TODO();
		/*
		this._javain = new ActiveCacheState(this, maxstack, maxlocals, config);
		this._javaout = new ActiveCacheState(this, maxstack, maxlocals,
			config);
		*/
		
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
		
		// Initialize the program
		ProgramState program = new ProgramState(bc, smtdata, smtmodern);
		this.program = program;
		
		throw new todo.TODO();
	}
	
	/**
	 * Decodes a single operation.
	 *
	 * @param __in The input code stream.
	 * @throws IOException On read errors.
	 * @throws JITException If the code decodes improperly.
	 * @since 2017/04/15
	 */
	private void __decodeOp(__CountStream__ __in)
		throws IOException, JITException
	{
		// Read operation
		int op = __in.readUnsignedByte();
		if (op == __OperandIndex__.WIDE)
			op = (op << 8) | __in.readUnsignedByte();
		
		throw new todo.TODO();
		/*
		// Depends
		switch (op)
		{
			case __OperandIndex__.ALOAD:
				__executeLoad(JavaType.OBJECT, __in.readUnsignedByte());
				break;
			
			case __OperandIndex__.WIDE_ALOAD:
				__executeLoad(JavaType.OBJECT, __in.readUnsignedShort());
				break;
			
			case __OperandIndex__.ALOAD_0:
			case __OperandIndex__.ALOAD_1:
			case __OperandIndex__.ALOAD_2:
			case __OperandIndex__.ALOAD_3:
				__executeLoad(JavaType.OBJECT, op - __OperandIndex__.ALOAD_0);
				break;
				
			case __OperandIndex__.ILOAD:
				__executeLoad(JavaType.INTEGER, __in.readUnsignedByte());
				break;
			
			case __OperandIndex__.WIDE_ILOAD:
				__executeLoad(JavaType.INTEGER, __in.readUnsignedShort());
				break;
			
			case __OperandIndex__.ILOAD_0:
			case __OperandIndex__.ILOAD_1:
			case __OperandIndex__.ILOAD_2:
			case __OperandIndex__.ILOAD_3:
				__executeLoad(JavaType.INTEGER, op - __OperandIndex__.ILOAD_0);
				break;
				
			case __OperandIndex__.LLOAD:
				__executeLoad(JavaType.LONG, __in.readUnsignedByte());
				break;
			
			case __OperandIndex__.WIDE_LLOAD:
				__executeLoad(JavaType.LONG, __in.readUnsignedShort());
				break;
				
			case __OperandIndex__.LLOAD_0:
			case __OperandIndex__.LLOAD_1:
			case __OperandIndex__.LLOAD_2:
			case __OperandIndex__.LLOAD_3:
				__executeLoad(JavaType.LONG, op - __OperandIndex__.LLOAD_0);
				break;
				
			case __OperandIndex__.FLOAD:
				__executeLoad(JavaType.FLOAT, __in.readUnsignedByte());
				break;
			
			case __OperandIndex__.WIDE_FLOAD:
				__executeLoad(JavaType.FLOAT, __in.readUnsignedShort());
				break;
				
			case __OperandIndex__.FLOAD_0:
			case __OperandIndex__.FLOAD_1:
			case __OperandIndex__.FLOAD_2:
			case __OperandIndex__.FLOAD_3:
				__executeLoad(JavaType.FLOAT, op - __OperandIndex__.FLOAD_0);
				break;
				
			case __OperandIndex__.DLOAD:
				__executeLoad(JavaType.DOUBLE, __in.readUnsignedByte());
				break;
			
			case __OperandIndex__.WIDE_DLOAD:
				__executeLoad(JavaType.DOUBLE, __in.readUnsignedShort());
				break;
				
			case __OperandIndex__.DLOAD_0:
			case __OperandIndex__.DLOAD_1:
			case __OperandIndex__.DLOAD_2:
			case __OperandIndex__.DLOAD_3:
				__executeLoad(JavaType.DOUBLE, op - __OperandIndex__.DLOAD_0);
				break;
		
				// {@squirreljme.error AQ1e Invalid byte code operation index.
				// (The operation index; The address of the operation)}
			default:
				throw new JITException(String.format("AQ1e %#02x %d", op,
					this._atpc));
		}
		*/
	}
	
	/**
	 * Generates code for the load operation which potentially aliases and/or
	 * copies the value from a local variable onto the stack.
	 *
	 * @param __t The type to copy.
	 * @param __from The local variable slot to copy from.
	 * @throws JITException If the type is not valid, the stack overflows, or
	 * the specified local variable is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/04/18
	 */
	void __executeLoad(JavaType __t, int __from)
		throws JITException, NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AQ1f The specified type cannot be loaded from a
		// local variable. (The type to load)}
		if (!__t.isValid())
			throw new JITException(String.format("AQ1f %s", __t));
		
		throw new todo.TODO();
		/*
		// Debug
		System.err.printf("DEBUG -- load: %s %d%n", __t, __from);
		
		CacheState javain = this._javain;
		ActiveCacheState javaout = this._javaout;
		
		// {@squirreljme.error AQ23 The incorrect type is in the local variable
		// slot. (The desired type; The actual type)}
		CacheState.Slot src = javain.getSlot(AreaType.LOCAL, __from);
		if (src.valueType() != __t)
			throw new JITException(String.format("AQ23 %s %s", __t, src));
		
		// Push a copy
		javaout.pushCopy(src);
		
		// Normally flows to the next instruction
		this._return.setFlow(__ExecutionFlow__.NEXT);
		*/
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
		/*
		SnapshotCacheStates smt = this._smt;
		CacheState javain = this._javain;
		ActiveCacheState javaout = this._javaout;
		__Return__ javaret = this._return;
		
		// Open stream to the code
		try (__CountStream__ code = new __CountStream__(
			new ByteArrayInputStream(this._code)))
		{
			// Reset the return state
			javaret.reset();
			
			// The address currently being handled
			int atpc = code.count();
			this._atpc = atpc;
			
			// {@squirreljme.error AQ1g No Java state exists for the specified
			// instruction address. (The address)}
			SnapshotCacheState existstate = smt.get(atpc);
			if (existstate == null)
				throw new JITException(String.format("AQ1g %d", atpc));
			
			// The input and base output state become the existing state
			((ActiveCacheState)javain).switchFrom(existstate);
			javaout.switchFrom(existstate);
			
			// Debug
			System.err.printf("DEBUG -- IN at %d: %s%n", atpc, javain);
			
			// Decode single operation
			__decodeOp(code);
			
			// Debug
			System.err.printf("DEBUG -- OUT at %d: %s%n", atpc, javaout);
			
			// Depends on the flow, always hide nexts
			__ExecutionFlow__ flow = javaret.flow();
			if (flow.equals(__ExecutionFlow__.NEXT))
				flow = new __ExecutionFlow__(code.count());
			
			throw new todo.TODO();
		}
		*/
	}
}

