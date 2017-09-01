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
 * This class is used to generate temporary fragments which are then output
 * to fragments which are then placed into sections.
 *
 * @see TemporaryFragment
 * @since 2017/08/24
 */
public class TemporaryFragmentBuilder
	extends DataBuilder
{
	/**
	 * Builds the fragment which has been generated from this.
	 *
	 * @return The resulting built fragment.
	 * @since 2017/08/29
	 */
	public final TemporaryFragment build()
	{
		return new TemporaryFragment(toByteArray());
	}
}

