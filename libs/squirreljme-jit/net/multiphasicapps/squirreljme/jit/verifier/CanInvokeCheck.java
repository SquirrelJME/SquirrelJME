// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.verifier;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.squirreljme.jit.java.ClassName;
import net.multiphasicapps.squirreljme.jit.java.MethodHandle;
import net.multiphasicapps.squirreljme.jit.java.MethodInvocationType;

/**
 * This check is used to determine whether a given source class is able to
 * invoke a method in the given target class using the specified type of
 * invocation.
 *
 * @since 2017/09/16
 */
public final class CanInvokeCheck
	implements VerificationCheck
{
	/** The source method. */
	protected final MethodHandle source;
	
	/** The target method. */
	protected final MethodHandle target;
	
	/** The type of invocation to perform. */
	protected final MethodInvocationType type;
	
	/** String representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the check to see if the given method can invoke the given
	 * method in another class.
	 *
	 * @param __src The source method which is invoking the other method.
	 * @param __dest The target method which is being invoked.
	 * @param __t The type of invocation being performed.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/16
	 */
	public CanInvokeCheck(MethodHandle __src, MethodHandle __dest,
		MethodInvocationType __t)
		throws NullPointerException
	{
		// Check
		if (__src == null || __dest == null || __t == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.source = __src;
		this.target = __dest;
		this.type = __t;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/16
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof CanInvokeCheck))
			return false;
		
		CanInvokeCheck o = (CanInvokeCheck)__o;
		return this.source.equals(o.source) &&
			this.target.equals(o.target) &&
			this.type.equals(o.type);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/16
	 */
	@Override
	public int hashCode()
	{
		return this.source.hashCode() ^
			this.target.hashCode() ^
			this.type.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/16
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = String.format(
				"%s invokes %s as %s?", this.source, this.target, this.type)));
		
		return rv;
	}
}

