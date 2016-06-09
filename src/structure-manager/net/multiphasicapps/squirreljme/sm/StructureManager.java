// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.sm;

/**
 * This is the manager which manages all allocated structures.
 *
 * @since 2016/06/08
 */
public class StructureManager
{
	/** The type used for pointers. */
	protected final PointerType pointertype;
	
	/** The number of bytes in a pointer. */
	protected final long pointerbytes;
	
	/** The pointer mask. */
	protected final long pointermask;
	
	/**
	 * Intializes the object manager.
	 *
	 * @param __pt The type of values used for pointers.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/08
	 */
	public StructureManager(PointerType __pt)
		throws NullPointerException
	{
		// Check
		if (__pt == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.pointertype = __pt;
		this.pointerbytes = __pt.bytes();
		this.pointermask = __pt.mask();
		
		if (true)
			throw new Error("TODO");
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

