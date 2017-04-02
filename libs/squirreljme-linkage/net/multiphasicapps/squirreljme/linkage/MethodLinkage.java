// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.linkage;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This is a link from a method which invokes another method.
 *
 * @since 2016/09/06
 */
public final class MethodLinkage
	extends MemberLinkage<MethodReference>
{
	/** The type of link this is. */
	protected final MethodInvokeType type;
	
	/** The method linkage table. */
	private volatile Reference<MethodTableLinkage> _table;
	
	/** String reference. */
	private volatile Reference<String> _string;
	
	/** Invocation target. */
	private volatile Reference<MethodInvokeTarget> _target;
	
	/**
	 * Initializes the link of one method to another.
	 *
	 * @param __from The source method.
	 * @param __to The target method.
	 * @param __t The type of call performed.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/06
	 */
	public MethodLinkage(MethodReference __from,
		MethodReference __to, MethodInvokeType __t)
		throws NullPointerException
	{
		super(__from, __to);
		
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.type = __t;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/06
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Must match
		if (!(__o instanceof MethodLinkage))
			return false;
		
		// Check super first
		if (!super.equals(__o))
			return false;
		
		return this.type.equals(((MethodLinkage)__o).type);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/06
	 */
	@Override
	public int hashCode()
	{
		return super.hashCode() ^ this.type.hashCode();
	}
	
	/**
	 * Returns the invoke target for this linkage.
	 *
	 * @return The invoke target.
	 * @since 2017/04/01
	 */
	public MethodInvokeTarget invokeTarget()
	{
		Reference<MethodInvokeTarget> ref = this._target;
		MethodInvokeTarget rv;
		
		// Check
		if (ref == null || null == (rv = ref.get()))
			this._target = new WeakReference<>(
				(rv = new MethodInvokeTarget(this.to, this.type)));
		
		return rv;
	}
	
	/**
	 * This returns the linkage to the target method's table.
	 *
	 * @return The linkage to the method table.
	 * @since 2017/03/22
	 */
	public MethodTableLinkage tableLink()
	{
		Reference<MethodTableLinkage> ref = this._table;
		MethodTableLinkage rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._table = new WeakReference<>(
				(rv = new MethodTableLinkage(this)));
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/06
	 */
	@Override
	public String toString()
	{
		// Get
		Reference<String> ref = this._string;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = this.from + "->" +
				this.to + "~[" + this.type + "]"));
		
		return rv;
	}
	
	/**
	 * Returns the type of call which was performed.
	 *
	 * @return The method call type.
	 * @since 2016/09/06
	 */
	public MethodInvokeType type()
	{
		return this.type;
	}
}

