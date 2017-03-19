// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.mips;

import net.multiphasicapps.squirreljme.jit.CodeFragmentOutput;
import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This is a fragment output which can be used to generated MIPS instructions.
 *
 * @since 2017/03/18
 */
public class MIPSFragmentOutput
	extends CodeFragmentOutput
{
	/** The MIPS configuration. */
	protected final MIPSConfig config;
	
	/**
	 * Sets the used MIPS configuration.
	 *
	 * @param __c The configuration to use.
	 * @since 2017/03/18
	 */
	public MIPSFragmentOutput(MIPSConfig __c)
	{
		super(__c);
		
		// Set
		this.config = __c;
	}
	
	/**
	 * This writes an i-type instruction.
	 *
	 * @param __op The opcode.
	 * @param __rs The source register.
	 * @param __rt The target register.
	 * @param __imm The immediate value, only the first 16-bits are considered.
	 * @throws JITException If the opcode is out of range.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/23
	 */
	public final void mipsTypeI(int __op, MIPSRegister __rs, MIPSRegister __rt,
		int __imm)
		throws JITException, NullPointerException
	{
		// Check
		if (__rs == null || __rt == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AM08 The operation has an illegal bit set in its
		// value. (The operation)}
		if ((__op & (~0b111111)) != 0)
			throw new JITException(String.format("AM08 %d", __op));
		
		// Build value to write to the output
		super.appendInteger((__op << 26) |
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
	{
		throw new Error("TODO");
	}
	
	/**
	 * Stores the specified register with the given offset into the given
	 * register.
	 *
	 * @param __rt The target register which contains the value to store.
	 * @param __off The offset from the base.
	 * @param __b The base register to derive the memory address from.
	 * @throws JITException If the offset is out of range or the input
	 * registers are not integer registers.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/19
	 */
	public final void storeWord(MIPSRegister __rt, int __off, MIPSRegister __b)
		throws JITException, NullPointerException
	{
		// Check
		if (__rt == null || __b == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AM09 Offset out of range. (The offset base)}
		if (__off < -32768 || __off > 32767)
			throw new JITException(String.format("AM09 %d", __off));
		
		// {@squirreljme.error AM0a Floating point registers specified. (The
		// target register; The base register)}
		if (__rt.isFloat() || __b.isFloat())
			throw new JITException(String.format("AM0a %s %s", __rt, __b));
		
		// Encode
		mipsTypeI(0b101011, __b, __rt, __off);
	}
}

