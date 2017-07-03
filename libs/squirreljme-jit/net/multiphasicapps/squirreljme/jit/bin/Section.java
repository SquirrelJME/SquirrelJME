// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.bin;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * This represents and stores the binary data and code which makes up a
 * section. Sections may have some regions of their output be determined in a
 * later stage of the linker on output such as the address where a method is
 * located. A section contains multiple fragments which make up the entire
 * section.
 *
 * @since 2017/06/15
 */
public final class Section
	extends __SubState__
{
	/** Fragments which exist within this section. */
	private final List<Fragment> _fragments =
		new ArrayList<>();
	
	/** The current size of the section, in bytes. */
	private volatile long _size;
	
	/**
	 * Initializes the section.
	 *
	 * @param __ls The reference to the owning linker state.
	 * @since 2017/06/15
	 */
	Section(Reference<LinkerState> __ls)
	{
		super(__ls);
	}
	
	/**
	 * Appends a fragment to this section.
	 *
	 * @param __b The bytes which make up the section, this is not copied.
	 * @throws NullPointerException On null arguments.
	 * @return The newly created fragment.
	 * @since 2017/06/28
	 */
	Reference<Fragment> __append(byte[] __b)
		throws NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// Create new fragment, fragments are just smashed right next to
		// each other
		long baseaddr = this._size;
		Fragment rv = new Fragment(__linkerState().__reference(), baseaddr,
			__b);
		this._fragments.add(rv);
		this._size = baseaddr + __b.length;
		
		// Fragments are referred to be reference to that they may be
		// garbage collected as needed
		return new WeakReference<>(rv);
	}
}

