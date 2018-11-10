// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This represents a branch or jump target that may be used by instructions.
 *
 * @since 2018/09/15
 */
public final class InstructionJumpTarget
	implements Comparable<InstructionJumpTarget>
{
	/** The value. */
	protected final int target;
	
	/** String representation. */
	private Reference<String> _string;
	
	/**
	 * Initializes the jump target.
	 *
	 * @param __t The target.
	 * @throws IllegalArgumentException If the target is negative.
	 * @since 2018/09/15
	 */
	public InstructionJumpTarget(int __t)
		throws IllegalArgumentException
	{
		// {@squirreljme.error JC1j Jump target is negative. (The target)}
		if (__t < 0)
			throw new IllegalArgumentException(String.format("JC1j %d", __t));
		
		this.target = __t;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/15
	 */
	@Override
	public final int compareTo(InstructionJumpTarget __o)
		throws NullPointerException
	{
		if (__o == null)
			throw new NullPointerException("NARG");
		
		return this.target - __o.target;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/15
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		
		if (!(__o instanceof InstructionJumpTarget))
			return false;
		
		return this.target == ((InstructionJumpTarget)__o).target;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/15
	 */
	@Override
	public final int hashCode()
	{
		return this.target;
	}
	
	/**
	 * Returns the target address of the jump.
	 *
	 * @return The jump target address.
	 * @since 2018/09/15
	 */
	public final int target()
	{
		return this.target;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/15
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>(
				(rv = String.format("->@%d", this.target)));
		
		return rv;
	}
}

