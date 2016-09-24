// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.nativecode.mips;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import net.multiphasicapps.squirreljme.nativecode.base.NativeTarget;
import net.multiphasicapps.squirreljme.nativecode.NativeCodeException;
import net.multiphasicapps.squirreljme.nativecode.NativeCodeWriterOptions;
import net.multiphasicapps.squirreljme.nativecode.NativeRegister;

/**
 * This is an output stream which writes MIPS machine code.
 *
 * @since 2016/09/21
 */
public class MIPSOutputStream
	extends DataOutputStream
{
	/** The options used for the output. */
	protected final NativeCodeWriterOptions options;
	
	/** Is this for a 64-bit CPU? */
	private final boolean _sixfour;
	
	/**
	 * Initializes the machine code output stream.
	 *
	 * @param __o The options used for code generation.
	 * @param __os The output stream where bytes are written to.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/21
	 */
	public MIPSOutputStream(NativeCodeWriterOptions __o, OutputStream __os)
		throws NullPointerException
	{
		super(__os);
		
		// Check
		if (__o == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.options = __o;
		
		// Determine some flags
		NativeTarget nt = __o.nativeTarget();
		this._sixfour = nt.bits() >= 64;
	}
	
	/**
	 * Writes a store of an integer value.
	 *
	 * @param __b The size of the value to store.
	 * @param __src The source register.
	 * @param __base The base register, may be {@code null} to specify that
	 * register zero should be used instead (absolute address).
	 * @param __off The offset from the base.
	 * @throws IOException On write errors.
	 * @throws NativeCodeException If the offset is out of range.
	 * @throws NullPointerException On null arguments, except for
	 * {@code __base}.
	 * @since 2016/09/23
	 */
	public final void mipsIntegerStore(int __b, NativeRegister __src,
		NativeRegister __base, int __off)
		throws IOException, NativeCodeException, NullPointerException
	{
		// Absolute address?
		if (__base == null)
		{
			// {@squirreljme.error AW06 Store at an absolute address is only
			// valid for the first 32KiB of memory. (The absolute address)}
			if (__off < 0 || __off > 32767)
				throw new NativeCodeException(String.format(
					"AW06 %d", __off));
			
			// Use Zero register
			__base = MIPSRegister.R0;
		}
		
		// {@squirreljme.error AW07 Relative register offset exceeds the
		// permitted range. (The offset)}
		if (__off < -32768 || __off > 32767)
			throw new NativeCodeException(String.format("AW07 %d", __off));
		
		// Get registers
		MIPSRegister src = ofInteger(__src);
		MIPSRegister base = ofInteger(__base);
		
		// The opcode depends on the store size
		int op;
		switch (__b)
		{
				// Always supported
			case 1: op = 0b101000; break;
			case 2: op = 0b101001; break;
			case 4: op = 0b101011; break;
			
				// 64-bits
			case 8:
				// {@squirreljme.error AW05 Storing 64-bit values it not
				// supported by this CPU.}
				if (!this._sixfour)
					throw new NativeCodeException("AW05");
				op = 0b111111;
				break;
			
				// {@squirreljme.error AW04 Cannot store a value of the
				// specified byte count. (The byte count)}
			default:
				throw new NativeCodeException(String.format("AW04 %d", __b));
		}
		
		throw new Error("TODO");
	}
	
	/**
	 * Converts a native register to an integer MIPS register.
	 *
	 * @param __r The register to convert.
	 * @return The converted register.
	 * @throws NativeCodeException If it is not an integral MIPS registers.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/24
	 */
	public MIPSRegister ofInteger(NativeRegister __r)
		throws NativeCodeException, NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AW09 The input register is not a MIPS register.
		// (The register)}
		if (!(__r instanceof MIPSRegister))
			throw new NativeCodeException(String.format("AW09 %s", __r));
		
		// {@squirreljme.error AW0a The specified register is a not an integer
		// register. (The register)}
		MIPSRegister rv = (MIPSRegister)__r;
		if (!rv.isInteger())
			throw new NativeCodeException(String.format("AW0a %s", rv));
		
		// Ok
		return rv;
	}
	
	/**
	 * This writes an i-type instruction.
	 *
	 * @param __op The opcode.
	 * @param __rs The source register.
	 * @param __rt The target register.
	 * @param __imm The immediate.
	 * @throws IOException On write errors.
	 * @since 2016/09/23
	 */
	public final void typeI(int __op, NativeRegister __rs, NativeRegister __rt,
		int __imm)
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * This writes a r-type instruction.
	 *
	 * @param __op The opcode.
	 * @param __rs The source register.
	 * @param __rt The target register.
	 * @param __rd The destination register.
	 * @param __sa The shift amount.
	 * @param __func The function.
	 * @throws IOException On write errors.
	 * @since 2016/09/23
	 */
	public final void typeR(int __op, NativeRegister __rs, NativeRegister __rt,
		NativeRegister __rd, int __sa, int __func)
		throws IOException
	{
		throw new Error("TODO");
	}
}

