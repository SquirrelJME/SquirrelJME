// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile.register;

import java.util.Arrays;
import java.util.Collection;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This represents a single instruction.
 *
 * @since 2019/03/22
 */
public final class RegisterInstruction
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
	public RegisterInstruction(int __op, Object... __args)
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
	public RegisterInstruction(int __op, Collection<Object> __args)
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
	public int argumentCount()
	{
		return this._args.length;
	}
	
	/**
	 * Returns the encoding of this instruction.
	 *
	 * @return The instruction encoding.
	 * @since 2019/03/27
	 */
	public final int encoding()
	{
		return RegisterInstruction.encoding(this.op);
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
				RegisterOperationMnemonics.toString(this.op) + ":" +
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
		switch (RegisterInstruction.encoding(__op))
		{
			case RegisterOperationType.NOP:
			case RegisterOperationType.RETURN:
			case RegisterOperationType.REF_CLEAR:
				return 0;
			
			case RegisterOperationType.ENCODING_U16:
			case RegisterOperationType.ENCODING_U16_2:
			case RegisterOperationType.ENCODING_J16:
			case RegisterOperationType.REF_ENQUEUE:
				return 1;
				
			case RegisterOperationType.X32_CONST:
			case RegisterOperationType.X64_CONST:
			case RegisterOperationType.INVOKE_METHOD:
			case RegisterOperationType.ENCODING_U16_J16:
			case RegisterOperationType.ENCODING_POOL16_U16:
			case RegisterOperationType.ENCODING_U16_U16:
			case RegisterOperationType.ENCODING_U16_U16_2:
				return 2;
				
			case RegisterOperationType.LOOKUPSWITCH:
			case RegisterOperationType.JUMP_IF_INSTANCE:
			case RegisterOperationType.ENCODING_U16_U16_J16:
			case RegisterOperationType.ENCODING_POOL16_U16_U16:
			case RegisterOperationType.ENCODING_U16_U16_U16:
			case RegisterOperationType.ENCODING_U16_U16_U16_2:
			case RegisterOperationType.ENCODING_U16_U16_U16_3:
				return 3;
				
			case RegisterOperationType.JUMP_IF_INSTANCE_GET_EXCEPTION:
				return 4;
				
			case RegisterOperationType.TABLESWITCH:
				return 6;
			
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
		if (upper != RegisterOperationType.ENCODING_SPECIAL)
			return upper;
		return __op;
	}
}

