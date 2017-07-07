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
import java.lang.ref.WeakReference;

/**
 * This condition table is used to verify that all conditions within the state
 * of the output executable pass in which case compilation is a success and
 * symbol resolution can be performed. Essentially this allows the compiler to
 * be simpler in design by deferring what would happen 99% of the time until
 * the near end of compilation (since most classes and compilation sets should
 * not be malformed).
 *
 * Despite a waste of potential resources this makes life much easier and
 * reduces stress levels and also reduces tons of complexity and waste.
 *
 * @since 2017/07/07
 */
public class Conditions
	extends __SubState__
{
	/**
	 * Initializes the conditions table.
	 *
	 * @param __ls The reference to the owning linker state.
	 * @since 2017/07/07
	 */
	Conditions(Reference<LinkerState> __ls)
	{
		super(__ls);
	}
}

