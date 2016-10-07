// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreldigger.game.entity;

import net.multiphasicapps.squirreldigger.game.Game;

/**
 * This class represents an entity that exists within the game.
 *
 * @since 2016/10/07
 */
public class Entity
{
	/** The game this entity is within. */
	protected final Game game;
	
	/**
	 * Initializes the entity.
	 *
	 * @param __g The game the entity is in.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/07
	 */
	public Entity(Game __g)
		throws NullPointerException
	{
		// Check
		if (__g == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.game = __g;
	}
}

