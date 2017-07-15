// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.java;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This describes a reference to a method.
 *
 * @since 2017/06/12
 */
public final class MethodReference
	extends MemberReference
{
	/** The method descriptor. */
	protected final MethodDescriptor descriptor;
	
	/** Is this an interface? */
	protected final boolean isinterface;
	
	/** The name of the method. */
	protected final MethodName name;
	
	/** String representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the method reference.
	 *
	 * @param __c The class the member resides in.
	 * @param __i The name of the member.
	 * @param __t The descriptor of the member.
	 * @param __int Does this refer to an interface method?
	 * @since 2017/06/12
	 */
	public MethodReference(ClassName __c, MethodName __i,
		MethodDescriptor __t, boolean __int)
		throws NullPointerException
	{
		super(__c);
		
		// Check
		if (__t == null || __i == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.descriptor = __t;
		this.name = __i;
		this.isinterface = __int;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/06/12
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (!(__o instanceof MethodReference))
			return false;
		
		MethodReference o = (MethodReference)__o;
		return this.classname.equals(o.classname) &&
			this.name.equals(o.name) &&
			this.descriptor.equals(o.descriptor) &&
			this.isinterface == o.isinterface;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/06/12
	 */
	@Override
	public int hashCode()
	{
		return this.classname.hashCode() ^ this.name.hashCode() ^
			this.descriptor.hashCode() ^ (this.isinterface ? 1 : 0);
	}
	
	/**
	 * Is this an interface method reference?
	 *
	 * @return {@code true} if a reference to an interface method.
	 * @since 2017/07/15
	 */
	public boolean isInterface()
	{
		return this.isinterface;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/07/08
	 */
	@Override
	public final MethodName memberName()
	{
		return this.name;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/06/12
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = String.format(
				"%smethod %s::%s%s",
				(this.isinterface ? "interface-" : ""), this.classname,
				this.name, this.descriptor)));
		
		return rv;
	}
}

