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
	 * Jumps and links to the specified register, the register for the return
	 * address is to be implied to be the link register.
	 *
	 * @param __t The target register to jump to.
	 * @throws JITException If the specified register is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/22
	 */
	public final void jumpAndLinkRegisterImplied(MIPSRegister __t)
		throws JITException, NullPointerException
	{
		jumpAndLinkRegister(NUBI.RA, __t);
	}
	
	/**
	 * Jumps and links to the specified register with the return value being
	 * placed in the return value register.
	 *
	 * @param __r The return address register.
	 * @param __t The target register to jump to.
	 * @throws JITException If the specified register is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/22
	 */
	public final void jumpAndLinkRegister(MIPSRegister __r, MIPSRegister __t)
		throws JITException, NullPointerException
	{
		// Check
		if (__r == null || __t == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AM0h Cannot jump and link with floating point
		// registers. (The return address register; The jump target register)}
		if (__r.isFloat() || __t.isFloat())
			throw new JITException(String.format("AM0h %s %s", __r, __t));
		
		// Generate
		mipsTypeR(0, __t.id(), 0, __r.id(), 0, 0b001001);
	}
	
	/**
	 * Loads the specified register with the given offset into the given
	 * register.
	 *
	 * @param __rt The target register where the value is placed.
	 * @param __off The offset from the base.
	 * @param __b The base register to derive the memory address from.
	 * @throws JITException If the offset is out of range or the input
	 * registers are not integer registers.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/22
	 */
	public final void loadWord(MIPSRegister __rt, int __off, MIPSRegister __b)
		throws JITException, NullPointerException
	{
		// Check
		if (__rt == null || __b == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AM0f Offset out of range. (The offset base)}
		if (__off < -32768 || __off > 32767)
			throw new JITException(String.format("AM0f %d", __off));
		
		// {@squirreljme.error AM0g Floating point registers specified. (The
		// target register; The base register)}
		if (__rt.isFloat() || __b.isFloat())
			throw new JITException(String.format("AM0g %s %s", __rt, __b));
		
		// Encode
		mipsTypeI(0b100011, __b, __rt, __off);
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
		
		// Build value to write to the output
		super.appendInteger((__checkOp(__op) << 26) |
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
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/23
	 */
	public final void mipsTypeR(int __op, MIPSRegister __rs, MIPSRegister __rt,
		MIPSRegister __rd, int __sa, int __func)
		throws NullPointerException
	{
		// Check
		if (__rs == null || __rt == null || __rd == null)
			throw new NullPointerException("NARG");
		
		// Generate
		mipsTypeR(__op, __rs.id(), __rt.id(), __rd.id(), __sa, __func);
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
	 * @throws JITException If the input registers, operation or function are
	 * out of range.
	 * @since 2017/03/22
	 */
	public final void mipsTypeR(int __op, int __rs, int __rt, int __rd,
		int __sa, int __func)
		throws JITException
	{
		// Combined
		super.appendInteger((__checkOp(__op) << 26) |
			(__checkRegister(__rs) << 21) |
			(__checkRegister(__rt) << 16) |
			(__checkRegister(__rd) << 11) |
			(__checkShift(__sa) << 6) |
			(__checkFunction(__func)));
	}
	
	/**
	 * Does nothing.
	 *
	 * @since 2017/03/22
	 */
	public final void nop()
	{
		mipsTypeR(0, 0, 0, 0, 0, 0);
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
	
	/**
	 * Checks the function value.
	 *
	 * @param __v The function to check.
	 * @return {@code __v}.
	 * @throws JITException If the function is out of range.
	 * @since 2017/03/22
	 */
	private static int __checkFunction(int __v)
	{
		// {@squirreljme.error AM08 The function has an illegal bit set in its
		// value. (The function)}
		if ((__v & (~0b111111)) != 0)
			throw new JITException(String.format("AM0j %d", __v));
		
		return __v;
	}
	
	/**
	 * Checks the operation value.
	 *
	 * @param __v The value to check.
	 * @return {@code __v}.
	 * @throws JITException If the operation is out of range.
	 * @since 2017/03/22
	 */
	private static int __checkOp(int __v)
		throws JITException
	{
		// {@squirreljme.error AM08 The operation has an illegal bit set in its
		// value. (The operation)}
		if ((__v & (~0b111111)) != 0)
			throw new JITException(String.format("AM08 %d", __v));
		
		return __v;
	}
	
	/**
	 * Checks the register index.
	 *
	 * @param __v The index to check.
	 * @return {@code __v}.
	 * @throws JITException If the register index is not valid.
	 * @since 2017/03/22
	 */
	private static int __checkRegister(int __v)
		throws JITException
	{
		// {@squirreljme.error AM0i Register index out of bounds.
		// (The register)}
		if (__v < 0 || __v > 0b11111)
			throw new JITException(String.format("AM0i %d", __v));
		
		return __v;
	}
	
	/**
	 * Checks the shift amount.
	 *
	 * @param __v The shift amount to check.
	 * @return {@code __v}.
	 * @throws JITException If the shift amount is not valid.
	 * @since 2017/03/22
	 */
	private static int __checkShift(int __v)
	{
		// {@squirreljme.error AM0l Shift amount out of bounds.
		// (The shift amount)}
		if (__v < 0 || __v > 0b11111)
			throw new JITException(String.format("AM0l %d", __v));
		
		return __v;
	}
}

