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
 * This describes a reference to a method.
 *
 * @since 2017/06/12
 */
public final class MethodReference
	extends MemberReference
{
	/** The method handle. */
	protected final MethodHandle handle;
	
	/** Is this an interface? */
	protected final boolean isinterface;
	
	/** String representation. */
	private volatile Reference<String> _string;
	
	/** The method index. */
	
	/**
	 * Initializes the method reference.
	 *
	 * @param __h The handle of the class.
	 * @param __int Does this refer to an interface method?
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/16
	 */
	public MethodReference(MethodHandle __h, boolean __int)
	{
		super(__h.outerClass());
		
		// Check
		if (__h == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.handle = __h;
		this.isinterface = __int;
	}
	
	/**
	 * Initializes the method reference.
	 *
	 * @param __c The class the member resides in.
	 * @param __i The name of the member.
	 * @param __t The descriptor of the member.
	 * @param __int Does this refer to an interface method?
	 * @throws NullPointerException On null arguments.
	 * @since 2017/06/12
	 */
	public MethodReference(ClassName __c, MethodName __i,
		MethodDescriptor __t, boolean __int)
		throws NullPointerException
	{
		this(new MethodHandle(__c, __i, __t), __int);
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
		return this.handle.equals(o.handle) &&
			this.isinterface == o.isinterface;
	}
	
	/**
	 * Returns the method handle.
	 *
	 * @return The method handle.
	 * @since 2017/09/16
	 */
	public MethodHandle handle()
	{
		return this.handle;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/06/12
	 */
	@Override
	public int hashCode()
	{
		return this.handle.hashCode() ^ (this.isinterface ? 1 : 0);
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
		return this.handle.name();
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
				"%smethod %s",
				(this.isinterface ? "interface-" : ""), this.handle)));
		
		return rv;
	}
}

