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
import java.util.Arrays;
import java.util.List;
import net.multiphasicapps.squirreljme.jit.java.MethodHandle;
import net.multiphasicapps.squirreljme.jit.java.TypedVariable;
import net.multiphasicapps.squirreljme.jit.JITException;
import net.multiphasicapps.util.unmodifiable.UnmodifiableList;

/**
 * This represents a method call.
 *
 * @since 2017/09/20
 */
public final class HLOMethodCall
	implements HLO
{
	/** The type of lookup to perform. */
	protected final MethodLookupType lookup;
	
	/** The method to call. */
	protected final MethodHandle method;
	
	/** The arguments to the method. */
	private final TypedVariable[] _args;
	
	/** Arguments as a list. */
	private volatile Reference<List<TypedVariable>> _listargs;
	
	/** String representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Appends a method call to the given method.
	 *
	 * @param __mlt The type of lookup to perform for the given method.
	 * @param __mh The method handle of the target method to call.
	 * @param __args The input arguments to the method.
	 * @throws JITException If the method call is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/20
	 */
	public HLOMethodCall(MethodLookupType __mlt, MethodHandle __mh,
		TypedVariable... __args)
		throws JITException, NullPointerException
	{
		// Check
		if (__mlt == null || __mh == null || __args == null)
			throw new NullPointerException("NARG");
		
		// Copy for nulls
		__args = __args.clone();
		for (TypedVariable v : __args)
			if (v == null)
				throw new NullPointerException("NARG");
		
		// Set
		this.lookup = __mlt;
		this.method = __mh;
		this._args = __args;
	}
	
	/**
	 * Returns the arguments that this method call uses.
	 *
	 * @return The method call arguments.
	 * @since 2017/09/20
	 */
	public List<TypedVariable> arguments()
	{
		Reference<List<TypedVariable>> ref = this._listargs;
		List<TypedVariable> rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._listargs = new WeakReference<>((rv = UnmodifiableList.
				<TypedVariable>of(Arrays.<TypedVariable>asList(this._args))));
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/20
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof HLOMethodCall))
			return false;
		
		HLOMethodCall o = (HLOMethodCall)__o;
		return this.lookup.equals(o.lookup) &&
			this.method.equals(o.method) &&
			Arrays.equals(this._args, o._args);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/20
	 */
	@Override
	public int hashCode()
	{
		return this.lookup.hashCode() ^
			this.method.hashCode() ^
			arguments().hashCode();
	}
	
	/**
	 * Returns the type of lookup to perform.
	 *
	 * @return The lookup type to be performed.
	 * @since 2017/09/20
	 */
	public MethodLookupType lookup()
	{
		return this.lookup;
	}
	
	/**
	 * Returns the method that is to be invoked.
	 *
	 * @return The method to be invoked.
	 * @since 2017/09/20
	 */
	public MethodHandle method()
	{
		return this.method;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/20
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = String.format(
				"Call %s(%s) via %s", this.method, arguments(), this.lookup)));
		
		return rv;
	}
}

