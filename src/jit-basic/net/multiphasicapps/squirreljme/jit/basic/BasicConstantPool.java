// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.basic;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This represents the shared constant pool which all basic namespaces use to
 * store specific constant values.
 *
 * @since 2016/09/11
 */
public class BasicConstantPool
{
	/** Stores objects that are stored in the pool. */
	private final Map<Object, BasicConstantEntry<Object>> _objects =
		new LinkedHashMap<>();
	
	/**
	 * Initializes the basic pool.
	 *
	 * @since 2016/09/11
	 */
	BasicConstantPool()
	{
	}
	
	/**
	 * Adds a string to the constant pool.
	 *
	 * @param __s The string to add.
	 * @return The constant entry for the string.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/11
	 */
	public BasicConstantEntry<String> addString(String __s)
		throws NullPointerException
	{
		return this.<String>__store(__s);
	}
	
	/**
	 * Internally gets an entry that already exists, otherwise it creates a
	 * entry to store the value.
	 *
	 * @param <T> The type of value to store.
	 * @param __v The value to store.
	 * @return The entry for the value.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/11
	 */
	@SuppressWarnings({"unchecked"})
	private <T> BasicConstantEntry<T> __store(T __v)
		throws NullPointerException
	{
		// Check
		if (__v == null)
			throw new NullPointerException("NARG");
		
		// Get entry, it may be already in the pool
		Map<Object, BasicConstantEntry<Object>> objects = this._objects;
		BasicConstantEntry<Object> rv = objects.get(__v);
		
		// Does not exist
		if (rv == null)
			objects.put(__v,
				(rv = new BasicConstantEntry<Object>(objects.size(), __v)));
		
		// Return it
		return (BasicConstantEntry<T>)rv;
	}
}

