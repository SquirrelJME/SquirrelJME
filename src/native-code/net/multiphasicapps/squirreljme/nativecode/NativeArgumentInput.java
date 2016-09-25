// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.nativecode;

import java.util.Objects;

/**
 * This is passed as input to the argument allocator which is used to map input
 * types with a specially stored mapping value, so that when it is passed
 * through the argument allocator it stores the specific object in the output.
 *
 * This class is immutable.
 *
 * @param <X> The type of special data stored.
 * @see NativeArgumentOutput
 * @since 2016/09/25
 */
public final class NativeArgumentInput<X>
{
	/** The type of value to allocate. */
	protected final NativeRegisterType type;
	
	/** The special value. */
	protected final X special;
	
	/**
	 * Initializes the input for argument allocation.
	 *
	 * @param __t The type of value to allocate.
	 * @param __v The special value to associate, this is passed to the output.
	 * @throws NullPointerException On null arguments, except for {@code __v}.
	 * @since 2016/09/25
	 */
	public NativeArgumentInput(NativeRegisterType __t, X __v)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.type = __t;
		this.special = __v;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/25
	 */
	@Override
	public final boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof NativeArgumentInput))
			return false;
		
		// Compare
		NativeArgumentInput o = (NativeArgumentInput)__o;
		return this.type.equals(o.type) &&
			Objects.equals(this.special, o.special);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/25
	 */
	@Override
	public final int hashCode()
	{
		return this.type.hashCode() ^ Objects.hashCode(this.special);
	}
	
	/**
	 * Returns the special value.
	 *
	 * @return The special value.
	 * @since 2016/09/25
	 */
	public final X special()
	{
		return this.special;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/25
	 */
	@Override
	public final String toString()
	{
		return "{type=" + this.type + ", special=" +
			this.special + "}";
	}
	
	/**
	 * Returns the type of value to allocate.
	 *
	 * @return The type of value to allocate.
	 * @since 2016/09/25
	 */
	public final NativeRegisterType type()
	{
		return this.type;
	}
}

