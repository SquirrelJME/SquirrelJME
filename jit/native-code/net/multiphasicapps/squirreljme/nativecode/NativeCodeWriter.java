// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.nativecode;

/**
 * This interface is used for native code writers.
 *
 * This is used for a more object-oriented output of native code and as such
 * should not handle things such as register allocation.
 *
 * @since 2016/09/14
 */
public interface NativeCodeWriter
{
	/**
	 * Copies the value contain in one allocation to another allocation.
	 *
	 * @param __src The source allocation.
	 * @param __dest The destination allocation.
	 * @throws NativeCodeException If the copy operation could not be
	 * performed.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/21
	 */
	public abstract void copy(NativeAllocation __src, NativeAllocation __dest)
		throws NativeCodeException, NullPointerException;
	
	/**
	 * Adds an immediate value to the specified register and places it in the
	 * destination register.
	 *
	 * @param __a The first input register.
	 * @param __b The immediate value to add.
	 * @param __dest The destination.
	 * @throws NativeCodeException If the add operationg could not be
	 * performed.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/25
	 */
	public abstract void integerAddImmediate(NativeRegister __a, long __b,
		NativeRegister __dest)
		throws NativeCodeException, NullPointerException;
	
	/**
	 * Returns the configuration that was used to configure the writer.
	 *
	 * @return The configuration used to configure the writer.
	 * @since 2016/09/15
	 */
	public abstract NativeCodeWriterOptions options();
	
	/**
	 * Reads from the source memory location, which may be register relative,
	 * into the specified register.
	 *
	 * @param __b The value size to write.
	 * @param __base The base register to use for register-value relative
	 * reads, may be {@code null} for absolute addresses.
	 * @param __addr If {@code __base} is {@code null} then this is treated as
	 * an absolute address to read from, otherwise it is an address relative to
	 * the value in a register.
	 * @param __dest The destination to store the read value into.
	 * @throws NativeCodeException If the read operation could not be
	 * performed.
	 * @throws NullPointerException On null arguments, except for
	 * {@code __base}.
	 * @since 2016/09/23
	 */
	public abstract void registerLoad(int __b, NativeRegister __base,
		long __addr, NativeRegister __dest)
		throws NativeCodeException, NullPointerException;
	
	/**
	 * This stores the given register and places it at the specified memory
	 * address, which may be relative to a register value.
	 *
	 * @param __b The value size to write.
	 * @param __src The source to read a value for writing to memory.
	 * @param __base The base register to use for register-value relative
	 * writes, may be {@code null} for absolute addresses.
	 * @param __addr If {@code __base} is {@code null} then this is treated as
	 * an absolute address to write to, otherwise it is an address relative to
	 * the value in a register.
	 * @throws NativeCodeException If the write operation could not be
	 * performed.
	 * @throws NullPointerException On null arguments, except for
	 * {@code __base}.
	 * @since 2016/09/23
	 */
	public abstract void registerStore(int __b, NativeRegister __src,
		NativeRegister __base, long __addr)
		throws NativeCodeException, NullPointerException;
}

