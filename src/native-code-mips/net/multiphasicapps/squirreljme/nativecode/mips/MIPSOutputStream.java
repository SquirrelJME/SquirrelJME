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
import net.multiphasicapps.io.data.DataEndianess;
import net.multiphasicapps.io.data.ExtendedDataOutputStream;
import net.multiphasicapps.squirreljme.nativecode.base.NativeEndianess;
import net.multiphasicapps.squirreljme.nativecode.base.NativeTarget;
import net.multiphasicapps.squirreljme.nativecode.NativeCodeException;
import net.multiphasicapps.squirreljme.nativecode.NativeCodeWriterOptions;
import net.multiphasicapps.squirreljme.nativecode.NativeRegister;
import net.multiphasicapps.squirreljme.nativecode.risc.RISCInstructionOutput;

/**
 * This is an output stream which writes MIPS machine code.
 *
 * @since 2016/09/21
 */
public class MIPSOutputStream
	extends ExtendedDataOutputStream
	implements RISCInstructionOutput<MIPSRegister>
{
	/** The options used for the output. */
	protected final NativeCodeWriterOptions options;
	
	/** The endianess. */
	protected final NativeEndianess endianess;
	
	/** Is this for a 64-bit CPU? */
	private final boolean _sixfour;
	
	/** Double float? */
	private final boolean _doubles;
	
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
		
		// Double floating point?
		this._doubles = nt.floatingPoint().isHardwareDouble();
		
		// Set endianess
		NativeEndianess nend;
		this.endianess = (nend = nt.endianess());
		switch (nend)
		{
			case BIG:
				super.setEndianess(DataEndianess.BIG);
				break;
				
			case LITTLE:
				super.setEndianess(DataEndianess.LITTLE);
				break;
			
				// Unknown
			default:
				throw new RuntimeException("OOPS");
		}
	}
	
	/**
	 * Adds the value of one register and an immediate value and places it in
	 * the destination.
	 *
	 * @param __src The source register.
	 * @param __imm The immediate to add.
	 * @param __dest The destination.
	 * @throws IOException On write errors.
	 * @throws NativeCodeException If the value is out of range.
	 */
	public void integerAddImmediate(MIPSRegister __src, long __imm,
		MIPSRegister __dest)
		throws IOException, NativeCodeException, NullPointerException
	{
		// Check
		if (__src == null || __dest == null)
			throw new NullPointerException("NARG");
		
		// Small immediate?
		if (__imm >= -32768 && __imm <= 32767)
		{
			mipsTypeI(0b001001, __src, __dest, (int)__imm);
			return;
		}
		
		throw new Error("TODO");
	}
	
	/**
	 * This writes an i-type instruction.
	 *
	 * @param __op The opcode.
	 * @param __rs The source register.
	 * @param __rt The target register.
	 * @param __imm The immediate value, only the first 16-bits are considered.
	 * @throws IOException On write errors.
	 * @throws NativeCodeException If the opcode is out of range.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/23
	 */
	public final void mipsTypeI(int __op, MIPSRegister __rs, MIPSRegister __rt,
		int __imm)
		throws IOException, NativeCodeException, NullPointerException
	{
		// Check
		if (__rs == null || __rt == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AW0c The operation has an illegal bit set in its
		// value. (The operation)}
		if ((__op & (~0b111111)) != 0)
			throw new NativeCodeException(String.format("AW0c %d", __op));
		
		// Build value to write to the output
		super.writeInt((__op << 26) |
			(__rs.id() << 21) |
			(__rt.id() << 16) |
			(__imm & 0xFFFF));
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
	public final void mipsTypeR(int __op, MIPSRegister __rs, MIPSRegister __rt,
		MIPSRegister __rd, int __sa, int __func)
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/23
	 */
	@Override
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
	 * {@inheritDoc}
	 * @since 2016/09/26
	 */
	@Override
	public void registerLoad(int __b, MIPSRegister __base, int __off,
		MIPSRegister __dest)
		throws IOException, NativeCodeException, NullPointerException
	{
		// Check
		if (__dest == null)
			throw new NullPointerException("NARG");
		
		// Absolute address?
		if (__base == null)
		{
			// {@squirreljme.error AW08 Load of an absolute address is only
			// valid for the first 32KiB of memory. (The absolute address)}
			if (__off < 0 || __off > 32767)
				throw new NativeCodeException(String.format(
					"AW08 %d", __off));
			
			// Use Zero register
			__base = MIPSRegister.R0;
		}
		
		// {@squirreljme.error AW0d The base register used for a load is not
		// an integer register. (The base register)}
		else if (!__base.isInteger())
			throw new NativeCodeException(String.format("AW0d %s", __base));
		
		// {@squirreljme.error AW0b Relative register offset exceeds the
		// permitted range. (The offset)}
		if (__off < -32768 || __off > 32767)
			throw new NativeCodeException(String.format("AW0b %d", __off));
		
		// Float?
		boolean isfloat = __dest.isFloat();
		
		// The opcode depends on the store size
		int op;
		switch (__b)
		{
				// 8-bit
			case 1:
				// {@squirreljme.error AW0h Cannot load 8-bit floating point
				// value.}
				if (isfloat)
					throw new NativeCodeException("AW0h");
				
				op = 0b100000;
				break;
				
				// 16-bit
			case 2:
				// {@squirreljme.error AW0i Cannot load 16-bit floating point
				// value.}
				if (isfloat)
					throw new NativeCodeException("AW0i");
				
				op = 0b100001;
				break;
			
				// Always supported
			case 4:
				op = (isfloat ? 0b110001 : 0b100011);
				break;
			
				// 64-bits
			case 8:
				// {@squirreljme.error AW0j Loading 64-bit values it not
				// supported by this CPU.}
				if ((isfloat && !this._doubles) ||
					(!isfloat && !this._sixfour))
					throw new NativeCodeException("AW0j");
				op = (isfloat ? 0b110101 : 0b110111);
				break;
			
				// {@squirreljme.error AW0k Cannot load a value of the
				// specified byte count. (The byte count)}
			default:
				throw new NativeCodeException(String.format("AW0k %d", __b));
		}
		
		// Encode
		mipsTypeI(op, __base, __dest, __off);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/23
	 */
	@Override
	public void registerStore(int __b, MIPSRegister __src,
		MIPSRegister __base, int __off)
		throws IOException, NativeCodeException, NullPointerException
	{
		// Check
		if (__src == null)
			throw new NullPointerException("NARG");
		
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
		
		// {@squirreljme.error AW0e The base register used for a store is not
		// an integer register. (The base register)}
		else if (!__base.isInteger())
			throw new NativeCodeException(String.format("AW0e %s", __base));
		
		// {@squirreljme.error AW07 Relative register offset exceeds the
		// permitted range. (The offset)}
		if (__off < -32768 || __off > 32767)
			throw new NativeCodeException(String.format("AW07 %d", __off));
		
		// Float?
		boolean isfloat = __src.isFloat();
		
		// The opcode depends on the store size
		int op;
		switch (__b)
		{
				// 8-bit
			case 1:
				// {@squirreljme.error AW0g Cannot store 8-bit floating point
				// value.}
				if (isfloat)
					throw new NativeCodeException("AW0g");
				
				op = 0b101000;
				break;
				
				// 16-bit
			case 2:
				// {@squirreljme.error AW0f Cannot store 16-bit floating point
				// value.}
				if (isfloat)
					throw new NativeCodeException("AW0f");
				
				op = 0b101001;
				break;
			
				// Always supported
			case 4:
				op = (isfloat ? 0b111001 : 0b101011);
				break;
			
				// 64-bits
			case 8:
				// {@squirreljme.error AW05 Storing 64-bit values it not
				// supported by this CPU.}
				if ((isfloat && !this._doubles) ||
					(!isfloat && !this._sixfour))
					throw new NativeCodeException("AW05");
				op = (isfloat ? 0b111101 : 0b111111);
				break;
			
				// {@squirreljme.error AW04 Cannot store a value of the
				// specified byte count. (The byte count)}
			default:
				throw new NativeCodeException(String.format("AW04 %d", __b));
		}
		
		// Encode
		mipsTypeI(op, __base, __src, __off);
	}
}

