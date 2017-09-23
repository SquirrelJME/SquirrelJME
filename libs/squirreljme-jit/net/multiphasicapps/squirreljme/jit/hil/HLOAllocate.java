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
import net.multiphasicapps.squirreljme.jit.java.ClassName;
import net.multiphasicapps.squirreljme.jit.java.Variable;
import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This operation allocate the specified class and places it within the
 * given destination register.
 *
 * @since 2017/09/22
 */
public final class HLOAllocate
	implements HLO
{
	/** The destination variable. */
	protected final Variable destination;
	
	/** The class to allocate. */
	protected final ClassName allocate;
	
	/** String representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the allocation operation.
	 *
	 * @param __dest The destination to place the allocated data into.
	 * @param __cl The class type to allocate.
	 * @throws JITException If the allocation is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/22
	 */
	public HLOAllocate(Variable __dest, ClassName __cl)
		throws JITException, NullPointerException
	{
		if (__dest == null || __cl == null)
			throw new NullPointerException("NARG");
		
		this.destination = __dest;
		this.allocate = __cl;
	}
	
	/**
	 * Returns the class being allocated.
	 *
	 * @return The class to allocate.
	 * @since 2017/09/22
	 */
	public ClassName allocate()
	{
		return this.allocate;
	}
	
	/**
	 * Returns the destination variable.
	 *
	 * @return The destination variable.
	 * @since 2017/09/22
	 */
	public Variable destination()
	{
		return this.destination;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/22
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (!(__o instanceof HLOAllocate))
			return false;
		
		HLOAllocate o = (HLOAllocate)__o;
		return this.destination.equals(o.destination) &&
			this.allocate.equals(o.allocate);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/22
	 */
	@Override
	public int hashCode()
	{
		return this.destination.hashCode() ^
			this.allocate.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/22
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = String.format(
				"Allocate %s into %s", this.allocate, this.destination)));
		
		return rv;
	}
}

