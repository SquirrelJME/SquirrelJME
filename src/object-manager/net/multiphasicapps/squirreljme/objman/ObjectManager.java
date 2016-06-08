// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.objman;

import net.multiphasicapps.squirreljme.memory.MemoryIOException;
import net.multiphasicapps.squirreljme.memory.MemoryPool;
import net.multiphasicapps.squirreljme.memory.MemoryPoolManager;

/**
 * This is the manager which manages all allocated objects.
 *
 * This is shared by all implementations of SquirrelJME so that they have a
 * uniform object layout structure.
 *
 * @since 2016/06/08
 */
public class ObjectManager
{
	/** The used memory pool. */
	protected final MemoryPoolManager poolman;
	
	/** The type used for pointers. */
	protected final PointerType pointertype;
	
	/**
	 * Intializes the object manager.
	 *
	 * @param __pm Initializes the memory pool manager.
	 * @param __pt The type of values used for pointers.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/08
	 */
	public ObjectManager(MemoryPoolManager __pm, PointerType __pt)
		throws NullPointerException
	{
		// Check
		if (__pm == null || __pm == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.poolman = __pm;
		this.pointertype = __pt;
	}
	
	/**
	 * Returns the data type to use for pointer based values.
	 *
	 * @return The pointer data type to use.
	 * @since 2016/06/08
	 */
	public final PointerType pointerType()
	{
		return this.pointertype;
	}
}

