// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.java;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This represents the byte code for a given method. It contains the actual
 * instructions, iterators over instructions, along with jump targets which
 * are available for usage.
 *
 * @since 2017/05/14
 */
@Deprecated
public class ByteCode
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
	
	/** The addresses of every instruction by index. */
	private final int[] _index;
	
	/** The constant pool. */
	protected final Pool pool;
	
	/** Jump targets. */
	private final JumpTarget[] _jumptargets;
	
	/** The cache of instructions in the byte code. */
	private final Reference<Instruction>[] _icache;
	
	/** The string representation. */
	private volatile Reference<String> _string;
	
	/** Basic blocks within the byte code. */
	private volatile Reference<BasicBlocks> _blocks;
	
	/**
	 * Represents the byte code.
	 *
	 * @param __ms The maximum number of stack entries.
	 * @param __ml The maximum number of local entries.
	 * @param __code The program's byte code, this is not copied.
	 * @param __eht The exception handler table.
	 * @param _pool The constant pool.
	 * @throws JITException If the byte code is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/05/14
	 */
	ByteCode(int __ms, int __ml, byte[] __code, ExceptionHandlerTable __eht,
		Pool __pool)
		throws JITException, NullPointerException
	{
		// Check
		if (__code == null || __eht == null || __pool == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._code = __code;
		this.codelen = __code.length;
		this.pool = __pool;
		int codelen = __code.length;
		this.maxstack = __ms;
		this.maxlocals = __ml;
		this.exceptions = __eht;
		
		// Cache instructions
		this._icache = __newCache(codelen);
		
		// The instruction index is used to lookup using a linear index count
		// rather than the potentially spaced out address lookup
		int[] index = new int[codelen];
		int indexat = 0;
		
		// Set all lengths initially to invalid positions, this used as a quick
		// marker to determine which positions have valid instructions
		int[] lengths = new int[codelen];
		for (int i = 0; i < codelen; i++)
			lengths[i] = -1;
		
		// Determine instruction lengths for each position
		for (int i = 0; i < codelen;)
		{
			// Store address of instruction for an index based lookup
			index[indexat++] = i;
			
			// Store length
			int oplen;
			lengths[i] = (oplen = __opLength(__code, i));
			
			// {@squirreljme.error JI1h The operation exceeds the bounds of
			// the method byte code. (The operation pointer; The operation
			// length; The code length)}
			if ((i += oplen) > codelen)
				throw new JITException(String.format("JI1h %d %d %d", i, oplen,
					codelen));
		}
		this._lengths = lengths;
		
		// Store addresses for all the indexes
		if (indexat == codelen)
			this._index = index;
		else
			this._index = Arrays.copyOf(index, indexat);
		
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
				for (JumpTarget jt : getByAddress(i).jumpTargets())
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
		// {@squirreljme.error JI1i The instruction at the specified address is
		// not valid. (The address)}
		if (!isValidAddress(__a))
			throw new JITException(String.format("JI1i %d", __a));
		
		return __a + this._lengths[__a];
	}
	
	/**
	 * Translates an address to an index.
	 *
	 * @param __a The address to translate.
	 * @return The index of the instruction or {@code -1} if it is not valid,
	 * if the address is the byte code length then the number of indexes is
	 * returned.
	 * @since 2017/08/02
	 */
	public int addressToIndex(int __a)
	{
		// Byte right at the end converts to the last index
		int[] index = this._index;
		if (__a == this.codelen)
			return index.length;
		
		// Not the end
		int rv = Arrays.binarySearch(index, __a);
		if (rv < 0)
			return -1;
		return rv;
	}
	
	/**
	 * Returns a representation of the basic blocks within the byte code which
	 * indicates which instructions are formed as a logical non-jumping unit.
	 *
	 * @return The basic blocks which make up the byte code.
	 * @since 2017/08/01
	 */
	public BasicBlocks basicBlocks()
	{
		Reference<BasicBlocks> ref = this._blocks;
		BasicBlocks rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._blocks = new WeakReference<>((rv = new BasicBlocks(this)));
		
		return rv;
	}
	
	/**
	 * Returns the exception handler key for the given address, this will
	 * return a unique key for the given address.
	 *
	 * @return The exception handler key for the given address or {@code null}
	 * if there are no exceptions to be handled by the given address.
	 * @throws IllegalArgumentException If the address is negative.
	 * @throws JITException If the address is not valid.
	 * @since 2017/08/08
	 */
	public ExceptionHandlerKey exceptionHandlerKeyByAddress(int __a)
		throws IllegalArgumentException, JITException
	{
		// {@squirreljme.error JI1y The address for the given key is
		// negative. (The address)}
		if (__a < 0)
			throw new IllegalArgumentException(String.format("JI1y %d", __a));
		
		// {@squirreljme.error JI1z The specified address cannot have
		// exceptions because it is not a valid address. (The address)}
		if (!isValidAddress(__a))
			throw new JITException(String.format("JI1z %d", __a));
		
		// If no exceptions are handled in the method do not bother at all
		ExceptionHandlerTable exceptions = this.exceptions;
		if (exceptions == null || exceptions.size() <= 0)
			return null;
		
		throw new todo.TODO();
	}
	
	/**
	 * Returns the instruction at the specified address.
	 *
	 * @param __a The address to get the instruction for.
	 * @return The represented instruction for the given address.
	 * @throws JITException If the address is not valid.
	 * @since 2017/05/18
	 */
	public Instruction getByAddress(int __a)
		throws JITException
	{
		// {@squirreljme.error JI1j The instruction at the specified address is
		// not valid. (The address)}
		if (!isValidAddress(__a))
			throw new JITException(String.format("JI1j %d", __a));
		
		Reference<Instruction>[] icache = this._icache;
		Reference<Instruction> ref = icache[__a];
		Instruction rv;
		
		if (ref == null || null == (rv = ref.get()))
			icache[__a] = new WeakReference<>((rv = new Instruction(this._code,
				this.pool, __a, this.exceptions, this)));
		
		return rv;
	}
	
	/**
	 * Returns the instruction based on an index in the order it apperas within
	 * the byte code rather than by address.
	 *
	 * @param __i The instruction index to get.
	 * @return The instruction at this index.
	 * @throws IndexOutOfBoundsException If the index is not within bounds.
	 * @throws JITException If the instruction is not valid.
	 * @since 2017/08/01
	 */
	public Instruction getByIndex(int __i)
		throws IndexOutOfBoundsException
	{
		// Check
		int[] index = this._index;
		if (__i < 0 || __i >= index.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		return getByAddress(index[__i]);
	}
	
	/**
	 * Translates an index to an address.
	 *
	 * @param __i The index to translate.
	 * @return The address of the index or {@code -1} if it is not valid, if
	 * the index is the number of indexes then the length of the byte code in
	 * addresses is returned.
	 * @since 2017/08/02
	 */
	public int indexToAddress(int __i)
	{
		// Last index translates to the length of the byte code
		int[] index = this._index;
		int n = index.length;
		if (__i == n)
			return this.codelen;
		
		// Normal position
		if (__i < 0 || __i >= n)
			return -1;
		return index[__i];
	}
	
	/**
	 * Returns the number of instructions which are within this method.
	 *
	 * @return The number of instructions which are in the method.
	 * @since 2017/08/01
	 */
	public int instructionCount()
	{
		return this._index.length;
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
	 * This returns an iterator over the instructions which are defined within
	 * this method.
	 *
	 * @return The iterator over byte code instructions.
	 * @since 2017/05/20
	 */
	public Iterator<Instruction> instructionIterator()
	{
		return new __InstructionIterator__();
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
	 * This returns the addresses of all jump targets within the method as a
	 * plain array of integers.
	 *
	 * @return An array of plain integers for every jump target in a method.
	 * @since 2017/08/02
	 */
	public int[] jumpTargetAddresses()
	{
		JumpTarget[] jumptargets = this._jumptargets;
		int n = jumptargets.length;
		int[] rv = new int[n];
		
		// Fill
		for (int i = 0; i < n; i++)
			rv[i] = jumptargets[i].address();
		
		return rv;
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
	 * Returns the constant pool being used.
	 *
	 * @return The constant pool the method uses.
	 * @since 2017/05/20
	 */
	public Pool pool()
	{
		return this.pool;
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
			for (Iterator<Instruction> it = instructionIterator();
				it.hasNext();)
			{
				if (comma)
					sb.append(", ");
				else
					comma = true;
				
				sb.append(it.next());
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
		if (op == InstructionIndex.WIDE)
		{
			// {@squirreljme.error JI1k The wide instruction cannot be the
			// last instruction in a method. (The address)}
			if (__a + 1 >= __code.length)
				throw new JITException(String.format("JI1k %d", __a));
			
			op = (op << 8) | (__code[__a + 1] & 0xFF);
			rv = 2;
		}
		
		// Depends on the operation
		switch (op)
		{
				// {@squirreljme.error JI1l Unsupported instruction specified
				// in the method byte code. (The operation; The address)}
			case InstructionIndex.BREAKPOINT:
			case InstructionIndex.IMPDEP1:
			case InstructionIndex.IMPDEP2:
			case InstructionIndex.JSR:
			case InstructionIndex.JSR_W:
			case InstructionIndex.RET:
			case InstructionIndex.WIDE:
				throw new JITException(String.format("JI1l %d %d", op, __a));
			
				// {@squirreljme.error JI1m Invokedynamic is not supported in
				// this virtual machine. (The address)}
			case InstructionIndex.INVOKEDYNAMIC:
				throw new JITException(String.format("JI1m %d", __a));
				
				// Operands with no arguments
			case InstructionIndex.AALOAD:
			case InstructionIndex.AASTORE:
			case InstructionIndex.ACONST_NULL:
			case InstructionIndex.ALOAD_0:
			case InstructionIndex.ALOAD_1:
			case InstructionIndex.ALOAD_2:
			case InstructionIndex.ALOAD_3:
			case InstructionIndex.ARETURN:
			case InstructionIndex.ARRAYLENGTH:
			case InstructionIndex.ASTORE_0:
			case InstructionIndex.ASTORE_1:
			case InstructionIndex.ASTORE_2:
			case InstructionIndex.ASTORE_3:
			case InstructionIndex.ATHROW:
			case InstructionIndex.BALOAD:
			case InstructionIndex.BASTORE:
			case InstructionIndex.CALOAD:
			case InstructionIndex.CASTORE:
			case InstructionIndex.D2F:
			case InstructionIndex.D2I:
			case InstructionIndex.D2L:
			case InstructionIndex.DADD:
			case InstructionIndex.DALOAD:
			case InstructionIndex.DASTORE:
			case InstructionIndex.DCMPG:
			case InstructionIndex.DCMPL:
			case InstructionIndex.DCONST_0:
			case InstructionIndex.DCONST_1:
			case InstructionIndex.DDIV:
			case InstructionIndex.DLOAD_0:
			case InstructionIndex.DLOAD_1:
			case InstructionIndex.DLOAD_2:
			case InstructionIndex.DLOAD_3:
			case InstructionIndex.DMUL:
			case InstructionIndex.DNEG:
			case InstructionIndex.DREM:
			case InstructionIndex.DRETURN:
			case InstructionIndex.DSTORE_0:
			case InstructionIndex.DSTORE_1:
			case InstructionIndex.DSTORE_2:
			case InstructionIndex.DSTORE_3:
			case InstructionIndex.DSUB:
			case InstructionIndex.DUP:
			case InstructionIndex.DUP2:
			case InstructionIndex.DUP2_X1:
			case InstructionIndex.DUP2_X2:
			case InstructionIndex.DUP_X1:
			case InstructionIndex.DUP_X2:
			case InstructionIndex.F2D:
			case InstructionIndex.F2I:
			case InstructionIndex.F2L:
			case InstructionIndex.FADD:
			case InstructionIndex.FALOAD:
			case InstructionIndex.FASTORE:
			case InstructionIndex.FCMPG:
			case InstructionIndex.FCMPL:
			case InstructionIndex.FCONST_0:
			case InstructionIndex.FCONST_1:
			case InstructionIndex.FCONST_2:
			case InstructionIndex.FDIV:
			case InstructionIndex.FLOAD_0:
			case InstructionIndex.FLOAD_1:
			case InstructionIndex.FLOAD_2:
			case InstructionIndex.FLOAD_3:
			case InstructionIndex.FMUL:
			case InstructionIndex.FNEG:
			case InstructionIndex.FREM:
			case InstructionIndex.FRETURN:
			case InstructionIndex.FSTORE_0:
			case InstructionIndex.FSTORE_1:
			case InstructionIndex.FSTORE_2:
			case InstructionIndex.FSTORE_3:
			case InstructionIndex.FSUB:
			case InstructionIndex.I2B:
			case InstructionIndex.I2C:
			case InstructionIndex.I2D:
			case InstructionIndex.I2F:
			case InstructionIndex.I2L:
			case InstructionIndex.I2S:
			case InstructionIndex.IADD:
			case InstructionIndex.IALOAD:
			case InstructionIndex.IAND:
			case InstructionIndex.IASTORE:
			case InstructionIndex.ICONST_0:
			case InstructionIndex.ICONST_1:
			case InstructionIndex.ICONST_2:
			case InstructionIndex.ICONST_3:
			case InstructionIndex.ICONST_4:
			case InstructionIndex.ICONST_5:
			case InstructionIndex.ICONST_M1:
			case InstructionIndex.IDIV:
			case InstructionIndex.ILOAD_0:
			case InstructionIndex.ILOAD_1:
			case InstructionIndex.ILOAD_2:
			case InstructionIndex.ILOAD_3:
			case InstructionIndex.IMUL:
			case InstructionIndex.INEG:
			case InstructionIndex.IOR:
			case InstructionIndex.IREM:
			case InstructionIndex.IRETURN:
			case InstructionIndex.ISHL:
			case InstructionIndex.ISHR:
			case InstructionIndex.ISTORE_0:
			case InstructionIndex.ISTORE_1:
			case InstructionIndex.ISTORE_2:
			case InstructionIndex.ISTORE_3:
			case InstructionIndex.ISUB:
			case InstructionIndex.IUSHR:
			case InstructionIndex.IXOR:
			case InstructionIndex.L2D:
			case InstructionIndex.L2F:
			case InstructionIndex.L2I:
			case InstructionIndex.LADD:
			case InstructionIndex.LALOAD:
			case InstructionIndex.LAND:
			case InstructionIndex.LASTORE:
			case InstructionIndex.LCMP:
			case InstructionIndex.LCONST_0:
			case InstructionIndex.LCONST_1:
			case InstructionIndex.LDIV:
			case InstructionIndex.LLOAD_0:
			case InstructionIndex.LLOAD_1:
			case InstructionIndex.LLOAD_2:
			case InstructionIndex.LLOAD_3:
			case InstructionIndex.LMUL:
			case InstructionIndex.LNEG:
			case InstructionIndex.LOR:
			case InstructionIndex.LREM:
			case InstructionIndex.LRETURN:
			case InstructionIndex.LSHL:
			case InstructionIndex.LSHR:
			case InstructionIndex.LSTORE_0:
			case InstructionIndex.LSTORE_1:
			case InstructionIndex.LSTORE_2:
			case InstructionIndex.LSTORE_3:
			case InstructionIndex.LSUB:
			case InstructionIndex.LUSHR:
			case InstructionIndex.LXOR:
			case InstructionIndex.MONITORENTER:
			case InstructionIndex.MONITOREXIT:
			case InstructionIndex.NOP:
			case InstructionIndex.POP:
			case InstructionIndex.POP2:
			case InstructionIndex.RETURN:
			case InstructionIndex.SALOAD:
			case InstructionIndex.SASTORE:
			case InstructionIndex.SWAP:
				break;
				
				// An additional byte
			case InstructionIndex.ALOAD:
			case InstructionIndex.ASTORE:
			case InstructionIndex.BIPUSH:
			case InstructionIndex.DLOAD:
			case InstructionIndex.DSTORE:
			case InstructionIndex.FLOAD:
			case InstructionIndex.FSTORE:
			case InstructionIndex.IINC:
			case InstructionIndex.ILOAD:
			case InstructionIndex.ISTORE:
			case InstructionIndex.LDC:
			case InstructionIndex.LLOAD:
			case InstructionIndex.LSTORE:
			case InstructionIndex.NEWARRAY:
				rv++;
				break;
				
				// Operations with two bytes following
			case InstructionIndex.ANEWARRAY:
			case InstructionIndex.CHECKCAST:
			case InstructionIndex.GETFIELD:
			case InstructionIndex.GETSTATIC:
			case InstructionIndex.GOTO:
			case InstructionIndex.IF_ACMPEQ:
			case InstructionIndex.IF_ACMPNE:
			case InstructionIndex.IFEQ:
			case InstructionIndex.IFGE:
			case InstructionIndex.IFGT:
			case InstructionIndex.IF_ICMPEQ:
			case InstructionIndex.IF_ICMPGE:
			case InstructionIndex.IF_ICMPGT:
			case InstructionIndex.IF_ICMPLE:
			case InstructionIndex.IF_ICMPLT:
			case InstructionIndex.IF_ICMPNE:
			case InstructionIndex.IFLE:
			case InstructionIndex.IFLT:
			case InstructionIndex.IFNE:
			case InstructionIndex.IFNONNULL:
			case InstructionIndex.IFNULL:
			case InstructionIndex.INSTANCEOF:
			case InstructionIndex.INVOKESPECIAL:
			case InstructionIndex.INVOKESTATIC:
			case InstructionIndex.INVOKEVIRTUAL:
			case InstructionIndex.LDC2_W:
			case InstructionIndex.LDC_W:
			case InstructionIndex.NEW:
			case InstructionIndex.PUTFIELD:
			case InstructionIndex.PUTSTATIC:
			case InstructionIndex.SIPUSH:
			case InstructionIndex.WIDE_ALOAD:
			case InstructionIndex.WIDE_ASTORE:
			case InstructionIndex.WIDE_DLOAD:
			case InstructionIndex.WIDE_DSTORE:
			case InstructionIndex.WIDE_FLOAD:
			case InstructionIndex.WIDE_FSTORE:
			case InstructionIndex.WIDE_IINC:
			case InstructionIndex.WIDE_ILOAD:
			case InstructionIndex.WIDE_ISTORE:
			case InstructionIndex.WIDE_LLOAD:
			case InstructionIndex.WIDE_LSTORE:
				rv += 2;
				break;
				
				// Three bytes
			case InstructionIndex.INVOKEINTERFACE:
			case InstructionIndex.MULTIANEWARRAY:
				rv += 3;
				break;
				
				// Four bytes
			case InstructionIndex.GOTO_W:
				rv += 4;
				break;
			
				// Table switch
			case InstructionIndex.TABLESWITCH:
				throw new todo.TODO();
				
				// Lookup switch
			case InstructionIndex.LOOKUPSWITCH:
				throw new todo.TODO();
			
				// {@squirreljme.error JI1n Cannot get the length of the
				// specified operation because it is not valid. (The operation;
				// The address)}
			default:
				throw new JITException(String.format("JI1n %d %d", op, __a));
		}
		
		return rv;
	}
	
	/**
	 * This iterates over each byte code instruction.
	 *
	 * @since 2017/05/20
	 */
	private final class __InstructionIterator__
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
			Instruction rv = ByteCode.this.getByAddress(at);
			
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

