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

/**
 * This is used to count which section should be created next within the
 * linker state since some operating systems may allow resources to be placed
 * in multiple locations rather than in one specific flat region of space.
 *
 * @since 2017/06/28
 */
public interface SectionCounter
{
	/**
	 * Returns the section type to use for the next resource.
	 *
	 * @param __ls The owning linker state.
	 * @param __cluster The cluster the resource is in.
	 * @param __name The name of the resource.
	 * @return The section that the resource is under.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/07/02
	 */
	public abstract SectionType nextResource(LinkerState __ls,
		Cluster __cluster, String __name)
		throws NullPointerException;
}

