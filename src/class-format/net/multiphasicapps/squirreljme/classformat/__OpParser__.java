// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.classformat;

import java.io.IOException;
import java.util.Map;
import net.multiphasicapps.io.data.ExtendedDataInputStream;
import net.multiphasicapps.squirreljme.java.symbols.FieldSymbol;
import net.multiphasicapps.squirreljme.java.symbols.MethodSymbol;

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
	protected final ClassClassFlags classflags;
	
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
		Map<Integer, __SMTState__> __smt, ClassClassFlags __cf,
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
	 * @throws ClassFormatException On decode errors.
	 * @since 2016/08/24
	 */
	void __decodeAll()
		throws IOException, ClassFormatException
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
			if (code == ClassByteCodeIndex.WIDE)
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
		throws IOException, ClassFormatException
	{
		// Get
		ExtendedDataInputStream input = this.input;
		
		// Depends
		switch (__code)
		{
				// Waste electrons
			case ClassByteCodeIndex.NOP:
				throw new Error("TODO");
			
				// Push null constant
			case ClassByteCodeIndex.ACONST_NULL:
				throw new Error("TODO");
			
				// Push int constant
			case ClassByteCodeIndex.ICONST_M1:
			case ClassByteCodeIndex.ICONST_0:
			case ClassByteCodeIndex.ICONST_1:
			case ClassByteCodeIndex.ICONST_2:
			case ClassByteCodeIndex.ICONST_3:
			case ClassByteCodeIndex.ICONST_4:
			case ClassByteCodeIndex.ICONST_5:
				throw new Error("TODO");
			
				// Push long constant
			case ClassByteCodeIndex.LCONST_0:
			case ClassByteCodeIndex.LCONST_1:
				throw new Error("TODO");
			
				// Push float constant
			case ClassByteCodeIndex.FCONST_0:
			case ClassByteCodeIndex.FCONST_1:
			case ClassByteCodeIndex.FCONST_2:
				throw new Error("TODO");
			
				// Push double constant
			case ClassByteCodeIndex.DCONST_0:
			case ClassByteCodeIndex.DCONST_1:
				throw new Error("TODO");
			
			case ClassByteCodeIndex.BIPUSH:
			case ClassByteCodeIndex.SIPUSH:
			case ClassByteCodeIndex.LDC:
			case ClassByteCodeIndex.LDC_W:
			case ClassByteCodeIndex.LDC2_W:
				throw new Error("TODO");
				
				// Push local int to the stack
			case ClassByteCodeIndex.ILOAD:
				return __executeLoad(__SMTType__.INTEGER,
					input.readUnsignedByte());
				
				// Push local int to the stack (wide)
			case ClassByteCodeIndex.WIDE_ILOAD:
				return __executeLoad(__SMTType__.INTEGER,
					input.readUnsignedShort());
				
				// Push local long to the stack
			case ClassByteCodeIndex.LLOAD:
				return __executeLoad(__SMTType__.LONG,
					input.readUnsignedByte());
				
				// Push local long to the stack (wide)
			case ClassByteCodeIndex.WIDE_LLOAD:
				return __executeLoad(__SMTType__.LONG,
					input.readUnsignedShort());
				
				// Push local float to the stack
			case ClassByteCodeIndex.FLOAD:
				return __executeLoad(__SMTType__.FLOAT,
					input.readUnsignedByte());
				
				// Push local float to the stack (wide)
			case ClassByteCodeIndex.WIDE_FLOAD:
				return __executeLoad(__SMTType__.FLOAT,
					input.readUnsignedShort());
			
				// Push local double to the stack
			case ClassByteCodeIndex.DLOAD:
				return __executeLoad(__SMTType__.DOUBLE,
					input.readUnsignedByte());
				
				// Push local double to the stack (wide)
			case ClassByteCodeIndex.WIDE_DLOAD:
				return __executeLoad(__SMTType__.DOUBLE,
					input.readUnsignedShort());
				
				// Push local reference to the stack
			case ClassByteCodeIndex.ALOAD:
				return __executeLoad(__SMTType__.OBJECT,
					input.readUnsignedByte());
				
				// Push local reference to the stack (wide)
			case ClassByteCodeIndex.WIDE_ALOAD:
				return __executeLoad(__SMTType__.OBJECT,
					input.readUnsignedShort());
			
				// Load int from local
			case ClassByteCodeIndex.ILOAD_0:
			case ClassByteCodeIndex.ILOAD_1:
			case ClassByteCodeIndex.ILOAD_2:
			case ClassByteCodeIndex.ILOAD_3:
				return __executeLoad(__SMTType__.INTEGER,
					__code - ClassByteCodeIndex.ILOAD_0);
			
				// Load long from local
			case ClassByteCodeIndex.LLOAD_0:
			case ClassByteCodeIndex.LLOAD_1:
			case ClassByteCodeIndex.LLOAD_2:
			case ClassByteCodeIndex.LLOAD_3:
				return __executeLoad(__SMTType__.LONG,
					__code - ClassByteCodeIndex.LLOAD_0);
			
				// Load float from local
			case ClassByteCodeIndex.FLOAD_0:
			case ClassByteCodeIndex.FLOAD_1:
			case ClassByteCodeIndex.FLOAD_2:
			case ClassByteCodeIndex.FLOAD_3:
				return __executeLoad(__SMTType__.FLOAT,
					__code - ClassByteCodeIndex.FLOAD_0);
			
				// Load double from local
			case ClassByteCodeIndex.DLOAD_0:
			case ClassByteCodeIndex.DLOAD_1:
			case ClassByteCodeIndex.DLOAD_2:
			case ClassByteCodeIndex.DLOAD_3:
				return __executeLoad(__SMTType__.DOUBLE,
					__code - ClassByteCodeIndex.DLOAD_0);
			
				// Load reference from local
			case ClassByteCodeIndex.ALOAD_0:
			case ClassByteCodeIndex.ALOAD_1:
			case ClassByteCodeIndex.ALOAD_2:
			case ClassByteCodeIndex.ALOAD_3:
				return __executeLoad(__SMTType__.OBJECT,
					__code - ClassByteCodeIndex.ALOAD_0);
			
			case ClassByteCodeIndex.IALOAD:
			case ClassByteCodeIndex.LALOAD:
			case ClassByteCodeIndex.FALOAD:
			case ClassByteCodeIndex.DALOAD:
			case ClassByteCodeIndex.AALOAD:
			case ClassByteCodeIndex.BALOAD:
			case ClassByteCodeIndex.CALOAD:
			case ClassByteCodeIndex.SALOAD:
			case ClassByteCodeIndex.ISTORE:
			case ClassByteCodeIndex.LSTORE:
			case ClassByteCodeIndex.FSTORE:
			case ClassByteCodeIndex.DSTORE:
			case ClassByteCodeIndex.ASTORE:
				throw new Error("TODO");
			
				// Store int into local
			case ClassByteCodeIndex.ISTORE_0:
			case ClassByteCodeIndex.ISTORE_1:
			case ClassByteCodeIndex.ISTORE_2:
			case ClassByteCodeIndex.ISTORE_3:
				throw new Error("TODO");
			
				// Store long into local
			case ClassByteCodeIndex.LSTORE_0:
			case ClassByteCodeIndex.LSTORE_1:
			case ClassByteCodeIndex.LSTORE_2:
			case ClassByteCodeIndex.LSTORE_3:
				throw new Error("TODO");
			
				// Store float into local
			case ClassByteCodeIndex.FSTORE_0:
			case ClassByteCodeIndex.FSTORE_1:
			case ClassByteCodeIndex.FSTORE_2:
			case ClassByteCodeIndex.FSTORE_3:
				throw new Error("TODO");
			
				// Store double into local
			case ClassByteCodeIndex.DSTORE_0:
			case ClassByteCodeIndex.DSTORE_1:
			case ClassByteCodeIndex.DSTORE_2:
			case ClassByteCodeIndex.DSTORE_3:
				throw new Error("TODO");
			
				// Store object into local
			case ClassByteCodeIndex.ASTORE_0:
			case ClassByteCodeIndex.ASTORE_1:
			case ClassByteCodeIndex.ASTORE_2:
			case ClassByteCodeIndex.ASTORE_3:
				throw new Error("TODO");
				
				// Invoke interface method
			case ClassByteCodeIndex.INVOKEINTERFACE:
				return __executeInvoke(ClassMethodInvokeType.INTERFACE,
					input.readUnsignedShort() | (input.readByte() & 0) |
					(input.readByte() & 0));
				
				// Invoke constructor or private method
			case ClassByteCodeIndex.INVOKESPECIAL:
				return __executeInvoke(ClassMethodInvokeType.SPECIAL,
					input.readUnsignedShort());
				
				// Invoke static method
			case ClassByteCodeIndex.INVOKESTATIC:
				return __executeInvoke(ClassMethodInvokeType.STATIC,
					input.readUnsignedShort());
				
				// Invoke virtual method
			case ClassByteCodeIndex.INVOKEVIRTUAL:
				return __executeInvoke(ClassMethodInvokeType.VIRTUAL,
					input.readUnsignedShort());
			
				// {@squirreljme.error AY38 Defined operation cannot be
				// used in Java ME programs. (The operation)}
			case ClassByteCodeIndex.JSR:
			case ClassByteCodeIndex.JSR_W:
			case ClassByteCodeIndex.RET:
			case ClassByteCodeIndex.INVOKEDYNAMIC:
			case ClassByteCodeIndex.BREAKPOINT:
			case ClassByteCodeIndex.IMPDEP1:
			case ClassByteCodeIndex.IMPDEP2:
				throw new ClassFormatException(String.format("AY38 %d", __code));
			
			case ClassByteCodeIndex.IASTORE:
			case ClassByteCodeIndex.LASTORE:
			case ClassByteCodeIndex.FASTORE:
			case ClassByteCodeIndex.DASTORE:
			case ClassByteCodeIndex.AASTORE:
			case ClassByteCodeIndex.BASTORE:
			case ClassByteCodeIndex.CASTORE:
			case ClassByteCodeIndex.SASTORE:
			case ClassByteCodeIndex.POP:
			case ClassByteCodeIndex.POP2:
			case ClassByteCodeIndex.DUP:
			case ClassByteCodeIndex.DUP_X1:
			case ClassByteCodeIndex.DUP_X2:
			case ClassByteCodeIndex.DUP2:
			case ClassByteCodeIndex.DUP2_X1:
			case ClassByteCodeIndex.DUP2_X2:
			case ClassByteCodeIndex.SWAP:
			case ClassByteCodeIndex.IADD:
			case ClassByteCodeIndex.LADD:
			case ClassByteCodeIndex.FADD:
			case ClassByteCodeIndex.DADD:
			case ClassByteCodeIndex.ISUB:
			case ClassByteCodeIndex.LSUB:
			case ClassByteCodeIndex.FSUB:
			case ClassByteCodeIndex.DSUB:
			case ClassByteCodeIndex.IMUL:
			case ClassByteCodeIndex.LMUL:
			case ClassByteCodeIndex.FMUL:
			case ClassByteCodeIndex.DMUL:
			case ClassByteCodeIndex.IDIV:
			case ClassByteCodeIndex.LDIV:
			case ClassByteCodeIndex.FDIV:
			case ClassByteCodeIndex.DDIV:
			case ClassByteCodeIndex.IREM:
			case ClassByteCodeIndex.LREM:
			case ClassByteCodeIndex.FREM:
			case ClassByteCodeIndex.DREM:
			case ClassByteCodeIndex.INEG:
			case ClassByteCodeIndex.LNEG:
			case ClassByteCodeIndex.FNEG:
			case ClassByteCodeIndex.DNEG:
			case ClassByteCodeIndex.ISHL:
			case ClassByteCodeIndex.LSHL:
			case ClassByteCodeIndex.ISHR:
			case ClassByteCodeIndex.LSHR:
			case ClassByteCodeIndex.IUSHR:
			case ClassByteCodeIndex.LUSHR:
			case ClassByteCodeIndex.IAND:
			case ClassByteCodeIndex.LAND:
			case ClassByteCodeIndex.IOR:
			case ClassByteCodeIndex.LOR:
			case ClassByteCodeIndex.IXOR:
			case ClassByteCodeIndex.LXOR:
			case ClassByteCodeIndex.IINC:
			case ClassByteCodeIndex.I2L:
			case ClassByteCodeIndex.I2F:
			case ClassByteCodeIndex.I2D:
			case ClassByteCodeIndex.L2I:
			case ClassByteCodeIndex.L2F:
			case ClassByteCodeIndex.L2D:
			case ClassByteCodeIndex.F2I:
			case ClassByteCodeIndex.F2L:
			case ClassByteCodeIndex.F2D:
			case ClassByteCodeIndex.D2I:
			case ClassByteCodeIndex.D2L:
			case ClassByteCodeIndex.D2F:
			case ClassByteCodeIndex.I2B:
			case ClassByteCodeIndex.I2C:
			case ClassByteCodeIndex.I2S:
			case ClassByteCodeIndex.LCMP:
			case ClassByteCodeIndex.FCMPL:
			case ClassByteCodeIndex.FCMPG:
			case ClassByteCodeIndex.DCMPL:
			case ClassByteCodeIndex.DCMPG:
			case ClassByteCodeIndex.IFEQ:
			case ClassByteCodeIndex.IFNE:
			case ClassByteCodeIndex.IFLT:
			case ClassByteCodeIndex.IFGE:
			case ClassByteCodeIndex.IFGT:
			case ClassByteCodeIndex.IFLE:
			case ClassByteCodeIndex.IF_ICMPEQ:
			case ClassByteCodeIndex.IF_ICMPNE:
			case ClassByteCodeIndex.IF_ICMPLT:
			case ClassByteCodeIndex.IF_ICMPGE:
			case ClassByteCodeIndex.IF_ICMPGT:
			case ClassByteCodeIndex.IF_ICMPLE:
			case ClassByteCodeIndex.IF_ACMPEQ:
			case ClassByteCodeIndex.IF_ACMPNE:
			case ClassByteCodeIndex.GOTO:
			case ClassByteCodeIndex.TABLESWITCH:
			case ClassByteCodeIndex.LOOKUPSWITCH:
			case ClassByteCodeIndex.IRETURN:
			case ClassByteCodeIndex.LRETURN:
			case ClassByteCodeIndex.FRETURN:
			case ClassByteCodeIndex.DRETURN:
			case ClassByteCodeIndex.ARETURN:
			case ClassByteCodeIndex.RETURN:
			case ClassByteCodeIndex.GETSTATIC:
			case ClassByteCodeIndex.PUTSTATIC:
			case ClassByteCodeIndex.GETFIELD:
			case ClassByteCodeIndex.PUTFIELD:
			case ClassByteCodeIndex.NEW:
			case ClassByteCodeIndex.NEWARRAY:
			case ClassByteCodeIndex.ANEWARRAY:
			case ClassByteCodeIndex.ARRAYLENGTH:
			case ClassByteCodeIndex.ATHROW:
			case ClassByteCodeIndex.CHECKCAST:
			case ClassByteCodeIndex.INSTANCEOF:
			case ClassByteCodeIndex.MONITORENTER:
			case ClassByteCodeIndex.MONITOREXIT:
			case ClassByteCodeIndex.MULTIANEWARRAY:
			case ClassByteCodeIndex.IFNULL:
			case ClassByteCodeIndex.IFNONNULL:
			case ClassByteCodeIndex.GOTO_W:
			
				// {@squirreljme.error AY37 Unknown byte-code operation.
				// (The operation)}
			default:
				throw new ClassFormatException(String.format("AY37 %d", __code));
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
	private int[] __executeInvoke(ClassMethodInvokeType __type, int __pid)
		throws NullPointerException
	{
		// Check
		if (__type == null)
			throw new NullPointerException("NARG");
		
		// Get pool reference
		ClassMethodReference ref = this.pool.get(__pid).<ClassMethodReference>
			get(true, ClassMethodReference.class);
		
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
		
		// {@squirreljme.error AY3y Stack underflow during invocation of
		// method.}
		int top = stack.top(), at = top - 1, end = top - xc;
		if (end < 0)
			throw new ClassFormatException("AY3y");
		
		// Stack positions and types
		JITVariableType[] st = new JITVariableType[xc];
		
		// Fill types and check that they are valid
		int write = xc - 1, popcount = 0;
		for (int i = 0; i < na; i++)
		{
			throw new Error("TODO");
		}
		
		// Check if instance is correct
		if (isinstance)
		{
			// Map it
			JITVariableType map = stack.get(at--).map();
			
			// {@squirreljme.error AY3z Expected an object to be the instance
			// variable. (The actual type)}
			if (map != JITVariableType.OBJECT)
				throw new ClassFormatException(String.format("AY3z %s", map));
			
			// Store
			st[write++] = map;
			
			// Increase the pop count
			popcount++;
		}
		
		// Handle return value if any
		FieldSymbol rv = sym.returnValue();
		JITVariableType rvt;
		int rvi;
		__SMTType__ rvs;
		if (rv != null)
		{
			rvs = __SMTType__.bySymbol(rv);
			rvt = rvs.map();
			rvi = end;
			
			// {@squirreljme.error AY40 Stack overflow writing return value.}
			if (rvi >= stack.size())
				throw new ClassFormatException("AY40");
		}
		
		// No return value
		else
		{
			rvs = null;
			rvt = null;
			rvi = -1;
		}
		
		// Pop all stack values
		int[] sp = stack.__pop(this, popcount);
		
		// Debug
		System.err.printf("DEBUG -- Invoke: %s rv=%s/%d vars=", ref, rvt, rvi);
		for (int i = 0; i < xc; i++)
			System.err.printf("%d ", sp[i]);
		System.err.println();
		
		// Send in the call
		this.writer.invoke(__type, __pid, ref, st, sp, rvt, rvi);
		
		// Push return value if there is one
		if (rv != null)
			stack.push(rvs, UNIQUE_STACK_VALUE);
		
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
		
		// {@squirreljme.error AY3w Attempt to push a local variable to the
		// stack of a different type. (The local variable index; The type that
		// the variable was; The expected type)}
		__SMTType__ was = locals.get(__from);
		if (was != __t)
			throw new ClassFormatException(String.format("AY3w %d %s %s", __from, was,
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

