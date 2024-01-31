// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.LinkedHashSet;
import java.util.Set;

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
	
	/** Jump targets. */
	protected final InstructionJumpTargets jumptargets;
	
	/** The logical function address. */
	protected final int logicalAddress;
	
	/** The length of this instruction. */
	protected final int length;
	
	/** The address which follows this one. */
	protected final int addressFollowing;
	
	/** Instruction arguments. */
	private final Object[] _args;
	
	/** Raw instruction arguments, usually an address or index. */
	private final int[] _rawArgs;
	
	/** String representation of the operation. */
	private Reference<String> _string;
	
	/** The normalized instruction. */
	private Reference<Instruction> _normalized;
	
	/**
	 * Initializes base instruction.
	 * 
	 * @param __address The address.
	 * @param __op The operation.
	 * @param __naturalflow Is this natural flow?
	 * @param __smtstate The SMT state.
	 * @param __jumptargets The jump targets.
	 * @param __args Arguments.
	 * @param __rawArgs Raw arguments.
	 * @param __logicalAddr The logical instruction address.
	 * @param __length The length of this instruction.
	 * @param __addressFollowing The address following this one.
	 * @since 2023/07/03
	 */
	private Instruction(int __address, int __op, boolean __naturalflow,
		StackMapTableState __smtstate, InstructionJumpTargets __jumptargets,
		Object[] __args, int[] __rawArgs, int __logicalAddr, int __length,
		int __addressFollowing)
	{
		this.address = __address;
		this.op = __op;
		this.naturalflow = __naturalflow;
		this.smtstate = __smtstate;
		this.jumptargets = __jumptargets;
		this._args = __args;
		this._rawArgs = __rawArgs;
		this.logicalAddress = __logicalAddr;
		this.length = __length;
		this.addressFollowing = __addressFollowing;
	}
	
	/**
	 * Initializes the instruction information.
	 *
	 * @param __code The instruction bytes.
	 * @param __pool The constant pool.
	 * @param __a The instruction address.
	 * @param __eh Exception handler table.
	 * @param __smt The stack map table data.
	 * @param __af Address of the instruction which follows this.
	 * @param __logicalAddr The logical instruction address.
	 * @throws InvalidClassFormatException If the instruction is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/05/18
	 */
	Instruction(byte[] __code, Pool __pool, int __a,
		ExceptionHandlerTable __eh, StackMapTable __smt, int __af,
		int __logicalAddr)
		throws InvalidClassFormatException, NullPointerException
	{
		// Check
		if (__code == null || __pool == null || __smt == null)
			throw new NullPointerException("NARG");
		
		// Get potential stack map entry for this instruction
		this.smtstate = __smt.get(__a);
		
		// Read operation here
		int op = ByteCodeUtils.instructionOpCode(__code,
			ByteCode.CODE_OFFSET, __a);
		
		this.op = op;
		this.address = __a;
		this.addressFollowing = __af;
		this.logicalAddress = __logicalAddr;
		this.length = __af - __a;
		
		// Arguments for this instruction
		int[] rawArgs = ByteCodeUtils.readRawArguments(__code,
			ByteCode.CODE_OFFSET, __a);
		Object[] args;
		
		// Depends on the operation
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
					rawArgs[0])};
				break;
				
				// First value is a signed byte
			case InstructionIndex.BIPUSH:
				naturalflow = true;
				args = new Object[]{
					rawArgs[0]};
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
					rawArgs[0]};
				break;
				
				// First value is a signed short
			case InstructionIndex.SIPUSH:
				naturalflow = true;
				args = new Object[]{
					rawArgs[0]};
				break;
				
				// Read or write of a field
			case InstructionIndex.GETSTATIC:
			case InstructionIndex.PUTSTATIC:
			case InstructionIndex.GETFIELD:
			case InstructionIndex.PUTFIELD:
				naturalflow = true;
				args = new Object[]{__pool.<FieldReference>require(
					FieldReference.class,
					rawArgs[0])};
				break;
				
				// Goto
			case InstructionIndex.GOTO:
				naturalflow = false;
				args = new Object[]{new InstructionJumpTarget(
					__a + rawArgs[0])};
				break;
				
				// Increment local variable
			case InstructionIndex.IINC:
				naturalflow = true;
				args = new Object[]{
					rawArgs[0],
					rawArgs[1]};
				break;
				
				// Increment local variable (wide)
			case InstructionIndex.WIDE_IINC:
				naturalflow = true;
				args = new Object[]{
					rawArgs[0],
					rawArgs[1]};
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
					__a + rawArgs[0])};
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
					rawArgs[0]);
				
				/* {@squirreljme.error JC31 Invocation of method did not
				have the matching interface/not-interface attribute.
				(The operation; The address; The method reference)} */
				if (mr.isInterface() !=
					(op == InstructionIndex.INVOKEINTERFACE))
					throw new InvalidClassFormatException(String.format(
						"JC31 %d %d %s", op, __a, mr));
				
				args = new Object[]{mr};
				break;
				
				// Load constant value
			case InstructionIndex.LDC:
			case InstructionIndex.LDC_W:
				naturalflow = true;
				
				// Could vary in type
				Object ldcv = __pool.<Object>require(Object.class,
					rawArgs[0]);
				
				// Turn into a class value
				ConstantValue cvalue;
				if (ldcv instanceof ClassName)
					cvalue = new ConstantValueClass((ClassName)ldcv);
				else
					cvalue = (ConstantValue)ldcv;
				
				/* {@squirreljme.error JC32 Cannot load a constant value which
				is not of a narrow type. (The operation; The address; The
				constant value)} */
				if (!cvalue.type().isNarrow())
					throw new InvalidClassFormatException(String.format(
						"JC32 %d %d %s", op, __a, cvalue));
				
				// Just use this value
				args = new Object[]{cvalue};
				break;
				
				// Load wide constant value
			case InstructionIndex.LDC2_W:
				naturalflow = true;
				
				// Just will be a constant value type
				cvalue = __pool.<ConstantValue>require(
					ConstantValue.class,
					rawArgs[0]);
				
				/* {@squirreljme.error JC33 Cannot load a constant value which
				is not of a wide type. (The operation; The address;
				The constant value)} */
				if (!cvalue.type().isWide())
					throw new InvalidClassFormatException(String.format(
						"JC33 %d %d %s", op, __a, cvalue));
				
				// Just use this value
				args = new Object[]{cvalue};
				break;
				
				// Allocate array of primitive type
			case InstructionIndex.NEWARRAY:
				naturalflow = true;
				
				// The primitive type depends
				PrimitiveType pt;
				int pd;
				switch ((pd = rawArgs[0]))
				{
					case 4:		pt = PrimitiveType.BOOLEAN; break;
					case 5:		pt = PrimitiveType.CHARACTER; break;
					case 6:		pt = PrimitiveType.FLOAT; break;
					case 7:		pt = PrimitiveType.DOUBLE; break;
					case 8:		pt = PrimitiveType.BYTE; break;
					case 9:		pt = PrimitiveType.SHORT; break;
					case 10:	pt = PrimitiveType.INTEGER; break;
					case 11:	pt = PrimitiveType.LONG; break;
					
						/* {@squirreljme.error JC34 Unknown type specified for
						new primitive array. (The operation; The address;
						The type specifier)} */
					default:
						throw new InvalidClassFormatException(String.format(
							"JC34 %d %d %d", op, __a, pd));
				}
				args = new Object[]{pt};
				break;
				
				// New multi-dimensional array
			case InstructionIndex.MULTIANEWARRAY:
				naturalflow = true;
				
				ClassName cname = __pool.<ClassName>require(ClassName.class,
					rawArgs[0]);
				int dims = rawArgs[1];
				
				/* {@squirreljme.error JC35 Dimensions represented in type
				is smaller than the represented dimensions.
				(The operation; The address; The dimensions)} */
				if (cname.dimensions() < dims)
					throw new InvalidClassFormatException(String.format(
						"JC35 %d %d %d", op, __a, dims));
				
				args = new Object[]{cname, dims};
				break;
			
				// Lookup switch lookup table
			case InstructionIndex.LOOKUPSWITCH:
				{
					// Read in the default
					InstructionJumpTarget def = new InstructionJumpTarget(
						rawArgs[0]);
					
					// Setup
					int n = rawArgs[1];
					int[] keys = new int[n];
					InstructionJumpTarget[] jumps =
						new InstructionJumpTarget[n];
					
					// Load in tables
					for (int i = 0, fromDx = 2; i < n; i++, fromDx += 2)
					{
						int key = rawArgs[fromDx];
						keys[i] = key;
						jumps[i] = new InstructionJumpTarget(
							rawArgs[fromDx] + 1,
							key);
					}
					
					// Setup instruction properties
					naturalflow = true;
					args = new Object[]{new LookupSwitch(def, keys, jumps)};
				}
				break;
			
				// Table switch lookup table
			case InstructionIndex.TABLESWITCH:
				{
					// Read in the default
					InstructionJumpTarget def = new InstructionJumpTarget(
						rawArgs[0]);
					
					// Read in low and high
					int lo = rawArgs[1];
					int hi = rawArgs[2];
					
					// Read jump targets
					int n = (hi - lo) + 1;
					InstructionJumpTarget[] jumps =
						new InstructionJumpTarget[n];
					
					// Load in tables
					for (int i = 0, fromDx = 3; i < n; i++, fromDx++)
						jumps[i] = new InstructionJumpTarget(
							rawArgs[fromDx], lo + i);
					
					// Setup instruction properties
					naturalflow = true;
					args = new Object[]{new TableSwitch(def, lo, hi, jumps)};
				}
				break;
				
				/* {@squirreljme.error JC37 The operation at the specified
				address is not supported yet. (The operation; The name of
				the operation; The address it is at)} */
			default:
				throw new RuntimeException(String.format("JC37 %d %s %d",
					op, InstructionMnemonics.toString(op), __a));
		}
		
		// Set
		this._args = args;
		this._rawArgs = rawArgs;
		this.naturalflow = naturalflow;
		
		// Figure out normal jump targets
		Set<InstructionJumpTarget> normal = new LinkedHashSet<>();
		if (naturalflow)
			normal.add(new InstructionJumpTarget(__af));
		for (int i = 0, n = args.length; i < n; i++)
		{
			Object v = args[i];
			
			// Jump
			if (v instanceof InstructionJumpTarget)
				normal.add((InstructionJumpTarget)v);
			
			// A table
			else if (v instanceof IntMatchingJumpTable)
				for (InstructionJumpTarget j :
					((IntMatchingJumpTable)v).targets())
					normal.add(j);
		}
		
		// Determine exceptional jump targets
		Set<InstructionJumpTarget> exception = new LinkedHashSet<>();
		for (ExceptionHandler eh : __eh)
			if (eh.inRange(__a))
				exception.add(new InstructionJumpTarget(eh.handlerAddress()));
		
		// Set jump targets
		this.jumptargets = new InstructionJumpTargets(
			normal.<InstructionJumpTarget>toArray(
				new InstructionJumpTarget[normal.size()]),
			exception.<InstructionJumpTarget>toArray(
				new InstructionJumpTarget[exception.size()]));
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
	 * Returns the address following this one.
	 *
	 * @return The resultant address.
	 * @since 2024/01/30
	 */
	public int addressFollowing()
	{
		return this.addressFollowing;
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
	 * Returns all the arguments.
	 *
	 * @return The arguments.
	 * @since 2019/04/03
	 */
	public final Object[] arguments()
	{
		return this._args.clone();
	}
	
	/**
	 * Obtains the given argument as a byte.
	 *
	 * @param __i The argument to get.
	 * @return The value of the argument.
	 * @throws ClassCastException If the given argument is not an number.
	 * @throws IndexOutOfBoundsException If the index is not within the
	 * bounds of the instruction arguments.
	 * @since 2019/04/03
	 */
	public byte byteArgument(int __i)
		throws ClassCastException, IndexOutOfBoundsException
	{
		return this.<Number>argument(__i, Number.class).byteValue();
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
	 * Returns the jump targets for this instruction.
	 *
	 * @return The jump targets for this instruction.
	 * @since 2019/03/30
	 */
	public final InstructionJumpTargets jumpTargets()
	{
		return this.jumptargets;
	}
	
	/**
	 * Returns the length of this instruction.
	 *
	 * @return The instruction length.
	 * @since 2024/01/23
	 */
	public int length()
	{
		return this.length;
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
	 * Normalizes the instruction.
	 * 
	 * @return The normalized instruction.
	 * @since 2023/07/03
	 */
	public Instruction normalize()
	{
		Reference<Instruction> ref = this._normalized;
		Instruction rv;
		
		if (ref == null || (rv = ref.get()) == null)
		{
			// The logical address of this function
			int logAddr = this.logicalAddress;
			
			// Base normalization state
			int normalizeTo = -1;
			Object[] normalizeArgs = this._args;
			int[] normalizeRaw = this._rawArgs;
			
			// Normalization depends on the input operation
			int op = this.op;
			switch (op)
			{
					// Goto becomes wide
				case InstructionIndex.GOTO:
					normalizeTo = InstructionIndex.GOTO_W;
					break;
				
					// Become wide version of these, same arguments
				case InstructionIndex.ILOAD:
					normalizeTo = InstructionIndex.WIDE_ILOAD;
					break;
					
				case InstructionIndex.LLOAD:
					normalizeTo = InstructionIndex.WIDE_LLOAD;
					break;
				
				case InstructionIndex.FLOAD:
					normalizeTo = InstructionIndex.WIDE_FLOAD;
					break;
				
				case InstructionIndex.DLOAD:
					normalizeTo = InstructionIndex.WIDE_DLOAD;
					break;
				
				case InstructionIndex.ALOAD:
					normalizeTo = InstructionIndex.WIDE_ALOAD;
					break;
					
				case InstructionIndex.ISTORE:
					normalizeTo = InstructionIndex.WIDE_ISTORE;
					break;
				
				case InstructionIndex.LSTORE:
					normalizeTo = InstructionIndex.WIDE_LSTORE;
					break;
				
				case InstructionIndex.FSTORE:
					normalizeTo = InstructionIndex.WIDE_FSTORE;
					break;
				
				case InstructionIndex.DSTORE:
					normalizeTo = InstructionIndex.WIDE_DSTORE;
					break;
				
				case InstructionIndex.ASTORE:
					normalizeTo = InstructionIndex.WIDE_ASTORE;
					break;
				
					// Becomes wide increment
				case InstructionIndex.IINC:
					normalizeTo = InstructionIndex.WIDE_IINC;
					break;
					
					// Becomes wide LDC
				case InstructionIndex.LDC:
					normalizeTo = InstructionIndex.LDC_W;
					break;
					
					// LDC Integer
				case InstructionIndex.ICONST_M1:
				case InstructionIndex.ICONST_0:
				case InstructionIndex.ICONST_1:
				case InstructionIndex.ICONST_2:
				case InstructionIndex.ICONST_3:
				case InstructionIndex.ICONST_4:
				case InstructionIndex.ICONST_5:
					normalizeTo = InstructionIndex.LDC_W;
					normalizeArgs = new Object[]{
						new ConstantValueInteger(
							-1 + (op - InstructionIndex.ICONST_M1))
					};
					normalizeRaw = new int[]{
						Instruction.__specialRaw(Pool.TAG_INTEGER, logAddr)
					};
					break;
					
					// Becomes LDC integer
				case InstructionIndex.BIPUSH:
				case InstructionIndex.SIPUSH:
					normalizeTo = InstructionIndex.LDC_W;
					normalizeArgs = new Object[]{
						new ConstantValueInteger((Integer)normalizeArgs[0])
					};
					normalizeRaw = new int[]{
						Instruction.__specialRaw(Pool.TAG_INTEGER, logAddr)
					};
					break;
					
					// Load long
				case InstructionIndex.LCONST_0:
				case InstructionIndex.LCONST_1:
					normalizeTo = InstructionIndex.LDC2_W;
					normalizeArgs = new Object[]{
						new ConstantValueLong(
							(op - InstructionIndex.LCONST_0))
					};
					normalizeRaw = new int[]{
						Instruction.__specialRaw(Pool.TAG_LONG, logAddr)
					};
					break;
					
					// Load float
				case InstructionIndex.FCONST_0:
				case InstructionIndex.FCONST_1:
				case InstructionIndex.FCONST_2:
					normalizeTo = InstructionIndex.LDC_W;
					normalizeArgs = new Object[]{
						new ConstantValueFloat(
							(op - InstructionIndex.FCONST_0))
					};
					normalizeRaw = new int[]{
						Instruction.__specialRaw(Pool.TAG_FLOAT, logAddr)
					};
					break;
					
					// Load double
				case InstructionIndex.DCONST_0:
				case InstructionIndex.DCONST_1:
					normalizeTo = InstructionIndex.LDC2_W;
					normalizeArgs = new Object[]{
						new ConstantValueDouble(
							(op - InstructionIndex.DCONST_0))
					};
					normalizeRaw = new int[]{
						Instruction.__specialRaw(Pool.TAG_DOUBLE, logAddr)
					};
					break;
					
					// Load local integer
				case InstructionIndex.ILOAD_0:
				case InstructionIndex.ILOAD_1:
				case InstructionIndex.ILOAD_2:
				case InstructionIndex.ILOAD_3:
					normalizeTo = InstructionIndex.WIDE_ILOAD;
					normalizeArgs = new Object[]{
						op - InstructionIndex.ILOAD_0
					};
					normalizeRaw = new int[]{
						op - InstructionIndex.ILOAD_0
					};
					break;
					
				case InstructionIndex.LLOAD_0:
				case InstructionIndex.LLOAD_1:
				case InstructionIndex.LLOAD_2:
				case InstructionIndex.LLOAD_3:
					normalizeTo = InstructionIndex.WIDE_LLOAD;
					normalizeArgs = new Object[]{
						op - InstructionIndex.LLOAD_0
					};
					normalizeRaw = new int[]{
						op - InstructionIndex.LLOAD_0
					};
					break;
					
				case InstructionIndex.FLOAD_0:
				case InstructionIndex.FLOAD_1:
				case InstructionIndex.FLOAD_2:
				case InstructionIndex.FLOAD_3:
					normalizeTo = InstructionIndex.WIDE_FLOAD;
					normalizeArgs = new Object[]{
						op - InstructionIndex.FLOAD_0
					};
					normalizeRaw = new int[]{
						op - InstructionIndex.FLOAD_0
					};
					break;
					
				case InstructionIndex.DLOAD_0:
				case InstructionIndex.DLOAD_1:
				case InstructionIndex.DLOAD_2:
				case InstructionIndex.DLOAD_3:
					normalizeTo = InstructionIndex.WIDE_DLOAD;
					normalizeArgs = new Object[]{
						op - InstructionIndex.DLOAD_0
					};
					normalizeRaw = new int[]{
						op - InstructionIndex.DLOAD_0
					};
					break;
					
				case InstructionIndex.ALOAD_0:
				case InstructionIndex.ALOAD_1:
				case InstructionIndex.ALOAD_2:
				case InstructionIndex.ALOAD_3:
					normalizeTo = InstructionIndex.WIDE_ALOAD;
					normalizeArgs = new Object[]{
						op - InstructionIndex.ALOAD_0
					};
					normalizeRaw = new int[]{
						op - InstructionIndex.ALOAD_0
					};
					break;
					
				case InstructionIndex.ISTORE_0:
				case InstructionIndex.ISTORE_1:
				case InstructionIndex.ISTORE_2:
				case InstructionIndex.ISTORE_3:
					normalizeTo = InstructionIndex.WIDE_ISTORE;
					normalizeArgs = new Object[]{
						op - InstructionIndex.ISTORE_0
					};
					normalizeRaw = new int[]{
						op - InstructionIndex.ISTORE_0
					};
					break;
					
				case InstructionIndex.LSTORE_0:
				case InstructionIndex.LSTORE_1:
				case InstructionIndex.LSTORE_2:
				case InstructionIndex.LSTORE_3:
					normalizeTo = InstructionIndex.WIDE_LSTORE;
					normalizeArgs = new Object[]{
						op - InstructionIndex.LSTORE_0
					};
					normalizeRaw = new int[]{
						op - InstructionIndex.LSTORE_0
					};
					break;
					
				case InstructionIndex.FSTORE_0:
				case InstructionIndex.FSTORE_1:
				case InstructionIndex.FSTORE_2:
				case InstructionIndex.FSTORE_3:
					normalizeTo = InstructionIndex.WIDE_FSTORE;
					normalizeArgs = new Object[]{
						op - InstructionIndex.FSTORE_0
					};
					normalizeRaw = new int[]{
						op - InstructionIndex.FSTORE_0
					};
					break;
					
				case InstructionIndex.DSTORE_0:
				case InstructionIndex.DSTORE_1:
				case InstructionIndex.DSTORE_2:
				case InstructionIndex.DSTORE_3:
					normalizeTo = InstructionIndex.WIDE_DSTORE;
					normalizeArgs = new Object[]{
						op - InstructionIndex.DSTORE_0
					};
					normalizeRaw = new int[]{
						op - InstructionIndex.DSTORE_0
					};
					break;
					
				case InstructionIndex.ASTORE_0:
				case InstructionIndex.ASTORE_1:
				case InstructionIndex.ASTORE_2:
				case InstructionIndex.ASTORE_3:
					normalizeTo = InstructionIndex.WIDE_ASTORE;
					normalizeArgs = new Object[]{
						op - InstructionIndex.ASTORE_0
					};
					normalizeRaw = new int[]{
						op - InstructionIndex.ASTORE_0
					};
					break;
			}
			
			// Was this instruction normalized?
			if (normalizeTo >= 0)
				rv = new Instruction(this.address, normalizeTo,
					this.naturalflow, this.smtstate, this.jumptargets,
					normalizeArgs, normalizeRaw, this.logicalAddress,
					this.length, this.addressFollowing);
			else
				rv = this;
			this._normalized = new WeakReference<>(rv);
		}
		
		return rv;
		
	}
	
	/**
	 * Returns the operation that this performs.
	 *
	 * @return The operation, one of {@link InstructionIndex}.
	 * @since 2017/05/20
	 */
	public int operation()
	{
		return this.op;
	}
	
	/**
	 * Returns the raw arguments for this function, which is just the number
	 * values.
	 *
	 * @return The raw instruction arguments.
	 * @since 2023/08/09
	 */
	public int[] rawArguments()
	{
		return this._rawArgs.clone();
	}
	
	/**
	 * Obtains the given argument as a short.
	 *
	 * @param __i The argument to get.
	 * @return The value of the argument.
	 * @throws ClassCastException If the given argument is not an number.
	 * @throws IndexOutOfBoundsException If the index is not within the
	 * bounds of the instruction arguments.
	 * @since 2019/04/03
	 */
	public final short shortArgument(int __i)
		throws ClassCastException, IndexOutOfBoundsException
	{
		return this.<Number>argument(__i, Number.class).shortValue();
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
			
			// Operation length
			sb.append("(l");
			sb.append(this.length);
			sb.append(')');
			
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
			
			// Address following
			sb.append("->[@");
			sb.append(this.addressFollowing);
			sb.append("]");
			
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
	static int __readByte(byte[] __a, int __o)
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
	static int __readInt(byte[] __a, int __o)
		throws InvalidClassFormatException, NullPointerException
	{
		// Check
		if (__a == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error JC38 Illegal int read off the end of the
		instruction array. (The offset; The length of the code array)} */
		if (__o < 0 || __o + 3 >= __a.length)
			throw new InvalidClassFormatException(
				String.format("JC38 %d %d", __o, __a.length));
		
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
	static int __readShort(byte[] __a, int __o)
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
	static int __readUnsignedByte(byte[] __a, int __o)
		throws InvalidClassFormatException, NullPointerException
	{
		// Check
		if (__a == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error JC39 Illegal byte read off the end of the
		instruction array. (The offset; The length of the code array)} */
		if (__o < 0 || __o >= __a.length)
			throw new InvalidClassFormatException(
				String.format("JC39 %d %d", __o, __a.length));
		
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
	static int __readUnsignedShort(byte[] __a, int __o)
		throws InvalidClassFormatException, NullPointerException
	{
		// Check
		if (__a == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error JC3a Illegal short read off the end of the
		instruction array. (The offset; The length of the code array)} */
		if (__o < 0 || __o + 1 >= __a.length)
			throw new InvalidClassFormatException(
				String.format("JC3a %d %d", __o, __a.length));
		
		return ((__a[__o] & 0xFF) << 8) | (__a[__o + 1] & 0xFF);
	}
	
	/**
	 * Returns a special raw tag.
	 *
	 * @param __tag The tag to use.
	 * @param __logAddr The logical address.
	 * @return The special tag.
	 * @since 2023/08/09
	 */
	private static int __specialRaw(int __tag, int __logAddr)
	{
		return 0x8000_0000 | ((__tag & 0xF) << 23) | __logAddr;
	}
}

