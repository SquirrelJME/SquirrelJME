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

import java.util.LinkedHashMap;
import java.util.Map;
import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This class represents a temporary binary which is used to contain the
 * compiled fragments which will eventually be placed within sections as the
 * output binary is built.
 *
 * This class is thread safe to allow the JIT to operate using multiple
 * threads at the same time.
 *
 * @since 2017/08/24
 */
public class TemporaryBinary
{
	/** Fragments which exist within the binary. */
	private final Map<LinkingPoint, TemporaryFragment> _fragments =
		new LinkedHashMap<>();
	
	/**
	 * Links the specified fragment 
	 *
	 * @param __lp The linking point of the fragment.
	 * @param __f The fragment to link.
	 * @throws JITException If the fragment would replace an existing
	 * fragment.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/29
	 */
	public final void link(LinkingPoint __lp, TemporaryFragment __f)
		throws JITException, NullPointerException
	{
		// Check
		if (__lp == null || __f == null)
			throw new NullPointerException("NARG");
		
		// Lock so multiple threads can setup linking points at the same time
		Map<LinkingPoint, TemporaryFragment> fragments = this._fragments;
		synchronized (fragments)
		{
			// {@squirreljme.erorr JI0y Cannot link the specified linking point
			// because it already exists within the program. (The linking
			// point)}
			if (fragments.containsKey(__lp))
				throw new JITException(String.format("JI0y %s", __lp));
			
			fragments.put(__lp, __f);
		}
	}
}

