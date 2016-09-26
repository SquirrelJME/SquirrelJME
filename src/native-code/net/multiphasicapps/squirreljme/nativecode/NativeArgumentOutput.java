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
 * This is returned by the argument allocator which is used to pass a specially
 * referenced object from the allocation input and bridge it to the output.
 *
 * This class is immutable.
 *
 * @param <X> The type of special data stored.
 * @see NativeArgumentInput
 * @since 2016/09/25
 */
public final class NativeArgumentOutput<X>
{
	/** The used allocation. */
	protected final NativeAllocation allocation;
	
	/** The special value. */
	protected final X special;
	
	/**
	 * Initializes the argument output.
	 *
	 * @param __a The allocation to use.
	 * @param __v The special value to store.
	 * @throws NullPointerException On null arguments, except for {@code __v}.
	 * @since 2016/09/25
	 */
	public NativeArgumentOutput(NativeAllocation __a, X __v)
		throws NullPointerException
	{
		// Check
		if (__a == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.allocation = __a;
		this.special = __v;
	}
	
	/**
	 * This returns the allocation which was used to specify how an input
	 * argument is to be stored.
	 *
	 * @return The allocation that was created for the argument.
	 * @since 2016/09/25
	 */
	public final NativeAllocation allocation()
	{
		return this.allocation;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/25
	 */
	@Override
	public final boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof NativeArgumentOutput))
			return false;
		
		// Compare
		NativeArgumentOutput o = (NativeArgumentOutput)__o;
		return this.allocation.equals(o.allocation) &&
			Objects.equals(this.special, o.special);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/25
	 */
	@Override
	public final int hashCode()
	{
		return this.allocation.hashCode() ^ Objects.hashCode(this.special);
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
		return "{allocation=" + this.allocation + ", special=" +
			this.special + "}";
	}
	
	/**
	 * Allocates an array using the specified special type.
	 *
	 * @param <X> The special value to store.
	 * @param __n The number of elements to use.
	 * @return An array for the given special type.
	 * @since 2016/09/25
	 */
	@SuppressWarnings({"unchecked"})
	public static <X> NativeArgumentOutput<X>[] allocateArray(int __n)
	{
		return (NativeArgumentOutput<X>[])
			((Object)new NativeArgumentOutput[__n]);
	}
}

