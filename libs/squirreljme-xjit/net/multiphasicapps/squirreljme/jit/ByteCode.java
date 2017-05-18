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

/**
 * This represents the byte code for a given method. It contains the actual
 * instructions, iterators over instructions, along with jump targets which
 * are available for usage.
 *
 * @since 2017/05/14
 */
public class ByteCode
{
	/** The number of stack variables. */
	protected final int maxstack;
	
	/** The number of local variables. */
	protected final int maxlocals;
	
	/** The byte code for the method. */
	private final byte[] _code;
	
	/** Instruction lengths at each position. */
	private final int[] _lengths;
	
	/** The constant pool. */
	private final __Pool__ _pool;
	
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
		this._pool = __pool;
		int codelen = __code.length;
		this.maxstack = __ms;
		this.maxlocals = __ml;
		
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
			
			// {@squirreljme.error AQ2b The operation exceeds the bounds of
			// the method byte code. (The operation pointer; The operation
			// length; The code length)}
			if ((i += oplen) > codelen)
				throw new JITException(String.format("AQ2b %d %d %d", i, oplen,
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
			op = (op << 8) | (__code[__a + 1] & 0xFF);
			rv = 2;
		}
		
		// Depends on the operation
		switch (op)
		{
				// {@squirreljme.error AQ2c Unsupported instruction specified
				// in the method byte code. (The operation; The address)}
			case __OperandIndex__.BREAKPOINT:
			case __OperandIndex__.IMPDEP1:
			case __OperandIndex__.IMPDEP2:
			case __OperandIndex__.JSR:
			case __OperandIndex__.JSR_W:
			case __OperandIndex__.RET:
			case __OperandIndex__.WIDE:
				throw new JITException(String.format("AQ2c %d %d", op, __a));
			
				// {@squirreljme.error AQ2d Invokedynamic is not supported in
				// this virtual machine. (The address)}
			case __OperandIndex__.INVOKEDYNAMIC:
				throw new JITException(String.format("AQ2d %d", __a));
				
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
			
				// {@squirreljme.error AQ2c Cannot get the length of the
				// specified operation because it is not valid. (The operation;
				// The address)}
			default:
				throw new JITException(String.format("AQ2c %d %d", op, __a));
		}
		
		return rv;
	}
}

