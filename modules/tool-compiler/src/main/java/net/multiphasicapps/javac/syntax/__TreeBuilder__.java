// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.syntax;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.collections.UnmodifiableList;
import net.multiphasicapps.collections.UnmodifiableMap;

/**
 * This is a utility class which can be used to build a tree from a structure,
 * a tree is essentially just a map of data.
 *
 * @since 2019/01/17
 */
final class __TreeBuilder__
{
	/** Resulting set. */
	private final Map<String, Object> _out =
		new LinkedHashMap<>();
	
	/**
	 * Adds the specified element.
	 *
	 * @param __k The key.
	 * @param __v The value.
	 * @return {@code this}.
	 * @throws NullPointerException If no key was specified.
	 * @since 2019/01/17
	 */
	public final __TreeBuilder__ add(String __k, Object __v)
		throws NullPointerException
	{
		if (__k == null)
			throw new NullPointerException("NARG");
		
		this._out.put(__k, __v);
		return this;
	}
	
	/**
	 * Adds the specified element.
	 *
	 * @param __k The key.
	 * @param __l The list.
	 * @return {@code this}.
	 * @throws NullPointerException If no key was specified.
	 * @since 2019/01/17
	 */
	public final __TreeBuilder__ addList(String __k, Object[] __l)
		throws NullPointerException
	{
		if (__k == null)
			throw new NullPointerException("NARG");
		
		this._out.put(__k, UnmodifiableList.of(
			Arrays.asList((__l == null ? new Object[0] : __l.clone()))));
		return this;
	}
	
	/**
	 * Builds the resulting map.
	 *
	 * @return The resulting map.
	 * @since 2019/01/17
	 */
	public final Map<String, Object> build()
	{
		return UnmodifiableMap.of(
			new LinkedHashMap<String, Object>(this._out));
	}
}

