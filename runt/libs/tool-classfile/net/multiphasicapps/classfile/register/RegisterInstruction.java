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

/**
 * This represents a single instruction.
 *
 * @since 2019/03/22
 */
public final class RegisterInstruction
{
	/** The operation. */
	protected final int op;
	
	/** The arguments. */
	final Object[] _args;
	
	/**
	 * Initializes the temporary instruction.
	 *
	 * @param __op The operation.
	 * @param __args The arguments.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/22
	 */
	public RegisterInstruction(int __op, Object... __args)
		throws NullPointerException
	{
		this.op = __op;
		this._args = (__args =
			(__args == null ? new Object[0] : __args.clone()));
		
		for (Object o : __args)
			if (o == null)
				throw new NullPointerException("NARG");
	}
	
	/**
	 * Initializes the temporary instruction.
	 *
	 * @param __op The operation.
	 * @param __args The arguments.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/22
	 */
	public RegisterInstruction(int __op, Collection<Object> __args)
		throws NullPointerException
	{
		if (__args == null)
			throw new NullPointerException("NARG");
		
		this.op = __op;
		
		Object[] args = __args.<Object>toArray(new Object[__args.size()]);
		this._args = args;
		for (Object o : args)
			if (o == null)
				throw new NullPointerException("NARG");
	}
}

