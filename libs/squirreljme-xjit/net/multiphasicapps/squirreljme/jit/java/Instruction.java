// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.java;

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

