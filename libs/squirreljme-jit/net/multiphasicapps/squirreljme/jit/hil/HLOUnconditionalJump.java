// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.hil;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.squirreljme.jit.java.BasicBlockKey;

/**
 * This represents an unconditional jump to a given target.
 *
 * @since 2017/09/01
 */
public final class HLOUnconditionalJump
	implements HLO, HLOJump
{
	/** The target of the jump. */
	protected final BasicBlockKey target;
	
	/** String representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes an unconditional jump to the specified target.
	 *
	 * @param __t The target to jump to.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/01
	 */
	public HLOUnconditionalJump(BasicBlockKey __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.target = __t;
	}
		
	/**
	 * {@inheritDoc}
	 * @since 2017/09/01
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof HLOUnconditionalJump))
			return false;
		
		HLOUnconditionalJump o = (HLOUnconditionalJump)__o;
		return this.target.equals(o.target);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/01
	 */
	@Override
	public int hashCode()
	{
		return this.target.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/01
	 */
	@Override
	public BasicBlockKey target()
	{
		return this.target;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/01
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = String.format(
				"Unconditional Jump %s", this.target)));
		
		return rv;
	}
}

