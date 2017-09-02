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
import net.multiphasicapps.squirreljme.jit.java.TypedVariable;
import net.multiphasicapps.squirreljme.jit.java.Variable;
import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This describes a high level copy operation from one variable to another.
 *
 * @since 2017/09/01
 */
public final class HLOCopy
	implements HLO
{
	/** The source variable. */
	protected final TypedVariable source;
	
	/** The target variable. */
	protected final Variable destination;
	
	/** String representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the copy operation.
	 *
	 * @param __src The source variable.
	 * @param __dest The destination variable.
	 * @throws JITException If the copy operation could not be appended.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/01
	 */
	public HLOCopy(TypedVariable __src, Variable __dest)
		throws JITException, NullPointerException
	{
		// Check
		if (__src == null || __dest == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.source = __src;
		this.destination = __dest;
	}
	
	/**
	 * Returns the destination variable.
	 *
	 * @return The destination variable.
	 * @since 2017/09/01
	 */
	public Variable destination()
	{
		return this.destination;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/01
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof HLOCopy))
			return false;
		
		HLOCopy o = (HLOCopy)__o;
		return this.source.equals(o.source) &&
			this.destination.equals(o.destination);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/01
	 */
	@Override
	public int hashCode()
	{
		return this.source.hashCode() ^
			this.destination.hashCode();
	}
	
	/**
	 * Returns the source.
	 *
	 * @return The source.
	 * @since 2017/09/01
	 */
	public TypedVariable source()
	{
		return this.source;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/01
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = String.format(
				"Copy %s -> %s", this.source, this.destination)));
		
		return rv;
	}
}

