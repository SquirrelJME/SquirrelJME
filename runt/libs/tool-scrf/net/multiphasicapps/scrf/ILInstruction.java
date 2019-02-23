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
 * Individual intermediate language instruction.
 *
 * @since 2019/02/23
 */
public final class ILInstruction
{
	/** The instruction type. */
	protected final ILInstructionType type;
	
	/** Arguments. */
	private final Object[] _args;
	
	public ILInstruction(ILInstructionType __type, Object... __args)
		throws NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		this.type = __type;
		this._args = (__args == null ? new Object[0] : __args.clone());
	}
}

