// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * All the {@link VariablePlacement}s for a given address within the stack
 * map.
 *
 * @see VariablePlacement
 * @see VariablePlacementMap
 * @since 2023/08/09
 */
public class VariablePlacements
{
	/**
	 * Returns the placement of the given local.
	 *
	 * @param __dx The local index.
	 * @return The placement of that variable.
	 * @since 2023/08/09
	 */
	public VariablePlacement local(int __dx)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the placement of the given stack entry.
	 *
	 * @param __dx The index of the stack entry.
	 * @return The placement of that variable.
	 * @since 2023/08/09
	 */
	public VariablePlacement stack(int __dx)
	{
		throw Debugging.todo();
	}
}
