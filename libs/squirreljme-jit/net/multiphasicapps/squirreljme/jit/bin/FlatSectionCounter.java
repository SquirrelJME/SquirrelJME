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
 * This is a section counter which only uses a single indexed text and data
 * section.
 *
 * @since 2017/06/28
 */
public class FlatSectionCounter
	implements SectionCounter
{
	/**
	 * {@inheritDoc}
	 * @since 2017/07/02
	 */
	@Override
	public SectionType nextResource(LinkerState __ls,
		Cluster __cluster, String __name)
		throws NullPointerException
	{
		// Check
		if (__ls == null || __cluster == null || __name == null)
			throw new NullPointerException("NARG");
		
		// Only the basic data section is always used so that resources are
		// packed onto each other
		return SectionType.DATA;
	}
}

