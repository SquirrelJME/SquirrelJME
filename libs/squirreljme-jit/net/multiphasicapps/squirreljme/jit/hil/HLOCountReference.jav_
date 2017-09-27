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
 * This describes a reference count change on object within the program.
 *
 * @since 2017/09/01
 */
public final class HLOCountReference
	implements HLO
{
	/** The variable count. */
	protected final TypedVariable count;
	
	/** Counting up? */
	protected final boolean up;
	
	/** String representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes a reference count of the given object.
	 *
	 * @param __v The variable to count.
	 * @param __up If {@code true} then the reference is counted up.
	 * @throws JITException If the variable is not an object.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/26
	 */
	public HLOCountReference(TypedVariable __v, boolean __up)
		throws JITException, NullPointerException
	{
		// Check
		if (__v == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error JI2a Cannot reference non-objects. (The variable
		// to reference count)}
		if (!__v.isObject())
			throw new JITException(String.format("JI2a %s", __v));
		
		// Set
		this.count = __v;
		this.up = __up;
	}
	
	/**
	 * Returns the variable that is going to be counted.
	 *
	 * @return The variable to count.
	 * @since 2017/09/01
	 */
	public TypedVariable count()
	{
		return this.count;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/01
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof HLOCountReference))
			return false;
		
		HLOCountReference o = (HLOCountReference)__o;
		return this.count.equals(o.count) &&
			this.up == o.up;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/01
	 */
	@Override
	public int hashCode()
	{
		return this.count.hashCode() ^ (this.up ? 1 : 0);
	}
	
	/**
	 * Is the reference being counted down?
	 *
	 * @return If the reference is being counted down.
	 * @since 2017/09/01
	 */
	public boolean isDown()
	{
		return !this.up;
	}
	
	/**
	 * Is the reference being counted up?
	 *
	 * @return If the reference is being counted up.
	 * @since 2017/09/01
	 */
	public boolean isUp()
	{
		return this.up;
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
				"Count %s %s", this.count, (this.up ? "up" : "down"))));
		
		return rv;
	}
}

