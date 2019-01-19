// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.classtree;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import net.multiphasicapps.collections.SortedTreeMap;

/**
 * This class describes a package which contains a bunch of classes that exist
 * within the package.
 *
 * @since 2019/01/17
 */
public final class Package
{
	/** The name of the package. */
	protected final String name;
	
	/** Units which are in this package. */
	private final Map<String, Unit> _units;
	
	/** String representation. */
	private Reference<String> _string;
	
	/**
	 * Initializes the package with all of the contained units.
	 *
	 * @param __n The name of this package.
	 * @param __m The package and maps.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/18
	 */
	public Package(String __n, Map<String, Unit> __m)
		throws NullPointerException
	{
		if (__n == null || __m == null)
			throw new NullPointerException("NARG");
		
		// Copy values
		Map<String, Unit> units = new SortedTreeMap<>();
		for (Map.Entry<String, Unit> e : __m.entrySet())
		{
			String k = e.getKey();
			Unit v = e.getValue();
			if (k == null || v == null)
				throw new NullPointerException("NARG");
			
			units.put(k, v);
		}
		
		this.name = __n;
		this._units = units;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/01/19
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>(
				(rv = this._units.toString()));
		
		return rv;
	}
}

