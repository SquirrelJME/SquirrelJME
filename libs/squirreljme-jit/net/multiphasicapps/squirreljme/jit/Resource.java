// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

/**
 * This contains the data for a single resource.
 *
 * @since 2017/06/08
 */
@Deprecated
public final class Resource
{
	/** The name of the resource. */
	protected final String name;
	
	/** The resource data. */
	private final byte[] _data;
	
	/**
	 * Initializes the resource.
	 *
	 * @param __n The name of the resource.
	 * @param __d The resource data, this is used directly and is not copied.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/06/08
	 */
	Resource(String __n, byte[] __d)
		throws NullPointerException
	{
		// Check
		if (__n == null || __d == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.name = __n;
		this._data = __d;
	}
	
	/**
	 * Returns the name of the resource.
	 *
	 * @return The resource name.
	 * @since 2017/06/08
	 */
	public final String name()
	{
		return this.name();
	}
}

