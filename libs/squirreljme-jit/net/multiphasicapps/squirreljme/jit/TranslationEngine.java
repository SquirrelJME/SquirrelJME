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

import java.util.Arrays;
import java.util.List;
import net.multiphasicapps.squirreljme.classformat.StackMapType;
import net.multiphasicapps.squirreljme.linkage.MethodLinkage;

/**
 * This is the base class for the JIT translation engine and is used to
 * translate class files to {@link ExecutableClass}es which may be executed
 * directly by the system, cached for later, or act as part of the kernel build
 * step.
 *
 * @since 2017/01/30
 */
public abstract class TranslationEngine
{
	/** The configuration used. */
	protected final JITConfig config;
	
	/** The accessor to the JIT. */
	protected final JITStateAccessor accessor;
	
	/**
	 * Initializes the base translation engine.
	 *
	 * @param __c The configuration.
	 * @param __jsa The JIT state accessor which is used to communicate and
	 * access the JIT directly.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/02
	 */
	public TranslationEngine(JITConfig __c, JITStateAccessor __jsa)
		throws NullPointerException
	{
		// Check
		if (__c == null || __jsa == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.config = __c;
		this.accessor = __jsa;
	}
	
	/**
	 * Returns the allocations for entry into a method with the specified set
	 * of arguments.
	 *
	 * @param __t The arguments of the method.
	 * @return The allocations for the method entry.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/20
	 */
	public abstract ArgumentAllocation[] allocationForEntry(StackMapType[] __t)
		throws NullPointerException;
	
	/**
	 * Returns the allocation that is used to return a value of the given type.
	 *
	 * @param __t The type of value to return.
	 * @return The allocation for the return value.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/22
	 */
	public abstract ArgumentAllocation allocationForReturn(StackMapType __t)
		throws NullPointerException;
	
	/**
	 * Invokes the pointer that is contained in the given register.
	 *
	 * @param __r The register to invoke.
	 * @throws JITException If the given register cannot be invoked.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/03
	 */
	public abstract void invokeRegister(Register __r)
		throws JITException, NullPointerException;
	
	/**
	 * This loads the address from the given base register into the destination
	 * register.
	 *
	 * @param __t The type of data to be read.
	 * @param __dest The destination registers.
	 * @param __off The offset from the base.
	 * @param __base The base register.
	 * @throws JITException If the load is not valid for the register type,
	 * the offset is out of range, or the base register is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/21
	 */
	public abstract void loadRegister(DataType __t, List<Register> __dest,
		int __off, Register __base)
		throws JITException, NullPointerException;
	
	/**
	 * Generates a return from the method.
	 *
	 * @throws JITException If the return could not be generated.
	 * @since 2017/03/23
	 */
	public abstract void methodReturn()
		throws JITException;
	
	/**
	 * Copies the value from the source registers to the destination
	 * registers.
	 *
	 * @param __t The type of data to be copied.
	 * @param __src The source registers.
	 * @param __dest The destination registers.
	 * @throws JITException If the copy is not valid for the given register
	 * types or the source and destination are not balanced.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/21
	 */
	public abstract void moveRegister(DataType __t, List<Register> __src,
		List<Register> __dest)
		throws JITException, NullPointerException;
	
	/**
	 * This stores the register to the specified offset with the given register
	 * at its base.
	 *
	 * @param __t The type of data to be stored.
	 * @param __src The source registers.
	 * @param __off The offset from the base.
	 * @param __base The base register.
	 * @throws JITException If the store is not valid for the register type,
	 * the offset is out of range, or the base register is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/21
	 */
	public abstract void storeRegister(DataType __t, List<Register> __src,
		int __off, Register __base)
		throws JITException, NullPointerException;
	
	/**
	 * This translates the specified stack type to the given data type, this
	 * is used for allocating space on the stack to store the value.
	 *
	 * @param __t The stack type to translate to a data type.
	 * @return The data type for the given stack type.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/11
	 */
	public abstract DataType toDataType(StackMapType __t)
		throws NullPointerException;
	
	/**
	 * Returns the configuration.
	 *
	 * @return The used configuration.
	 * @since 2017/04/01
	 */
	public final JITConfig C config()
	{
		return this.config;
	}
	
	/**
	 * This returns the configuration that the translation engine was
	 * initialized with.
	 *
	 * @param <C> The class to cast to.
	 * @param __cl The class to cast to.
	 * @return The configuration to use for the JIT.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/02
	 */
	public final <C extends JITConfig> C config(Class<C> __cl)
		throws NullPointerException
	{
		// Check
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		return __cl.cast(this.config);
	}
	
	/**
	 * Is the target big endian?
	 *
	 * @return {@code true} if the target is big endian.
	 * @since 2017/03/21
	 */
	public final boolean isBigEndian()
	{
		return Endian.BIG == this.config.endianess();
	}
	
	/**
	 * Is the target little endian?
	 *
	 * @return {@code true} if the target is little endian.
	 * @since 2017/03/21
	 */
	public final boolean isLittleEndian()
	{
		return Endian.LITTLE == this.config.endianess();
	}
	
	/**
	 * Is the target system a 64-bit system?
	 *
	 * @return {@code true} if the target system is 64-bit.
	 * @since 2017/03/21
	 */
	public final boolean isLongLong()
	{
		return (this.config.bits() > 32);
	}
	
	/**
	 * This loads the address from the given base register into the destination
	 * register.
	 *
	 * @param __t The type of data to be read.
	 * @param __dest The destination register.
	 * @param __off The offset from the base.
	 * @param __base The base register.
	 * @throws JITException If the load is not valid for the register type,
	 * the offset is out of range, or the base register is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/21
	 */
	public final void loadRegister(DataType __t, Register __dest,
		int __off, Register __base)
		throws JITException, NullPointerException
	{
		loadRegister(__t, new Register[]{__dest}, __off, __base);
	}
	
	/**
	 * This loads the address from the given base register into the destination
	 * register.
	 *
	 * @param __t The type of data to be read.
	 * @param __dest The destination registers.
	 * @param __off The offset from the base.
	 * @param __base The base register.
	 * @throws JITException If the load is not valid for the register type,
	 * the offset is out of range, or the base register is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/21
	 */
	public final void loadRegister(DataType __t, Register[] __dest,
		int __off, Register __base)
		throws JITException, NullPointerException
	{
		loadRegister(__t, Arrays.<Register>asList(__dest), __off, __base);
	}
	
	/**
	 * Copies the value from the source registers to the destination
	 * registers.
	 *
	 * @param __t The type of data to be copied.
	 * @param __src The source register.
	 * @param __dest The destination register.
	 * @throws JITException If the copy is not valid for the given register
	 * types or the source and destination are not balanced.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/21
	 */
	public final void moveRegister(DataType __t, Register __src,
		Register __dest)
		throws JITException, NullPointerException
	{
		moveRegister(__t, new Register[]{__src}, new Register[]{__dest});
	}
	
	/**
	 * Copies the value from the source registers to the destination
	 * registers.
	 *
	 * @param __t The type of data to be copied.
	 * @param __src The source registers.
	 * @param __dest The destination registers.
	 * @throws JITException If the copy is not valid for the given register
	 * types or the source and destination are not balanced.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/21
	 */
	public final void moveRegister(DataType __t, Register[] __src,
		Register[] __dest)
		throws JITException, NullPointerException
	{
		moveRegister(__t, Arrays.<Register>asList(__src),
			Arrays.<Register>asList(__dest));
	}
	
	/**
	 * This stores the register to the specified offset with the given register
	 * at its base.
	 *
	 * @param __t The type of data to be stored.
	 * @param __src The source registers.
	 * @param __off The offset from the base.
	 * @param __base The base register.
	 * @throws JITException If the store is not valid for the register type,
	 * the offset is out of range, or the base register is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/21
	 */
	public final void storeRegister(DataType __t, Register __src,
		int __off, Register __base)
		throws JITException, NullPointerException
	{
		storeRegister(__t, new Register[]{__src}, __off, __base);
	}
	
	/**
	 * This stores the register to the specified offset with the given register
	 * at its base.
	 *
	 * @param __t The type of data to be stored.
	 * @param __src The source registers.
	 * @param __off The offset from the base.
	 * @param __base The base register.
	 * @throws JITException If the store is not valid for the register type,
	 * the offset is out of range, or the base register is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/21
	 */
	public final void storeRegister(DataType __t, Register[] __src,
		int __off, Register __base)
		throws JITException, NullPointerException
	{
		storeRegister(__t, Arrays.<Register>asList(__src), __off, __base);
	}
}

