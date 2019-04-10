// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.nncc;

import dev.shadowtail.classfile.xlate.CompareType;
import dev.shadowtail.classfile.xlate.DataType;
import dev.shadowtail.classfile.xlate.MathType;
import dev.shadowtail.classfile.xlate.StackJavaType;
import java.util.Arrays;
import java.util.Collection;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This represents a single instruction.
 *
 * @since 2019/03/22
 */
public final class NativeInstruction
{
	/** The operation. */
	protected final int op;
	
	/** The arguments. */
	final Object[] _args;
	
	/** String form. */
	private Reference<String> _string;
	
	/**
	 * Initializes the temporary instruction.
	 *
	 * @param __op The operation.
	 * @param __args The arguments.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/22
	 */
	public NativeInstruction(int __op, Object... __args)
		throws NullPointerException
	{
		this.op = __op;
		this._args = (__args =
			(__args == null ? new Object[0] : __args.clone()));
		
		for (Object o : __args)
			if (o == null)
				throw new NullPointerException("NARG");
	}
	
	/**
	 * Initializes the temporary instruction.
	 *
	 * @param __op The operation.
	 * @param __args The arguments.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/22
	 */
	public NativeInstruction(int __op, Collection<Object> __args)
		throws NullPointerException
	{
		if (__args == null)
			throw new NullPointerException("NARG");
		
		this.op = __op;
		
		Object[] args = __args.<Object>toArray(new Object[__args.size()]);
		this._args = args;
		for (Object o : args)
			if (o == null)
				throw new NullPointerException("NARG");
	}
	
	/**
	 * Returns the argument for the given index.
	 *
	 * @param __i The index of the argument.
	 * @return The argument.
	 * @throws IndexOutOfBoundsException If the argument index is not
	 * within bounds.
	 * @since 2019/03/24
	 */
	public Object argument(int __i)
		throws IndexOutOfBoundsException
	{
		return this._args[__i];
	}
	
	/**
	 * Returns the argument for the given index.
	 *
	 * @param <T> The type of argument to get.
	 * @param __i The index of the argument.
	 * @param __cl The class to cast to.
	 * @return The argument as the given class.
	 * @throws ClassCastException If the class is not of the given type.
	 * @throws IndexOutOfBoundsException If the argument index is not
	 * within bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/23
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
	 * @since 2019/03/23
	 */
	public final int argumentCount()
	{
		return this._args.length;
	}
	
	/**
	 * Returns all of the arguments.
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
	 * Returns the encoding of this instruction.
	 *
	 * @return The instruction encoding.
	 * @since 2019/03/27
	 */
	public final int encoding()
	{
		return NativeInstruction.encoding(this.op);
	}
	
	/**
	 * Obtains the given argument as an integer.
	 *
	 * @param __i The argument to get.
	 * @return The value of the argument.
	 * @throws ClassCastException If the given argument is not an number.
	 * @throws IndexOutOfBoundsException If the index is not within the
	 * bounds of the instruction arguments.
	 * @since 2018/03/23
	 */
	public int intArgument(int __i)
		throws ClassCastException, IndexOutOfBoundsException
	{
		return this.<Number>argument(__i, Number.class).intValue();
	}
	
	/**
	 * Returns the operation this instruction performs.
	 *
	 * @return The operation performed.
	 * @since 2019/03/23
	 */
	public final int operation()
	{
		return this.op;
	}
	
	/**
	 * Obtains the given argument as a short.
	 *
	 * @param __i The argument to get.
	 * @return The value of the argument.
	 * @throws ClassCastException If the given argument is not a number.
	 * @throws IndexOutOfBoundsException If the index is not within the
	 * bounds of the instruction arguments.
	 * @since 2018/03/23
	 */
	public short shortArgument(int __i)
		throws ClassCastException, IndexOutOfBoundsException
	{
		return this.<Number>argument(__i, Number.class).shortValue();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/22
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv =
				NativeInstruction.mnemonic(this.op) + ":" +
				Arrays.asList(this._args)));
		
		return rv;
	}
	
	/**
	 * Returns the argument count of the operation.
	 *
	 * @param __op The operation to get the argument count of.
	 * @return The number of used arguments.
	 * @throws IllegalArgumentException If the encoding is not known.
	 * @since 2019/03/27
	 */
	public static final int argumentCount(int __op)
		throws IllegalArgumentException
	{
		// Depends on the encoding
		switch (NativeInstruction.encoding(__op))
		{
			case NativeInstructionType.REF_CLEAR:
			case NativeInstructionType.REF_RESET:
			case NativeInstructionType.RETURN:
				return 0;
			
			case NativeInstructionType.COUNT:
			case NativeInstructionType.INVOKE:
			case NativeInstructionType.LOAD_POOL:
			case NativeInstructionType.REF_PUSH:
			case NativeInstructionType.MONITORENTER:
			case NativeInstructionType.MONITOREXIT:
			case NativeInstructionType.UNCOUNT:
				return 1;
				
			case NativeInstructionType.ARRAYLEN:
			case NativeInstructionType.CONVERSION:
			case NativeInstructionType.LOOKUPSWITCH:
			case NativeInstructionType.NEW:
			case NativeInstructionType.TABLESWITCH:
				return 2;
			
			case NativeInstructionType.ARRAY_ACCESS:
			case NativeInstructionType.IF_ICMP:
			case NativeInstructionType.MATH_REG_INT:
			case NativeInstructionType.MATH_REG_LONG:
			case NativeInstructionType.MATH_REG_FLOAT:
			case NativeInstructionType.MATH_REG_DOUBLE:
			case NativeInstructionType.MATH_CONST_INT:
			case NativeInstructionType.MATH_CONST_LONG:
			case NativeInstructionType.MATH_CONST_FLOAT:
			case NativeInstructionType.MATH_CONST_DOUBLE:
			case NativeInstructionType.MEMORY_OFF_REG:
			case NativeInstructionType.MEMORY_OFF_ICONST:
			case NativeInstructionType.NEWARRAY:
				return 3;
				
				// {@squirreljme.error JC2r Unknown instruction argument
				// count.}
			default:
				throw new IllegalArgumentException("JC2r " + __op);
		}
	}
	
	/**
	 * Returns the encoding of the given instruction.
	 *
	 * @param __op The operation to get the encoding of.
	 * @return The encoding for the given operation.
	 * @since 2019/03/24
	 */
	public static final int encoding(int __op)
	{
		// Special operations all use unique encodings so just return their
		// opcode, while all of the other operations use one of the pre-defined
		// encodings.
		int upper = __op & 0xF0;
		if (upper == NativeInstructionType.SPECIAL)
			return __op;
		return upper;
	}
	
	/**
	 * Returns the mnemonic of the given operation.
	 *
	 * @param __op The operation to get.
	 * @return The mnemonic for the operation.
	 * @since 2019/04/07
	 */
	public static final String mnemonic(int __op)
	{
		switch (NativeInstruction.encoding(__op))
		{
			case NativeInstructionType.MATH_REG_INT:
			case NativeInstructionType.MATH_REG_LONG:
			case NativeInstructionType.MATH_REG_FLOAT:
			case NativeInstructionType.MATH_REG_DOUBLE:
			case NativeInstructionType.MATH_CONST_INT:
			case NativeInstructionType.MATH_CONST_LONG:
			case NativeInstructionType.MATH_CONST_FLOAT:
			case NativeInstructionType.MATH_CONST_DOUBLE:
				return StackJavaType.of((__op >> 4) & 0x3).name() +
					"_" +
					MathType.of(__op & 0x0F).name() +
					"_" +
					(((__op & 0x80) != 0) ? "CONST" : "REG");
			
			case NativeInstructionType.IF_ICMP:
				{
					boolean refclear = ((__op & 0x08) != 0);
					
					CompareType ct = CompareType.of(__op & 0x07);
					if (ct == CompareType.TRUE)
						return "GOTO" + (refclear ? "_REF_CLEAR" : "");
					else if (ct == CompareType.FALSE)
						return "NOP";
					
					return "IF_ICMP_" +
						ct.name() +
						(refclear ? "_REF_CLEAR" : "");
				}
				
			case NativeInstructionType.MEMORY_OFF_REG:
			case NativeInstructionType.MEMORY_OFF_ICONST:
				return "MEM_" +
					(((__op & 0x08) != 0) ? "LOAD" : "STORE") +
					"_" +
					DataType.of(__op & 0x07).name() +
					"_" +
					(((__op & 0x80) != 0) ? "ICONST" : "REG");
				
			case NativeInstructionType.ARRAY_ACCESS:
				return "ARRAY_" +
					(((__op & 0x08) != 0) ? "LOAD" : "STORE") +
					"_" +
					DataType.of(__op & 0x07).name();
			
			case NativeInstructionType.CONVERSION:
				{
					StackJavaType a = StackJavaType.of((__op >> 2) & 0x3),
						b = StackJavaType.of(__op & 0x03);
					if (a == b)
						return "COPY_" + a.name();
					
					return "CONV_" + a.name() + "_TO_" + b.name();
				}

			case NativeInstructionType.NEWARRAY:		return "NEWARRAY";
			case NativeInstructionType.ARRAYLEN:		return "ARRAYLEN";
			case NativeInstructionType.RETURN:			return "RETURN";
			case NativeInstructionType.LOOKUPSWITCH:	return "LOOKUPSWITCH";
			case NativeInstructionType.TABLESWITCH:		return "TABLESWITCH";
			case NativeInstructionType.MONITORENTER:	return "MONITORENTER";
			case NativeInstructionType.INVOKE:			return "INVOKE";
			case NativeInstructionType.REF_PUSH:		return "REF_PUSH";
			case NativeInstructionType.REF_CLEAR:		return "REF_CLEAR";
			case NativeInstructionType.REF_RESET:		return "REF_RESET";
			case NativeInstructionType.COUNT:			return "COUNT";
			case NativeInstructionType.UNCOUNT:			return "UNCOUNT";
			case NativeInstructionType.BREAKPOINT:		return "BREAKPOINT";
			case NativeInstructionType.MONITOREXIT:		return "MONITOREXIT";
			case NativeInstructionType.LOAD_POOL:		return "LOAD_POOL";
			
			default:
				return String.format("UNKNOWN_%02x", __op);
		}
	}
}

