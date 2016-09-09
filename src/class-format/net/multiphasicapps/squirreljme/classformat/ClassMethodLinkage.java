// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.classformat;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This is a link from a method which invokes another method.
 *
 * @since 2016/09/06
 */
public final class GenericMethodLinkage
	extends ClassMemberLinkage<ClassMethodReference>
{
	/** The type of link this is. */
	protected final ClassMethodInvokeType type;
	
	/** String reference. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the link of one method to another.
	 *
	 * @param __from The source method.
	 * @param __to The target method.
	 * @param __t The type of call performed.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/06
	 */
	public GenericMethodLinkage(ClassMethodReference __from,
		ClassMethodReference __to, ClassMethodInvokeType __t)
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
		if (!(__o instanceof GenericMethodLinkage))
			return false;
		
		// Check super first
		if (!super.equals(__o))
			return false;
		
		return this.type.equals(((GenericMethodLinkage)__o).type);
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
	public ClassMethodInvokeType type()
	{
		return this.type;
	}
}

