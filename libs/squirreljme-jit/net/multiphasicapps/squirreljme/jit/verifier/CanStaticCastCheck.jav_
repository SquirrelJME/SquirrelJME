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
import net.multiphasicapps.squirreljme.jit.java.FieldDescriptor;

/**
 * This is a check which is used to determine if the source type can be
 * statically cast by the compiler to the destination type.
 *
 * @since 2017/09/20
 */
public final class CanStaticCastCheck
	implements VerificationCheck
{
	/** The type to cast from. */
	protected final FieldDescriptor from;
	
	/** The type to cast to. */
	protected final FieldDescriptor to;
	
	/** The string representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the check for the static cast for the given types.
	 *
	 * @param __from The type to cast from.
	 * @param __to The type to cast to.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/20
	 */
	public CanStaticCastCheck(FieldDescriptor __from, FieldDescriptor __to)
		throws NullPointerException
	{
		if (__from == null || __to == null)
			throw new NullPointerException("NARG");
		
		this.from = __from;
		this.to = __to;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/20
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (!(__o instanceof CanStaticCastCheck))
			return false;
		
		CanStaticCastCheck o = (CanStaticCastCheck)__o;
		return this.from.equals(o.from) &&
			this.to.equals(o.to);
	}
	
	/**
	 * Returns the type to cast from.
	 *
	 * @return The source type.
	 * @since 2017/09/20
	 */
	public FieldDescriptor from()
	{
		return this.from;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/20
	 */
	@Override
	public int hashCode()
	{
		return this.from.hashCode() ^ this.to.hashCode();
	}
	
	/**
	 * Returns the type to cast to.
	 *
	 * @return The destination type.
	 * @since 2017/09/20
	 */
	public FieldDescriptor to()
	{
		return this.to;
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
				"%s static casts to %s?", this.from, this.to)));
		
		return rv;
	}
}

