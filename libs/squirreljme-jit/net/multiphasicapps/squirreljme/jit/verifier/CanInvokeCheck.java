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
import net.multiphasicapps.squirreljme.jit.java.MethodInvocationType;
import net.multiphasicapps.squirreljme.jit.java.MethodReference;

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
	/** The source class. */
	protected final ClassName sourceclass;
	
	/** The source method name. */
	protected final MethodName sourcename;
	
	/** The source descriptor. */
	protected final 
	
	/** The type of invocation to perform. */
	protected final MethodInvocationType type;
	
	/** The method being invoke. */
	protected final MethodReference target;
	
	/** String representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the check to see if the given class can invoke the given
	 * method in another class.
	 *
	 * @param __srccl The class doing the invoking.
	 * @param __srcname The name of the method doing the call.
	 * @param __srcdesc The descriptor of the method doing the call.
	 * @param __t The type of invocation being performed.
	 * @param __m The target method being called.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/16
	 */
	public CanInvokeCheck(ClassName __srccl, MethodName __srcname,
		MethodDescriptor __srcdesc, MethodInvocationType __t,
		MethodReference __m)
		throws NullPointerException
	{
		// Check
		if (__srccl == null || __srcname == null || __srcdesc == null ||
			__t == null || __m == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.sourceclass = __srccl;
		this.sourceclass = __srcname;
		this.sourceclass = __srcdesc;
		this.type = __t;
		this.target = __m;
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
			this.type.equals(o.type) &&
			this.target.equals(o.target);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/16
	 */
	@Override
	public int hashCode()
	{
		return this.source.hashCode() ^
			this.type.hashCode() ^
			this.target.hashCode();
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

