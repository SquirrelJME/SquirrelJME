// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.bin;

import java.lang.ref.Reference;

/**
 * This is a fragment which exists within a section which holds a bunch of
 * data with potential dynamic areas.
 *
 * @since 2017/06/27
 */
public class Fragment
	extends __SubState__
{
	/** The fragment byte data. */
	private final byte[] _data;
	
	/**
	 * Initializes the fragment.
	 *
	 * @param __ls The owning linker state.
	 * @param __b The data which makes up the fragment, this is not copied.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/06/27
	 */
	public Fragment(Reference<LinkerState> __ls, byte[] __b)
		throws NullPointerException
	{
		super(__ls);
		
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._data = __b;
	}
}

