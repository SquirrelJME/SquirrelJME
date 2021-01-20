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
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Collection;

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
	
	/** Hash. */
	private int _hash;
	
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
	 * Returns the argument format for the instruction.
	 *
	 * @return The argument format.
	 * @since 2018/04/16
	 */
	public final ArgumentFormat[] argumentFormat()
	{
		return NativeInstruction.argumentFormat(this.op);
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
	 * {@inheritDoc}
	 * @since 2019/04/11
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		if (!(__o instanceof NativeInstruction))
			return false;
		
		NativeInstruction o = (NativeInstruction)__o;
		return this.hashCode() == o.hashCode() &&
			this.op == o.op &&
			Arrays.equals(this._args, o._args);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/11
	 */
	@Override
	public final int hashCode()
	{
		int rv = this._hash;
		if (rv == 0)
			this._hash = (rv = (this.op ^
				Arrays.asList(this._args).hashCode()));
		return rv;
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
	 * @throws InvalidInstructionException If the encoding is not known.
	 * @since 2019/03/27
	 */
	public static int argumentCount(int __op)
		throws InvalidInstructionException
	{
		// Depends on the encoding
		switch (NativeInstruction.encoding(__op))
		{
			case NativeInstructionType.BREAKPOINT:
			case NativeInstructionType.DEBUG_EXIT:
			case NativeInstructionType.RETURN:
				return 0;
				
			case NativeInstructionType.MEM_HANDLE_COUNT_UP:
				return 1;
			
			case NativeInstructionType.ATOMIC_INT_INCREMENT:
			case NativeInstructionType.COPY:
			case NativeInstructionType.INVOKE:
			case NativeInstructionType.LOAD_POOL:
			case NativeInstructionType.STORE_POOL:
			case NativeInstructionType.SYSTEM_CALL:
			case NativeInstructionType.INVOKE_POINTER_ONLY:
			case NativeInstructionType.MEM_HANDLE_COUNT_DOWN:
				return 2;
					
			case NativeInstructionType.ATOMIC_INT_DECREMENT_AND_GET:
			case NativeInstructionType.DEBUG_POINT:
			case NativeInstructionType.IF_ICMP:
			case NativeInstructionType.IFEQ_CONST:
			case NativeInstructionType.LOAD_FROM_INTARRAY:
			case NativeInstructionType.MATH_REG_INT:
			case NativeInstructionType.MATH_CONST_INT:
			case NativeInstructionType.MEMORY_OFF_REG:
			case NativeInstructionType.MEMORY_OFF_REG_JAVA:
			case NativeInstructionType.MEMORY_OFF_ICONST:
			case NativeInstructionType.MEMORY_OFF_ICONST_JAVA:
			case NativeInstructionType.STORE_TO_INTARRAY:
			case NativeInstructionType.INTERFACE_I_FOR_OBJECT:
			case NativeInstructionType.INTERFACE_VT_DX_LOOKUP:
			case NativeInstructionType.INVOKE_POINTER_AND_POOL:
				return 3;
				
			case NativeInstructionType.MEM_HANDLE_OFF_REG:
			case NativeInstructionType.MEM_HANDLE_OFF_ICONST:
				if (DataType.of(__op & DataType.MASK).isWide())
					return 4;
				return 3;
				
			case NativeInstructionType.DEBUG_ENTRY:
			case NativeInstructionType.INTERFACE_VT_LOAD:
				return 4;
			
			case NativeInstructionType.ATOMIC_COMPARE_GET_AND_SET:
				return 5;
				
				// {@squirreljme.error JC10 Unknown instruction argument
				// count.}
			default:
				throw new InvalidInstructionException("JC10 " + __op);
		}
	}
	
	
	/**
	 * Returns the argument format for the instruction.
	 *
	 * @param __op The operation to get the encoding of.
	 * @return The argument format.
	 * @since 2018/04/16
	 */
	public static ArgumentFormat[] argumentFormat(int __op)
	{
		switch (NativeInstruction.encoding(__op))
		{
				// []
			case NativeInstructionType.BREAKPOINT:
			case NativeInstructionType.DEBUG_EXIT:
			case NativeInstructionType.RETURN:
				return ArgumentFormat.of();
				
				// [r16]
			case NativeInstructionType.MEM_HANDLE_COUNT_UP:
				return ArgumentFormat.of(
					ArgumentFormat.VUREG);
				
				// [r16, reglist]
			case NativeInstructionType.SYSTEM_CALL:
			case NativeInstructionType.INVOKE:
			case NativeInstructionType.INVOKE_POINTER_ONLY:
				return ArgumentFormat.of(
					ArgumentFormat.VUREG,
					ArgumentFormat.REGLIST);
				
				// [r16, r16]
			case NativeInstructionType.COPY:
			case NativeInstructionType.MEM_HANDLE_COUNT_DOWN:
				return ArgumentFormat.of(
					ArgumentFormat.VUREG,
					ArgumentFormat.VUREG);
				
				// [r16, u16]
			case NativeInstructionType.ATOMIC_INT_INCREMENT:
				return ArgumentFormat.of(
					ArgumentFormat.VUREG,
					ArgumentFormat.VUINT);
				
				// [p16, r16]
			case NativeInstructionType.LOAD_POOL:
			case NativeInstructionType.STORE_POOL:
				return ArgumentFormat.of(
					ArgumentFormat.VPOOL,
					ArgumentFormat.VUREG);
					
				// [r16, r16, reglist]
			case NativeInstructionType.INVOKE_POINTER_AND_POOL:
				return ArgumentFormat.of(
					ArgumentFormat.VUREG,
					ArgumentFormat.VUREG,
					ArgumentFormat.REGLIST);
					
				// [p16, r16, r16]
			case NativeInstructionType.INTERFACE_I_FOR_OBJECT:
			case NativeInstructionType.INTERFACE_VT_DX_LOOKUP:
				return ArgumentFormat.of(
					ArgumentFormat.VPOOL,
					ArgumentFormat.VUREG,
					ArgumentFormat.VUREG);
					
				// [p16, p16, p16, p16]
			case NativeInstructionType.DEBUG_ENTRY:
				return ArgumentFormat.of(
					ArgumentFormat.VPOOL,
					ArgumentFormat.VPOOL,
					ArgumentFormat.VPOOL,
					ArgumentFormat.VPOOL);
				
				// [u16, u16, u16]
			case NativeInstructionType.DEBUG_POINT:
				return ArgumentFormat.of(
					ArgumentFormat.VUINT,
					ArgumentFormat.VUINT,
					ArgumentFormat.VUINT);
				
				// [r16, r16, u16]
			case NativeInstructionType.ATOMIC_INT_DECREMENT_AND_GET:
				return ArgumentFormat.of(
					ArgumentFormat.VUREG,
					ArgumentFormat.VUREG,
					ArgumentFormat.VUINT);
				
				// [r16, r16, r16]
			case NativeInstructionType.LOAD_FROM_INTARRAY:
			case NativeInstructionType.MATH_REG_INT:
			case NativeInstructionType.MEMORY_OFF_REG:
			case NativeInstructionType.MEMORY_OFF_REG_JAVA:
			case NativeInstructionType.STORE_TO_INTARRAY:
				return ArgumentFormat.of(
					ArgumentFormat.VUREG,
					ArgumentFormat.VUREG,
					ArgumentFormat.VUREG);
					
				// [r16 [+r16], r16, r16]
			case NativeInstructionType.MEM_HANDLE_OFF_REG:
				if (DataType.of(__op & DataType.MASK).isWide())
					return ArgumentFormat.of(
						ArgumentFormat.VUREG,
						ArgumentFormat.VUREG,
						ArgumentFormat.VUREG,
						ArgumentFormat.VUREG);
				return ArgumentFormat.of(
					ArgumentFormat.VUREG,
					ArgumentFormat.VUREG,
					ArgumentFormat.VUREG);
			
				// [r16, i32, r16]
			case NativeInstructionType.MATH_CONST_INT:
				return ArgumentFormat.of(
					ArgumentFormat.VUREG,
					ArgumentFormat.INT32,
					ArgumentFormat.VUREG);
				
				// [r16, r16, i32]
			case NativeInstructionType.MEMORY_OFF_ICONST:
			case NativeInstructionType.MEMORY_OFF_ICONST_JAVA:
				return ArgumentFormat.of(
					ArgumentFormat.VUREG,
					ArgumentFormat.VUREG,
					ArgumentFormat.INT32);
				
				// [r16 [+r16], r16, i32]
			case NativeInstructionType.MEM_HANDLE_OFF_ICONST:
				if (DataType.of(__op & DataType.MASK).isWide())
					return ArgumentFormat.of(
						ArgumentFormat.VUREG,
						ArgumentFormat.VUREG,
						ArgumentFormat.VUREG,
						ArgumentFormat.INT32);
				return ArgumentFormat.of(
					ArgumentFormat.VUREG,
					ArgumentFormat.VUREG,
					ArgumentFormat.INT32);
				
				// [r16, r16, j16]
			case NativeInstructionType.IF_ICMP:
				return ArgumentFormat.of(
					ArgumentFormat.VUREG,
					ArgumentFormat.VUREG,
					ArgumentFormat.VJUMP);
			
				// [r16, i32, j16]
			case NativeInstructionType.IFEQ_CONST:
				return ArgumentFormat.of(
					ArgumentFormat.VUREG,
					ArgumentFormat.INT32,
					ArgumentFormat.VJUMP);
			
				// [r16, r16, r16, r16]
			case NativeInstructionType.INTERFACE_VT_LOAD:
				return ArgumentFormat.of(
					ArgumentFormat.VUREG,
					ArgumentFormat.VUREG,
					ArgumentFormat.VUREG,
					ArgumentFormat.VUREG);
			
			// [r16 (check), r16 (get), r16 (set), r16 (addr), u16 (off)]
			case NativeInstructionType.ATOMIC_COMPARE_GET_AND_SET:
				return ArgumentFormat.of(
					ArgumentFormat.VUREG,
					ArgumentFormat.VUREG,
					ArgumentFormat.VUREG,
					ArgumentFormat.VUREG,
					ArgumentFormat.VUINT);
		}
		
		// {@squirreljme.error JC11 Invalid operation. (The operation)}
		throw new InvalidInstructionException("JC11 " +
			NativeInstruction.mnemonic(__op));
	}
	
	/**
	 * Returns the encoding of the given instruction.
	 *
	 * @param __op The operation to get the encoding of.
	 * @return The encoding for the given operation.
	 * @since 2019/03/24
	 */
	public static int encoding(int __op)
	{
		// Special operations all use unique encodings so just return their
		// opcode, while all of the other operations use one of the pre-defined
		// encodings.
		int upper = __op & 0xF0;
		if (upper == NativeInstructionType.SPECIAL_A ||
			upper == NativeInstructionType.SPECIAL_B)
			return __op;
		
		// Plain
		return upper;
	}
	
	/**
	 * Returns the mnemonic of the given operation.
	 *
	 * @param __op The operation to get.
	 * @return The mnemonic for the operation.
	 * @since 2019/04/07
	 */
	public static String mnemonic(int __op)
	{
		switch (NativeInstruction.encoding(__op))
		{
			case NativeInstructionType.MATH_REG_INT:
			case NativeInstructionType.MATH_CONST_INT:
				return StackJavaType.of((__op >> 4) & 0x3).name() +
					"_" +
					MathType.of(__op & 0x0F).name() +
					"_" +
					(((__op & 0x80) != 0) ? "CONST" : "REG");
			
			case NativeInstructionType.IF_ICMP:
				{
					CompareType ct = CompareType.of(__op & 0x07);
					if (ct == CompareType.TRUE)
						return "GOTO";
					else if (ct == CompareType.FALSE)
						return "NOP";
					else
						return "IF_ICMP_" + ct.name();
				}
				
			case NativeInstructionType.MEMORY_OFF_REG:
			case NativeInstructionType.MEMORY_OFF_ICONST:
				return "MEM_" +
					(((__op & 0x08) != 0) ? "LOAD" : "STORE") +
					"_" +
					DataType.of(__op & 0x07).name() +
					"_" +
					(((__op & 0x80) != 0) ? "ICONST" : "REG");
			
			case NativeInstructionType.MEMORY_OFF_REG_JAVA:
			case NativeInstructionType.MEMORY_OFF_ICONST_JAVA:
				return "MEM_" +
					(((__op & 0x08) != 0) ? "LOAD" : "STORE") +
					"_" +
					DataType.of(__op & 0x07).name() +
					"_" +
					(((__op & 0x80) != 0) ? "ICONST" : "REG") +
					"_JAVA";
			
			case NativeInstructionType.MEM_HANDLE_OFF_REG:
			case NativeInstructionType.MEM_HANDLE_OFF_ICONST:
				return "MEM_HANDLE_OFF_" +
					(((__op & 0x08) != 0) ? "LOAD" : "STORE") +
					"_" +
					DataType.of(__op & 0x07).name() +
					"_" +
					(((__op & 0x80) != 0) ? "ICONST" : "REG") +
					"_JAVA";
				
			case NativeInstructionType.ATOMIC_INT_DECREMENT_AND_GET:
				return "ATOMIC_INT_DECREMENT_AND_GET";
			case NativeInstructionType.ATOMIC_INT_INCREMENT:
				return "ATOMIC_INT_INCREMENT";
			case NativeInstructionType.BREAKPOINT:		return "BREAKPOINT";
			case NativeInstructionType.COPY:			return "COPY";
			case NativeInstructionType.DEBUG_ENTRY:		return "DEBUG_ENTRY";
			case NativeInstructionType.DEBUG_EXIT:		return "DEBUG_EXIT";
			case NativeInstructionType.DEBUG_POINT:		return "DEBUG_POINT";
			case NativeInstructionType.IFEQ_CONST:		return "IFEQ_CONST";
			case NativeInstructionType.INVOKE:			return "INVOKE";
			case NativeInstructionType.LOAD_POOL:		return "LOAD_POOL";
			case NativeInstructionType.LOAD_FROM_INTARRAY:
				return "LOAD_FROM_INTARRAY";
			case NativeInstructionType.RETURN:			return "RETURN";
			case NativeInstructionType.STORE_POOL:		return "STORE_POOL";
			case NativeInstructionType.STORE_TO_INTARRAY:
				return "STORE_TO_INTARRAY";
			case NativeInstructionType.SYSTEM_CALL:		return "SYSTEM_CALL";
		
			case NativeInstructionType.INTERFACE_I_FOR_OBJECT:
				return "INTERFACE_I_FOR_OBJECT";
			
			case NativeInstructionType.INTERFACE_VT_DX_LOOKUP:
				return "INTERFACE_VT_DX_LOOKUP";
			
			case NativeInstructionType.INTERFACE_VT_LOAD:
				return "INTERFACE_VT_LOAD";
			
			case NativeInstructionType.INVOKE_POINTER_ONLY:
				return "INVOKE_POINTER_ONLY";
			
			case NativeInstructionType.INVOKE_POINTER_AND_POOL:
				return "INVOKE_POINTER_AND_POOL";
			
			case NativeInstructionType.MEM_HANDLE_COUNT_DOWN:
				return "MEM_HANDLE_COUNT_DOWN";
				
			case NativeInstructionType.MEM_HANDLE_COUNT_UP:
				return "MEM_HANDLE_COUNT_UP";
			
			default:
				return String.format("UNKNOWN_%02x", __op);
		}
	}
}

