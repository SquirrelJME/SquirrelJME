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
import net.multiphasicapps.util.datadeque.ByteDeque;

/**
 * This class is used to build sections which are placed in the section table.
 * Since {@link Section} data should be as immutable as possible, this allows
 * such sections to be dynamically created.
 *
 * @since 2017/06/23
 */
public class SectionBuilder
	extends __SubState__
{
	/** Deque for the bytes within the section. */
	protected final ByteDeque bytes =
		new ByteDeque();
	
	/**
	 * Initializes the section builder.
	 *
	 * @param __ls The owning linker state.
	 * @since 2017/06/23
	 */
	SectionBuilder(Reference<LinkerState> __ls)
	{
		super(__ls);
	}
}

