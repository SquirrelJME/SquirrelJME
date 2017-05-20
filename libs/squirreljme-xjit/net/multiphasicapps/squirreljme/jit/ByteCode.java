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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import net.multiphasicapps.squirreljme.linkage.MethodReference;

/**
 * This represents the byte code for a given method. It contains the actual
 * instructions, iterators over instructions, along with jump targets which
 * are available for usage.
 *
 * @since 2017/05/14
 */
public class ByteCode
	implements Iterable<ByteCode.Instruction>
{
	/** The number of stack variables. */
	protected final int maxstack;
	
	/** The number of local variables. */
	protected final int maxlocals;
	
	/** The exception handler table. */
	protected final ExceptionHandlerTable exceptions;
	
	/** The length of the byte code. */
	protected final int codelen;
	
	/** The byte code for the method. */
	private final byte[] _code;
	
	/** Instruction lengths at each position. */
	private final int[] _lengths;
	
	/** Jump targets. */
	private final JumpTarget[] _jumptargets;
	
	/** The constant pool. */
	private final __Pool__ _pool;
	
	/** The cache of instructions in the byte code. */
	private final Reference<Instruction>[] _icache;
	
	/** The string representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Represents the byte code.
	 *
	 * @param __ms The maximum number of stack entries.
	 * @param __ml The maximum number of local entries.
	 * @param __code The program's byte code, this is not copied.
	 * @param __eht The exception handler table.
	 * @param __pool The constant pool.
	 * @throws JITException If the byte code is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/05/14
	 */
	ByteCode(int __ms, int __ml, byte[] __code, ExceptionHandlerTable __eht,
		__Pool__ __pool)
		throws JITException, NullPointerException
	{
		// Check
		if (__code == null || __eht == null || __pool == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._code = __code;
		this.codelen = __code.length;
		this._pool = __pool;
		int codelen = __code.length;
		this.maxstack = __ms;
		this.maxlocals = __ml;
		this.exceptions = __eht;
		
		// Cache instructions
		this._icache = __newCache(codelen);
		
		// Set all lengths initially to invalid positions, this used as a quick
		// marker to determine which positions have valid instructions
		int[] lengths = new int[codelen];
		for (int i = 0; i < codelen; i++)
			lengths[i] = -1;
		
		// Determine instruction lengths for each position
		for (int i = 0; i < codelen;)
		{
			// Store length
			int oplen;
			lengths[i] = (oplen = __opLength(__code, i));
			
			// {@squirreljme.error AQ15 The operation exceeds the bounds of
			// the method byte code. (The operation pointer; The operation
			// length; The code length)}
			if ((i += oplen) > codelen)
				throw new JITException(String.format("AQ15 %d %d %d", i, oplen,
					codelen));
		}
		this._lengths = lengths;
		
		// Debug
		System.err.print("DEBUG -- Lengths: [");
		for (int i = 0; i < codelen; i++)
		{
			if (i != 0)
				System.err.print(", ");
			System.err.print(lengths[i]);
		}
		System.err.println("]");
		
		// Initialize basic jump targets with no actual targets
		JumpTarget[] jumptargets = new JumpTarget[codelen];
		jumptargets[0] = new JumpTarget(0);
		int jumpcount = 1;
		
		// Set jump targets for every exception to form basic block barriers
		for (ExceptionHandler eh : __eht)
			jumpcount = __addSortedArray(eh.handlerAddress(), jumptargets,
				jumpcount);
		
		// Handle for each instruction
		for (int i = 0; i < codelen; i++)
			if (isValidAddress(i))
				for (JumpTarget jt : get(i).jumpTargets())
					jumpcount = __addSortedArray(jt, jumptargets, jumpcount);
		
		// Store jump targets
		if (jumpcount == codelen)
			this._jumptargets = jumptargets;
		else
			this._jumptargets = Arrays.<JumpTarget>copyOf(jumptargets,
				jumpcount);
	}
	
	/**
	 * Returns the address of the instruction following the specified one.
	 *
	 * @param __a The following address.
	 * @return The instruction address following the instruction at the
	 * specified address.
	 * @throws JITException If the specified address is not valid.
	 * @since 2017/05/20
	 */
	public int addressFollowing(int __a)
		throws JITException
	{
		// {@squirreljme.error AQ1a The instruction at the specified address is
		// not valid. (The address)}
		if (!isValidAddress(__a))
			throw new JITException(String.format("AQ1a %d", __a));
		
		return __a + this._lengths[__a];
	}
	
	/**
	 * Returns the instruction at the specified address.
	 *
	 * @param __a The address to get the instruction for.
	 * @return The represented instruction for the given address.
	 * @throws JITException If the address is not valid.
	 * @since 2017/05/18
	 */
	public Instruction get(int __a)
		throws JITException
	{
		// {@squirreljme.error AQ16 The instruction at the specified address is
		// not valid. (The address)}
		if (!isValidAddress(__a))
			throw new JITException(String.format("AQ16 %d", __a));
		
		Reference<Instruction>[] icache = this._icache;
		Reference<Instruction> ref = icache[__a];
		Instruction rv;
		
		if (ref == null || null == (rv = ref.get()))
			icache[__a] = new WeakReference<>((rv = new Instruction(__a)));
		
		return rv;
	}
	
	/**
	 * Checks whether the given address is a valid instruction address.
	 *
	 * @param __a The address to check.
	 * @return {@code true} if the address is valid.
	 * @since 2017/05/18
	 */
	public boolean isValidAddress(int __a)
	{
		// Out of range
		if (__a < 0 || __a >= this.codelen)
			return false;
		
		// Has to have a positive non-zero length
		return (this._lengths[__a] > 0);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/20
	 */
	@Override
	public Iterator<ByteCode.Instruction> iterator()
	{
		return new __Iterator__();
	}
	
	/**
	 * Returns the jump targets within the method.
	 *
	 * @return The jump targets in the method.
	 * @since 2017/05/20
	 */
	public JumpTarget[] jumpTargets()
	{
		return this._jumptargets.clone();
	}
	
	/**
	 * Returns the length of the byte code.
	 *
	 * @return The byte code length.
	 * @since 2017/05/20
	 */
	public int length()
	{
		return this.codelen;
	}
	
	/**
	 * Returns the maximum number of locals.
	 *
	 * @return The maximum number of locals.
	 * @since 2017/05/20
	 */
	public int maxLocals()
	{
		return this.maxlocals;
	}
	
	/**
	 * Returns the maximum size of the stack.
	 *
	 * @return The maximum stack size.
	 * @since 2017/05/20
	 */
	public int maxStack()
	{
		return this.maxstack;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/20
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
		{
			StringBuilder sb = new StringBuilder("[");
			
			// Fill in instructions
			boolean comma = false;
			for (Instruction i : this)
			{
				if (comma)
					sb.append(", ");
				else
					comma = true;
				
				sb.append(i);
			}
			
			sb.append(']');
			this._string = new WeakReference<>((rv = sb.toString()));
		}
		
		return rv;
	}
	
	/**
	 * Adds the specified value to the sorted array for jump targets.
	 *
	 * @param __v The value to add.
	 * @param __a The array to add to.
	 * @param __l The number of entries in the array.
	 * @return The new entry count of the array.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/05/18
	 */
	private static int __addSortedArray(JumpTarget __v, JumpTarget[] __a,
		int __l)
		throws NullPointerException
	{
		// Check
		if (__a == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Initializes a new cache array.
	 *
	 * @param __l The length of the array.
	 * @return The cache array.
	 * @since 2017/05/18
	 */
	@SuppressWarnings({"unchecked"})
	private static Reference<Instruction>[] __newCache(int __l)
	{
		return (Reference<Instruction>[])((Object)new Reference[__l]);
	}
	
	/**
	 * Returns the length of the operation at the given address.
	 *
	 * @param __code The method byte code.
	 * @param __a The address of the instruction to get the length of.
	 * @throws JITException If the instruction is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/05/17
	 */
	private static int __opLength(byte[] __code, int __a)
		throws JITException, NullPointerException
	{
		// Check
		if (__code == null)
			throw new NullPointerException("NARG");
		
		// Base instruction length is always 1
		int rv = 1;
		
		// Read operation
		int op = (__code[__a] & 0xFF);
		if (op == __OperandIndex__.WIDE)
		{
			// {@squirreljme.error AQ17 The wide instruction cannot be the
			// last instruction in a method. (The address)}
			if (__a + 1 >= __code.length)
				throw new JITException(String.format("AQ17 %d", __a));
			
			op = (op << 8) | (__code[__a + 1] & 0xFF);
			rv = 2;
		}
		
		// Depends on the operation
		switch (op)
		{
				// {@squirreljme.error AQ12 Unsupported instruction specified
				// in the method byte code. (The operation; The address)}
			case __OperandIndex__.BREAKPOINT:
			case __OperandIndex__.IMPDEP1:
			case __OperandIndex__.IMPDEP2:
			case __OperandIndex__.JSR:
			case __OperandIndex__.JSR_W:
			case __OperandIndex__.RET:
			case __OperandIndex__.WIDE:
				throw new JITException(String.format("AQ12 %d %d", op, __a));
			
				// {@squirreljme.error AQ13 Invokedynamic is not supported in
				// this virtual machine. (The address)}
			case __OperandIndex__.INVOKEDYNAMIC:
				throw new JITException(String.format("AQ13 %d", __a));
				
				// Operands with no arguments
			case __OperandIndex__.AALOAD:
			case __OperandIndex__.AASTORE:
			case __OperandIndex__.ACONST_NULL:
			case __OperandIndex__.ALOAD_0:
			case __OperandIndex__.ALOAD_1:
			case __OperandIndex__.ALOAD_2:
			case __OperandIndex__.ALOAD_3:
			case __OperandIndex__.ARETURN:
			case __OperandIndex__.ARRAYLENGTH:
			case __OperandIndex__.ASTORE_0:
			case __OperandIndex__.ASTORE_1:
			case __OperandIndex__.ASTORE_2:
			case __OperandIndex__.ASTORE_3:
			case __OperandIndex__.ATHROW:
			case __OperandIndex__.BALOAD:
			case __OperandIndex__.BASTORE:
			case __OperandIndex__.CALOAD:
			case __OperandIndex__.CASTORE:
			case __OperandIndex__.D2F:
			case __OperandIndex__.D2I:
			case __OperandIndex__.D2L:
			case __OperandIndex__.DADD:
			case __OperandIndex__.DALOAD:
			case __OperandIndex__.DASTORE:
			case __OperandIndex__.DCMPG:
			case __OperandIndex__.DCMPL:
			case __OperandIndex__.DCONST_0:
			case __OperandIndex__.DCONST_1:
			case __OperandIndex__.DDIV:
			case __OperandIndex__.DLOAD_0:
			case __OperandIndex__.DLOAD_1:
			case __OperandIndex__.DLOAD_2:
			case __OperandIndex__.DLOAD_3:
			case __OperandIndex__.DMUL:
			case __OperandIndex__.DNEG:
			case __OperandIndex__.DREM:
			case __OperandIndex__.DRETURN:
			case __OperandIndex__.DSTORE_0:
			case __OperandIndex__.DSTORE_1:
			case __OperandIndex__.DSTORE_2:
			case __OperandIndex__.DSTORE_3:
			case __OperandIndex__.DSUB:
			case __OperandIndex__.DUP:
			case __OperandIndex__.DUP2:
			case __OperandIndex__.DUP2_X1:
			case __OperandIndex__.DUP2_X2:
			case __OperandIndex__.DUP_X1:
			case __OperandIndex__.DUP_X2:
			case __OperandIndex__.F2D:
			case __OperandIndex__.F2I:
			case __OperandIndex__.F2L:
			case __OperandIndex__.FADD:
			case __OperandIndex__.FALOAD:
			case __OperandIndex__.FASTORE:
			case __OperandIndex__.FCMPG:
			case __OperandIndex__.FCMPL:
			case __OperandIndex__.FCONST_0:
			case __OperandIndex__.FCONST_1:
			case __OperandIndex__.FCONST_2:
			case __OperandIndex__.FDIV:
			case __OperandIndex__.FLOAD_0:
			case __OperandIndex__.FLOAD_1:
			case __OperandIndex__.FLOAD_2:
			case __OperandIndex__.FLOAD_3:
			case __OperandIndex__.FMUL:
			case __OperandIndex__.FNEG:
			case __OperandIndex__.FREM:
			case __OperandIndex__.FRETURN:
			case __OperandIndex__.FSTORE_0:
			case __OperandIndex__.FSTORE_1:
			case __OperandIndex__.FSTORE_2:
			case __OperandIndex__.FSTORE_3:
			case __OperandIndex__.FSUB:
			case __OperandIndex__.I2B:
			case __OperandIndex__.I2C:
			case __OperandIndex__.I2D:
			case __OperandIndex__.I2F:
			case __OperandIndex__.I2L:
			case __OperandIndex__.I2S:
			case __OperandIndex__.IADD:
			case __OperandIndex__.IALOAD:
			case __OperandIndex__.IAND:
			case __OperandIndex__.IASTORE:
			case __OperandIndex__.ICONST_0:
			case __OperandIndex__.ICONST_1:
			case __OperandIndex__.ICONST_2:
			case __OperandIndex__.ICONST_3:
			case __OperandIndex__.ICONST_4:
			case __OperandIndex__.ICONST_5:
			case __OperandIndex__.ICONST_M1:
			case __OperandIndex__.IDIV:
			case __OperandIndex__.ILOAD_0:
			case __OperandIndex__.ILOAD_1:
			case __OperandIndex__.ILOAD_2:
			case __OperandIndex__.ILOAD_3:
			case __OperandIndex__.IMUL:
			case __OperandIndex__.INEG:
			case __OperandIndex__.IOR:
			case __OperandIndex__.IREM:
			case __OperandIndex__.IRETURN:
			case __OperandIndex__.ISHL:
			case __OperandIndex__.ISHR:
			case __OperandIndex__.ISTORE_0:
			case __OperandIndex__.ISTORE_1:
			case __OperandIndex__.ISTORE_2:
			case __OperandIndex__.ISTORE_3:
			case __OperandIndex__.ISUB:
			case __OperandIndex__.IUSHR:
			case __OperandIndex__.IXOR:
			case __OperandIndex__.L2D:
			case __OperandIndex__.L2F:
			case __OperandIndex__.L2I:
			case __OperandIndex__.LADD:
			case __OperandIndex__.LALOAD:
			case __OperandIndex__.LAND:
			case __OperandIndex__.LASTORE:
			case __OperandIndex__.LCMP:
			case __OperandIndex__.LCONST_0:
			case __OperandIndex__.LCONST_1:
			case __OperandIndex__.LDIV:
			case __OperandIndex__.LLOAD_0:
			case __OperandIndex__.LLOAD_1:
			case __OperandIndex__.LLOAD_2:
			case __OperandIndex__.LLOAD_3:
			case __OperandIndex__.LMUL:
			case __OperandIndex__.LNEG:
			case __OperandIndex__.LOR:
			case __OperandIndex__.LREM:
			case __OperandIndex__.LRETURN:
			case __OperandIndex__.LSHL:
			case __OperandIndex__.LSHR:
			case __OperandIndex__.LSTORE_0:
			case __OperandIndex__.LSTORE_1:
			case __OperandIndex__.LSTORE_2:
			case __OperandIndex__.LSTORE_3:
			case __OperandIndex__.LSUB:
			case __OperandIndex__.LUSHR:
			case __OperandIndex__.LXOR:
			case __OperandIndex__.MONITORENTER:
			case __OperandIndex__.MONITOREXIT:
			case __OperandIndex__.NOP:
			case __OperandIndex__.POP:
			case __OperandIndex__.POP2:
			case __OperandIndex__.RETURN:
			case __OperandIndex__.SALOAD:
			case __OperandIndex__.SASTORE:
			case __OperandIndex__.SWAP:
				break;
				
				// An additional byte
			case __OperandIndex__.ALOAD:
			case __OperandIndex__.ASTORE:
			case __OperandIndex__.BIPUSH:
			case __OperandIndex__.DLOAD:
			case __OperandIndex__.DSTORE:
			case __OperandIndex__.FLOAD:
			case __OperandIndex__.FSTORE:
			case __OperandIndex__.IINC:
			case __OperandIndex__.ILOAD:
			case __OperandIndex__.ISTORE:
			case __OperandIndex__.LDC:
			case __OperandIndex__.LLOAD:
			case __OperandIndex__.LSTORE:
			case __OperandIndex__.NEWARRAY:
				rv++;
				break;
				
				// Operations with two bytes following
			case __OperandIndex__.ANEWARRAY:
			case __OperandIndex__.CHECKCAST:
			case __OperandIndex__.GETFIELD:
			case __OperandIndex__.GETSTATIC:
			case __OperandIndex__.GOTO:
			case __OperandIndex__.IF_ACMPEQ:
			case __OperandIndex__.IF_ACMPNE:
			case __OperandIndex__.IFEQ:
			case __OperandIndex__.IFGE:
			case __OperandIndex__.IFGT:
			case __OperandIndex__.IF_ICMPEQ:
			case __OperandIndex__.IF_ICMPGE:
			case __OperandIndex__.IF_ICMPGT:
			case __OperandIndex__.IF_ICMPLE:
			case __OperandIndex__.IF_ICMPLT:
			case __OperandIndex__.IF_ICMPNE:
			case __OperandIndex__.IFLE:
			case __OperandIndex__.IFLT:
			case __OperandIndex__.IFNE:
			case __OperandIndex__.IFNONNULL:
			case __OperandIndex__.IFNULL:
			case __OperandIndex__.INSTANCEOF:
			case __OperandIndex__.INVOKESPECIAL:
			case __OperandIndex__.INVOKESTATIC:
			case __OperandIndex__.INVOKEVIRTUAL:
			case __OperandIndex__.LDC2_W:
			case __OperandIndex__.LDC_W:
			case __OperandIndex__.NEW:
			case __OperandIndex__.PUTFIELD:
			case __OperandIndex__.PUTSTATIC:
			case __OperandIndex__.SIPUSH:
			case __OperandIndex__.WIDE_ALOAD:
			case __OperandIndex__.WIDE_ASTORE:
			case __OperandIndex__.WIDE_DLOAD:
			case __OperandIndex__.WIDE_DSTORE:
			case __OperandIndex__.WIDE_FLOAD:
			case __OperandIndex__.WIDE_FSTORE:
			case __OperandIndex__.WIDE_IINC:
			case __OperandIndex__.WIDE_ILOAD:
			case __OperandIndex__.WIDE_ISTORE:
			case __OperandIndex__.WIDE_LLOAD:
			case __OperandIndex__.WIDE_LSTORE:
				rv += 2;
				break;
				
				// Three bytes
			case __OperandIndex__.INVOKEINTERFACE:
			case __OperandIndex__.MULTIANEWARRAY:
				rv += 3;
				break;
				
				// Four bytes
			case __OperandIndex__.GOTO_W:
				rv += 4;
				break;
			
				// Table switch
			case __OperandIndex__.TABLESWITCH:
				throw new todo.TODO();
				
				// Lookup switch
			case __OperandIndex__.LOOKUPSWITCH:
				throw new todo.TODO();
			
				// {@squirreljme.error AQ14 Cannot get the length of the
				// specified operation because it is not valid. (The operation;
				// The address)}
			default:
				throw new JITException(String.format("AQ14 %d %d", op, __a));
		}
		
		return rv;
	}
	
	/**
	 * This represents a single instruction within the byte code.
	 *
	 * @since 2017/05/18
	 */
	public final class Instruction
	{
		/** The instruction address. */
		protected final int address;
		
		/** The instruction operation. */
		protected final int op;
		
		// Does this method naturally flow?
		protected final boolean naturalflow;
		
		/** Instruction arguments. */
		private final Object[] _args;
		
		/** Jump targets for this instruction. */
		private final JumpTarget[] _jumptargets;
		
		/** String representation of the operation. */
		private volatile Reference<String> _string;
		
		/**
		 * Initializes the instruction information.
		 *
		 * @param __a The instruction address.
		 * @since 2017/05/18
		 */
		private Instruction(int __a)
		{
			// Read operation here
			byte[] code = ByteCode.this._code;
			int op = (code[__a] & 0xFF),
				argbase = __a + 1;
			if (op == __OperandIndex__.WIDE)
			{
				op = (op << 8) | (code[__a + 1] & 0xFF);
				argbase++;
			}
			this.op = op;
			this.address = __a;
			
			// Depends on the operation
			Object[] args;
			boolean naturalflow;
			__Pool__ pool = ByteCode.this._pool;
			switch (op)
			{
					// No arguments and does not flow naturally
				case __OperandIndex__.ATHROW:
				case __OperandIndex__.ARETURN:
				case __OperandIndex__.DRETURN:
				case __OperandIndex__.FRETURN:
				case __OperandIndex__.IRETURN:
				case __OperandIndex__.LRETURN:
				case __OperandIndex__.RETURN:
					args = new Object[0];
					naturalflow = false;
					break;
				
					// Operands with no arguments, natural flow
				case __OperandIndex__.AALOAD:
				case __OperandIndex__.AASTORE:
				case __OperandIndex__.ACONST_NULL:
				case __OperandIndex__.ALOAD_0:
				case __OperandIndex__.ALOAD_1:
				case __OperandIndex__.ALOAD_2:
				case __OperandIndex__.ALOAD_3:
				case __OperandIndex__.ARRAYLENGTH:
				case __OperandIndex__.ASTORE_0:
				case __OperandIndex__.ASTORE_1:
				case __OperandIndex__.ASTORE_2:
				case __OperandIndex__.ASTORE_3:
				case __OperandIndex__.BALOAD:
				case __OperandIndex__.BASTORE:
				case __OperandIndex__.CALOAD:
				case __OperandIndex__.CASTORE:
				case __OperandIndex__.D2F:
				case __OperandIndex__.D2I:
				case __OperandIndex__.D2L:
				case __OperandIndex__.DADD:
				case __OperandIndex__.DALOAD:
				case __OperandIndex__.DASTORE:
				case __OperandIndex__.DCMPG:
				case __OperandIndex__.DCMPL:
				case __OperandIndex__.DCONST_0:
				case __OperandIndex__.DCONST_1:
				case __OperandIndex__.DDIV:
				case __OperandIndex__.DLOAD_0:
				case __OperandIndex__.DLOAD_1:
				case __OperandIndex__.DLOAD_2:
				case __OperandIndex__.DLOAD_3:
				case __OperandIndex__.DMUL:
				case __OperandIndex__.DNEG:
				case __OperandIndex__.DREM:
				case __OperandIndex__.DSTORE_0:
				case __OperandIndex__.DSTORE_1:
				case __OperandIndex__.DSTORE_2:
				case __OperandIndex__.DSTORE_3:
				case __OperandIndex__.DSUB:
				case __OperandIndex__.DUP:
				case __OperandIndex__.DUP2:
				case __OperandIndex__.DUP2_X1:
				case __OperandIndex__.DUP2_X2:
				case __OperandIndex__.DUP_X1:
				case __OperandIndex__.DUP_X2:
				case __OperandIndex__.F2D:
				case __OperandIndex__.F2I:
				case __OperandIndex__.F2L:
				case __OperandIndex__.FADD:
				case __OperandIndex__.FALOAD:
				case __OperandIndex__.FASTORE:
				case __OperandIndex__.FCMPG:
				case __OperandIndex__.FCMPL:
				case __OperandIndex__.FCONST_0:
				case __OperandIndex__.FCONST_1:
				case __OperandIndex__.FCONST_2:
				case __OperandIndex__.FDIV:
				case __OperandIndex__.FLOAD_0:
				case __OperandIndex__.FLOAD_1:
				case __OperandIndex__.FLOAD_2:
				case __OperandIndex__.FLOAD_3:
				case __OperandIndex__.FMUL:
				case __OperandIndex__.FNEG:
				case __OperandIndex__.FREM:
				case __OperandIndex__.FSTORE_0:
				case __OperandIndex__.FSTORE_1:
				case __OperandIndex__.FSTORE_2:
				case __OperandIndex__.FSTORE_3:
				case __OperandIndex__.FSUB:
				case __OperandIndex__.I2B:
				case __OperandIndex__.I2C:
				case __OperandIndex__.I2D:
				case __OperandIndex__.I2F:
				case __OperandIndex__.I2L:
				case __OperandIndex__.I2S:
				case __OperandIndex__.IADD:
				case __OperandIndex__.IALOAD:
				case __OperandIndex__.IAND:
				case __OperandIndex__.IASTORE:
				case __OperandIndex__.ICONST_0:
				case __OperandIndex__.ICONST_1:
				case __OperandIndex__.ICONST_2:
				case __OperandIndex__.ICONST_3:
				case __OperandIndex__.ICONST_4:
				case __OperandIndex__.ICONST_5:
				case __OperandIndex__.ICONST_M1:
				case __OperandIndex__.IDIV:
				case __OperandIndex__.ILOAD_0:
				case __OperandIndex__.ILOAD_1:
				case __OperandIndex__.ILOAD_2:
				case __OperandIndex__.ILOAD_3:
				case __OperandIndex__.IMUL:
				case __OperandIndex__.INEG:
				case __OperandIndex__.IOR:
				case __OperandIndex__.IREM:
				case __OperandIndex__.ISHL:
				case __OperandIndex__.ISHR:
				case __OperandIndex__.ISTORE_0:
				case __OperandIndex__.ISTORE_1:
				case __OperandIndex__.ISTORE_2:
				case __OperandIndex__.ISTORE_3:
				case __OperandIndex__.ISUB:
				case __OperandIndex__.IUSHR:
				case __OperandIndex__.IXOR:
				case __OperandIndex__.L2D:
				case __OperandIndex__.L2F:
				case __OperandIndex__.L2I:
				case __OperandIndex__.LADD:
				case __OperandIndex__.LALOAD:
				case __OperandIndex__.LAND:
				case __OperandIndex__.LASTORE:
				case __OperandIndex__.LCMP:
				case __OperandIndex__.LCONST_0:
				case __OperandIndex__.LCONST_1:
				case __OperandIndex__.LDIV:
				case __OperandIndex__.LLOAD_0:
				case __OperandIndex__.LLOAD_1:
				case __OperandIndex__.LLOAD_2:
				case __OperandIndex__.LLOAD_3:
				case __OperandIndex__.LMUL:
				case __OperandIndex__.LNEG:
				case __OperandIndex__.LOR:
				case __OperandIndex__.LREM:
				case __OperandIndex__.LSHL:
				case __OperandIndex__.LSHR:
				case __OperandIndex__.LSTORE_0:
				case __OperandIndex__.LSTORE_1:
				case __OperandIndex__.LSTORE_2:
				case __OperandIndex__.LSTORE_3:
				case __OperandIndex__.LSUB:
				case __OperandIndex__.LUSHR:
				case __OperandIndex__.LXOR:
				case __OperandIndex__.MONITORENTER:
				case __OperandIndex__.MONITOREXIT:
				case __OperandIndex__.NOP:
				case __OperandIndex__.POP:
				case __OperandIndex__.POP2:
				case __OperandIndex__.SALOAD:
				case __OperandIndex__.SASTORE:
				case __OperandIndex__.SWAP:
					args = new Object[0];
					naturalflow = true;
					break;
					
					// Method invocations
				case __OperandIndex__.INVOKEINTERFACE:
				case __OperandIndex__.INVOKESPECIAL:
				case __OperandIndex__.INVOKESTATIC:
				case __OperandIndex__.INVOKEVIRTUAL:
					naturalflow = true;
					
					// Reference is in the constant pool
					MethodReference mr = pool.get(
						((code[argbase] & 0xFF) << 8) |
						(code[argbase + 1] & 0xFF)).<MethodReference>get(
						MethodReference.class);
					
					// {@squirreljme.error AQ19 Invocation of method did not
					// have the matching interface/not-interface attribute.
					// (The operation; The address; The method reference)}
					if (mr.isInterface() !=
						(op == __OperandIndex__.INVOKEINTERFACE))
						throw new JITException(String.format("AQ19 %d %d %s",
							op, __a, mr));
					
					args = new Object[]{mr};
					break;
					
					// {@squirreljme.error AQ18 The operation at the specified
					// address is not supported yet. (The operation;
					// The address it is at)}
				default:
					throw new RuntimeException(String.format("AQ18 %d %d",
						op, __a));
			}
			
			// Set
			this._args = args;
			this.naturalflow = naturalflow;
			
			// Copy jump targets
			List<JumpTarget> jt = new ArrayList<>();
			for (Object o : args)
				if (o instanceof JumpTarget)
					jt.add((JumpTarget)o);
			this._jumptargets = jt.<JumpTarget>toArray(
				new JumpTarget[jt.size()]);
		}
		
		/**
		 * Returns the address of this instruction.
		 *
		 * @return The instruction address.
		 * @since 2017/05/20
		 */
		public int address()
		{
			return this.address;
		}
		
		/**
		 * Returns the argument for this given instruction.
		 *
		 * @param <T> The type of argument to get.
		 * @param __i The index of the argument.
		 * @param __cl The class to cast to.
		 * @return The argument as the given class.
		 * @throws ClassCastException If the class is not castable.
		 * @throws IndexOutOfBoundsException If the argument index is not
		 * within bounds.
		 * @throws NullPointerException On null arguments.
		 * @since 2017/05/20
		 */
		public <T> T argument(int __i, Class<T> __cl)
			throws ClassCastException, IndexOutOfBoundsException,
				NullPointerException
		{
			// Check
			if (__cl == null)
				throw new NullPointerException("NARG");
			
			return __cl.cast(this._args[__i]);
		}
		
		/**
		 * The number of arguments the instruction takes.
		 *
		 * @return The argument count.
		 * @since 2017/05/20
		 */
		public int count()
		{
			return this._args.length;
		}
		
		/**
		 * Returns {@code true} if the instruction has natural flow.
		 *
		 * @return The instruction flows to the next naturally.
		 * @since 2017/05/20
		 */
		public boolean hasNaturalFlow()
		{
			return this.naturalflow;
		}
		
		/**
		 * Returns the jump targets for this instruction.
		 *
		 * @return The jump targets for this instruction.
		 * @since 2017/05/18
		 */
		public JumpTarget[] jumpTargets()
		{
			return this._jumptargets.clone();
		}
		
		/**
		 * Returns the operation that this performs.
		 *
		 * @return The operation.
		 * @since 2017/05/20
		 */
		public int operation()
		{
			return op;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/05/20
		 */
		@Override
		public String toString()
		{
			Reference<String> ref = this._string;
			String rv;
			
			if (ref == null || null == (rv = ref.get()))
			{
				// Address first
				StringBuilder sb = new StringBuilder("@");
				sb.append(this.address);
				
				// Then the operation
				sb.append('#');
				sb.append(__Mnemonics__.__toString(this.op));
				
				// Add marker if it flows naturally
				if (this.naturalflow)
					sb.append('~');
				
				// Then the arguments
				sb.append(":[");
				Object[] args = this._args;
				for (int i = 0, n = args.length; i < n; i++)
				{
					if (i > 0)
						sb.append(", ");
					sb.append(args[i]);
				}
				sb.append(']');
				
				// Cache
				this._string = new WeakReference<>((rv = sb.toString()));
			}
			
			return rv;
		}
	}
	
	/**
	 * This iterates over each byte code instruction.
	 *
	 * @since 2017/05/20
	 */
	private final class __Iterator__
		implements Iterator<Instruction>
	{
		/** The code length. */
		protected final int codelen =
			ByteCode.this.codelen;
		
		/** The read address. */
		private volatile int _at =
			0;
		
		/**
		 * {@inheritDoc}
		 * @since 2017/05/20
		 */
		@Override
		public boolean hasNext()
		{
			return this._at < this.codelen;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/05/20
		 */
		@Override
		public Instruction next()
			throws NoSuchElementException
		{
			// No more?
			if (!hasNext())
				throw new NoSuchElementException("NSEE");
			
			// Instruction at current pointer
			int at = this._at;
			Instruction rv = ByteCode.this.get(at);
			
			// Skip length of instruction
			this._at += ByteCode.this._lengths[at];
			
			return rv;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/05/20
		 */
		@Override
		public void remove()
			throws UnsupportedOperationException
		{
			throw new UnsupportedOperationException("RORO");
		}
	}
}

