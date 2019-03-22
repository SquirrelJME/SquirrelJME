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
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This is used to build {@link RegisterCode} and add instructions to it.
 *
 * @since 2019/03/16
 */
final class __RegisterCodeBuilder__
{
	/** Temporary instruction layout. */
	final Map<Integer, __TempInstruction__> _instructions =
		new LinkedHashMap<>();
	
	/** Next address to use. */
	int _nextaddr;
	
	/**
	 * Initializes the code builder at the default start address.
	 *
	 * @since 2019/03/22
	 */
	__RegisterCodeBuilder__()
	{
		this._nextaddr = 0;
	}
	
	/**
	 * Initializes the code builder at the given start address.
	 *
	 * @param __pc The address to start at.
	 * @since 2019/03/22
	 */
	__RegisterCodeBuilder__(int __pc)
	{
		this._nextaddr = __pc;
	}
	
	/**
	 * Adds a new instruction.
	 *
	 * @param __op The operation to add.
	 * @param __args The arguments to the operation.
	 * @return The resulting temporary instruction.
	 * @since 2019/03/16
	 */
	public final __TempInstruction__ add(int __op, Object... __args)
	{
		// Create instruction
		int atdx;
		__TempInstruction__ rv = new __TempInstruction__(
			(atdx = this._nextaddr++), __op, __args);
		
		// Debug
		todo.DEBUG.note("@%d -> %s %s", atdx,
			RegisterOperationMnemonics.toString(__op), Arrays.asList(__args));
		
		// Store and return the instruction, it will have the address
		this._instructions.put(atdx, rv);
		return rv;
	}
}

