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

/**
 * This is the default destination for fragments.
 *
 * @since 2017/08/14
 */
final class __DefaultFragmentDestination__
	implements FragmentDestination
{
	/** The linker state to append to. */
	protected final LinkerState linkerstate;
	
	/** The section to append to. */
	protected final SectionType section;
	
	/**
	 * Initializes the default destination.
	 *
	 * @param __ls The linker state to write to.
	 * @param __t The type of section to add.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/14
	 */
	__DefaultFragmentDestination__(LinkerState __ls, SectionType __t)
		throws NullPointerException
	{
		// Check
		if (__ls == null || __t == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.linkerstate = __ls;
		this.section = __t;
	}
}

