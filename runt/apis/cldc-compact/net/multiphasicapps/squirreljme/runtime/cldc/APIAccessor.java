// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.cldc;

import net.multiphasicapps.squirreljme.runtime.cldc.core.Clock;

/**
 * This is used to provide access to SquirrelJME specific APIs.
 *
 * @since 2017/12/07
 */
public final class APIAccessor
{
	/**
	 * The list of available APIs.
	 *
	 * This can be initialized by the virtual machine by one of two ways:
	 * Overriding {@link APIAccessor#_APILIST} using an unspecified means to
	 * return the appropriate object.
	 * Setting this field to an object array containing the valid set of APIs.
	 */
	private static final Object[] _APILIST =
		__apiList();
	
	/**
	 * Not used.
	 *
	 * @since 2017/12/07
	 */
	private APIAccessor()
	{
	}
	
	/**
	 * Returns the clock.
	 *
	 * @return The system clock.
	 * @since 2017/12/07
	 */
	public static final Clock clock()
	{
		return APIAccessor.<Clock>of(APIList.CLOCK, Clock.class);
	}
	
	/**
	 * Returns the object which is associated with the given API.
	 *
	 * @param __id The ID of the API to get.
	 * @return The object for the class interface of the given API.
	 * @throws IllegalArgumentException If the API is not valid.
	 * @since 2017/12/07
	 */
	public static final Object of(int __id)
		throws IllegalArgumentException
	{
		// {@squirreljme.error ZZ0c API index is outside of bounds.}
		Object[] apilist = APIAccessor._APILIST;
		if (__id < 0 || __id >= apilist.length)
			throw new IllegalArgumentException("ZZ0c");
		
		// {@squirreljme.error ZZ0d A requested API was not implemented.}
		Object rv = apilist[__id];
		if (rv == null)
			throw new IllegalArgumentException("ZZ0d");
		return rv;
	}
	
	/**
	 * Obtains the API with the given index and casts to the given class.
	 *
	 * @param <T> The class to cast to.
	 * @param __id The ID of the API to return.
	 * @param __cl The class to cast to.
	 * @return The casted class of the given API.
	 * @throws ClassCastException If the class type is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/07
	 */
	public static final <T> T of(int __id, Class<T> __cl)
		throws ClassCastException, NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		return __cl.cast(APIAccessor.of(__id));
	}
	
	/**
	 * This returns {@link APIAccessor#_APILIST}.
	 *
	 * @return {@link APIAccessor#_APILIST}.
	 * @since 2017/12/07
	 */
	private static final Object[] __apiList()
	{
		return APIAccessor._APILIST;
	}
}

