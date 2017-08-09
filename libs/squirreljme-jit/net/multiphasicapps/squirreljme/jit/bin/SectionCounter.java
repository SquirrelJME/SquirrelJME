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

import net.multiphasicapps.squirreljme.jit.java.ClassName;
import net.multiphasicapps.squirreljme.jit.java.MethodDescriptor;
import net.multiphasicapps.squirreljme.jit.java.MethodFlags;
import net.multiphasicapps.squirreljme.jit.java.MethodName;

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
	
	/**
	 * Returns the section type to use for the given method.
	 *
	 * @param __ls The owning linker state.
	 * @param __c The name of the class the method is in.
	 * @param __n The name of the method.
	 * @param __t The type of the method.
	 * @param __f The flags for the method.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/09
	 */
	public abstract SectionType nextSection(LinkerState __ls,
		ClassName __c, MethodName __n, MethodDescriptor __t, MethodFlags __f)
		throws NullPointerException;
}

