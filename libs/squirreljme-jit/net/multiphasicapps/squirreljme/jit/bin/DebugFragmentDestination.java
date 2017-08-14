// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.bin;

import java.io.PrintStream;

/**
 * This is a fragment debugger which dumps the fragment and then forwards it
 * to a given fragment destination.
 *
 * @since 2017/08/14
 */
public class DebugFragmentDestination
	implements FragmentDestination
{
	/** The destination fragment destination. */
	protected final FragmentDestination destination;
	
	/**
	 * Initializes the debug fragment destination.
	 *
	 * @param __fd The destination fragment to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/14
	 */
	public DebugFragmentDestination(FragmentDestination __fd)
		throws NullPointerException
	{
		this(System.err, __fd);
	}
	 
	/**
	 * Initializes the debug fragment destination.
	 *
	 * @param __p The stream to print to.
	 * @param __fd The destination fragment to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/14
	 */
	public DebugFragmentDestination(PrintStream __p, FragmentDestination __fd)
		throws NullPointerException
	{
		// Check
		if (__fd == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.destination = __fd;
	}
}

