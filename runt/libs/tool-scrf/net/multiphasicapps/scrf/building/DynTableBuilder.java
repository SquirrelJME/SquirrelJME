// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.scrf.building;

import java.util.LinkedHashMap;
import java.util.Map;
import net.multiphasicapps.classfile.MethodReference;
import net.multiphasicapps.scrf.DynTableLocation;
import net.multiphasicapps.scrf.InvokedMethodReference;
import net.multiphasicapps.scrf.InvokeType;

/**
 * This class is used to build the dynamic table which refers to locations
 * which are dynamically linked or determined at run-time.
 *
 * @since 2019/02/23
 */
public final class DynTableBuilder
{
	/** Dynamic table map. */
	private final Map<Integer, Object> _dyntable =
		new LinkedHashMap<>();
	
	/** The next index in the table. */
	private int _nextdx;
	
	/**
	 * Add object to the dynamic table.
	 *
	 * @param __v The value to add.
	 * @return The location of this entry.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/02/24
	 */
	public final DynTableLocation add(Object __v)
		throws NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		// Push to dynamic table
		int dx = this._nextdx++;
		this._dyntable.put(dx, __v);
		
		// New location
		return new DynTableLocation(dx);
	}
	
	/**
	 * Adds reference to another method based on a given invocation type.
	 *
	 * @param __t The type of invocation to perform.
	 * @param __m The reference to the method.
	 * @return The dynamic table location.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/02/24
	 */
	public final DynTableLocation addInvoke(InvokeType __t,
		MethodReference __m)
		throws NullPointerException
	{
		if (__t == null || __m == null)
			throw new NullPointerException("NARG");
		
		return this.add(new InvokedMethodReference(__t, __m));
	}
}

