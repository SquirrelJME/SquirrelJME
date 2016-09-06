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

import java.io.IOException;
import java.util.Map;
import net.multiphasicapps.io.data.ExtendedDataInputStream;
import net.multiphasicapps.squirreljme.java.symbols.MethodSymbol;
import net.multiphasicapps.squirreljme.jit.base.JITClassFlags;
import net.multiphasicapps.squirreljme.jit.base.JITException;

/**
 * This performs the actual parsing of the opcodes and generates operations
 * for it.
 *
 * @since 2016/08/29
 */
final class __OpParser__
{
	/** Indicates that a local/stack item is not a duplicate of anything. */
	static final int UNIQUE_STACK_VALUE =
		__SMTStack__.UNIQUE_STACK_VALUE;
	
	/** Flags that the cache is a copy from the stack and not a local. */
	static final int CACHE_STACK_MASK =
		__SMTStack__.CACHE_STACK_MASK;
	
	/** Implicit next operation. */
	private static final int[] IMPLICIT_NEXT =
		new int[]{-1};
	
	/** The input operation data. */
	protected final ExtendedDataInputStream input;
	
	/** The writer used for output. */
	protected final JITMethodWriter writer;
	
	/** The class flags. */
	protected final JITClassFlags classflags;
	
	/** The constant pool. */
	protected final JITConstantPool pool;
	
	/** The stack map table. */
	private final Map<Integer, __SMTState__> _smt;
	
	/** The working stack map state. */
	private final __SMTState__ _smwork;
	
	/** Was the stack cache state flushed? */
	private volatile boolean _stackflushed;
	
	/**
	 * Initializes the operation parser.
	 *
	 * @param __dis The input data source.
	 * @param __smt The stack map table.
	 * @param __cf The class flags.
	 * @param __pool The constant pool.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/29
	 */
	__OpParser__(JITMethodWriter __jmw, ExtendedDataInputStream __dis,
		Map<Integer, __SMTState__> __smt, JITClassFlags __cf,
		JITConstantPool __pool)
		throws NullPointerException
	{
		// Check
		if (__dis == null || __smt == null || __cf == null || __pool == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.writer = __jmw;
		this.input = __dis;
		this.classflags = __cf;
		this._smt = __smt;
		this.pool = __pool;
		
		// Set working state
		this._smwork = new __SMTState__(__smt.get(0));
	}
	
	/**
	 * Decodes operations and calls the method logic generator interface.
	 *
	 * @throws IOException On read errors.
	 * @throws JITException On decode errors.
	 * @since 2016/08/24
	 */
	void __decodeAll()
		throws IOException, JITException
	{
		// Get
		ExtendedDataInputStream input = this.input;
		JITMethodWriter writer = this.writer;
		Map<Integer, __SMTState__> smt = this._smt;
		__SMTState__ smwork = this._smwork;
		
		// Decode loop
		for (;;)
		{
			// Read
			int nowpos = (int)input.size();
			int code = input.read();
			
			// EOF?
			if (code < 0)
				break;
			
			// Wide? Read another
			if (code == __OpIndex__.WIDE)
				code = (code << 8) | input.readUnsignedByte();
			
			// If there is stack state for this position then set it
			__SMTState__ bias = smt.get(nowpos);
			if (bias != null)
				smwork.from(bias);
			
			// Stack not flushed
			this._stackflushed = false;
			
			// Report it
			writer.atInstruction(code, nowpos);
			
			// Decode single operation
			int[] jt = __decodeOne(code);
			if (jt == null)
				throw new RuntimeException("OOPS");
			
			// Handle jump targets
			// This verifies that for each jump target or implicit flow that
			// the resulting stack state matches the expected one
			// Stack cache flushing may also be performed
			int n = jt.length;
			boolean hasflushed = this._stackflushed;
			for (int i = 0; i < n; i++)
			{
				// Get jump target, if it is implicit next then use the
				// following instruction address
				int jumpto = jt[i];
				boolean implicit;
				if ((implicit = (jumpto == -1)))
					jumpto = (int)input.size();
				
				// If there is no state, do not need to verify
				__SMTState__ intostate = smt.get(jumpto);
				if (intostate == null)
					continue;
				
				// Verify that they are correct
				if (true)
					throw new Error("TODO");
				
				// If this instruction naturally flows into a jump target
				// (since jump handling flushes before the actual jump) then
				// the stack cache will have to be flushed where locals are
				// copied to the stack.
				if (!hasflushed)
				{
					// Flush the stack
					__flushStack();
					
					// Operations flushed, do not need to generate code for
					// them now
					hasflushed = true;
				}
			}
		}
		
		throw new Error("TODO");
	}
	
	/**
	 * Decodes a single operation.
	 *
	 * @param __code The byte code to decode.
	 * @return The jump targets.
	 * @since 2016/08/26
	 */
	private int[] __decodeOne(int __code)
		throws IOException, JITException
	{
		// Get
		ExtendedDataInputStream input = this.input;
		
		// Depends
		switch (__code)
		{
				// Waste electrons
			case __OpIndex__.NOP:
				throw new Error("TODO");
			
				// Push null constant
			case __OpIndex__.ACONST_NULL:
				throw new Error("TODO");
			
				// Push int constant
			case __OpIndex__.ICONST_M1:
			case __OpIndex__.ICONST_0:
			case __OpIndex__.ICONST_1:
			case __OpIndex__.ICONST_2:
			case __OpIndex__.ICONST_3:
			case __OpIndex__.ICONST_4:
			case __OpIndex__.ICONST_5:
				throw new Error("TODO");
			
				// Push long constant
			case __OpIndex__.LCONST_0:
			case __OpIndex__.LCONST_1:
				throw new Error("TODO");
			
				// Push float constant
			case __OpIndex__.FCONST_0:
			case __OpIndex__.FCONST_1:
			case __OpIndex__.FCONST_2:
				throw new Error("TODO");
			
				// Push double constant
			case __OpIndex__.DCONST_0:
			case __OpIndex__.DCONST_1:
				throw new Error("TODO");
			
			case __OpIndex__.BIPUSH:
			case __OpIndex__.SIPUSH:
			case __OpIndex__.LDC:
			case __OpIndex__.LDC_W:
			case __OpIndex__.LDC2_W:
				throw new Error("TODO");
				
				// Push local int to the stack
			case __OpIndex__.ILOAD:
				return __executeLoad(__SMTType__.INTEGER,
					input.readUnsignedByte());
				
				// Push local int to the stack (wide)
			case __OpIndex__.WIDE_ILOAD:
				return __executeLoad(__SMTType__.INTEGER,
					input.readUnsignedShort());
				
				// Push local long to the stack
			case __OpIndex__.LLOAD:
				return __executeLoad(__SMTType__.LONG,
					input.readUnsignedByte());
				
				// Push local long to the stack (wide)
			case __OpIndex__.WIDE_LLOAD:
				return __executeLoad(__SMTType__.LONG,
					input.readUnsignedShort());
				
				// Push local float to the stack
			case __OpIndex__.FLOAD:
				return __executeLoad(__SMTType__.FLOAT,
					input.readUnsignedByte());
				
				// Push local float to the stack (wide)
			case __OpIndex__.WIDE_FLOAD:
				return __executeLoad(__SMTType__.FLOAT,
					input.readUnsignedShort());
			
				// Push local double to the stack
			case __OpIndex__.DLOAD:
				return __executeLoad(__SMTType__.DOUBLE,
					input.readUnsignedByte());
				
				// Push local double to the stack (wide)
			case __OpIndex__.WIDE_DLOAD:
				return __executeLoad(__SMTType__.DOUBLE,
					input.readUnsignedShort());
				
				// Push local reference to the stack
			case __OpIndex__.ALOAD:
				return __executeLoad(__SMTType__.OBJECT,
					input.readUnsignedByte());
				
				// Push local reference to the stack (wide)
			case __OpIndex__.WIDE_ALOAD:
				return __executeLoad(__SMTType__.OBJECT,
					input.readUnsignedShort());
			
				// Load int from local
			case __OpIndex__.ILOAD_0:
			case __OpIndex__.ILOAD_1:
			case __OpIndex__.ILOAD_2:
			case __OpIndex__.ILOAD_3:
				return __executeLoad(__SMTType__.INTEGER,
					__code - __OpIndex__.ILOAD_0);
			
				// Load long from local
			case __OpIndex__.LLOAD_0:
			case __OpIndex__.LLOAD_1:
			case __OpIndex__.LLOAD_2:
			case __OpIndex__.LLOAD_3:
				return __executeLoad(__SMTType__.LONG,
					__code - __OpIndex__.LLOAD_0);
			
				// Load float from local
			case __OpIndex__.FLOAD_0:
			case __OpIndex__.FLOAD_1:
			case __OpIndex__.FLOAD_2:
			case __OpIndex__.FLOAD_3:
				return __executeLoad(__SMTType__.FLOAT,
					__code - __OpIndex__.FLOAD_0);
			
				// Load double from local
			case __OpIndex__.DLOAD_0:
			case __OpIndex__.DLOAD_1:
			case __OpIndex__.DLOAD_2:
			case __OpIndex__.DLOAD_3:
				return __executeLoad(__SMTType__.DOUBLE,
					__code - __OpIndex__.DLOAD_0);
			
				// Load reference from local
			case __OpIndex__.ALOAD_0:
			case __OpIndex__.ALOAD_1:
			case __OpIndex__.ALOAD_2:
			case __OpIndex__.ALOAD_3:
				return __executeLoad(__SMTType__.OBJECT,
					__code - __OpIndex__.ALOAD_0);
			
			case __OpIndex__.IALOAD:
			case __OpIndex__.LALOAD:
			case __OpIndex__.FALOAD:
			case __OpIndex__.DALOAD:
			case __OpIndex__.AALOAD:
			case __OpIndex__.BALOAD:
			case __OpIndex__.CALOAD:
			case __OpIndex__.SALOAD:
			case __OpIndex__.ISTORE:
			case __OpIndex__.LSTORE:
			case __OpIndex__.FSTORE:
			case __OpIndex__.DSTORE:
			case __OpIndex__.ASTORE:
				throw new Error("TODO");
			
				// Store int into local
			case __OpIndex__.ISTORE_0:
			case __OpIndex__.ISTORE_1:
			case __OpIndex__.ISTORE_2:
			case __OpIndex__.ISTORE_3:
				throw new Error("TODO");
			
				// Store long into local
			case __OpIndex__.LSTORE_0:
			case __OpIndex__.LSTORE_1:
			case __OpIndex__.LSTORE_2:
			case __OpIndex__.LSTORE_3:
				throw new Error("TODO");
			
				// Store float into local
			case __OpIndex__.FSTORE_0:
			case __OpIndex__.FSTORE_1:
			case __OpIndex__.FSTORE_2:
			case __OpIndex__.FSTORE_3:
				throw new Error("TODO");
			
				// Store double into local
			case __OpIndex__.DSTORE_0:
			case __OpIndex__.DSTORE_1:
			case __OpIndex__.DSTORE_2:
			case __OpIndex__.DSTORE_3:
				throw new Error("TODO");
			
				// Store object into local
			case __OpIndex__.ASTORE_0:
			case __OpIndex__.ASTORE_1:
			case __OpIndex__.ASTORE_2:
			case __OpIndex__.ASTORE_3:
				throw new Error("TODO");
				
				// Invoke interface method
			case __OpIndex__.INVOKEINTERFACE:
				return __executeInvoke(JITInvokeType.INTERFACE,
					input.readUnsignedShort() | (input.readByte() & 0) |
					(input.readByte() & 0));
				
				// Invoke constructor or private method
			case __OpIndex__.INVOKESPECIAL:
				return __executeInvoke(JITInvokeType.SPECIAL,
					input.readUnsignedShort());
				
				// Invoke static method
			case __OpIndex__.INVOKESTATIC:
				return __executeInvoke(JITInvokeType.STATIC,
					input.readUnsignedShort());
				
				// Invoke virtual method
			case __OpIndex__.INVOKEVIRTUAL:
				return __executeInvoke(JITInvokeType.VIRTUAL,
					input.readUnsignedShort());
			
				// {@squirreljme.error ED08 Defined operation cannot be
				// used in Java ME programs. (The operation)}
			case __OpIndex__.JSR:
			case __OpIndex__.JSR_W:
			case __OpIndex__.RET:
			case __OpIndex__.INVOKEDYNAMIC:
			case __OpIndex__.BREAKPOINT:
			case __OpIndex__.IMPDEP1:
			case __OpIndex__.IMPDEP2:
				throw new JITException(String.format("ED08 %d", __code));
			
			case __OpIndex__.IASTORE:
			case __OpIndex__.LASTORE:
			case __OpIndex__.FASTORE:
			case __OpIndex__.DASTORE:
			case __OpIndex__.AASTORE:
			case __OpIndex__.BASTORE:
			case __OpIndex__.CASTORE:
			case __OpIndex__.SASTORE:
			case __OpIndex__.POP:
			case __OpIndex__.POP2:
			case __OpIndex__.DUP:
			case __OpIndex__.DUP_X1:
			case __OpIndex__.DUP_X2:
			case __OpIndex__.DUP2:
			case __OpIndex__.DUP2_X1:
			case __OpIndex__.DUP2_X2:
			case __OpIndex__.SWAP:
			case __OpIndex__.IADD:
			case __OpIndex__.LADD:
			case __OpIndex__.FADD:
			case __OpIndex__.DADD:
			case __OpIndex__.ISUB:
			case __OpIndex__.LSUB:
			case __OpIndex__.FSUB:
			case __OpIndex__.DSUB:
			case __OpIndex__.IMUL:
			case __OpIndex__.LMUL:
			case __OpIndex__.FMUL:
			case __OpIndex__.DMUL:
			case __OpIndex__.IDIV:
			case __OpIndex__.LDIV:
			case __OpIndex__.FDIV:
			case __OpIndex__.DDIV:
			case __OpIndex__.IREM:
			case __OpIndex__.LREM:
			case __OpIndex__.FREM:
			case __OpIndex__.DREM:
			case __OpIndex__.INEG:
			case __OpIndex__.LNEG:
			case __OpIndex__.FNEG:
			case __OpIndex__.DNEG:
			case __OpIndex__.ISHL:
			case __OpIndex__.LSHL:
			case __OpIndex__.ISHR:
			case __OpIndex__.LSHR:
			case __OpIndex__.IUSHR:
			case __OpIndex__.LUSHR:
			case __OpIndex__.IAND:
			case __OpIndex__.LAND:
			case __OpIndex__.IOR:
			case __OpIndex__.LOR:
			case __OpIndex__.IXOR:
			case __OpIndex__.LXOR:
			case __OpIndex__.IINC:
			case __OpIndex__.I2L:
			case __OpIndex__.I2F:
			case __OpIndex__.I2D:
			case __OpIndex__.L2I:
			case __OpIndex__.L2F:
			case __OpIndex__.L2D:
			case __OpIndex__.F2I:
			case __OpIndex__.F2L:
			case __OpIndex__.F2D:
			case __OpIndex__.D2I:
			case __OpIndex__.D2L:
			case __OpIndex__.D2F:
			case __OpIndex__.I2B:
			case __OpIndex__.I2C:
			case __OpIndex__.I2S:
			case __OpIndex__.LCMP:
			case __OpIndex__.FCMPL:
			case __OpIndex__.FCMPG:
			case __OpIndex__.DCMPL:
			case __OpIndex__.DCMPG:
			case __OpIndex__.IFEQ:
			case __OpIndex__.IFNE:
			case __OpIndex__.IFLT:
			case __OpIndex__.IFGE:
			case __OpIndex__.IFGT:
			case __OpIndex__.IFLE:
			case __OpIndex__.IF_ICMPEQ:
			case __OpIndex__.IF_ICMPNE:
			case __OpIndex__.IF_ICMPLT:
			case __OpIndex__.IF_ICMPGE:
			case __OpIndex__.IF_ICMPGT:
			case __OpIndex__.IF_ICMPLE:
			case __OpIndex__.IF_ACMPEQ:
			case __OpIndex__.IF_ACMPNE:
			case __OpIndex__.GOTO:
			case __OpIndex__.TABLESWITCH:
			case __OpIndex__.LOOKUPSWITCH:
			case __OpIndex__.IRETURN:
			case __OpIndex__.LRETURN:
			case __OpIndex__.FRETURN:
			case __OpIndex__.DRETURN:
			case __OpIndex__.ARETURN:
			case __OpIndex__.RETURN:
			case __OpIndex__.GETSTATIC:
			case __OpIndex__.PUTSTATIC:
			case __OpIndex__.GETFIELD:
			case __OpIndex__.PUTFIELD:
			case __OpIndex__.NEW:
			case __OpIndex__.NEWARRAY:
			case __OpIndex__.ANEWARRAY:
			case __OpIndex__.ARRAYLENGTH:
			case __OpIndex__.ATHROW:
			case __OpIndex__.CHECKCAST:
			case __OpIndex__.INSTANCEOF:
			case __OpIndex__.MONITORENTER:
			case __OpIndex__.MONITOREXIT:
			case __OpIndex__.MULTIANEWARRAY:
			case __OpIndex__.IFNULL:
			case __OpIndex__.IFNONNULL:
			case __OpIndex__.GOTO_W:
			
				// {@squirreljme.error ED07 Unknown byte-code operation.
				// (The operation)}
			default:
				throw new JITException(String.format("ED07 %d", __code));
		}
	}
	
	/**
	 * Executes an invoke of a method.
	 *
	 * @param __type The type of invocation performed.
	 * @param __pid The pool index with the method reference.
	 * @return Jump targets.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/05
	 */
	private int[] __executeInvoke(JITInvokeType __type, int __pid)
		throws NullPointerException
	{
		// Check
		if (__type == null)
			throw new NullPointerException("NARG");
		
		// Get pool reference
		JITMethodReference ref = this.pool.get(__pid).<JITMethodReference>
			get(true, JITMethodReference.class);
		
		// Debug
		System.err.printf("DEBUG -- Invoke %s %d %s%n", __type, __pid, ref);
		
		// Is this an instance invocation?
		boolean isinstance = __type.isInstance();
		
		// Get the stack layout, which is needed for verifcation
		__SMTState__ smwork = this._smwork;
		__SMTStack__ stack = smwork._stack;
		
		// Count the number of stack elements to count and pass to the method
		MethodSymbol sym = ref.methodType();
		int na = sym.argumentCount();
		int xc = (isinstance ? 1:  0);
		for (int i = 0; i < na; i++)
			if (__SMTType__.bySymbol(sym.get(na)).isWide())
				xc += 2;
			else
				xc++;
		
		// {@squirreljme.error ED0y Stack underflow during invocation of
		// method.}
		int top = stack.top(), at = top - 1, end = top - xc;
		if (end < 0)
			throw new JITException("ED0y");
		
		// Stack positions and types
		JITVariableType[] st = new JITVariableType[xc];
		int[] sp = new int[xc];
		
		// Fill types and check that they are valid
		int write = xc - 1;
		for (int i = 0; i < na; i++)
		{
			throw new Error("TODO");
		}
		
		// Check if instance is correct
		if (isinstance)
		{
			// Map it
			JITVariableType map = stack.get(at).map();
			
			// {@squirreljme.error ED0z Expected an object to be the instance
			// variable. (The actual type)}
			if (map != JITVariableType.OBJECT)
				throw new JITException(String.format("ED0z %s", map));
			
			// Store
			st[write] = map;
			sp[write] = smwork.cacheStack(at);
			
			// Move
			at--;
			write++;
		}
		
		// Debug
		System.err.printf("DEBUG -- Invoke: %s vars=", ref);
		for (int i = 0; i < xc; i++)
			System.err.printf("%d ", sp[i]);
		System.err.println();
		
		// Send in the call
		this.writer.invoke(__type, __pid, ref, st, sp);
		
		// Pop values from the stack
		while (stack.top() > end)
			stack.pop();
		
		// Implicit next
		return IMPLICIT_NEXT;
	}
	
	/**
	 * Load from local variable and push to the top of the stack.
	 *
	 * @param __t The type of value to push.
	 * @param __from The variable to load from.
	 * @return Jump targets.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/27
	 */
	private int[] __executeLoad(__SMTType__ __t, int __from)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Get
		__SMTState__ smwork = this._smwork;
		__SMTLocals__ locals = smwork._locals;
		
		// {@squirreljme.error ED0w Attempt to push a local variable to the
		// stack of a different type. (The local variable index; The type that
		// the variable was; The expected type)}
		__SMTType__ was = locals.get(__from);
		if (was != __t)
			throw new JITException(String.format("ED0w %d %s %s", __from, was,
				__t));
		
		// Cache it on the stack
		smwork._stack.push(was, __from);
		
		// Implicit next
		return IMPLICIT_NEXT;
	}
	
	/**
	 * Flushes the stack and makes all stack variables get values copied from
	 * locals by the values they cache.
	 *
	 * @since 2016/09/04
	 */
	private void __flushStack()
	{
		throw new Error("TODO");
	}
}

