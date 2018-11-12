// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel.units;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * These are the units which are available in the game and what are actively
 * used within the game.
 *
 * @since 2017/02/14
 */
public enum UnitType
{
	/** Starting location. */
	START_LOCATION,
	
	/** End. */
	;
	
	/** A reference to the unit information. */
	private Reference<BaseUnitInfo> _info;
	
	/**
	 * Returns the information which is associated with the given unit.
	 *
	 * @return The unit information.
	 * @since 2017/02/14
	 */
	public BaseUnitInfo baseInfo()
	{
		Reference<BaseUnitInfo> ref = this._info;
		BaseUnitInfo rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			throw new todo.TODO();
			/*this._info = new WeakReference<>(rv = new UnitInfo(this));*/
		
		return rv;
	}
}

