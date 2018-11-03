// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This represents a single instruction within the byte code.
 *
 * @since 2017/10/15
 */
public final class Instruction
{
	/** The instruction address. */
	protected final int address;
	
	/** The instruction operation. */
	protected final int op;
	
	/** Does this instruction naturally flow? */
	protected final boolean naturalflow;
	
	/** Stack map table entry for this instruction. */
	protected final StackMapTableState smtstate;
	
	/** Instruction arguments. */
	private final Object[] _args;
	
	/** String representation of the operation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the instruction information.
	 *
	 * @param __code The instruction bytes.
	 * @param __pool The constant pool.
	 * @param __a The instruction address.
	 * @param __eh Exception handler table.
	 * @param __smt The stack map table data.
	 * @throws InvalidClassFormatException If the instruction is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/05/18
	 */
	Instruction(byte[] __code, Pool __pool, int __a,
		ExceptionHandlerTable __eh, StackMapTable __smt)
		throws InvalidClassFormatException, NullPointerException
	{
		// Check
		if (__code == null || __pool == null || __smt == null)
			throw new NullPointerException("NARG");
		
		// Get potential stack map entry for this instruction
		this.smtstate = __smt.get(__a);
		
		// Calculate real instruction address
		int aa = __a + ByteCode._CODE_OFFSET;
		
		// Read operation here
		int op = (__code[aa] & 0xFF),
			argbase = aa + 1;
		if (op == InstructionIndex.WIDE)
		{
			op = (op << 8) | (__code[aa + 1] & 0xFF);
			argbase++;
		}
		this.op = op;
		this.address = __a;
		
		// Depends on the operation
		Object[] args;
		boolean naturalflow;
		switch (op)
		{
				// No arguments and does not flow naturally
			case InstructionIndex.ATHROW:
			case InstructionIndex.ARETURN:
			case InstructionIndex.DRETURN:
			case InstructionIndex.FRETURN:
			case InstructionIndex.IRETURN:
			case InstructionIndex.LRETURN:
			case InstructionIndex.RETURN:
				args = new Object[0];
				naturalflow = false;
				break;
			
				// Operands with no arguments, natural flow
			case InstructionIndex.AALOAD:
			case InstructionIndex.AASTORE:
			case InstructionIndex.ACONST_NULL:
			case InstructionIndex.ALOAD_0:
			case InstructionIndex.ALOAD_1:
			case InstructionIndex.ALOAD_2:
			case InstructionIndex.ALOAD_3:
			case InstructionIndex.ARRAYLENGTH:
			case InstructionIndex.ASTORE_0:
			case InstructionIndex.ASTORE_1:
			case InstructionIndex.ASTORE_2:
			case InstructionIndex.ASTORE_3:
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
			case InstructionIndex.SALOAD:
			case InstructionIndex.SASTORE:
			case InstructionIndex.SWAP:
				args = new Object[0];
				naturalflow = true;
				break;
				
				// Argument is a class
			case InstructionIndex.ANEWARRAY:
			case InstructionIndex.CHECKCAST:
			case InstructionIndex.INSTANCEOF:
			case InstructionIndex.NEW:
				naturalflow = true;
				args = new Object[]{__pool.<ClassName>require(ClassName.class,
					Instruction.__readUnsignedShort(__code, argbase))};
				break;
				
				// First value is a signed byte
			case InstructionIndex.BIPUSH:
				naturalflow = true;
				args = new Object[]{
					Instruction.__readByte(__code, argbase)};
				break;
				
				// First value is an unsigned byte
			case InstructionIndex.ALOAD:
			case InstructionIndex.ILOAD:
			case InstructionIndex.LLOAD:
			case InstructionIndex.FLOAD:
			case InstructionIndex.DLOAD:
			case InstructionIndex.ASTORE:
			case InstructionIndex.ISTORE:
			case InstructionIndex.LSTORE:
			case InstructionIndex.FSTORE:
			case InstructionIndex.DSTORE:
				naturalflow = true;
				args = new Object[]{
					Instruction.__readUnsignedByte(__code, argbase)};
				break;
				
				// First value is a signed short
			case InstructionIndex.SIPUSH:
				naturalflow = true;
				args = new Object[]{
					Instruction.__readShort(__code, argbase)};
				break;
				
				// Read or write of a field
			case InstructionIndex.GETSTATIC:
			case InstructionIndex.PUTSTATIC:
			case InstructionIndex.GETFIELD:
			case InstructionIndex.PUTFIELD:
				naturalflow = true;
				args = new Object[]{__pool.<FieldReference>require(
					FieldReference.class,
					Instruction.__readUnsignedShort(__code, argbase))};
				break;
				
				// Goto
			case InstructionIndex.GOTO:
				naturalflow = false;
				args = new Object[]{new InstructionJumpTarget(
					__a + Instruction.__readShort(__code, argbase))};
				break;
				
				// Increment local variable
			case InstructionIndex.IINC:
				naturalflow = true;
				args = new Object[]{
					Instruction.__readUnsignedByte(__code, argbase),
					Instruction.__readByte(__code, argbase + 1)};
				break;
				
				// Increment local variable (wide)
			case InstructionIndex.WIDE_IINC:
				naturalflow = true;
				args = new Object[]{
					Instruction.__readUnsignedShort(__code, argbase),
					Instruction.__readShort(__code, argbase + 2)};
				break;
				
				// Branches
			case InstructionIndex.IFNONNULL:
			case InstructionIndex.IFNULL:
			case InstructionIndex.IF_ACMPEQ:
			case InstructionIndex.IF_ACMPNE:
			case InstructionIndex.IF_ICMPEQ:
			case InstructionIndex.IF_ICMPNE:
			case InstructionIndex.IF_ICMPLT:
			case InstructionIndex.IF_ICMPGE:
			case InstructionIndex.IF_ICMPGT:
			case InstructionIndex.IF_ICMPLE:
			case InstructionIndex.IFEQ:
			case InstructionIndex.IFNE:
			case InstructionIndex.IFLT:
			case InstructionIndex.IFGE:
			case InstructionIndex.IFGT:
			case InstructionIndex.IFLE:
				naturalflow = true;
				args = new Object[]{new InstructionJumpTarget(
					__a + Instruction.__readShort(__code, argbase))};
				break;
				
				// Method invocations
			case InstructionIndex.INVOKEINTERFACE:
			case InstructionIndex.INVOKESPECIAL:
			case InstructionIndex.INVOKESTATIC:
			case InstructionIndex.INVOKEVIRTUAL:
				naturalflow = true;
				
				// Reference is in the constant pool
				MethodReference mr = __pool.<MethodReference>require(
					MethodReference.class,
					Instruction.__readUnsignedShort(__code, argbase));
				
				// {@squirreljme.error JC0z Invocation of method did not
				// have the matching interface/not-interface attribute.
				// (The operation; The address; The method reference)}
				if (mr.isInterface() !=
					(op == InstructionIndex.INVOKEINTERFACE))
					throw new InvalidClassFormatException(String.format(
						"JC0z %d %d %s", op, __a, mr));
				
				args = new Object[]{mr};
				break;
				
				// Load constant value
			case InstructionIndex.LDC:
				naturalflow = true;
				
				// Could vary in type
				Object ldcv = __pool.<Object>require(Object.class,
					Instruction.__readUnsignedByte(__code, argbase));
				
				// Turn into a class value
				ConstantValue cvalue;
				if (ldcv instanceof ClassName)
					cvalue = new ConstantValueClass((ClassName)ldcv);
				else
					cvalue = (ConstantValue)ldcv;
				
				// {@squirreljme.error JC28 Cannot load a constant value which
				// is not of a narrow type. (The operation; The address; The
				// constant value)}
				if (!cvalue.type().isNarrow())
					throw new InvalidClassFormatException(String.format(
						"JC28 %d %d %s", op, __a, cvalue));
				
				// Just use this value
				args = new Object[]{cvalue};
				break;
				
				// Load wide constant value
			case InstructionIndex.LDC2_W:
				naturalflow = true;
				
				// Just will be a constant value type
				cvalue = __pool.<ConstantValue>require(
					ConstantValue.class,
					Instruction.__readUnsignedShort(__code, argbase));
				
				// {@squirreljme.error JC2j Cannot load a constant value which
				// is not of a wide type. (The operation; The address;
				// The constant value)}
				if (!cvalue.type().isWide())
					throw new InvalidClassFormatException(String.format(
						"JC2j %d %d %s", op, __a, cvalue));
				
				// Just use this value
				args = new Object[]{cvalue};
				break;
				
				// Allocate array of primitive type
			case InstructionIndex.NEWARRAY:
				naturalflow = true;
				
				// The primitive type depends
				PrimitiveType pt;
				int pd;
				switch ((pd = Instruction.__readUnsignedByte(__code, argbase)))
				{
					case 4:		pt = PrimitiveType.BOOLEAN; break;
					case 5:		pt = PrimitiveType.CHARACTER; break;
					case 6:		pt = PrimitiveType.FLOAT; break;
					case 7:		pt = PrimitiveType.DOUBLE; break;
					case 8:		pt = PrimitiveType.BYTE; break;
					case 9:		pt = PrimitiveType.SHORT; break;
					case 10:	pt = PrimitiveType.INTEGER; break;
					case 11:	pt = PrimitiveType.LONG; break;
					
						// {@squirreljme.error JC2d Unknown type specified for
						// new primitive array. (The operation; The address;
						// The type specifier)}
					default:
						throw new InvalidClassFormatException(String.format(
							"JC2d %d %d %d", op, __a, pd));
				}
				args = new Object[]{pt};
				break;
				
				// New multi-dimensional array
			case InstructionIndex.MULTIANEWARRAY:
				naturalflow = true;
				
				ClassName cname = __pool.<ClassName>require(ClassName.class,
					Instruction.__readUnsignedShort(__code, argbase));
				int dims = Instruction.__readUnsignedByte(__code,
					argbase + 2);
				
				// {@squirreljme.error JC2n Dimensions represented in type
				// is smaller than the represented dimensions.
				// (The operation; The address; The dimensions)}
				if (cname.dimensions() < dims)
					throw new InvalidClassFormatException(String.format(
						"JC2n %d %d %d", op, __a, dims));
				
				args = new Object[]{cname, dims};
				break;
			
				// Lookup switch lookup table
			case InstructionIndex.LOOKUPSWITCH:
				{
					// Determine the real address of the table
					int pa = ((aa + 4) & (~3));
					
					// Read in the default
					InstructionJumpTarget def = new InstructionJumpTarget(
						__a + Instruction.__readInt(__code, pa));
					
					// {@squirreljme.error JC2h Pair count for lookup switch
					// is negative. (The opcode; The address; The after padded
					// address; The read length)}
					int n = Instruction.__readInt(__code, pa + 4);
					if (n < 0)
						throw new InvalidClassFormatException(String.format(
							"JC2h %d %d %d %d", op, __a, pa, n));
					
					// Setup
					int[] keys = new int[n];
					InstructionJumpTarget[] jumps =
						new InstructionJumpTarget[n];
					
					// Load in tables
					for (int i = 0, ra = pa + 8; i < n; i++, ra += 8)
					{
						keys[i] = Instruction.__readInt(__code, ra);
						jumps[i] = new InstructionJumpTarget(
							__a + Instruction.__readInt(__code, ra + 4));
					}
					
					// Setup instruction properties
					naturalflow = true;
					args = new Object[]{new LookupSwitch(def, keys, jumps)};
				}
				break;
			
				// Table switch lookup table
			case InstructionIndex.TABLESWITCH:
				{
					// Determine the real address of the table
					int pa = ((aa + 4) & (~3));
					
					// Read in the default
					InstructionJumpTarget def = new InstructionJumpTarget(
						__a + Instruction.__readInt(__code, pa));
					
					// Read in low and high
					int lo = Instruction.__readInt(__code, pa + 4),
						hi = Instruction.__readInt(__code, pa + 8);
					
					// Read jump targets
					int n = (hi - lo) + 1;
					InstructionJumpTarget[] jumps =
						new InstructionJumpTarget[n];
					
					// Load in tables
					for (int i = 0, ra = pa + 12; i < n; i++, ra += 4)
						jumps[i] = new InstructionJumpTarget(
							__a + Instruction.__readInt(__code, ra));
					
					// Setup instruction properties
					naturalflow = true;
					args = new Object[]{new TableSwitch(def, lo, hi, jumps)};
				}
				break;
				
				// {@squirreljme.error JC10 The operation at the specified
				// address is not supported yet. (The operation; The name of
				// the operation; The address it is at)}
			default:
				throw new RuntimeException(String.format("JC10 %d %s %d",
					op, InstructionMnemonics.toString(op), __a));
		}
		
		// Set
		this._args = args;
		this.naturalflow = naturalflow;
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
	 * Obtains the given argument as an integer.
	 *
	 * @param __i The argument to get.
	 * @return The value of the argument.
	 * @throws ClassCastException If the given argument is not an integer.
	 * @throws IndexOutOfBoundsException If the index is not within the
	 * bounds of the instruction arguments.
	 * @since 2017/09/02
	 */
	public int intArgument(int __i)
		throws ClassCastException, IndexOutOfBoundsException
	{
		return this.<Integer>argument(__i, Integer.class).intValue();
	}
	
	/**
	 * Returns the mnemonic of this instruction.
	 *
	 * @return The instruction mnemonic.
	 * @since 2018/09/20
	 */
	public final String mnemonic()
	{
		return InstructionMnemonics.toString(this.op);
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
	 * Returns the stack map table state for this entry.
	 *
	 * @return The stack map table state.
	 * @since 2017/10/16
	 */
	public StackMapTableState stackMapTableState()
	{
		return this.smtstate;
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
			sb.append(InstructionMnemonics.toString(this.op));
			
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
	
	/**
	 * Reads a signed byte from the specified array.
	 *
	 * @param __a The array to read from.
	 * @param __o The offset to read from.
	 * @return The read value.
	 * @throws InvalidClassFormatException If the offset exceeds the bounds of
	 * the given array.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/16
	 */
	static final int __readByte(byte[] __a, int __o)
		throws InvalidClassFormatException, NullPointerException
	{
		return (byte)Instruction.__readUnsignedByte(__a, __o);
	}
	
	/**
	 * Reads a signed int from the specified array.
	 *
	 * @param __a The array to read from.
	 * @param __o The offset to read from.
	 * @return The read value.
	 * @throws InvalidClassFormatException If the offset exceeds the bounds of
	 * the given array.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/18
	 */
	static final int __readInt(byte[] __a, int __o)
		throws InvalidClassFormatException, NullPointerException
	{
		// Check
		if (__a == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error JC2e Illegal int read off the end of the
		// instruction array. (The offset; The length of the code array)}
		if (__o < 0 || __o + 3 >= __a.length)
			throw new InvalidClassFormatException(
				String.format("JC2e %d %d", __o, __a.length));
		
		return ((__a[__o] & 0xFF) << 24)  |
			((__a[__o + 1] & 0xFF) << 16)  |
			((__a[__o + 2] & 0xFF) << 8) |
			(__a[__o + 3] & 0xFF);
	}
	
	/**
	 * Reads a signed short from the specified array.
	 *
	 * @param __a The array to read from.
	 * @param __o The offset to read from.
	 * @return The read value.
	 * @throws InvalidClassFormatException If the offset exceeds the bounds of
	 * the given array.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/15
	 */
	static final int __readShort(byte[] __a, int __o)
		throws InvalidClassFormatException, NullPointerException
	{
		return (short)Instruction.__readUnsignedShort(__a, __o);
	}
	
	/**
	 * Reads an unsigned byte from the specified array.
	 *
	 * @param __a The array to read from.
	 * @param __o The offset to read from.
	 * @return The read value.
	 * @throws InvalidClassFormatException If the offset exceeds the bounds of
	 * the given array.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/08
	 */
	static final int __readUnsignedByte(byte[] __a, int __o)
		throws InvalidClassFormatException, NullPointerException
	{
		// Check
		if (__a == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error JC29 Illegal byte read off the end of the
		// instruction array. (The offset; The length of the code array)}
		if (__o < 0 || __o >= __a.length)
			throw new InvalidClassFormatException(
				String.format("JC29 %d %d", __o, __a.length));
		
		return (__a[__o] & 0xFF);
	}
	
	/**
	 * Reads an unsigned short from the specified array.
	 *
	 * @param __a The array to read from.
	 * @param __o The offset to read from.
	 * @return The read value.
	 * @throws InvalidClassFormatException If the offset exceeds the bounds of
	 * the given array.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/07/15
	 */
	static final int __readUnsignedShort(byte[] __a, int __o)
		throws InvalidClassFormatException, NullPointerException
	{
		// Check
		if (__a == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error JC11 Illegal short read off the end of the
		// instruction array. (The offset; The length of the code array)}
		if (__o < 0 || __o + 1 >= __a.length)
			throw new InvalidClassFormatException(
				String.format("JC11 %d %d", __o, __a.length));
		
		return ((__a[__o] & 0xFF) << 8) | (__a[__o + 1] & 0xFF);
	}
}

