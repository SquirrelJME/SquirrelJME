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

import java.util.Collection;
import net.multiphasicapps.classfile.ByteCode;

/**
 * This is similar to {@link ByteCode} except that it instead of using a
 * stack for intermediate Java operations, this instead uses registers. This
 * provides a more concise and easier to use format by virtual machines.
 *
 * @see ByteCode
 * @since 2019/03/09
 */
public final class RegisterCode
{
	/** Instructions for this code. */
	private final RegisterInstruction[] _instructions;
	
	/** Line number table. */
	private final short[] _lines;
	
	/**
	 * Initializes the register code.
	 *
	 * @param __i The associated instructions.
	 * @param __l The lines for each instruction.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/22
	 */
	public RegisterCode(RegisterInstruction[] __i, short[] __l)
		throws NullPointerException
	{
		__i = (__i == null ? new RegisterInstruction[0] : __i.clone());
		for (RegisterInstruction i : __i)
			if (i == null)
				throw new NullPointerException("NARG");
		
		this._instructions = __i;
		this._lines = (__l == null ? new short[0] : __l.clone());
	}
	
	/**
	 * Initializes the register code.
	 *
	 * @param __i The associated instructions.
	 * @param __l The lines for each instruction.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/22
	 */
	public RegisterCode(Collection<RegisterInstruction> __i, short[] __l)
		throws NullPointerException
	{
		if (__i == null)
			throw new NullPointerException("NARG");
		
		RegisterInstruction[] ii = __i.<RegisterInstruction>toArray(
			new RegisterInstruction[__i.size()]);
		for (RegisterInstruction i : ii)
			if (i == null)
				throw new NullPointerException("NARG");
		
		this._instructions = ii;
		this._lines = (__l == null ? new short[0] : __l.clone());
	}
	
	/**
	 * This translates the input byte code and creates a register code which
	 * removes all stack operations and maps them to register operations.
	 *
	 * @param __bc The input byte code.
	 * @return The resulting register code.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/09
	 */
	public static final RegisterCode of(ByteCode __bc)
		throws NullPointerException
	{
		if (__bc == null)
			throw new NullPointerException("NARG");
		
		return new __Registerize__(__bc).convert();
	}
}

