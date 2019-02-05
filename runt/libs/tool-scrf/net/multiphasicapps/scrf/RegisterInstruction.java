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
 * This represents a single instruction within the compiled register format.
 *
 * @since 2019/01/23
 */
public final class RegisterInstruction
{
	/** The operation. */
	protected final int op;
	
	/** Arguments. */
	private final Object[] _args;
	
	/**
	 * Initializes the register instruction.
	 *
	 * @param __op The operation.
	 * @param __args Arguments to the operation.
	 * @throws IllegalArgumentException If the operation is not valid.
	 * @since 2019/01/23
	 */
	public RegisterInstruction(int __op, Object... __args)
		throws IllegalArgumentException
	{
		// {@squirreljme.error AV01 Invalid operation. (The operation)}
		if (__op < 0 || __op >= RegisterInstructionType.NUM_INSTRUCTIONS)
			throw new IllegalArgumentException("AV01 " + __op);
		
		this.op = __op;
		this._args = (__args = (__args == null ? new Object[0] :
			__args.clone()));
		
		// Check arguments for null
		for (Object a : __args)
			if (a == null)
				throw new NullPointerException("NARG");
	}
}

