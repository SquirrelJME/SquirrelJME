// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.nativecode.risc;

import java.io.DataOutput;
import java.io.IOException;
import net.multiphasicapps.squirreljme.nativecode.NativeCodeException;
import net.multiphasicapps.squirreljme.nativecode.NativeRegister;

/**
 * Since RISC systems in general have very similar means of performing
 * operations, this provides a base interface for those.
 *
 * @param <R> The type of register to use.
 * @since 2016/09/25
 */
public interface RISCInstructionOutput<R extends RISCRegister>
	extends DataOutput
{
	/**
	 * Adds the value of one register and an immediate value and places it in
	 * the destination register.
	 *
	 * @param __src The source register.
	 * @param __imm The immediate to add.
	 * @param __dest The destination register.
	 * @throws IOException On write errors.
	 * @throws NativeCodeException If the value is out of range or the register
	 * set is incorrect.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/25
	 */
	public abstract void integerAddImmediate(R __src, long __imm, R __dest)
		throws IOException, NativeCodeException, NullPointerException;
	
	/**
	 * Checks that the given native register is a native register and of the
	 * integer type for this output.
	 *
	 * @param __r The register to convert.
	 * @return The converted register if it is the correct integer register.
	 * @throws NativeCodeException If the class type is incorrect or it is not
	 * an integer register.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/25
	 */
	public abstract R ofInteger(NativeRegister __r)
		throws NativeCodeException, NullPointerException;
		
	/**
	 * Loads a value at the specified memory address into the specified
	 * register.
	 *
	 * @param __b The size of the value to load.
	 * @param __base The base register, may be {@code null} to specificy that
	 * the address is absolute.
	 * @param __off The offset from the base register, or the absolute address
	 * if it there is no base.
	 * @param __dest The destination register.
	 * @throws IOException On write errors.
	 * @throws NativeCodeException If the offset is out of range or the base
	 * register is not an integer register.
	 * @throws NullPointerException On null arguments, except for
	 * {@code __base}.
	 * @since 2016/09/26
	 */
	public abstract void registerLoad(int __b, R __base, int __off, R __dest)
		throws IOException, NativeCodeException, NullPointerException;
	
	/**
	 * Stores the specified register at the specified memory address.
	 *
	 * @param __b The size of the value to store.
	 * @param __src The source register.
	 * @param __base The base register, may be {@code null} to specify that
	 * the address is absolute.
	 * @param __off The offset from the base.
	 * @throws IOException On write errors.
	 * @throws NativeCodeException If the offset is out of range or the base
	 * register is not an integer register.
	 * @throws NullPointerException On null arguments, except for
	 * {@code __base}.
	 * @since 2016/09/23
	 */
	public abstract void registerStore(int __b, R __src, R __base, int __off)
		throws IOException, NativeCodeException, NullPointerException;
}

