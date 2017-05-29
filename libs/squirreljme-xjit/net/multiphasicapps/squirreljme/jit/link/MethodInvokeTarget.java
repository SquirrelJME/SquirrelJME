// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.link;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This represents the target of an invocation based on a given invocation
 * type.
 *
 * @since 2017/04/01
 */
public final class MethodInvokeTarget
{
	/** The referenced method. */
	protected final MethodReference reference;
	
	/** The invocation type. */
	protected final MethodInvokeType type;
	
	/** String representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the invocation target.
	 *
	 * @param __to The target reference.
	 * @param __t The type of invocation to perform.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/04/01
	 */
	public MethodInvokeTarget(MethodReference __to, MethodInvokeType __t)
		throws NullPointerException
	{
		// Check
		if (__to == null || __t == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.reference = __to;
		this.type = __t;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/04/01
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof MethodInvokeTarget))
			return false;
		
		MethodInvokeTarget o = (MethodInvokeTarget)__o;
		return this.reference.equals(o.reference) &&
			this.type.equals(o.type);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/04/01
	 */
	@Override
	public int hashCode()
	{
		return this.reference.hashCode() ^ this.type.hashCode();
	}
	
	/**
	 * Returns the reference to the method.
	 *
	 * @return The referenced method.
	 * @since 2017/04/01
	 */
	public MethodReference reference()
	{
		return this.reference;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/04/01
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		// Check
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = this.reference + ":" +
				this.type));
		
		return rv;
	}
	
	/**
	 * Returns the type of the invocation.
	 *
	 * @return The invocation type.
	 * @since 2017/04/01
	 */
	public MethodInvokeType type()
	{
		return this.type;
	}
}

