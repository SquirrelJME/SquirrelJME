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
import net.multiphasicapps.squirreljme.jit.java.TypedVariable;
import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This jumps to the target if the given variable is not null.
 *
 * @since 2017/09/22
 */
public final class HLOJumpOnNotNull
	implements HLOJump, HLOSingularConditional
{
	/** The variable to check. */
	protected final TypedVariable variable;
	
	/** The jump target. */
	protected final BasicBlockKey target;
	
	/** String representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes a jump if the given variable is not null.
	 *
	 * @param __v The variable to check.
	 * @param __t The target to jump to.
	 * @throws JITException If the variable is not an object.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/22
	 */
	public HLOJumpOnNotNull(TypedVariable __v, BasicBlockKey __t)
		throws JITException, NullPointerException
	{
		// Check
		if (__v == null || __t == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error JI2p Cannot check a non-object against null.
		// (The variable to check)}
		if (!__v.isObject())
			throw new JITException(String.format("JI2p %s", __v));
		
		// Set
		this.variable = __v;
		this.target = __t;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/22
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof HLOJumpOnNotNull))
			return false;
		
		HLOJumpOnNotNull o = (HLOJumpOnNotNull)__o;
		return this.variable.equals(o.variable) &&
			this.target.equals(o.target);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/22
	 */
	@Override
	public int hashCode()
	{
		return this.variable.hashCode() ^
			this.target.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/22
	 */
	@Override
	public BasicBlockKey target()
	{
		return this.target;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/22
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = String.format(
				"Jump to %s if %s != null", this.target, this.variable)));
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/22
	 */
	@Override
	public TypedVariable variable()
	{
		return this.variable;
	}
}

