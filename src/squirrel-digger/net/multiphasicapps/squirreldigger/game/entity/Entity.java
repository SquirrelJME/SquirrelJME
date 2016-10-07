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
	
	/** The type of entity that this is. */
	private volatile EntityType _type;
	
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
	
	/**
	 * Initializes the entity so that it is the given type as if it were
	 * freshly spawned.
	 *
	 * @param __t The type of entity to initialize to.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/07
	 */
	public void initializeTo(EntityType __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Set the type
		this._type = __t;
	}
}

