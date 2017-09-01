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

import net.multiphasicapps.util.datadeque.ByteDeque;

/**
 * This represents a single fragment within a section which is used to
 * store compiled machine code or data temporarily before the binary is fully
 * formed.
 *
 * @see TemporarySection
 * @since 2017/08/24
 */
public final class TemporaryFragment
{
	/** The data which makes up the fragment. */
	private final byte[] _data;
	
	/**
	 * Initializes the fragment with just data.
	 *
	 * @param __d The data to initialize the fragment with, this is not
	 * copied.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/29
	 */
	TemporaryFragment(byte[] __d)
		throws NullPointerException
	{
		// Check
		if (__d == null)
			throw new NullPointerException("NARG");
		
		// Copy data
		this._data = __d;
	}
}

