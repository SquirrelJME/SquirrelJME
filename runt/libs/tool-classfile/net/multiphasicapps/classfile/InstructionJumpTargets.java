// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This represents the targets of jumps that an instruction may jump to.
 *
 * @since 2019/03/30
 */
public final class InstructionJumpTargets
{
	/** Normal jumps. */
	private final InstructionJumpTarget[] _normal;
	
	/** Exceptional jumps. */
	private final InstructionJumpTarget[] _exception;
	
	/** String representation. */
	private Reference<String> _string;
	
	/**
	 * Initializes the jump targets.
	 * @param __n Normal jumps.
	 * @param __e Exceptional jumps.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/30
	 */
	public InstructionJumpTargets(InstructionJumpTarget[] __n,
		InstructionJumpTarget[] __e)
		throws NullPointerException
	{
		if (__n == null || __e == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/30
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/30
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Checks if any of the jump targets has an address which follows the
	 * given address.
	 *
	 * @param __pc The address to check.
	 * @return True if any address in the jump targets has an address which
	 * is higher than this address.
	 * @since 2019/03/30
	 */
	public final boolean hasLaterAddress(int __pc)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/30
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
}

