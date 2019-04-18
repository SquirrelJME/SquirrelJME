// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

/**
 * Represents a point in memory which points to an instance for allocation.
 *
 * @since 2019/04/18
 */
@Deprecated
public final class AllocationPoint
{
	/** The allocated instance. */
	public final Instance instance;
	
	/** The pointer it was allocated at. */
	public final int vptr;
	
	/**
	 * Initializes a null allocation point.
	 *
	 * @since 2019/04/18
	 */
	public AllocationPoint()
	{
		this.instance = null;
		this.vptr = 0;
	}
	
	/**
	 * Initializes the allocation point.
	 *
	 * @param __i The instance.
	 * @param __vptr The virtual pointer.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/18
	 */
	public AllocationPoint(Instance __i, int __vptr)
		throws NullPointerException
	{
		if (__i == null)
			throw new NullPointerException("NARG");
		
		this.instance = __i;
		this.vptr = __vptr;
	}
}

