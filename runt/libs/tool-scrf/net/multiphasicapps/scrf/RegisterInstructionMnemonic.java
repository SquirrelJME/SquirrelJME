// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.scrf;

/**
 * Mnemonics for instructions.
 *
 * @since 2019/02/06
 */
public class RegisterInstructionMnemonic
{
	/**
	 * Not used.
	 *
	 * @since 2019/02/06
	 */
	private RegisterInstructionMnemonic()
	{
	}
	
	/**
	 * Returns the name of the operation.
	 *
	 * @param __i The instruction type.
	 * @return The operation mnemonic.
	 * @since 2019/02/06
	 */
	public static final String of(int __i)
	{
		switch (__i)
		{
			case RegisterInstructionType.NOP:		return "NOP";
			case RegisterInstructionType.CONST:		return "CONST";
			case RegisterInstructionType.COPY:		return "COPY";
			case RegisterInstructionType.LOAD:		return "LOAD";
			case RegisterInstructionType.STORE:		return "STORE";
			default:
				return "UNKNOWN" + __i;
		}
	}
}

