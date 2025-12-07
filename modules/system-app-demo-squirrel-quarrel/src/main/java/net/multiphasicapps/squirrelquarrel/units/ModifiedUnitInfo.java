// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel.units;

/**
 * This class allows for the modification of unit information which is used
 * to manage such stuff as upgrades which happen over the course of the game.
 *
 * @since 2018/03/18
 */
public final class ModifiedUnitInfo
{
	/** Basic unit information. */
	protected final BaseUnitInfo base;
	
	/**
	 * Initializes the modified unit information.
	 *
	 * @param __i The base unit information.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/19
	 */
	public ModifiedUnitInfo(BaseUnitInfo __i)
		throws NullPointerException
	{
		if (__i == null)
			throw new NullPointerException("NARG");
		
		this.base = __i;
	}
}

