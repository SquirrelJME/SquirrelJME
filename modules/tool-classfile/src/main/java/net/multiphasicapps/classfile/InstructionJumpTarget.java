// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
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
	/** Does this have a key? */
	protected final boolean hasKey;
	
	/** The key. */
	protected final int key;
	
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
		/* {@squirreljme.error JC3b Jump target is negative. (The target)} */
		if (__t < 0)
			throw new IllegalArgumentException(String.format("JC3b %d", __t));
		
		this.target = __t;
		this.hasKey = false;
		this.key = -1;
	}
	
	/**
	 * Initializes the jump target.
	 *
	 * @param __t The target.
	 * @param __key The key value.
	 * @throws IllegalArgumentException If the target is negative.
	 * @since 2023/07/04
	 */
	public InstructionJumpTarget(int __t, int __key)
		throws IllegalArgumentException
	{
		/* {@squirreljme.error JC3m Jump target is negative. (The target)} */
		if (__t < 0)
			throw new IllegalArgumentException(String.format("JC3m %d", __t));
		
		this.target = __t;
		this.hasKey = true;
		this.key = __key;
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
		
		// Sort ones that have keys after, so "default" branches are lower
		if (this.hasKey != __o.hasKey)
			return (this.hasKey ? 1 : -1);
		
		// Sort by key values first
		if (this.hasKey)
		{
			int diff = this.key - __o.key;
			if (diff != 0)
				return diff;
		}
		
		// Sort by target last
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
		
		InstructionJumpTarget o = (InstructionJumpTarget)__o;
		return this.target == o.target &&
			this.hasKey == o.hasKey &&
			this.key == o.key;
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
	 * Returns the jump target key, if there is one.
	 * 
	 * @return The key for the jump target.
	 * @throws IllegalStateException If there is no key.
	 * @since 2023/07/04
	 */
	public int key()
		throws IllegalStateException
	{
		/* {@squirreljme.error JC06 Jump target has no key.} */
		if (!this.hasKey)
			throw new IllegalStateException("JC06");
		
		return this.key;
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

