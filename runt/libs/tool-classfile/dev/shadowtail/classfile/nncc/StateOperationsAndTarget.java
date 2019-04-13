// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.nncc;

import dev.shadowtail.classfile.xlate.StateOperations;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.classfile.InstructionJumpTarget;

/**
 * This represents state operations and a target.
 *
 * @since 2019/04/12
 */
public final class StateOperationsAndTarget
{
	/** State Operations. */
	protected final StateOperations operations;
	
	/** Jump target. */
	protected final InstructionJumpTarget target;
	
	/** Hashcode. */
	private int _hash;
	
	/** String form. */
	private Reference<String> _string;
	
	/**
	 * Initializes the operations and target.
	 *
	 * @param __ops The operations.
	 * @param __t The target.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/12
	 */
	public StateOperationsAndTarget(StateOperations __ops,
		InstructionJumpTarget __t)
		throws NullPointerException
	{
		if (__ops == null || __t == null)
			throw new NullPointerException("NARG");
		
		this.operations = __ops;
		this.target = __t;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/12
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		if (!(__o instanceof StateOperationsAndTarget))
			return false;
		
		if (this.hashCode() != __o.hashCode())
			return false;
		
		StateOperationsAndTarget o = (StateOperationsAndTarget)__o;
		return this.operations.equals(o.operations) &&
			this.target.equals(o.target);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/12
	 */
	@Override
	public final int hashCode()
	{
		int rv = this._hash;
		if (rv == 0)
			this._hash = (rv = this.operations.hashCode() ^
				this.target.hashCode());
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/12
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = this.operations + "+@" +
				this.target));
		
		return rv;
	}
}

